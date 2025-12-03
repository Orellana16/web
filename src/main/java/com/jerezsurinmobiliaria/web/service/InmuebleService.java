package com.jerezsurinmobiliaria.web.service;

import com.jerezsurinmobiliaria.web.dto.InmuebleListDTO;
// ⭐ Importar el DTO para el Dashboard (Asegúrate de que el paquete sea correcto)
import com.jerezsurinmobiliaria.web.dto.InmuebleDashboardDTO; 

import com.jerezsurinmobiliaria.web.model.Inmueble;
import com.jerezsurinmobiliaria.web.repository.InmuebleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors; // Necesario para el mapeo con Streams

@Service
@RequiredArgsConstructor
public class InmuebleService {

    private final InmuebleRepository inmuebleRepository;

    // --- MÉTODOS EXISTENTES (Sin cambios relevantes) ---
    
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

    // ⭐ Método para buscar con filtros (sin cambios)
    @Transactional(readOnly = true)
    public Page<InmuebleListDTO> buscarConFiltros(
            String direccion, List<String> tipoVivienda, List<Byte> tipoOperacion,
            Double precioMin, Double precioMax, Integer numHabMin, Integer numHabMax,
            Double metrosMin, Double metrosMax, List<String> comunidad, List<String> estado,
            Byte valido, Pageable pageable) {
        
        return inmuebleRepository.buscarConFiltros(
            direccion, tipoVivienda, tipoOperacion, precioMin, precioMax,
            numHabMin, numHabMax, metrosMin, metrosMax, comunidad, estado, valido,
            pageable
        );
    }

    // OBTENER VALORES ÚNICOS PARA FILTROS (Sin cambios)
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
    public Object[] obtenerRangoPrecio() {
        return inmuebleRepository.obtenerRangoPrecio();
    }

    // ESTADÍSTICAS
    @Transactional(readOnly = true)
    public Long contarInmuebles() {
        return inmuebleRepository.count();
    }

    // Método auxiliar para el conteo de interesados (útil para el mapeo)
    @Transactional(readOnly = true)
    public Long countInteresados(Integer inmuebleId) {
        return inmuebleRepository.countInteresadosByInmuebleId(inmuebleId);
    }

    // Método para obtener los 5 más populares
    @Transactional(readOnly = true)
    public List<Inmueble> findTop5ByOrderByInteresados() {
        return inmuebleRepository.findTop5ByOrderByInteresados();
    }

    // Obtiene y mapea los datos al DTO para el Dashboard
    @Transactional(readOnly = true)
    public List<InmuebleDashboardDTO> findTop5InmueblesConteoInteresados() {
        
        // 1. Obtener los 5 Inmuebles más populares
        List<Inmueble> topInmuebles = findTop5ByOrderByInteresados(); 

        // 2. Mapear y calcular el conteo de interesados para cada uno
        return topInmuebles.stream()
            .map(inmueble -> {
                // LLAMAMOS AL CONTEO POR ID (Problema N+1, pero encapsulamos la lógica aquí)
                Long numeroInteresados = countInteresados(inmueble.getId());
                
                // Creamos el DTO usando el constructor @AllArgsConstructor de Lombok
                return new InmuebleDashboardDTO(
                    inmueble.getId().longValue(), // Asumiendo que tu DTO usa Long
                    inmueble.getTipoVivienda(),
                    inmueble.getDireccion(),
                    inmueble.getPrecio(), // Asegúrate de que el tipo de precio coincida (BigDecimal o Double)
                    numeroInteresados 
                );
            })
            .collect(Collectors.toList());
    }
}