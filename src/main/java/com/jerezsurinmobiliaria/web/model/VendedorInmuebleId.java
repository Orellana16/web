package com.jerezsurinmobiliaria.web.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendedorInmuebleId implements Serializable {

    private Integer vendedorId;
    private Integer inmuebleId;
}