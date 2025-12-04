package com.jerezsurinmobiliaria.web.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jerezsurinmobiliaria.web.model.Inmueble;
import com.jerezsurinmobiliaria.web.model.PropiedadAdicional;
import com.jerezsurinmobiliaria.web.service.InmuebleService;
import com.jerezsurinmobiliaria.web.service.PropiedadAdicionalService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

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
            Pageable pageable,
            Model model) {

        Page<PropiedadAdicional> resultado = propiedadService.buscarPropiedadesConFiltros(
                tipo,
                derramaMin,
                derramaMax,
                direccionInmueble,
                inmuebleId,
                pageable);

        // Controlador (Controller) - Modificar aquí
        model.addAttribute("propiedades", resultado);
        model.addAttribute("filtro_tipo", tipo); // Cambiado de 'tipoSeleccionado'
        model.addAttribute("filtro_derramaMin", derramaMin); // Cambiado de 'derramaMin'
        model.addAttribute("filtro_derramaMax", derramaMax); // Cambiado de 'derramaMax'
        model.addAttribute("filtro_direccionInmueble", direccionInmueble); // Cambiado de 'direccionInmueble'
        model.addAttribute("filtro_inmuebleId", inmuebleId); // Cambiado de 'inmuebleId'

        // También faltan estas en tu controlador para que la paginación funcione:
        model.addAttribute("currentPage", resultado.getNumber());
        model.addAttribute("totalPages", resultado.getTotalPages());
        model.addAttribute("totalItems", resultado.getTotalElements());

        // Y para la ordenación:
        model.addAttribute("sortBy", pageable.getSort().get().findFirst().map(o -> o.getProperty()).orElse(null));
        model.addAttribute("sortDir",
                pageable.getSort().get().findFirst().map(o -> o.getDirection().name().toLowerCase()).orElse(null));

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