package com.jerezsurinmobiliaria.web.controller;

import com.jerezsurinmobiliaria.web.dto.EstadisticasPageDTO;
import com.jerezsurinmobiliaria.web.service.EstadisticasService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

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

        // 1. Obtener los datos del servicio
        EstadisticasPageDTO stats = estadisticasService.generarEstadisticas();

        // 2. Añadir al modelo con valores seguros (nunca null)
        model.addAttribute("totalInmuebles", stats.getTotalInmuebles());
        model.addAttribute("totalCitas", stats.getTotalCitas());
        model.addAttribute("totalInteresados", stats.getTotalInteresados());
        model.addAttribute("valorCartera", stats.getValorCartera());

        // Aseguramos que chartLabels y chartData nunca sean null
        model.addAttribute("chartLabels",
                stats.getChartLabels() != null ? stats.getChartLabels() : new ArrayList<>());

        model.addAttribute("chartData",
                stats.getChartData() != null ? stats.getChartData() : new ArrayList<>());

        model.addAttribute("topInmuebles",
                stats.getTopInmuebles() != null ? stats.getTopInmuebles() : new ArrayList<>());

        model.addAttribute("title", "Panel de Estadísticas");

        return "estadisticas/stats";
    }
}