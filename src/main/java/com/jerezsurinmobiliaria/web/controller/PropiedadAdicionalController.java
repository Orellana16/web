package com.jerezsurinmobiliaria.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jerezsurinmobiliaria.web.model.Inmueble;
import com.jerezsurinmobiliaria.web.model.PropiedadAdicional;
import com.jerezsurinmobiliaria.web.service.InmuebleService;
import com.jerezsurinmobiliaria.web.service.PropiedadAdicionalService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/propiedades")
@RequiredArgsConstructor
@Slf4j
public class PropiedadAdicionalController {

    private final PropiedadAdicionalService propiedadService;
    private final InmuebleService inmuebleService;

    // ========================================================================
    // ENDPOINT DE BÚSQUEDA CON FILTROS Y PAGINACIÓN
    // ========================================================================
@GetMapping
public String buscarPropiedadesConFiltros(
        @RequestParam(required = false) List<String> tipo,
        @RequestParam(required = false) Double derramaMin,
        @RequestParam(required = false) Double derramaMax,
        @RequestParam(required = false) String direccionInmueble,
        @RequestParam(required = false) Integer inmuebleId,
        // Agregamos estos para capturarlos fácilmente como en el otro controller
        @RequestParam(defaultValue = "id") String sortBy,
        @RequestParam(defaultValue = "asc") String sortDir,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        Model model) {

    // Creamos el pageable manualmente para asegurar la ordenación
    Sort sort = sortDir.equalsIgnoreCase("asc") 
                ? Sort.by(sortBy).ascending() 
                : Sort.by(sortBy).descending();
    Pageable pageable = PageRequest.of(page, size, sort);

    Page<PropiedadAdicional> resultado = propiedadService.buscarPropiedadesConFiltros(
            tipo, derramaMin, derramaMax, direccionInmueble, inmuebleId, pageable);

    model.addAttribute("propiedades", resultado);
    
    // Filtros
    model.addAttribute("filtro_tipo", tipo != null ? tipo : new ArrayList<>());
    model.addAttribute("filtro_derramaMin", derramaMin);
    model.addAttribute("filtro_derramaMax", derramaMax);
    model.addAttribute("filtro_direccionInmueble", direccionInmueble);
    model.addAttribute("filtro_inmuebleId", inmuebleId);

    // Paginación y Orden (USAMOS LAS VARIABLES DIRECTAS)
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", resultado.getTotalPages());
    model.addAttribute("totalItems", resultado.getTotalElements());
    model.addAttribute("sortBy", sortBy);
    model.addAttribute("sortDir", sortDir);

    return "propiedades/list";
}

