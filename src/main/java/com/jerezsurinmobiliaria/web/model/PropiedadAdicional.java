package com.jerezsurinmobiliaria.web.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "propiedad_adicional")
@Data
public class PropiedadAdicional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "Tipo", nullable = false, length = 45)
    private String tipo;

    @Column(name = "Derrama", nullable = false)
    private Double derrama;

    @Column(name = "IBI", nullable = false, length = 45)
    private String ibi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Inmueble_id", nullable = false)
    @ToString.Exclude
    private Inmueble inmueble;

    public Integer getInmuebleId() {
        return inmueble != null ? inmueble.getId() : null;
    }

    public String getInmuebleDireccion() {
        return inmueble != null ? inmueble.getDireccion() : null;
    }
}