package com.jerezsurinmobiliaria.web.service;

import com.jerezsurinmobiliaria.web.model.PropiedadAdicional;
import com.jerezsurinmobiliaria.web.repository.PropiedadAdicionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PropiedadAdicionalService {
    
    @Autowired
    private PropiedadAdicionalRepository propiedadAdicionalRepository;
    
    // ========================================================================
    // BÚSQUEDA CON FILTROS DINÁMICOS
    // ========================================================================
    public Page<PropiedadAdicional> buscarPropiedadesConFiltros(
            List<String> tipo,
            Double derramaMin,
            Double derramaMax,
            String direccionInmueble,
            Integer inmuebleId,
            Pageable pageable) {

        // Llamada directa al método del repositorio:
        return propiedadAdicionalRepository.buscarConFiltros(
                tipo,
                derramaMin,
                derramaMax,
                direccionInmueble,
                inmuebleId,
                pageable
        );
    }

    // ========================================================================
    // OBTENER VALORES ÚNICOS PARA FILTROS
    // ========================================================================
    public Double[] obtenerRangoDerrama() {
        Object[] rango = propiedadAdicionalRepository.obtenerRangoDerrama();
        if (rango != null && rango.length == 2 && rango[0] != null && rango[1] != null) {
            return new Double[]{(Double) rango[0], (Double) rango[1]};
        }
        return new Double[]{0.0, 1000.0};
    }

    // ========================================================================
    // OTROS METODOS
    // ========================================================================

    public Optional<PropiedadAdicional> findById(Integer id) {
        return propiedadAdicionalRepository.findById(id);
    }

    public PropiedadAdicional save(PropiedadAdicional propiedad) {
        return propiedadAdicionalRepository.save(propiedad);
    }

    public Optional<PropiedadAdicional> findByIdPropiedadAdicional(Integer id) {
        return propiedadAdicionalRepository.findById(id);
    }

    public void deleteById(Integer id) {
        propiedadAdicionalRepository.deleteById(id);
    }

    public List<PropiedadAdicional> findByInmuebleId(Integer id) {
        return propiedadAdicionalRepository.findByInmueble_Id(id);
    }
}