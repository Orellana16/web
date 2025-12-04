package com.jerezsurinmobiliaria.web.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Interesados")
@Data
public class Interesado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "Nombre", nullable = false, length = 45)
    private String nombre;

    @Column(name = "Apellidos", nullable = false, length = 45)
    private String apellidos;

    @Column(name = "Contacto", nullable = false, length = 45)
    private String contacto;

    @Column(name = "Demanda", length = 255)
    private String demanda;

    @Column(name = "Direccion", length = 100)
    private String direccion;

    @Column(name = "CP", length = 10)
    private String cp;

    @Column(name = "NIF", unique = true, length = 10)
    private String nif;

    @OneToMany(mappedBy = "interesado", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Cita> citas = new ArrayList<>();

    @ManyToMany(mappedBy = "interesados")
    @ToString.Exclude
    private List<Inmueble> inmueblesInteres = new ArrayList<>();

    // Helper method
    public String getNombreCompleto() {
        return nombre + " " + apellidos;
    }
}