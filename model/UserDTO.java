package com.pinguela.rentexpres.model;

import java.io.File;
import java.util.List;

/**
 * Clase DTO (Data Transfer Object) para representar la información de un
 * usuario en la aplicación. Incluye datos personales, credenciales y lista de
 * imágenes asociadas al usuario.
 * 
 * @author Jesús Félix
 * @version 1.0
 */
public class UserDTO extends ValueObject {

	/** Identificador único del usuario. */
	private Integer id;
	/** Nombre propio del usuario. */
	private String nombre;
	/** Primer apellido del usuario. */
	private String apellido1;
	/** Segundo apellido del usuario. */
	private String apellido2;
	/** Correo electrónico de contacto. */
	private String email;
	/** Número de teléfono del usuario. */
	private String telefono;
	/** Contraseña encriptada del usuario. */
	private String contrasena;
	/** Nombre de usuario (login) empleado para autenticación. */
	private String nombreUsuario;
	/** Identificador del tipo de usuario (por ejemplo, rol o perfil). */
	private Integer idTipoUsuario;
	/** Nombre legible del tipo de usuario. */
	private String nombreTipoUsuario;

	/** Lista de archivos de imágenes asociados al usuario (p. ej. avatar). */
	private List<File> imagenes;

	/**
	 * Constructor por defecto. Inicializa un objeto UserDTO vacío.
	 */
	public UserDTO() {
		super();
	}

	/**
	 * Devuelve el identificador único del usuario.
	 * 
	 * @return Integer con el ID del usuario, o null si no está asignado.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Establece el identificador único del usuario.
	 * 
	 * @param id Integer con el nuevo ID que se asignará.
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Devuelve el nombre propio del usuario.
	 * 
	 * @return String con el nombre del usuario.
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Establece el nombre propio del usuario.
	 * 
	 * @param nombre String con el nombre que se asignará.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Devuelve el primer apellido del usuario.
	 * 
	 * @return String con el primer apellido.
	 */
	public String getApellido1() {
		return apellido1;
	}

	/**
	 * Establece el primer apellido del usuario.
	 * 
	 * @param apellido1 String con el primer apellido a asignar.
	 */
	public void setApellido1(String apellido1) {
		this.apellido1 = apellido1;
	}

	/**
	 * Devuelve el segundo apellido del usuario.
	 * 
	 * @return String con el segundo apellido.
	 */
	public String getApellido2() {
		return apellido2;
	}

	/**
	 * Establece el segundo apellido del usuario.
	 * 
	 * @param apellido2 String con el segundo apellido a asignar.
	 */
	public void setApellido2(String apellido2) {
		this.apellido2 = apellido2;
	}

	/**
	 * Devuelve el correo electrónico del usuario.
	 * 
	 * @return String con el email de contacto.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Establece el correo electrónico del usuario.
	 * 
	 * @param email String con el email que se asignará.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Devuelve el número de teléfono del usuario.
	 * 
	 * @return String con el teléfono de contacto.
	 */
	public String getTelefono() {
		return telefono;
	}

	/**
	 * Establece el número de teléfono del usuario.
	 * 
	 * @param telefono String con el teléfono que se asignará.
	 */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	/**
	 * Devuelve la contraseña encriptada del usuario.
	 * 
	 * @return String con la contraseña encriptada.
	 */
	public String getContrasena() {
		return contrasena;
	}

	/**
	 * Establece la contraseña encriptada del usuario.
	 * 
	 * @param contrasena String con la contraseña encriptada.
	 */
	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	/**
	 * Devuelve el nombre de usuario para login.
	 * 
	 * @return String con el nombre de usuario.
	 */
	public String getNombreUsuario() {
		return nombreUsuario;
	}

	/**
	 * Establece el nombre de usuario (login) del usuario.
	 * 
	 * @param nombreUsuario String con el login que se asignará.
	 */
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	/**
	 * Devuelve el identificador del tipo de usuario (rol o perfil).
	 * 
	 * @return Integer con el ID del tipo de usuario.
	 */
	public Integer getIdTipoUsuario() {
		return idTipoUsuario;
	}

	/**
	 * Establece el identificador del tipo de usuario (rol o perfil).
	 * 
	 * @param idTipoUsuario Integer con el nuevo ID de tipo de usuario.
	 */
	public void setIdTipoUsuario(Integer idTipoUsuario) {
		this.idTipoUsuario = idTipoUsuario;
	}

	/**
	 * Devuelve el nombre legible del tipo de usuario (p. ej. "ADMIN", "CLIENTE").
	 * 
	 * @return String con el nombre del tipo de usuario.
	 */
	public String getNombreTipoUsuario() {
		return nombreTipoUsuario;
	}

	/**
	 * Establece el nombre legible del tipo de usuario (p. ej. "ADMIN", "CLIENTE").
	 * 
	 * @param nombreTipoUsuario String con el nombre que se asignará.
	 */
	public void setNombreTipoUsuario(String nombreTipoUsuario) {
		this.nombreTipoUsuario = nombreTipoUsuario;
	}

	/**
	 * Devuelve la lista de archivos de imágenes asociadas al usuario (p. ej.
	 * avatar).
	 * 
	 * @return List&lt;File&gt; con los ficheros de imagen, o null si no hay
	 *         ninguno.
	 */
	public List<File> getImagenes() {
		return imagenes;
	}

	/**
	 * Establece la lista de archivos de imágenes asociadas al usuario (p. ej.
	 * avatar).
	 * 
	 * @param imagenes List&lt;File&gt; con los ficheros de imagen a asignar.
	 */
	public void setImagenes(List<File> imagenes) {
		this.imagenes = imagenes;
	}
}
