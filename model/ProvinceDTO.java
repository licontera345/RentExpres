package com.pinguela.rentexpres.model;

public class ProvinceDTO extends ValueObject {

    private Integer id;      
    private String nombre;     

    public ProvinceDTO() {
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
