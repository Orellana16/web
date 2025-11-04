package com.jerezsurinmobiliaria.web.service;

import com.jerezsurinmobiliaria.web.model.Inmueble;
import com.jerezsurinmobiliaria.web.model.PropiedadAdicional;
import com.jerezsurinmobiliaria.web.repository.PropiedadAdicionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropiedadAdicionalService {
    
    private final PropiedadAdicionalRepository propiedadAdicionalRepository;
    
    @Transactional(readOnly = true)
    public List<PropiedadAdicional> findAll() {
        return propiedadAdicionalRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public PropiedadAdicional findById(Integer id) {
        return propiedadAdicionalRepository.findById(id).orElse(null);
    }
    
    @Transactional
    public PropiedadAdicional save(PropiedadAdicional propiedadAdicional) {
        System.out.println("=== GUARDANDO PROPIEDAD ===");
        System.out.println("Tipo: " + propiedadAdicional.getTipo());
        System.out.println("Derrama: " + propiedadAdicional.getDerrama());
        System.out.println("IBI: " + propiedadAdicional.getIbi());
        System.out.println("Inmueble: " + propiedadAdicional.getInmueble());
        System.out.println("Inmueble ID: " + (propiedadAdicional.getInmueble() != null ? propiedadAdicional.getInmueble().getId() : "NULL"));
        
        return propiedadAdicionalRepository.save(propiedadAdicional);
    }
    
    @Transactional
    public void deleteById(Integer id) {
        propiedadAdicionalRepository.deleteById(id);
    }
    
    // ========================================================================
    // BÚSQUEDA CON FILTROS DINÁMICOS
    // ========================================================================
    
    @Transactional(readOnly = true)
    public Page<PropiedadAdicional> buscarConFiltros(
            List<String> tipo,
            Double derramaMin,
            Double derramaMax,
            String direccionInmueble,
            Integer inmuebleId,
            Pageable pageable) {
        
        return propiedadAdicionalRepository.buscarConFiltros(
            tipo, derramaMin, derramaMax, direccionInmueble, inmuebleId, pageable
        );
    }
    
    // ========================================================================
    // OBTENER VALORES ÚNICOS PARA FILTROS
    // ========================================================================
    
    @Transactional(readOnly = true)
    public List<String> obtenerTipos() {
        return propiedadAdicionalRepository.findDistinctTipo();
    }
    
    @Transactional(readOnly = true)
    public Double[] obtenerRangoDerrama() {
        Object[] rango = propiedadAdicionalRepository.obtenerRangoDerrama();
        if (rango != null && rango.length == 2 && rango[0] != null && rango[1] != null) {
            return new Double[]{(Double) rango[0], (Double) rango[1]};
        }
        return new Double[]{0.0, 1000.0};
    }
    
    // ========================================================================
    // MÉTODOS ADICIONALES
    // ========================================================================
    
    @Transactional(readOnly = true)
    public List<PropiedadAdicional> findByInmuebleId(Integer inmuebleId) {
        return propiedadAdicionalRepository.findByInmueble_Id(inmuebleId);
    }
    
    @Transactional(readOnly = true)
    public List<PropiedadAdicional> findByTipo(String tipo) {
        return propiedadAdicionalRepository.findByTipo(tipo);
    }
    
    @Transactional(readOnly = true)
    public List<PropiedadAdicional> findByInmuebleIdOrdenadas(Integer inmuebleId) {
        return propiedadAdicionalRepository.findByInmueble_IdOrderByTipoAsc(inmuebleId);
    }
    
    @Transactional(readOnly = true)
    public boolean existsById(Integer id) {
        return propiedadAdicionalRepository.existsById(id);
    }
    
    @Transactional(readOnly = true)
    public long countByInmuebleId(Integer inmuebleId) {
        return findByInmuebleId(inmuebleId).size();
    }
    
    @Transactional
    public void deleteByInmuebleId(Integer inmuebleId) {
        List<PropiedadAdicional> propiedades = findByInmuebleId(inmuebleId);
        propiedadAdicionalRepository.deleteAll(propiedades);
    }
    
    @Transactional(readOnly = true)
    public Double calcularDerramaTotalInmueble(Integer inmuebleId) {
        List<PropiedadAdicional> propiedades = findByInmuebleId(inmuebleId);
        return propiedades.stream()
                .mapToDouble(PropiedadAdicional::getDerrama)
                .sum();
    }
    
    @Transactional(readOnly = true)
    public boolean inmuebleTieneGaraje(Integer inmuebleId) {
        List<PropiedadAdicional> propiedades = findByInmuebleId(inmuebleId);
        return propiedades.stream()
                .anyMatch(p -> p.getTipo().equalsIgnoreCase("Garaje"));
    }
    
    @Transactional(readOnly = true)
    public boolean inmuebleTieneTrastero(Integer inmuebleId) {
        List<PropiedadAdicional> propiedades = findByInmuebleId(inmuebleId);
        return propiedades.stream()
                .anyMatch(p -> p.getTipo().equalsIgnoreCase("Trastero"));
    }
    
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
        
        if (resumen.length() > 2) {
            resumen.setLength(resumen.length() - 2);
        }
        
        return resumen.toString();
    }
    
    // ========================================================================
    // ESTADÍSTICAS
    // ========================================================================
    
    @Transactional(readOnly = true)
    public Long contarTodas() {
        return propiedadAdicionalRepository.count();
    }
    
    @Transactional(readOnly = true)
    public List<PropiedadAdicional> findTop10MasCaras() {
        return propiedadAdicionalRepository.findTop10ByOrderByDerramaDesc();
    }
    
    @Transactional(readOnly = true)
    public List<Object[]> contarPorTipo() {
        return propiedadAdicionalRepository.contarPorTipo();
    }
}