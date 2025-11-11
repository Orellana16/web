package com.jerezsurinmobiliaria.web.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Entity
@Table(name = "Vendedores_has_Inmuebles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendedorInmueble implements Serializable {

    @EmbeddedId
    private VendedorInmuebleId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("vendedorId")
    @JoinColumn(name = "Vendedor_id")
    @ToString.Exclude
    private Vendedor vendedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("inmuebleId")
    @JoinColumn(name = "Inmueble_id")
    @ToString.Exclude
    private Inmueble inmueble;

    @Column(name = "Representante", nullable = false, length = 100)
    private String representante;

    // Constructor helper
    public VendedorInmueble(Vendedor vendedor, Inmueble inmueble, String representante) {
        this.vendedor = vendedor;
        this.inmueble = inmueble;
        this.representante = representante;
        this.id = new VendedorInmuebleId(vendedor.getId(), inmueble.getId());
    }
}