package com.jerezsurinmobiliaria.web.dto; 

public class InmuebleDashboardDTO {

    private Long id;
    private String tipoVivienda;
    private String direccion;
    private Double precio;
    private Long numeroInteresados;

    // Constructor para mapear desde el servicio/controlador
    public InmuebleDashboardDTO(Long id, String tipoVivienda, String direccion, Double precio, Long numeroInteresados) {
        this.id = id;
        this.tipoVivienda = tipoVivienda;
        this.direccion = direccion;
        this.precio = precio;
        this.numeroInteresados = numeroInteresados;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTipoVivienda() { return tipoVivienda; }
    public void setTipoVivienda(String tipoVivienda) { this.tipoVivienda = tipoVivienda; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }

    public Long getNumeroInteresados() { return numeroInteresados; }
    public void setNumeroInteresados(Long numeroInteresados) { this.numeroInteresados = numeroInteresados; }
}