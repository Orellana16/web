package com.jerezsurinmobiliaria.web.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Inmuebles")
@Data
public class Inmueble {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "Direccion", nullable = false, length = 45)
    private String direccion;

    @Column(name = "Tipo_Operacion", nullable = false)
    private Byte tipoOperacion;

    @Column(name = "Precio", nullable = false)
    private Double precio;

    @Column(name = "Num_Hab", nullable = false)
    private Integer numHab;

    @Column(name = "Num_Baños", nullable = false)
    private Integer numBanos;

    @Column(name = "Metros_Cuadrados", nullable = false)
    private Double metrosCuadrados;

    @Column(name = "Estado", nullable = false, length = 45)
    private String estado;

    @Column(name = "Compartido", nullable = false)
    private Boolean compartido;

    @Column(name = "Anotaciones", length = 255)
    private String anotaciones;

    @Column(name = "Comunidad", nullable = false, length = 45)
    private String comunidad;

    @Column(name = "Certificado_Energia", nullable = false, length = 45)
    private String certificadoEnergia;

    @Column(name = "IBI", nullable = false, length = 45)
    private String ibi;

    @Column(name = "Nota_Simple", nullable = false, length = 45)
    private String notaSimple;

    @Column(name = "Valido", nullable = false)
    private Byte valido;

    @Column(name = "Tipo_Vivienda", nullable = false, length = 45)
    private String tipoVivienda;

    // =========================================================================
    // RELACIONES (Set en lugar de List para evitar MultipleBagFetchException)
    // =========================================================================
    
    @OneToMany(mappedBy = "inmueble", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<PropiedadAdicional> propiedadesAdicionales;

    @OneToMany(mappedBy = "inmueble", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Cita> citas;

    @OneToMany(mappedBy = "inmueble", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<VendedorInmueble> vendedorInmuebles = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "Inmuebles_has_Interesados",
        joinColumns = @JoinColumn(name = "Inmueble_id"),
        inverseJoinColumns = @JoinColumn(name = "Interesado_id")
    )
    @ToString.Exclude
    private Set<Interesado> interesados = new HashSet<>();
}