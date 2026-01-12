package com.jerezsurinmobiliaria.web.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.jerezsurinmobiliaria.web.dto.InmuebleListDTO;
import com.jerezsurinmobiliaria.web.model.Inmueble;
import com.jerezsurinmobiliaria.web.repository.InmuebleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InmuebleService {

    private final InmuebleRepository inmuebleRepository;

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