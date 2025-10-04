package com.pinguela.rentexpres.model;

public class CategoriaVehicleDTO extends ValueObject {


    private Integer id;          
    private String nombreCategoria; 

    public CategoriaVehicleDTO() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }
    public String toString() {
        return nombreCategoria != null ? nombreCategoria : "";
    }
}
