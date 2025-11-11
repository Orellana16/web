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
    private int cantidadPropiedadesAdicionales; // ⭐ Aquí guardaremos el conteo

    // Constructor que convierte una Entidad Inmueble a este DTO
    public InmuebleListDTO(Integer id, String direccion, String tipoVivienda, Byte tipoOperacion,
            Double precio, Integer numHab, Double metrosCuadrados,
            int cantidadPropiedadesAdicionales) {
        this.id = id;
        this.direccion = direccion;
        this.tipoVivienda = tipoVivienda;
        this.tipoOperacion = tipoOperacion;
        this.precio = precio;
        this.numHab = numHab;
        this.metrosCuadrados = metrosCuadrados;
        this.cantidadPropiedadesAdicionales = cantidadPropiedadesAdicionales;
    }
}