package com.jerezsurinmobiliaria.web.service;

import com.jerezsurinmobiliaria.web.dto.EstadisticasPageDTO;
import com.jerezsurinmobiliaria.web.model.Inmueble;
import com.jerezsurinmobiliaria.web.repository.InmuebleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EstadisticasService {

    private final InmuebleRepository inmuebleRepository;
    
    // private final CitaRepository citaRepository; // DESCOMENTAR CUANDO LO TENGAS
    // private final InteresadoRepository interesadoRepository; // DESCOMENTAR CUANDO LO TENGAS

    @Transactional(readOnly = true)
    public EstadisticasPageDTO generarEstadisticas() {

        // 1. KPIs Superiores (Consultas optimizadas)
        long totalInmuebles = inmuebleRepository.count();
        Double valorCartera = inmuebleRepository.sumarPrecioTotal();

        // TODO: Cambiar por citaRepository.count() cuando exista
        long totalCitas = 0; 
        
        // TODO: Cambiar por interesadoRepository.count() cuando exista
        long totalInteresados = 0; 

        // 2. Datos para el Gráfico (Transformación de DB a Listas para JS)
        List<Object[]> resultadosGrafica = inmuebleRepository.contarInmueblesPorTipo();
        List<String> labels = new ArrayList<>();
        List<Long> data = new ArrayList<>();

        for (Object[] fila : resultadosGrafica) {
            // Control de nulos por seguridad
            String tipo = fila[0] != null ? fila[0].toString() : "Sin Tipo";
            Long cantidad = (Long) fila[1];
            
            labels.add(tipo);
            data.add(cantidad);
        }

        // 3. Top Inmuebles
        // Usamos la query nativa en vez de traer todos y ordenar en Java
        List<Inmueble> topInmuebles = inmuebleRepository.findTop5ByOrderByInteresados();

        // 4. Construir DTO
        return EstadisticasPageDTO.builder()
                .totalInmuebles(totalInmuebles)
                .totalCitas(totalCitas)
                .totalInteresados(totalInteresados)
                .valorCartera(valorCartera)
                .chartLabels(labels)
                .chartData(data)
                .topInmuebles(topInmuebles)
                .build();
    }
}