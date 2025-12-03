package com.jerezsurinmobiliaria.web.controller;

import com.jerezsurinmobiliaria.web.dto.InmuebleDashboardDTO;
import com.jerezsurinmobiliaria.web.model.Inmueble;
import com.jerezsurinmobiliaria.web.service.CitaService;
import com.jerezsurinmobiliaria.web.service.InmuebleService;
import com.jerezsurinmobiliaria.web.service.InteresadoService;
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
        private final InteresadoService InteresadoService;

        @GetMapping
        public String showStatistics(Model model) {

                // 1. Obtener todos los inmuebles
                List<Inmueble> todosInmuebles = inmuebleService.findAll();

                // 2. Tarjetas superiores
                long totalInmuebles = todosInmuebles.size();
                long totalCitas = citaService.findAll().size();
                long totalInteresados = InteresadoService.findAll().size();

                // Calcular valor total de la cartera
                double valorCartera = todosInmuebles.stream() // Contiene un "map/diccionario" de cada inmueble
                                .mapToDouble(Inmueble::getPrecio) // Devuelve el campo asignado a la variable "precio" y
                                                                  // lo convierte a double
                                .sum(); // Suma todos los precios

                // 3. TOP 5 Inmuebles con más interesados
                List<InmuebleDashboardDTO> topInmuebles = inmuebleService.findTop5InmueblesConteoInteresados();

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

                model.addAttribute("chartLabels", chartLabels);
                model.addAttribute("chartData", chartData);

                model.addAttribute("title", "Panel de Estadísticas");
                return "estadisticas/stats";
        }
}