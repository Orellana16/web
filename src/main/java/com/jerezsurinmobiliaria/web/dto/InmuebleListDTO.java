package com.jerezsurinmobiliaria.web.dto; // Puedes crear una nueva carpeta 'dto'

import lombok.Data;

@Data // Genera getters, setters, etc.
public class InmuebleListDTO {

    private Integer id;
    private String direccion;
    private String tipoVivienda;
    private Byte tipoOperacion;
    private Double precio;
    private Integer numHab;
    private Double metrosCuadrados;
    private int cantidadPropiedadesAdicionales;
}