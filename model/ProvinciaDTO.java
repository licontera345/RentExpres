package com.pinguela.rentexpres.model;

public class ProvinciaDTO extends ValueObject {

    private Integer id;      
    private String nombre;     

    public ProvinciaDTO() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
