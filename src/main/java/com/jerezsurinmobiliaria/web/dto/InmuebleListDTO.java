package com.jerezsurinmobiliaria.web.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor  // <--- Necesario para construir el DTO en el constructor de la consulta JPQL
@NoArgsConstructor  // <--- Necesario para que Spring/Jackson no fallen al serializar
public class InmuebleListDTO {

    private Integer id;
    private String direccion;
    private String tipoVivienda;
    private Byte tipoOperacion;
    private Double precio;
    private Integer numHab;
    private Double metrosCuadrados;
    private int cantidadPropiedadesAdicionales; // Nota: SIZE() devuelve Integer, Java lo convierte a int automáticamente
}