package com.jerezsurinmobiliaria.web.service;

import com.jerezsurinmobiliaria.web.model.Inmueble;
import com.jerezsurinmobiliaria.web.repository.InmuebleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InmuebleService {
    
    private final InmuebleRepository inmuebleRepository;
    
    @Transactional(readOnly = true)
    public List<Inmueble> findAll() {
        return inmuebleRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Inmueble findById(Integer id) {
        return inmuebleRepository.findById(id).orElse(null);
    }
    
    @Transactional
    public Inmueble save(Inmueble inmueble) {
        return inmuebleRepository.save(inmueble);
    }
    
    @Transactional
    public void deleteById(Integer id) {
        inmuebleRepository.deleteById(id);
    }
    
    // BÚSQUEDA CON FILTROS DINÁMICOS
    @Transactional(readOnly = true)
    public Page<Inmueble> buscarConFiltros(
            String direccion,
            List<String> tipoVivienda,
            List<Byte> tipoOperacion,
            Double precioMin,
            Double precioMax,
            Integer numHabMin,
            Integer numHabMax,
            Double metrosMin,
            Double metrosMax,
            List<String> comunidad,
            List<String> estado,
            Byte valido,
            Pageable pageable) {
        
        return inmuebleRepository.buscarConFiltros(
            direccion, tipoVivienda, tipoOperacion, precioMin, precioMax,
            numHabMin, numHabMax, metrosMin, metrosMax, comunidad, estado, valido,
            pageable
        );
    }
    
    // ⭐ OBTENER VALORES ÚNICOS PARA FILTROS
    @Transactional(readOnly = true)
    public List<String> obtenerTiposVivienda() {
        return inmuebleRepository.findDistinctTipoVivienda();
    }
    
    @Transactional(readOnly = true)
    public List<String> obtenerComunidades() {
        return inmuebleRepository.findDistinctComunidad();
    }
    
    @Transactional(readOnly = true)
    public List<String> obtenerEstados() {
        return inmuebleRepository.findDistinctEstado();
    }
    
    @Transactional(readOnly = true)
    public Double[] obtenerRangoPrecio() {
        Object[] rango = inmuebleRepository.obtenerRangoPrecio();
        if (rango != null && rango.length == 2) {
            return new Double[]{(Double) rango[0], (Double) rango[1]};
        }
        return new Double[]{0.0, 1000000.0};
    }
    
    // ESTADÍSTICAS
    @Transactional(readOnly = true)
    public Long contarInmuebles() {
        return inmuebleRepository.count();
    }
    
    @Transactional(readOnly = true)
    public Double calcularPrecioPromedio() {
        return inmuebleRepository.calcularPrecioPromedio((byte) 1);
    }
    
    @Transactional(readOnly = true)
    public List<Inmueble> findTop5Caros() {
        return inmuebleRepository.findTop5ByValidoOrderByPrecioDesc((byte) 1);
    }
}