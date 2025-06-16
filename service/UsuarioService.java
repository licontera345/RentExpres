package com.pinguela.rentexpres.service;

import java.util.List;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.model.UsuarioCriteria;
import com.pinguela.rentexpres.model.UsuarioDTO;

/**
 * Interfaz para la capa de servicio de usuario. Define operaciones de negocio
 * para CRUD, autenticación y búsqueda por criterios, lanzando
 * RentexpresException en caso de fallo en la lógica o acceso a datos.
 * 
 * @author Jesús Félix
 * @version 1.0
 */
public interface UsuarioService {

	/**
	 * Recupera un usuario por su identificador.
	 * 
	 * @param id Identificador del usuario a buscar.
	 * @return {@link UsuarioDTO} con los datos del usuario o null si no existe.
	 * @throws RentexpresException Si ocurre un error en la capa de datos o lógica.
	 */
	public UsuarioDTO findById(Integer id) throws RentexpresException;

	/**
	 * Crea un nuevo usuario en el sistema.
	 * 
	 * @param usuario {@link UsuarioDTO} con la información del nuevo usuario.
	 * @return true si la creación fue exitosa; false en caso contrario.
	 * @throws RentexpresException Si ocurre un error en la capa de datos o lógica.
	 */
	public boolean create(UsuarioDTO usuario) throws RentexpresException;

	/**
	 * Actualiza los datos de un usuario existente.
	 * 
	 * @param usuario {@link UsuarioDTO} con la información actualizada (ID no
	 *                nulo).
	 * @return true si la actualización fue exitosa; false en caso contrario.
	 * @throws RentexpresException Si ocurre un error en la capa de datos o lógica.
	 */
	public boolean update(UsuarioDTO usuario) throws RentexpresException;

	/**
	 * Elimina un usuario del sistema.
	 * 
	 * @param usuario {@link UsuarioDTO} del usuario a eliminar (ID no nulo).
	 * @param id      Identificador del usuario (debe coincidir con
	 *                usuario.getId()).
	 * @return true si la eliminación fue exitosa; false en caso contrario.
	 * @throws RentexpresException Si ocurre un error en la capa de datos o lógica.
	 */
	public boolean delete(UsuarioDTO usuario, Integer id) throws RentexpresException;

	/**
	 * Autentica a un usuario con nombre de usuario y contraseña en texto claro.
	 * 
	 * @param nombreUsuario     Nombre de usuario para iniciar sesión.
	 * @param contrasenaEnClaro Contraseña en texto claro para comparar.
	 * @return {@link UsuarioDTO} con datos del usuario autenticado o null si falla.
	 * @throws RentexpresException Si ocurre un error en la capa de datos o lógica.
	 */
	public UsuarioDTO autenticar(String nombreUsuario, String contrasenaEnClaro) throws RentexpresException;

	/**
	 * Recupera todos los usuarios registrados en el sistema.
	 * 
	 * @return Lista de {@link UsuarioDTO} con todos los usuarios existentes.
	 * @throws RentexpresException Si ocurre un error en la capa de datos o lógica.
	 */
	public List<UsuarioDTO> findAll() throws RentexpresException;

	/**
	 * Busca usuarios según criterios de filtrado y paginación.
	 * 
	 * @param criteria {@link UsuarioCriteria} con filtros y parámetros de página.
	 * @return {@link Results}&lt;{@link UsuarioDTO}&gt; con la lista paginada y el
	 *         total.
	 * @throws RentexpresException Si ocurre un error en la capa de datos o lógica.
	 */
	public Results<UsuarioDTO> findByCriteria(UsuarioCriteria criteria) throws RentexpresException;
}
