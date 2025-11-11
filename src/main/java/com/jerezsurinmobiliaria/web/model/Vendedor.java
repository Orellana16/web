package com.jerezsurinmobiliaria.web.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Vendedores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vendedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "Nombre", nullable = false, length = 45)
    private String nombre;

    @Column(name = "Apellido", nullable = false, length = 45)
    private String apellido;

    @Column(name = "Contacto", nullable = false, length = 45)
    private String contacto;

    @Column(name = "Demanda", length = 255)
    private String demanda;

    @Column(name = "Direccion", nullable = false, length = 100)
    private String direccion;

    @Column(name = "CP", nullable = false, length = 10)
    private String cp;

    @Column(name = "NIF", nullable = false, unique = true, length = 10)
    private String nif;

    @OneToMany(mappedBy = "vendedor", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<VendedorInmueble> vendedorInmuebles = new ArrayList<>();

    // Helper method
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }
}