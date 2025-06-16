package com.pinguela.rentexpres.model;

public class AlquilerDTO extends ValueObject {

	// alquiler
	private Integer id;
	private String fechaInicioEfectivo;
	private String fechaFinEfectivo;
	private Integer kmInicial;
	private Integer kmFinal;
	private Integer costetotal;

	// reserva
	private Integer idReserva;

	// cliente
	private Integer idCliente;
	private String nombre;
	private String apellido1;
	private String telefono;

	// usuario
	private Integer idUsuario;
	private String nombreUsuario;

	// estado
	private Integer idEstadoAlquiler;
	private String nombreEstado;

	// vehiculo
	private Integer idVehiculo;
	private String marca;
	private String placa;
	private String modelo;
	private Double precioDia;

	public AlquilerDTO() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIdReserva() {
		return idReserva;
	}

	public void setIdReserva(Integer idReserva) {
		this.idReserva = idReserva;
	}

	public String getFechaInicioEfectivo() {
		return fechaInicioEfectivo;
	}

	public void setFechaInicioEfectivo(String fechaInicioEfectivo) {
		this.fechaInicioEfectivo = fechaInicioEfectivo;
	}

	public String getFechaFinEfectivo() {
		return fechaFinEfectivo;
	}

	public void setFechaFinEfectivo(String fechaFinEfectivo) {
		this.fechaFinEfectivo = fechaFinEfectivo;
	}

	public Integer getKmInicial() {
		return kmInicial;
	}

	public void setKmInicial(Integer kmInicial) {
		this.kmInicial = kmInicial;
	}

	public Integer getKmFinal() {
		return kmFinal;
	}

	public void setKmFinal(Integer kmFinal) {
		this.kmFinal = kmFinal;
	}

	public Integer getIdEstadoAlquiler() {
		return idEstadoAlquiler;
	}

	public void setIdEstadoAlquiler(Integer idEstadoAlquiler) {
		this.idEstadoAlquiler = idEstadoAlquiler;
	}

	public Integer getCostetotal() {
		return costetotal;
	}

	public void setCostetotal(Integer costetotal) {
		this.costetotal = costetotal;
	}

	public String getNombreEstado() {
		return nombreEstado;
	}

	public void setNombreEstado(String nombreEstado) {
		this.nombreEstado = nombreEstado;
	}

	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public Integer getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido1() {
		return apellido1;
	}

	public void setApellido1(String apellido) {
		this.apellido1 = apellido;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public Integer getIdVehiculo() {
		return idVehiculo;
	}

	public void setIdVehiculo(Integer idVehiculo) {
		this.idVehiculo = idVehiculo;
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
	
	@Override
	public String toString() {
	    return nombreEstado;
	}


}
