package com.jerezsurinmobiliaria.web.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jerezsurinmobiliaria.web.model.Inmueble;
import com.jerezsurinmobiliaria.web.service.InmuebleService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/inmuebles")
@RequiredArgsConstructor
public class InmuebleController {
    
    private final InmuebleService inmuebleService;
    
    // ===============================================
    // LISTAR TODOS
    // ===============================================
@GetMapping
@Transactional
public String list(Model model) {
    List<Inmueble> inmuebles = inmuebleService.findAll();
    model.addAttribute("inmuebles", inmuebles);
    model.addAttribute("title", "Listado de Inmuebles");
    return "inmuebles/list";
}
    
    // ===============================================
    // VER DETALLE
    // ===============================================
    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model, RedirectAttributes flash) {
        Inmueble inmueble = inmuebleService.findById(id);
        
        if (inmueble == null) {
            flash.addFlashAttribute("error", "El inmueble no existe");
            return "redirect:/inmuebles";
        }
        
        model.addAttribute("inmueble", inmueble);
        model.addAttribute("title", "Detalle del Inmueble");
        return "inmuebles/detail";
    }
    
    // ===============================================
    // FORMULARIO CREAR (mostrar)
    // ===============================================
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        Inmueble inmueble = new Inmueble();
        inmueble.setValido((byte) 1); // Valor por defecto
        inmueble.setCompartido(false);
        
        model.addAttribute("inmueble", inmueble);
        model.addAttribute("title", "Crear Inmueble");
        model.addAttribute("action", "Crear");
        return "inmuebles/form";
    }
    
    // ===============================================
    // GUARDAR NUEVO
    // ===============================================
    @PostMapping
    public String create(@ModelAttribute Inmueble inmueble, RedirectAttributes flash) {
        try {
            inmuebleService.save(inmueble);
            flash.addFlashAttribute("success", "Inmueble creado exitosamente");
            return "redirect:/inmuebles";
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al crear el inmueble: " + e.getMessage());
            return "redirect:/inmuebles/new";
        }
    }
    
    // ===============================================
    // FORMULARIO EDITAR (mostrar)
    // ===============================================
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Integer id, Model model, RedirectAttributes flash) {
        Inmueble inmueble = inmuebleService.findById(id);
        
        if (inmueble == null) {
            flash.addFlashAttribute("error", "El inmueble no existe");
            return "redirect:/inmuebles";
        }
        
        model.addAttribute("inmueble", inmueble);
        model.addAttribute("title", "Editar Inmueble");
        model.addAttribute("action", "Actualizar");
        return "inmuebles/form";
    }
    
    // ===============================================
    // ACTUALIZAR
    // ===============================================
    @PostMapping("/{id}")
    public String update(@PathVariable Integer id, 
                        @ModelAttribute Inmueble inmueble, 
                        RedirectAttributes flash) {
        try {
            inmueble.setId(id); // Asegurar que el ID es correcto
            inmuebleService.save(inmueble);
            flash.addFlashAttribute("success", "Inmueble actualizado exitosamente");
            return "redirect:/inmuebles/" + id;
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al actualizar: " + e.getMessage());
            return "redirect:/inmuebles/" + id + "/edit";
        }
    }
    
    // ===============================================
    // ELIMINAR
    // ===============================================
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id, RedirectAttributes flash) {
        try {
            Inmueble inmueble = inmuebleService.findById(id);
            
            if (inmueble == null) {
                flash.addFlashAttribute("error", "El inmueble no existe");
                return "redirect:/inmuebles";
            }
            
            inmuebleService.deleteById(id);
            flash.addFlashAttribute("success", "Inmueble eliminado exitosamente");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
        }
        
        return "redirect:/inmuebles";
    }
}