package com.pinguela.rentexpres.model;

public class VehicleStatusDTO extends ValueObject {

	private Integer id;
	private String nombreEstado;

	public VehicleStatusDTO() {
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

	public String toString() {
		return nombreEstado != null ? nombreEstado : "";
	}
}
