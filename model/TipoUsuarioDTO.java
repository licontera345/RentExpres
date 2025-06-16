package com.pinguela.rentexpres.model;

public class TipoUsuarioDTO extends ValueObject {
 

    private Integer id;    
    private String nombreTipo; 

    public TipoUsuarioDTO() {
        super();
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombreTipo() {
        return nombreTipo;
    }
    public void setNombreTipo(String nombreTipo) {
        this.nombreTipo = nombreTipo;
    }
}
