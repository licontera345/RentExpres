package com.pinguela.rentexpres.model;

public class DireccionDTO extends ValueObject {


    private Integer id;    
    private Integer idLocalidad;  
    private String calle;
    private String numero;

    private String nombreLocalidad;
    private String nombreProvincia;

    public DireccionDTO() {
        super();
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdLocalidad() {
        return idLocalidad;
    }
    public void setIdLocalidad(Integer idLocalidad) {
        this.idLocalidad = idLocalidad;
    }

    public String getCalle() {
        return calle;
    }
    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumero() {
        return numero;
    }
    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNombreLocalidad() {
        return nombreLocalidad;
    }
    public void setNombreLocalidad(String nombreLocalidad) {
        this.nombreLocalidad = nombreLocalidad;
    }

    public String getNombreProvincia() {
        return nombreProvincia;
    }
    public void setNombreProvincia(String nombreProvincia) {
        this.nombreProvincia = nombreProvincia;
    }
}
