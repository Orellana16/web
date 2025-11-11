package com.jerezsurinmobiliaria.web.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "Citas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "Fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "Hora", nullable = false)
    private LocalTime hora;

    @Column(name = "Anotaciones", length = 255)
    private String anotaciones;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Inmueble_id", nullable = false)
    @ToString.Exclude
    private Inmueble inmueble;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Trabajador_id", nullable = false)
    @ToString.Exclude
    private Trabajador trabajador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Interesado_id", nullable = false)
    @ToString.Exclude
    private Interesado interesado;

    // Helper methods
    public Integer getInmuebleId() {
        return inmueble != null ? inmueble.getId() : null;
    }

    public Integer getTrabajadorId() {
        return trabajador != null ? trabajador.getId() : null;
    }

    public Integer getInteresadoId() {
        return interesado != null ? interesado.getId() : null;
    }
}