package com.jerezsurinmobiliaria.web.dto;

import com.jerezsurinmobiliaria.web.model.Inmueble;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class EstadisticasPageDTO {
    // Tarjetas superiores
    private long totalInmuebles;
    private long totalCitas;
    private long totalInteresados;
    private Double valorCartera;

    // Gráficos
    private List<String> chartLabels;
    private List<Long> chartData;

    // Tabla de Inmuebles Populares
    private List<Inmueble> topInmuebles;
}