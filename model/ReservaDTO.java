package com.pinguela.rentexpres.model;

public class ReservaDTO extends ValueObject {

	//reserva
	private Integer id;
	private String fechaInicio;
	private String fechaFin;

	// usuario
	private Integer idUsuario;
	private String nombreUsuario;

	// vehiculo
	private Integer idVehiculo;
	private String marca;
	private String placa;
	private String modelo;
	private Double precioDia;

	// cliente
	private Integer idCliente;
	private String nombre;
	private String telefono;
	private String apellido1;

	// estado
	private Integer idEstadoReserva;
	private String nombreEstado;

	private boolean selected;

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public ReservaDTO() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIdVehiculo() {
		return idVehiculo;
	}

	public void setIdVehiculo(Integer idVehiculo) {
		this.idVehiculo = idVehiculo;
	}

	public Integer getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}

	public String getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public String getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public Double getPrecioDia() {
		return precioDia;
	}

	public void setPrecioDia(Double precioDia) {
		this.precioDia = precioDia;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getApellido1() {
		return apellido1;
	}

	public void setApellido1(String apellido1) {
		this.apellido1 = apellido1;
	}

	public Integer getIdEstadoReserva() {
		return idEstadoReserva != null ? idEstadoReserva : 1;
	}

	public void setIdEstadoReserva(Integer idEstadoReserva) {
		this.idEstadoReserva = idEstadoReserva;
	}

	public void setMatricula(String matricula) {
		this.placa = matricula;
	}

	public String getNombreEstado() {
		return nombreEstado;
	}

	public void setNombreEstadoReserva(String nombreEstado) {
		this.nombreEstado = nombreEstado;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}
	@Override
	public String toString() {
	    return nombreEstado;
	}

}
