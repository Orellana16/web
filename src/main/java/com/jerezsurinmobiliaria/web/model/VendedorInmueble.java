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
    // Es buena práctica inicializarlo para evitar nulos al crear instancias nuevas
    private VendedorInmuebleId id = new VendedorInmuebleId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("vendedorId") // Busca la variable 'vendedorId' en VendedorInmuebleId
    @JoinColumn(name = "Vendedor_id") // Columna en la BD
    @ToString.Exclude
    private Vendedor vendedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("inmuebleId") // Busca la variable 'inmuebleId' en VendedorInmuebleId
    @JoinColumn(name = "Inmueble_id") // Columna en la BD
    @ToString.Exclude
    private Inmueble inmueble;

    @Column(name = "Representante", nullable = false, length = 100)
    private String representante;

    public VendedorInmueble(Vendedor vendedor, Inmueble inmueble, String representante) {
        this.vendedor = vendedor;
        this.inmueble = inmueble;
        this.representante = representante;
        this.id = new VendedorInmuebleId(vendedor.getId(), inmueble.getId());
    }
}