package com.pinguela.rentexpres.dao;

import java.sql.Connection;
import java.util.List;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.model.UsuarioCriteria;
import com.pinguela.rentexpres.model.UsuarioDTO;

/**
 * Interfaz DAO para operaciones CRUD y de búsqueda de objetos UsuarioDTO. Todas
 * las operaciones reciben una conexión JDBC y, en caso de error, lanzan
 * DataException.
 *
 * @author Jesús Félix
 * @version 1.0
 */
public interface UsuarioDAO {

	/**
	 * Busca un usuario por su identificador.
	 *
	 * @param connection Conexión JDBC usada para la operación.
	 * @param id         Identificador del usuario a buscar.
	 * @return UsuarioDTO con los datos del usuario encontrado, o null si no existe.
	 * @throws DataException Si ocurre un error en la consulta.
	 */
	public UsuarioDTO findById(Connection connection, Integer id) throws DataException;

	/**
	 * Crea un nuevo usuario en la base de datos.
	 *
	 * @param connection Conexión JDBC usada para la operación.
	 * @param usuario    DTO con la información del usuario a insertar.
	 * @return true si la inserción tuvo éxito, false en caso contrario.
	 * @throws DataException Si ocurre un error al insertar.
	 */
	public boolean create(Connection connection, UsuarioDTO usuario) throws DataException;

	/**
	 * Actualiza los datos de un usuario existente.
	 *
	 * @param connection Conexión JDBC usada para la operación.
	 * @param usuario    DTO con la información actualizada del usuario.
	 * @return true si la actualización tuvo éxito, false en caso contrario.
	 * @throws DataException Si ocurre un error al actualizar.
	 */
	public boolean update(Connection connection, UsuarioDTO usuario) throws DataException;

	/**
	 * Elimina un usuario de la base de datos.
	 *
	 * @param connection Conexión JDBC usada para la operación.
	 * @param usuario    DTO del usuario (puede usarse para auditoría).
	 * @param id         Identificador del usuario a eliminar.
	 * @return true si la eliminación tuvo éxito, false en caso contrario.
	 * @throws DataException Si ocurre un error al eliminar.
	 */
	public boolean delete(Connection connection, UsuarioDTO usuario, Integer id) throws DataException;

	/**
	 * Recupera todos los usuarios en la base de datos.
	 *
	 * @param connection Conexión JDBC usada para la operación.
	 * @return Lista de UsuarioDTO con todos los usuarios existentes.
	 * @throws DataException Si ocurre un error en la consulta.
	 */
	public List<UsuarioDTO> findAll(Connection connection) throws DataException;

	/**
	 * Autentica a un usuario en función de nombre de usuario y contraseña en claro.
	 *
	 * @param connection        Conexión JDBC usada para la operación.
	 * @param nombreUsuario     Nombre de usuario a autenticar.
	 * @param contrasenaEnClaro Contraseña en texto claro para comparar.
	 * @return UsuarioDTO con los datos del usuario autenticado, o null si falla la
	 *         autenticación.
	 * @throws DataException Si ocurre un error en la consulta.
	 */
	public UsuarioDTO autenticar(Connection connection, String nombreUsuario, String contrasenaEnClaro)
			throws DataException;

	/**
	 * Busca usuarios según criterios de paginación y filtros.
	 *
	 * @param connection Conexión JDBC usada para la operación.
	 * @param criteria   Objeto UsuarioCriteria con los filtros y la paginación.
	 * @return Objeto Results<UsuarioDTO> con la lista de usuarios y la información
	 *         de total de registros.
	 * @throws DataException Si ocurre un error en la consulta.
	 */
	public Results<UsuarioDTO> findByCriteria(Connection connection, UsuarioCriteria criteria) throws DataException;
}
