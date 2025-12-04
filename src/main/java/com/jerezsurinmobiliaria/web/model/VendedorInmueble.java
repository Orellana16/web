package com.jerezsurinmobiliaria.web.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "Vendedores_has_Inmuebles")
@Data
public class VendedorInmueble {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Vendedor_id")
    @ToString.Exclude
    private Vendedor vendedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Inmueble_id")
    @ToString.Exclude
    private Inmueble inmueble;

    @Column(name = "Representante", nullable = false, length = 100)
    private String representante;
}