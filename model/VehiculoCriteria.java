package com.pinguela.rentexpres.model;

public class VehiculoCriteria {

	private String marca;
	private String modelo;
	private Integer anioDesde;
	private Integer anioHasta;
	private Double precioMax;
	private String placa;
	private String numeroBastidor;
	private Integer kilometrajeActual;

	// estado vehiculos
	private Integer idEstadoVehiculo;
	private String nombreEstado;

	// categoria
	private Integer idCategoria;
	private String nombreCategoria;

	private Integer pageNumber;
	private Integer pageSize;

	public String getNombreEstado() {
		return nombreEstado;
	}

	public void setNombreEstado(String nombreEstado) {
		this.nombreEstado = nombreEstado;
	}

	public String getNombreCategoria() {
		return nombreCategoria;
	}

	public void setNombreCategoria(String nombreCategoria) {
		this.nombreCategoria = nombreCategoria;
	}

	public Integer getPageNumber() {
		return pageNumber;
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

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public VehiculoCriteria() {
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

	public Integer getAnioDesde() {
		return anioDesde;
	}

	public void setAnioDesde(Integer anioDesde) {
		this.anioDesde = anioDesde;
	}

	public Integer getAnioHasta() {
		return anioHasta;
	}

	public void setAnioHasta(Integer anioHasta) {
		this.anioHasta = anioHasta;
	}

	public Double getPrecioMax() {
		return precioMax;
	}

	public void setPrecioMax(Double precioMax) {
		this.precioMax = precioMax;
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

}
