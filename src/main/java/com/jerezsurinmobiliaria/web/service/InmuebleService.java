package com.jerezsurinmobiliaria.web.service;

import com.jerezsurinmobiliaria.web.dto.InmuebleListDTO;
import com.jerezsurinmobiliaria.web.dto.InmuebleDashboardDTO;

import com.jerezsurinmobiliaria.web.model.Inmueble;
import com.jerezsurinmobiliaria.web.repository.InmuebleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InmuebleService {

    @Autowired
    private InmuebleRepository inmuebleRepository;

    // ========================================================================
    // BÚSQUEDA CON FILTROS DINÁMICOS
    // ========================================================================
    public Page<InmuebleListDTO> buscarConFiltros(
            String direccion, List<String> tipoVivienda, List<Byte> tipoOperacion,
            Double precioMin, Double precioMax, Integer numHabMin, Integer numHabMax,
            Double metrosMin, Double metrosMax, List<String> comunidad, List<String> estado,
            Byte valido, Pageable pageable) {

        return inmuebleRepository.buscarConFiltros(
                direccion, tipoVivienda, tipoOperacion, precioMin, precioMax,
                numHabMin, numHabMax, metrosMin, metrosMax, comunidad, estado, valido,
                pageable);
    }

    // =======================================================================
    // MÉTODO PARA EL DASHBOARD - TOP 5 INMUEBLES MÁS POPULARES
    // ========================================================================
    public List<InmuebleDashboardDTO> findTop5InmueblesConteoInteresados() {

        // 1. Obtener los 5 Inmuebles más populares
        List<Inmueble> topInmuebles = inmuebleRepository.findTop5ByOrderByInteresados();

        // 2. Mapear y calcular el conteo de interesados para cada uno
        return topInmuebles.stream().map(inmueble -> {
            InmuebleDashboardDTO dto = new InmuebleDashboardDTO();
            dto.setId(inmueble.getId().longValue());
            dto.setTipoVivienda(inmueble.getTipoVivienda());
            dto.setDireccion(inmueble.getDireccion());
            dto.setPrecio(inmueble.getPrecio());
            // Usar el método auxiliar para obtener el conteo de interesados
            Integer numeroInteresados = inmuebleRepository.countInteresadosByInmuebleId(inmueble.getId());
            dto.setNumeroInteresados(numeroInteresados);
            return dto;
        }).collect(Collectors.toList());
    }

    // =======================================================================
    // MÉTODOS ADICIONALES DEL SERVICIO
    // =======================================================================

    public List<Inmueble> findAll() {
        return inmuebleRepository.findAll();
    }

    public Optional<Inmueble> findById(Integer id) {
        return inmuebleRepository.findById(id);
    }

    public Inmueble save(Inmueble inmueble) {
        return inmuebleRepository.save(inmueble);
    }

    public void deleteById(Integer id) {
        inmuebleRepository.deleteById(id);
    }

    public List<String> obtenerEstados() {
        return inmuebleRepository.findDistinctEstado();
    }

    public List<String> obtenerComunidades() {
        return inmuebleRepository.findDistinctComunidad();
    }

    public Object[] obtenerRangoPrecio() {
        return inmuebleRepository.obtenerRangoPrecio();
    }

    public List<String> obtenerTiposVivienda() {
        return inmuebleRepository.findDistinctTipoVivienda();
    }

    public int countInteresados(Integer inmuebleId) {
        return inmuebleRepository.countInteresadosByInmuebleId(inmuebleId);
    }
}