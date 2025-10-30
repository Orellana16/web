package com.jerezsurinmobiliaria.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jerezsurinmobiliaria.web.model.Inmueble;
import com.jerezsurinmobiliaria.web.model.PropiedadAdicional;
import com.jerezsurinmobiliaria.web.service.InmuebleService;
import com.jerezsurinmobiliaria.web.service.PropiedadAdicionalService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/propiedades")
@RequiredArgsConstructor
public class PropiedadAdicionalController {
    
    private final PropiedadAdicionalService propiedadService;
    private final InmuebleService inmuebleService;
    
    // ===============================================
    // LISTAR TODAS
    // ===============================================
    @GetMapping
    public String list(Model model) {
        model.addAttribute("propiedades", propiedadService.findAll());
        model.addAttribute("title", "Listado de Propiedades Adicionales");
        return "propiedades/list";
    }
    
    // ===============================================
    // VER DETALLE
    // ===============================================
    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model, RedirectAttributes flash) {
        PropiedadAdicional propiedad = propiedadService.findById(id);
        
        if (propiedad == null) {
            flash.addFlashAttribute("error", "La propiedad adicional no existe");
            return "redirect:/propiedades";
        }
        
        model.addAttribute("propiedad", propiedad);
        model.addAttribute("title", "Detalle de la Propiedad Adicional");
        return "propiedades/detail";
    }
    
    // ===============================================
    // FORMULARIO CREAR GENÉRICO
    // ===============================================
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        PropiedadAdicional propiedad = new PropiedadAdicional();
        
        model.addAttribute("propiedad", propiedad);
        model.addAttribute("inmuebles", inmuebleService.findAll());
        model.addAttribute("title", "Crear Propiedad Adicional");
        model.addAttribute("action", "Crear");
        return "propiedades/form";
    }
    
    // ===============================================
    // FORMULARIO CREAR DESDE INMUEBLE ESPECÍFICO
    // ===============================================
    @GetMapping("/new/inmueble/{inmuebleId}")
    public String showCreateFormForInmueble(@PathVariable Integer inmuebleId, 
                                           Model model, 
                                           RedirectAttributes flash) {
        Inmueble inmueble = inmuebleService.findById(inmuebleId);
        
        if (inmueble == null) {
            flash.addFlashAttribute("error", "El inmueble no existe");
            return "redirect:/inmuebles";
        }
        
        PropiedadAdicional propiedad = new PropiedadAdicional();
        propiedad.setInmueble(inmueble);
        
        model.addAttribute("propiedad", propiedad);
        model.addAttribute("inmuebles", inmuebleService.findAll());
        model.addAttribute("inmuebleSeleccionado", inmueble);
        model.addAttribute("title", "Crear Propiedad para " + inmueble.getDireccion());
        model.addAttribute("action", "Crear");
        return "propiedades/form";
    }
    

    // ===============================================
    // GUARDAR NUEVA
    // ===============================================
    @PostMapping
    public String create(@ModelAttribute PropiedadAdicional propiedad,
                        @RequestParam(value = "inmuebleId", required = false) Integer inmuebleId,
                        RedirectAttributes flash) {
        try {
            // VERIFICACIÓN
            if (inmuebleId == null) {
                flash.addFlashAttribute("error", "Debe seleccionar un inmueble");
                return "redirect:/propiedades/new";
            }
            
            Inmueble inmueble = inmuebleService.findById(inmuebleId);
            
            if (inmueble == null) {
                flash.addFlashAttribute("error", "El inmueble seleccionado no existe");
                return "redirect:/propiedades/new";
            }
            
            // ASIGNAR INMUEBLE
            propiedad.setInmueble(inmueble);
            
            // LOG DE DEBUG
            System.out.println("=== CONTROLLER - CREAR ===");
            System.out.println("Inmueble ID: " + inmuebleId);
            System.out.println("Propiedad Inmueble: " + propiedad.getInmueble());
            System.out.println("Tipo: " + propiedad.getTipo());
            System.out.println("Derrama: " + propiedad.getDerrama());
            
            propiedadService.save(propiedad);
            flash.addFlashAttribute("success", "Propiedad adicional creada exitosamente");
            return "redirect:/inmuebles/" + inmuebleId;  // ← Redirigir al inmueble
        } catch (Exception e) {
            e.printStackTrace();
            flash.addFlashAttribute("error", "Error al crear la propiedad: " + e.getMessage());
            return "redirect:/propiedades/new";
        }
    }
    
    // ===============================================
    // FORMULARIO EDITAR
    // ===============================================
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Integer id, Model model, RedirectAttributes flash) {
        PropiedadAdicional propiedad = propiedadService.findById(id);
        
        if (propiedad == null) {
            flash.addFlashAttribute("error", "La propiedad adicional no existe");
            return "redirect:/propiedades";
        }
        
        model.addAttribute("propiedad", propiedad);
        model.addAttribute("inmuebles", inmuebleService.findAll());
        model.addAttribute("title", "Editar Propiedad Adicional");
        model.addAttribute("action", "Actualizar");
        return "propiedades/form";
    }
    
    // ===============================================
    // ACTUALIZAR
    // ===============================================
    @PostMapping("/{id}")
    public String update(@PathVariable Integer id,
                        @ModelAttribute PropiedadAdicional propiedad,
                        @RequestParam(value = "inmuebleId", required = false) Integer inmuebleId,  // ← required = false
                        RedirectAttributes flash) {
        try {
            // VERIFICACIÓN
            if (inmuebleId == null) {
                flash.addFlashAttribute("error", "Debe seleccionar un inmueble");
                return "redirect:/propiedades/" + id + "/edit";
            }
            
            Inmueble inmueble = inmuebleService.findById(inmuebleId);
            
            if (inmueble == null) {
                flash.addFlashAttribute("error", "El inmueble seleccionado no existe");
                return "redirect:/propiedades/" + id + "/edit";
            }
            
            propiedad.setId(id);
            propiedad.setInmueble(inmueble);
            
            propiedadService.save(propiedad);
            flash.addFlashAttribute("success", "Propiedad adicional actualizada exitosamente");
            return "redirect:/propiedades/" + id;
        } catch (Exception e) {
            e.printStackTrace();
            flash.addFlashAttribute("error", "Error al actualizar: " + e.getMessage());
            return "redirect:/propiedades/" + id + "/edit";
        }
    }
    
    // ===============================================
    // ELIMINAR
    // ===============================================
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id, 
                        @RequestParam(value = "returnToInmueble", required = false) Integer inmuebleId,
                        RedirectAttributes flash) {
        try {
            PropiedadAdicional propiedad = propiedadService.findById(id);
            
            if (propiedad == null) {
                flash.addFlashAttribute("error", "La propiedad adicional no existe");
                return "redirect:/propiedades";
            }
            
            // Guardar ID del inmueble para redirigir
            Integer idInmueble = propiedad.getInmueble().getId();
            
            propiedadService.deleteById(id);
            flash.addFlashAttribute("success", "Propiedad adicional eliminada exitosamente");
            
            // Si viene de un inmueble, redirigir al inmueble
            if (inmuebleId != null) {
                return "redirect:/inmuebles/" + inmuebleId;
            } else if (idInmueble != null) {
                return "redirect:/inmuebles/" + idInmueble;
            }
            
            return "redirect:/propiedades";
        } catch (Exception e) {
            e.printStackTrace();
            flash.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
            return "redirect:/propiedades";
        }
    }
    
    // ===============================================
    // LISTAR PROPIEDADES DE UN INMUEBLE ESPECÍFICO
    // ===============================================
    @GetMapping("/inmueble/{inmuebleId}")
    public String listByInmueble(@PathVariable Integer inmuebleId, 
                                 Model model, 
                                 RedirectAttributes flash) {
        Inmueble inmueble = inmuebleService.findById(inmuebleId);
        
        if (inmueble == null) {
            flash.addFlashAttribute("error", "El inmueble no existe");
            return "redirect:/inmuebles";
        }
        
        model.addAttribute("propiedades", propiedadService.findByInmuebleId(inmuebleId));
        model.addAttribute("inmueble", inmueble);
        model.addAttribute("title", "Propiedades de " + inmueble.getDireccion());
        return "propiedades/list";
    }
}