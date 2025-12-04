package com.jerezsurinmobiliaria.web.dto; 

import lombok.Data;

@Data
public class InmuebleDashboardDTO {

    private Long id;
    private String tipoVivienda;
    private String direccion;
    private Double precio;
    private Long numeroInteresados;

}