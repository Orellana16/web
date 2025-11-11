package com.jerezsurinmobiliaria.web.controller;

import com.jerezsurinmobiliaria.web.dto.InmuebleListDTO;
import com.jerezsurinmobiliaria.web.model.Cita;
import com.jerezsurinmobiliaria.web.model.Inmueble;
import com.jerezsurinmobiliaria.web.model.PropiedadAdicional;
import com.jerezsurinmobiliaria.web.model.VendedorInmueble;
import com.jerezsurinmobiliaria.web.service.CitaService;
import com.jerezsurinmobiliaria.web.service.InmuebleService;
import com.jerezsurinmobiliaria.web.service.PropiedadAdicionalService;
import com.jerezsurinmobiliaria.web.service.VendedorInmuebleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/inmuebles")
@RequiredArgsConstructor
public class InmuebleController {
    
    private final InmuebleService inmuebleService;
    private final CitaService citaService;
    private final VendedorInmuebleService vendedorInmuebleService;
    private final PropiedadAdicionalService propiedadAdicionalService;
    

    /**
     * Muestra el listado de inmuebles.
     * Utiliza un DTO (InmuebleListDTO) para evitar LazyInitializationException en la vista.
     */
    @GetMapping
    @Transactional(readOnly = true)
    public String list(
            // Paginación
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,

            // Ordenamiento
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
        
        // ⭐ Se busca usando el método que devuelve DTOs
        Page<InmuebleListDTO> inmueblesPage = inmuebleService.buscarConFiltros(
            direccion, tipoVivienda, tipoOperacion, precioMin, precioMax,
            numHabMin, numHabMax, metrosMin, metrosMax, comunidad, estado,
            (byte) 1, // Solo válidos
            pageable
        );
        
        // Datos para la vista (ahora son DTOs, pero la vista funciona igual)
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
        Double[] rangoPrecio = inmuebleService.obtenerRangoPrecio();
        model.addAttribute("precioMinGlobal", rangoPrecio[0]);
        model.addAttribute("precioMaxGlobal", rangoPrecio[1]);
        
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
    
    /**
     * Muestra la vista de detalle.
     * Carga cada colección de datos por separado para evitar errores y ser eficiente.
     */
    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public String detail(@PathVariable Integer id, Model model, RedirectAttributes flash) {
        
        // 1. Cargar la entidad principal
        Inmueble inmueble = inmuebleService.findById(id);
        
        if (inmueble == null) {
            flash.addFlashAttribute("error", "El inmueble no existe");
            return "redirect:/inmuebles";
        }
        
        // 2. Cargar cada colección de datos por separado
        List<Cita> citas = citaService.findByInmuebleId(id);
        List<VendedorInmueble> vendedores = vendedorInmuebleService.findByInmuebleId(id);
        List<PropiedadAdicional> propiedadesAdicionales = propiedadAdicionalService.findByInmuebleId(id);
        
        // 3. Obtener conteo de interesados con una query eficiente
        Long cantidadInteresados = inmuebleService.countInteresados(id);
        
        // 4. Pasar todos los datos a la vista
        model.addAttribute("inmueble", inmueble);
        model.addAttribute("citas", citas);
        model.addAttribute("vendedores", vendedores);
        model.addAttribute("propiedadesAdicionales", propiedadesAdicionales);
        model.addAttribute("cantidadInteresados", cantidadInteresados);
        
        model.addAttribute("title", "Detalle del Inmueble");
        return "inmuebles/detail";
    }
    
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
            inmuebleService.save(inmueble);
            flash.addFlashAttribute("success", "Inmueble creado exitosamente");
            return "redirect:/inmuebles";
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al crear el inmueble: " + e.getMessage());
            return "redirect:/inmuebles/new";
        }
    }
    
    /**
     * Muestra el formulario para editar un inmueble existente.
     */
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
    
    /**
     * Procesa la actualización de un inmueble.
     */
    @PostMapping("/{id}")
    public String update(@PathVariable Integer id, @ModelAttribute Inmueble inmueble, RedirectAttributes flash) {
        try {
            // Asegurarse de que el ID del path y del objeto coinciden
            inmueble.setId(id);
            inmuebleService.save(inmueble);
            flash.addFlashAttribute("success", "Inmueble actualizado exitosamente");
            return "redirect:/inmuebles/" + id;
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al actualizar: " + e.getMessage());
            return "redirect:/inmuebles/" + id + "/edit";
        }
    }
    
    /**
     * Procesa la eliminación de un inmueble.
     */
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