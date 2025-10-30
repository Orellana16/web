package com.jerezsurinmobiliaria.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @GetMapping("/")
    public String home() {
        return "redirect:/inmuebles";
    }
    
    @GetMapping("/test")
    public String test() {
        return "test";  // Verifica que Thymeleaf funcione
    }
}