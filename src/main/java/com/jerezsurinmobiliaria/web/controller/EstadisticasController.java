package com.jerezsurinmobiliaria.web.controller;

import com.jerezsurinmobiliaria.web.model.Inmueble;
import com.jerezsurinmobiliaria.web.service.CitaService;
import com.jerezsurinmobiliaria.web.service.InmuebleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/estadisticas")
@RequiredArgsConstructor
@Slf4j
public class EstadisticasController {

    private final InmuebleService inmuebleService;
    private final CitaService citaService;

    @GetMapping
    public String showStatistics(Model model) {

        // 1. Obtener todos los inmuebles (Para estadísticas reales, lo ideal sería hacer consultas COUNT en repositorio)
        List<Inmueble> todosInmuebles = inmuebleService.findAll();
        
        // 2. KPIs (Tarjetas superiores)
        long totalInmuebles = todosInmuebles.size();
        long totalCitas = citaService.findAll().size();
        
        // Calcular valor total de la cartera (Suma de precios)
        double valorCartera = todosInmuebles.stream()
                .mapToDouble(Inmueble::getPrecio)
                .sum();

        // Calcular total de interesados (Sumando la cantidad de interesados de cada inmueble)
        long totalInteresados = todosInmuebles.stream()
                .mapToLong(inmueble -> inmuebleService.countInteresados(inmueble.getId()))
                .sum();

        // 3. TOP 5 Inmuebles con más interesados
        // Nota: Esto es procesamiento en memoria. Con muchos datos, mejor hacer una @Query en el repositorio.
        List<Inmueble> topInmuebles = todosInmuebles.stream()
                .sorted((i1, i2) -> Long.compare(
                        inmuebleService.countInteresados(i2.getId()), 
                        inmuebleService.countInteresados(i1.getId())))
                .limit(5)
                .collect(Collectors.toList());

        // 4. Datos para el Gráfico (Inmuebles por Tipo)
        Map<String, Long> inmueblesPorTipo = todosInmuebles.stream()
                .collect(Collectors.groupingBy(Inmueble::getTipoVivienda, Collectors.counting()));

        // Separar claves (labels) y valores (data) para Chart.js
        List<String> chartLabels = inmueblesPorTipo.keySet().stream().toList();
        List<Long> chartData = inmueblesPorTipo.values().stream().toList();

        // 5. Pasar datos al modelo
        model.addAttribute("totalInmuebles", totalInmuebles);
        model.addAttribute("totalCitas", totalCitas);
        model.addAttribute("valorCartera", valorCartera);
        model.addAttribute("totalInteresados", totalInteresados);
        
        model.addAttribute("topInmuebles", topInmuebles);
        model.addAttribute("topInmueblesService", inmuebleService); // Para llamar a countInteresados en la vista
        
        model.addAttribute("chartLabels", chartLabels);
        model.addAttribute("chartData", chartData);

        model.addAttribute("title", "Panel de Estadísticas");
        return "estadisticas/stats";
    }
}