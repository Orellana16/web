package com.jerezsurinmobiliaria.web.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "propiedad_adicional")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
    @JoinColumn(name = "Inmueble_id", nullable = false)  // ← EXACTAMENTE ASÍ
    @ToString.Exclude
    private Inmueble inmueble;

    public PropiedadAdicional(String tipo, Double derrama, String ibi, Inmueble inmueble) {
        this.tipo = tipo;
        this.derrama = derrama;
        this.ibi = ibi;
        this.inmueble = inmueble;
    }

    public Integer getInmuebleId() {
        return inmueble != null ? inmueble.getId() : null;
    }

    public String getInmuebleDireccion() {
        return inmueble != null ? inmueble.getDireccion() : null;
    }
}