package com.pinguela.rentexpres.model;

public class EstadoAlquilerDTO extends ValueObject {


    private Integer id;       
    private String nombreEstado;  

    public EstadoAlquilerDTO() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombreEstado() {
        return nombreEstado;
    }

    public void setNombreEstado(String nombreEstado) {
        this.nombreEstado = nombreEstado;
    }
    @Override
    public String toString() {
        return nombreEstado;
    }

}
