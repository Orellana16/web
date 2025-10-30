package com.jerezsurinmobiliaria.web.service;

import com.jerezsurinmobiliaria.web.model.PropiedadAdicional;
import com.jerezsurinmobiliaria.web.repository.PropiedadAdicionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropiedadAdicionalService {
    
    private final PropiedadAdicionalRepository propiedadAdicionalRepository;
    
    /**
     * Obtener todas las propiedades adicionales
     */
    @Transactional(readOnly = true)
    public List<PropiedadAdicional> findAll() {
        return propiedadAdicionalRepository.findAll();
    }
    
    /**
     * Buscar propiedad adicional por ID
     * @return PropiedadAdicional o null si no existe
     */
    @Transactional(readOnly = true)
    public PropiedadAdicional findById(Integer id) {
        return propiedadAdicionalRepository.findById(id).orElse(null);
    }
    
    /**
     * Guardar o actualizar propiedad adicional
     */
    @Transactional
    public PropiedadAdicional save(PropiedadAdicional propiedadAdicional) {
        // DEBUG - Logs para detectar problemas
        System.out.println("=== GUARDANDO PROPIEDAD ===");
        System.out.println("Tipo: " + propiedadAdicional.getTipo());
        System.out.println("Derrama: " + propiedadAdicional.getDerrama());
        System.out.println("IBI: " + propiedadAdicional.getIbi());
        System.out.println("Inmueble: " + propiedadAdicional.getInmueble());
        System.out.println("Inmueble ID: " + (propiedadAdicional.getInmueble() != null ? propiedadAdicional.getInmueble().getId() : "NULL"));
        
        return propiedadAdicionalRepository.save(propiedadAdicional);
    }
    
    /**
     * Eliminar propiedad adicional por ID
     */
    @Transactional
    public void deleteById(Integer id) {
        propiedadAdicionalRepository.deleteById(id);
    }
    
    // ===============================================
    // MÉTODOS ADICIONALES ÚTILES
    // ===============================================
    
    /**
     * Obtener todas las propiedades de un inmueble específico
     * @param inmuebleId ID del inmueble
     */
    @Transactional(readOnly = true)
    public List<PropiedadAdicional> findByInmuebleId(Integer inmuebleId) {
        return propiedadAdicionalRepository.findByInmueble_Id(inmuebleId);  // ← CORREGIDO
    }
    
    /**
     * Obtener propiedades por tipo (Garaje, Trastero, etc.)
     */
    @Transactional(readOnly = true)
    public List<PropiedadAdicional> findByTipo(String tipo) {
        return propiedadAdicionalRepository.findByTipo(tipo);
    }
    
    /**
     * Obtener propiedades de un inmueble ordenadas por tipo
     */
    @Transactional(readOnly = true)
    public List<PropiedadAdicional> findByInmuebleIdOrdenadas(Integer inmuebleId) {
        return propiedadAdicionalRepository.findByInmueble_IdOrderByTipoAsc(inmuebleId);  // ← CORREGIDO
    }
    
    /**
     * Verificar si existe una propiedad adicional
     */
    @Transactional(readOnly = true)
    public boolean existsById(Integer id) {
        return propiedadAdicionalRepository.existsById(id);
    }
    
    /**
     * Contar propiedades adicionales de un inmueble
     */
    @Transactional(readOnly = true)
    public long countByInmuebleId(Integer inmuebleId) {
        return findByInmuebleId(inmuebleId).size();
    }
    
    /**
     * Eliminar todas las propiedades de un inmueble
     */
    @Transactional
    public void deleteByInmuebleId(Integer inmuebleId) {
        List<PropiedadAdicional> propiedades = findByInmuebleId(inmuebleId);
        propiedadAdicionalRepository.deleteAll(propiedades);
    }
    
    /**
     * Calcular derrama total de un inmueble
     */
    @Transactional(readOnly = true)
    public Double calcularDerramaTotalInmueble(Integer inmuebleId) {
        List<PropiedadAdicional> propiedades = findByInmuebleId(inmuebleId);
        return propiedades.stream()
                .mapToDouble(PropiedadAdicional::getDerrama)
                .sum();
    }
    
    /**
     * Verificar si un inmueble tiene garaje
     */
    @Transactional(readOnly = true)
    public boolean inmuebleTieneGaraje(Integer inmuebleId) {
        List<PropiedadAdicional> propiedades = findByInmuebleId(inmuebleId);
        return propiedades.stream()
                .anyMatch(p -> p.getTipo().equalsIgnoreCase("Garaje"));
    }
    
    /**
     * Verificar si un inmueble tiene trastero
     */
    @Transactional(readOnly = true)
    public boolean inmuebleTieneTrastero(Integer inmuebleId) {
        List<PropiedadAdicional> propiedades = findByInmuebleId(inmuebleId);
        return propiedades.stream()
                .anyMatch(p -> p.getTipo().equalsIgnoreCase("Trastero"));
    }
    
    /**
     * Obtener resumen de propiedades de un inmueble
     */
    @Transactional(readOnly = true)
    public String obtenerResumenPropiedades(Integer inmuebleId) {
        List<PropiedadAdicional> propiedades = findByInmuebleId(inmuebleId);
        
        if (propiedades.isEmpty()) {
            return "Sin propiedades adicionales";
        }
        
        StringBuilder resumen = new StringBuilder();
        for (PropiedadAdicional prop : propiedades) {
            resumen.append(prop.getTipo()).append(", ");
        }
        
        // Eliminar última coma
        if (resumen.length() > 2) {
            resumen.setLength(resumen.length() - 2);
        }
        
        return resumen.toString();
    }
}