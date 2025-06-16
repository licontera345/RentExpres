package com.pinguela.rentexpres.model;

import java.io.Serializable;

public class VehiculoDTO extends ValueObject implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String marca;
	private String modelo;
	private Integer anioFabricacion;
	private Double precioDia;
	private String placa;
	private String numeroBastidor;
	private Integer kilometrajeActual;
	private Integer idEstadoVehiculo;
	private Integer idCategoria;
	private String nombreEstadoVehiculo;
	private String nombreCategoria;
	private String imagenPath;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public Integer getAnioFabricacion() {
		return anioFabricacion;
	}

	public void setAnioFabricacion(Integer anioFabricacion) {
		this.anioFabricacion = anioFabricacion;
	}

	public Double getPrecioDia() {
		return precioDia;
	}

	public void setPrecioDia(Double precioDia) {
		this.precioDia = precioDia;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public String getNumeroBastidor() {
		return numeroBastidor;
	}

	public void setNumeroBastidor(String numeroBastidor) {
		this.numeroBastidor = numeroBastidor;
	}

	public Integer getKilometrajeActual() {
		return kilometrajeActual;
	}

	public void setKilometrajeActual(Integer kilometrajeActual) {
		this.kilometrajeActual = kilometrajeActual;
	}

	public Integer getIdEstadoVehiculo() {
		return idEstadoVehiculo;
	}

	public void setIdEstadoVehiculo(Integer idEstadoVehiculo) {
		this.idEstadoVehiculo = idEstadoVehiculo;
	}

	public Integer getIdCategoria() {
		return idCategoria;
	}

	public void setIdCategoria(Integer idCategoria) {
		this.idCategoria = idCategoria;
	}

	public String getNombreEstadoVehiculo() {
		return nombreEstadoVehiculo;
	}

	public void setNombreEstadoVehiculo(String nombreEstadoVehiculo) {
		this.nombreEstadoVehiculo = nombreEstadoVehiculo;
	}

	public String getNombreCategoria() {
		return nombreCategoria;
	}

	public void setNombreCategoria(String nombreCategoria) {
		this.nombreCategoria = nombreCategoria;
	}

	public String getImagenPath() {
		return imagenPath;
	}

	public void setImagenPath(String imagenPath) {
		this.imagenPath = imagenPath;
	}
}
