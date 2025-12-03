package com.jerezsurinmobiliaria.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    //Redirecciona la raíz al listado de inmuebles
    @GetMapping("/")
    public String home() {
        return "redirect:/inmuebles";
    }
}