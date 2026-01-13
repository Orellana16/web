package com.jerezsurinmobiliaria.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.jerezsurinmobiliaria.web.dto.InmuebleListDTO;
import com.jerezsurinmobiliaria.web.model.Inmueble;
import com.jerezsurinmobiliaria.web.model.PropiedadAdicional;
import com.jerezsurinmobiliaria.web.service.InmueblePdfExporter;
import com.jerezsurinmobiliaria.web.service.InmuebleService;
import com.jerezsurinmobiliaria.web.service.PropiedadAdicionalService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/inmuebles")
@Slf4j
public class InmuebleController {

    @Autowired
    private InmuebleService inmuebleService;

    @Autowired
    private PropiedadAdicionalService propiedadAdicionalService;

    @Autowired
    private InmueblePdfExporter pdfExporter;

    // --- LISTADO Y FILTROS (GET /inmuebles) --------------------------------------

    /**
     * Muestra el listado de inmuebles con paginación, ordenamiento y filtros.
     * La lógica de filtros y DTOs está centralizada en el Service.
     */
    @GetMapping
    public String list(
            // Paginación y Ordenamiento
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,

            // Filtros
            @RequestParam(required = false) String direccion,
            @RequestParam(required = false) List<String> tipoVivienda,
            @RequestParam(required = false) List<Byte> tipoOperacion,
            @RequestParam(required = false) Double precioMin,
            @RequestParam(required = false) Double precioMax,
            @RequestParam(required = false) Integer numHabMin,
            @RequestParam(required = false) Integer numHabMax,
            @RequestParam(required = false) Double metrosMin,
            @RequestParam(required = false) Double metrosMax,
            @RequestParam(required = false) List<String> comunidad,
            @RequestParam(required = false) List<String> estado,

            Model model) {

        // Crear Pageable con ordenamiento
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        // Se busca usando el método que devuelve DTOs (lógica en el Service)
        Page<InmuebleListDTO> inmueblesPage = inmuebleService.buscarConFiltros(
                direccion, tipoVivienda, tipoOperacion, precioMin, precioMax,
                numHabMin, numHabMax, metrosMin, metrosMax, comunidad, estado,
                (byte) 1, // Solo válidos
                pageable);

        // Datos para la vista (Paginación)
        model.addAttribute("inmuebles", inmueblesPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", inmueblesPage.getTotalPages());
        model.addAttribute("totalItems", inmueblesPage.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);

        // Valores para los desplegables de filtros
        model.addAttribute("tiposVivienda", inmuebleService.obtenerTiposVivienda());
        model.addAttribute("comunidades", inmuebleService.obtenerComunidades());
        model.addAttribute("estados", inmuebleService.obtenerEstados());
        Object[] rangoPrecio = inmuebleService.obtenerRangoPrecio();

        // Lógica de valores por defecto movida al Service si es complejo, pero se deja
        // el manejo de la array aquí por simplicidad.
        if (rangoPrecio != null && rangoPrecio.length >= 2) {
            model.addAttribute("precioMinGlobal", rangoPrecio[0]);
            model.addAttribute("precioMaxGlobal", rangoPrecio[1]);
        } else {
            model.addAttribute("precioMinGlobal", 0.0);
            model.addAttribute("precioMaxGlobal", 1000000.0);
        }

        // Mantener los valores de los filtros seleccionados
        model.addAttribute("filtro_direccion", direccion);
        model.addAttribute("filtro_tipoVivienda", tipoVivienda != null ? tipoVivienda : new ArrayList<>());
        model.addAttribute("filtro_tipoOperacion", tipoOperacion != null ? tipoOperacion : new ArrayList<>());
        model.addAttribute("filtro_precioMin", precioMin);
        model.addAttribute("filtro_precioMax", precioMax);
        model.addAttribute("filtro_numHabMin", numHabMin);
        model.addAttribute("filtro_numHabMax", numHabMax);
        model.addAttribute("filtro_metrosMin", metrosMin);
        model.addAttribute("filtro_metrosMax", metrosMax);
        model.addAttribute("filtro_comunidad", comunidad != null ? comunidad : new ArrayList<>());
        model.addAttribute("filtro_estado", estado != null ? estado : new ArrayList<>());

        model.addAttribute("title", "Listado de Inmuebles");
        return "inmuebles/list";
    }

    // --- DETALLE (GET /inmuebles/{id}) ------------------------------------------

    /**
     * Muestra la vista de detalle.
     * Asume que el Service lanza ResourceNotFoundException si el ID no existe.
     */
    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model, RedirectAttributes flash) {

        // 3. El Service debe lanzar ResourceNotFoundException si el inmueble no existe.
        // Si el método no encuentra el Inmueble, lanza una excepción que se captura
        Optional<Inmueble> inmueble = inmuebleService.findById(id);

        if (!inmueble.isPresent()) {
            log.warn("Inmueble no encontrado ID: {}", id);
            flash.addFlashAttribute("error", "El inmueble no existe");
            return "redirect:/inmuebles";
        }

        // Cargar colecciones relacionadas
        // En un futuro cargar citas y vendedores
        List<PropiedadAdicional> propiedadesAdicionales = propiedadAdicionalService.findByInmuebleId(id);

        // Obtener conteo de interesados con una query
        Integer cantidadInteresados = inmuebleService.countInteresados(id);

        // Pasar todos los datos a la vista
        model.addAttribute("inmueble", inmueble.get());
        model.addAttribute("propiedadesAdicionales", propiedadesAdicionales);
        model.addAttribute("cantidadInteresados", cantidadInteresados);

        model.addAttribute("title", "Detalle del Inmueble");
        return "inmuebles/detail";
    }

