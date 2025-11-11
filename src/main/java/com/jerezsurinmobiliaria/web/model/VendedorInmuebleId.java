package com.jerezsurinmobiliaria.web.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendedorInmuebleId implements Serializable {

    @Column(name = "Vendedor_id")
    private Integer vendedorId;

    @Column(name = "Inmueble_id")
    private Integer inmuebleId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VendedorInmuebleId that = (VendedorInmuebleId) o;
        return Objects.equals(vendedorId, that.vendedorId) &&
               Objects.equals(inmuebleId, that.inmuebleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vendedorId, inmuebleId);
    }
}