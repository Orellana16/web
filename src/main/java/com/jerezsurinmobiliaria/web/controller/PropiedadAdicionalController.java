package com.jerezsurinmobiliaria.web.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jerezsurinmobiliaria.web.model.Inmueble;
import com.jerezsurinmobiliaria.web.model.PropiedadAdicional;
import com.jerezsurinmobiliaria.web.service.InmuebleService;
import com.jerezsurinmobiliaria.web.service.PropiedadAdicionalService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // Import necesario

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/propiedades")
@RequiredArgsConstructor
@Slf4j
public class PropiedadAdicionalController {
    
    private final PropiedadAdicionalService propiedadService;
    private final InmuebleService inmuebleService;
    
    @GetMapping
    public String list(
            // Paginación y ordenamiento
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            
            // Filtros
            @RequestParam(required = false) List<String> tipo,
            @RequestParam(required = false) Double derramaMin,
            @RequestParam(required = false) Double derramaMax,
            @RequestParam(required = false) String direccionInmueble,
            @RequestParam(required = false) Integer inmuebleId,
            
            Model model) {
        
        // Crear Pageable con ordenamiento
        Sort sort = sortDir.equalsIgnoreCase("asc") 
            ? Sort.by(sortBy).ascending() 
            : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        // Buscar con filtros
        Page<PropiedadAdicional> propiedadesPage = propiedadService.buscarConFiltros(
            tipo, derramaMin, derramaMax, direccionInmueble, inmuebleId, pageable
        );
        
        // Datos para la vista
        model.addAttribute("propiedades", propiedadesPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", propiedadesPage.getTotalPages());
        model.addAttribute("totalItems", propiedadesPage.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        
        // Valores para filtros
        model.addAttribute("tiposDisponibles", propiedadService.obtenerTipos());
        model.addAttribute("inmueblesDisponibles", inmuebleService.findAll());
        Double[] rangoDerrama = propiedadService.obtenerRangoDerrama();
        model.addAttribute("derramaMinGlobal", rangoDerrama[0]);
        model.addAttribute("derramaMaxGlobal", rangoDerrama[1]);
        
        // Mantener filtros seleccionados
        model.addAttribute("filtro_tipo", tipo != null ? tipo : new ArrayList<>());
        model.addAttribute("filtro_derramaMin", derramaMin);
        model.addAttribute("filtro_derramaMax", derramaMax);
        model.addAttribute("filtro_direccionInmueble", direccionInmueble);
        model.addAttribute("filtro_inmuebleId", inmuebleId);
        
        model.addAttribute("title", "Listado de Propiedades Adicionales");
        return "propiedades/list";
    }
    
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
    
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        PropiedadAdicional propiedad = new PropiedadAdicional();
        
        model.addAttribute("propiedad", propiedad);
        model.addAttribute("inmuebles", inmuebleService.findAll());
        model.addAttribute("title", "Crear Propiedad Adicional");
        model.addAttribute("action", "Crear");
        return "propiedades/form";
    }
    
    @GetMapping("/new/inmueble/{inmuebleId}")
    public String showCreateFormForInmueble(@PathVariable Integer inmuebleId, Model model, RedirectAttributes flash) {
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
    
    @PostMapping
    public String create(@ModelAttribute PropiedadAdicional propiedad, @RequestParam(value = "inmuebleId", required = false) Integer inmuebleId, RedirectAttributes flash) {
        
        try {
            if (inmuebleId == null) {
                log.warn("Intento de creación fallido: inmuebleId es nulo");
                flash.addFlashAttribute("error", "Debe seleccionar un inmueble");
                return "redirect:/propiedades/new";
            }
            
            Inmueble inmueble = inmuebleService.findById(inmuebleId);
            
            if (inmueble == null) {
                log.warn("Intento de creación fallido: Inmueble ID {} no encontrado", inmuebleId);
                flash.addFlashAttribute("error", "El inmueble seleccionado no existe");
                return "redirect:/propiedades/new";
            }
            
            propiedad.setInmueble(inmueble);
            
            PropiedadAdicional saved = propiedadService.save(propiedad);
            
            // Log éxito
            log.info("Propiedad Adicional creada exitosamente con ID: {}", saved.getId());
            
            flash.addFlashAttribute("success", "Propiedad adicional creada exitosamente");
            return "redirect:/inmuebles/" + inmuebleId;
        } catch (Exception e) {
            // Log error con traza
            log.error("Error al crear la propiedad adicional para inmueble ID: {}", inmuebleId, e);
            
            flash.addFlashAttribute("error", "Error al crear la propiedad: " + e.getMessage());
            return "redirect:/propiedades/new";
        }
    }
    
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
    
    @PostMapping("/{id}")
    public String update(@PathVariable Integer id,
                        @ModelAttribute PropiedadAdicional propiedad,
                        @RequestParam(value = "inmuebleId", required = false) Integer inmuebleId,
                        RedirectAttributes flash) {
        
        try {
            if (inmuebleId == null) {
                flash.addFlashAttribute("error", "Debe seleccionar un inmueble");
                return "redirect:/propiedades/" + id + "/edit";
            }
            
            Inmueble inmueble = inmuebleService.findById(inmuebleId);
            
            if (inmueble == null) {
                log.warn("Actualización fallida: Inmueble ID {} no existe", inmuebleId);
                flash.addFlashAttribute("error", "El inmueble seleccionado no existe");
                return "redirect:/propiedades/" + id + "/edit";
            }
            
            propiedad.setId(id);
            propiedad.setInmueble(inmueble);
            
            propiedadService.save(propiedad);
            
            // Log éxito
            log.info("Propiedad Adicional ID: {} actualizada exitosamente", id);
            
            flash.addFlashAttribute("success", "Propiedad adicional actualizada exitosamente");
            return "redirect:/propiedades/" + id;
        } catch (Exception e) {
            // Log error con traza
            log.error("Error al actualizar propiedad adicional ID: {}", id, e);
            
            flash.addFlashAttribute("error", "Error al actualizar: " + e.getMessage());
            return "redirect:/propiedades/" + id + "/edit";
        }
    }
    
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id, 
                        @RequestParam(value = "returnToInmueble", required = false) Integer inmuebleId,
                        RedirectAttributes flash) {
        
        try {
            PropiedadAdicional propiedad = propiedadService.findById(id);
            
            if (propiedad == null) {
                log.warn("Intento de eliminar propiedad inexistente ID: {}", id);
                flash.addFlashAttribute("error", "La propiedad adicional no existe");
                return "redirect:/propiedades";
            }
            
            Integer idInmueble = propiedad.getInmueble().getId();
            
            propiedadService.deleteById(id);
            
            // Log éxito
            log.info("Propiedad Adicional ID: {} eliminada exitosamente", id);
            
            flash.addFlashAttribute("success", "Propiedad adicional eliminada exitosamente");
            
            if (inmuebleId != null) {
                return "redirect:/inmuebles/" + inmuebleId;
            } else if (idInmueble != null) {
                return "redirect:/inmuebles/" + idInmueble;
            }
            
            return "redirect:/propiedades";
        } catch (Exception e) {
            // Log error crítico
            log.error("Excepción al eliminar propiedad adicional ID: {}", id, e);
            
            flash.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
            return "redirect:/propiedades";
        }
    }
}