    // --- EXPORTAR PDF (GET /inmuebles/{id}/pdf) ----------------------------------

    @GetMapping("/{id}/pdf")
    public String exportToPDF(@PathVariable Integer id, HttpServletResponse response, RedirectAttributes flash)
            throws IOException {
        log.info("Solicitud de exportación a PDF para inmueble ID: {}", id);

        // 1. Obtener Inmueble Principal (Service lanza ResourceNotFoundException)
        Optional<Inmueble> inmuebleOpt = inmuebleService.findById(id);

        if (!inmuebleOpt.isPresent()) {
            log.warn("Inmueble no encontrado para PDF ID: {}", id);
            flash.addFlashAttribute("error", "El inmueble no existe");
            return "redirect:/inmuebles";
        }

        Inmueble inmueble = inmuebleOpt.get();
        // 2. Cargar las relaciones necesarias para el PDF
        List<PropiedadAdicional> propiedades = propiedadAdicionalService.findByInmuebleId(id);

        // 3. Configurar respuesta HTTP
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Ficha_Inmueble_" + id + ".pdf";
        response.setHeader(headerKey, headerValue);

        // 4. Generar PDF
        pdfExporter.exportar(response, inmueble, propiedades);

        log.info("PDF generado y enviado exitosamente para inmueble ID: {}", id);
        return null; // La respuesta se maneja directamente con HttpServletResponse
    }

    // --- CREAR (GET/POST /inmuebles) ---------------------------------------------

    /**
     * Muestra el formulario para crear un nuevo inmueble.
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        Inmueble inmueble = new Inmueble();
        inmueble.setValido((byte) 1);
        inmueble.setCompartido(false);
        model.addAttribute("inmueble", inmueble);
        model.addAttribute("title", "Crear Inmueble");
        model.addAttribute("action", "Crear");
        return "inmuebles/form";
    }

    /**
     * Procesa la creación de un nuevo inmueble.
     */
    @PostMapping
    public String create(@ModelAttribute Inmueble inmueble, RedirectAttributes flash) {

        try {
            Inmueble saved = inmuebleService.save(inmueble);

            log.info("Inmueble creado exitosamente con ID: {}", saved.getId());

            flash.addFlashAttribute("success", "Inmueble creado exitosamente");
            return "redirect:/inmuebles/" + saved.getId(); // Redirige al detalle
        } catch (Exception e) {

            log.error("Error al crear el inmueble. Datos: {}", inmueble, e);

            flash.addFlashAttribute("error", "Error al crear el inmueble: " + e.getMessage());
            return "redirect:/inmuebles/new";
        }
    }

    // --- EDITAR (GET/POST /inmuebles/{id}/edit) ----------------------------------

    /**
     * Muestra el formulario para editar un inmueble existente.
     */
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Integer id, Model model, RedirectAttributes flash) {
        // El Service lanza ResourceNotFoundException si no existe
        Optional<Inmueble> inmueble = inmuebleService.findById(id);

        if (!inmueble.isPresent()) {
            log.warn("Inmueble no encontrado para edición ID: {}", id);
            flash.addFlashAttribute("error", "El inmueble no existe");
            return "redirect:/inmuebles";
        }

        model.addAttribute("inmueble", inmueble.get());
        model.addAttribute("title", "Editar Inmueble");
        model.addAttribute("action", "Actualizar");
        return "inmuebles/form";
    }

    /**
     * Procesa la actualización de un inmueble.
     */
    @PostMapping("/{id}")
    public String update(@PathVariable Integer id, @ModelAttribute Inmueble inmueble, RedirectAttributes flash) {

        try {
            // Asegurarse de que el ID del path y del objeto coinciden
            inmueble.setId(id);
            inmuebleService.save(inmueble);

            log.info("Inmueble ID: {} actualizado exitosamente", id);

            flash.addFlashAttribute("success", "Inmueble actualizado exitosamente");
            return "redirect:/inmuebles/" + id;
        } catch (Exception e) {
            log.error("Error al actualizar inmueble ID: {}", id, e);

            flash.addFlashAttribute("error", "Error al actualizar: " + e.getMessage());
            return "redirect:/inmuebles/" + id + "/edit";
        }
    }

    // --- ELIMINAR (POST /inmuebles/{id}/delete)
    // -----------------------------------

    /**
     * Procesa la eliminación de un inmueble.
     * Asume que el Service lanza ResourceNotFoundException si el ID no existe.
     */
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id, RedirectAttributes flash) {

        try {
            inmuebleService.deleteById(id);

            log.info("Inmueble ID: {} eliminado exitosamente", id);

            flash.addFlashAttribute("success", "Inmueble eliminado exitosamente");
        } catch (Exception e) {
            log.error("Error crítico al eliminar inmueble ID: {}", id, e);
            flash.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
        }
        return "redirect:/inmuebles";
    }
}