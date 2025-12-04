package com.jerezsurinmobiliaria.web.controller;

import com.jerezsurinmobiliaria.web.dto.EstadisticasPageDTO;
import com.jerezsurinmobiliaria.web.service.EstadisticasService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/estadisticas")
@RequiredArgsConstructor
@Slf4j
public class EstadisticasController {

    private final EstadisticasService estadisticasService;

    @GetMapping
    public String showStatistics(Model model) {
        log.info("Generando panel de estadísticas...");

        // 1. Delegar toda la lógica al servicio
        EstadisticasPageDTO stats = estadisticasService.generarEstadisticas();

        // 2. Desempaquetar el DTO en el modelo para que coincida con tu HTML
        model.addAttribute("totalInmuebles", stats.getTotalInmuebles());
        model.addAttribute("totalCitas", stats.getTotalCitas());
        model.addAttribute("totalInteresados", stats.getTotalInteresados());
        model.addAttribute("valorCartera", stats.getValorCartera());
        
        model.addAttribute("chartLabels", stats.getChartLabels());
        model.addAttribute("chartData", stats.getChartData());
        
        model.addAttribute("topInmuebles", stats.getTopInmuebles());

        model.addAttribute("title", "Panel de Estadísticas");

        // Asegúrate de que esta ruta coincida con tu estructura de carpetas
        // Si stats.html está directamente en templates/, pon solo "stats"
        return "estadisticas/stats"; 
    }
}