    // --- DETALLE ---

    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model, RedirectAttributes flash) {
        Optional<PropiedadAdicional> propiedad = propiedadService.findById(id);
        if (!propiedad.isPresent()) {
            flash.addFlashAttribute("error", "La propiedad adicional no existe");
            return "redirect:/propiedades";
        }
        model.addAttribute("propiedad", propiedad.get());
        model.addAttribute("title", "Detalle de la Propiedad Adicional");
        return "propiedades/detail";
    }

    // --- FORMULARIO DE CREACIÓN BÁSICO ---

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        PropiedadAdicional propiedad = new PropiedadAdicional();

        model.addAttribute("propiedad", propiedad);
        model.addAttribute("inmuebles", inmuebleService.findAll());
        model.addAttribute("title", "Crear Propiedad Adicional");
        model.addAttribute("action", "Crear");
        return "propiedades/form";
    }

    // --- FORMULARIO DE CREACIÓN ENLAZADO A INMUEBLE ---

    @GetMapping("/new/inmueble/{inmuebleId}")
    public String showCreateFormForInmueble(@PathVariable Integer inmuebleId, Model model, RedirectAttributes flash) {
        Optional<Inmueble> inmuebleOpt = inmuebleService.findById(inmuebleId);

        if (!inmuebleOpt.isPresent()) {
            flash.addFlashAttribute("error", "El inmueble no existe");
            return "redirect:/inmuebles";
        }

        Inmueble inmueble = inmuebleOpt.get();
        PropiedadAdicional propiedad = new PropiedadAdicional();
        propiedad.setInmueble(inmueble); // Pre-seleccionar el inmueble

        model.addAttribute("propiedad", propiedad);
        model.addAttribute("inmuebles", inmuebleService.findAll());
        model.addAttribute("inmuebleSeleccionado", inmueble);
        model.addAttribute("title", "Crear Propiedad para " + inmueble.getDireccion());
        model.addAttribute("action", "Crear");
        return "propiedades/form";
    }

    // --- CREACIÓN (POST) ---

    @PostMapping
    public String create(@ModelAttribute PropiedadAdicional propiedad,
            @RequestParam(value = "inmuebleId") Integer inmuebleId, RedirectAttributes flash) {

        try {
            if (inmuebleId == null) {
                log.warn("Intento de creación fallido: inmuebleId es nulo");
                flash.addFlashAttribute("error", "Debe seleccionar un inmueble");
                return "redirect:/propiedades/new";
            }

            Optional<Inmueble> inmuebleOpt = inmuebleService.findById(inmuebleId);

            if (!inmuebleOpt.isPresent()) {
                log.warn("Intento de creación fallido: Inmueble ID {} no encontrado", inmuebleId);
                flash.addFlashAttribute("error", "El inmueble seleccionado no existe");
                return "redirect:/propiedades/new";
            }

            Inmueble inmueble = inmuebleOpt.get();
            propiedad.setInmueble(inmueble);

            PropiedadAdicional saved = propiedadService.save(propiedad); // Delega el guardado

            log.info("Propiedad Adicional creada exitosamente con ID: {}", saved.getId());

            flash.addFlashAttribute("success", "Propiedad adicional creada exitosamente");
            // Redirige al detalle del inmueble para ver la nueva propiedad
            return "redirect:/inmuebles/" + inmuebleId;
        } catch (Exception e) {
            log.error("Error al crear la propiedad adicional para inmueble ID: {}", inmuebleId, e);

            flash.addFlashAttribute("error", "Error al crear la propiedad: " + e.getMessage());
            return "redirect:/propiedades/new";
        }
    }

    // --- FORMULARIO DE EDICIÓN ---

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Integer id, Model model, RedirectAttributes flash) {
        Optional<PropiedadAdicional> propiedad = propiedadService.findById(id);

        if (!propiedad.isPresent()) {
            flash.addFlashAttribute("error", "La propiedad adicional no existe");
            return "redirect:/propiedades";
        }

        model.addAttribute("propiedad", propiedad.get());
        model.addAttribute("inmuebles", inmuebleService.findAll());
        model.addAttribute("title", "Editar Propiedad Adicional");
        model.addAttribute("action", "Actualizar");
        return "propiedades/form";
    }

    // --- ACTUALIZACIÓN (POST) ---

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

            Optional<Inmueble> inmueble = inmuebleService.findById(inmuebleId);

            if (!inmueble.isPresent()) {
                log.warn("Actualización fallida: Inmueble ID {} no existe", inmuebleId);
                flash.addFlashAttribute("error", "El inmueble seleccionado no existe");
                return "redirect:/propiedades/" + id + "/edit";
            }

            // Establecer ID y Inmueble antes de guardar
            propiedad.setId(id);
            propiedad.setInmueble(inmueble.get());

            propiedadService.save(propiedad); // Delega la actualización

            log.info("Propiedad Adicional ID: {} actualizada exitosamente", id);

            flash.addFlashAttribute("success", "Propiedad adicional actualizada exitosamente");
            return "redirect:/propiedades/" + id;
        } catch (Exception e) {
            log.error("Error al actualizar propiedad adicional ID: {}", id, e);

            flash.addFlashAttribute("error", "Error al actualizar: " + e.getMessage());
            return "redirect:/propiedades/" + id + "/edit";
        }
    }

    // --- ELIMINACIÓN (POST) ---

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id, RedirectAttributes flash) {

        try {
            Optional<PropiedadAdicional> propiedadOpt = propiedadService.findById(id);

            if (!propiedadOpt.isPresent()) {
                log.warn("Intento de eliminar propiedad inexistente ID: {}", id);
                flash.addFlashAttribute("error", "La propiedad adicional no existe");
                return "redirect:/propiedades";
            }

            propiedadService.deleteById(id); // Delega la eliminación

            log.info("Propiedad Adicional ID: {} eliminada exitosamente", id);

            flash.addFlashAttribute("success", "Propiedad adicional eliminada exitosamente");

            return "redirect:/propiedades";

        } catch (Exception e) {
            log.error("Excepción al eliminar propiedad adicional ID: {}", id, e);

            flash.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
            return "redirect:/propiedades";
        }
    }
}