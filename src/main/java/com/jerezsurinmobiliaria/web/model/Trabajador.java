package com.jerezsurinmobiliaria.web.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Trabajadores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trabajador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "Nombre", nullable = false, length = 45)
    private String nombre;

    @Column(name = "Apellido", nullable = false, length = 45)
    private String apellido;

    @Column(name = "NIF", nullable = false, unique = true, length = 10)
    private String nif;

    @Column(name = "Contacto", length = 45)
    private String contacto;

    @Column(name = "Puesto", length = 45)
    private String puesto;

    @OneToMany(mappedBy = "trabajador", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Cita> citas = new ArrayList<>();

    // Helper method
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }
}