package com.pinguela.rentexpres.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jasypt.util.password.StrongPasswordEncryptor;

import com.pinguela.rentexpres.dao.UsuarioDAO;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.model.UsuarioCriteria;
import com.pinguela.rentexpres.model.UsuarioDTO;
import com.pinguela.rentexpres.util.JDBCUtils;

/**
 * Implementación de UsuarioDAO que gestiona el acceso a la tabla "usuario" en
 * la base de datos mediante JDBC. Incluye operaciones CRUD, búsqueda por
 * criterios y autenticación, utilizando JDBCUtils para cerrar recursos y
 * StrongPasswordEncryptor para el cifrado de contraseñas.
 *
 * @author Jesús Félix
 * @version 1.0
 */
public class UsuarioDAOImpl implements UsuarioDAO {

	/** Logger de la clase para mensajes de información y error. */
	private static final Logger logger = LogManager.getLogger(UsuarioDAOImpl.class);

	/** Objeto para encriptar y verificar contraseñas con Jasypt. */
	private static final StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();

	/** Base SELECT que obtiene todos los campos del usuario. */
	private static final String USUARIO_SELECT_BASE = "SELECT id_usuario, nombre_usuario, contrasena, id_tipo_usuario, nombre, apellido1, apellido2, telefono, email "
			+ "FROM usuario";

	/**
	 * {@inheritDoc}
	 * <p>
	 * Busca un usuario por su identificador. Si el id es null, devuelve null y
	 * registra una advertencia en el logger.
	 * </p>
	 *
	 * @param connection Conexión JDBC usada para la operación.
	 * @param id         Identificador del usuario a buscar.
	 * @return UsuarioDTO con los datos del usuario encontrado, o null si no existe
	 *         o si el id es null.
	 * @throws DataException Si ocurre un error SQL al realizar la consulta.
	 */
	@Override
	public UsuarioDTO findById(Connection connection, Integer id) throws DataException {
		if (id == null) {
			logger.warn("findById called with null id.");
			return null;
		}
		String sql = USUARIO_SELECT_BASE + " WHERE id_usuario = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				logger.info("Usuario encontrado con id: {}", id);
				return loadUsuario(rs, false);
			}
		} catch (SQLException e) {
			logger.error("Error al buscar Usuario por ID: {}", id, e);
			throw new DataException("Error al buscar Usuario por ID: " + id, e);
		} finally {
			JDBCUtils.close(ps, rs);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Inserta un nuevo registro en la tabla "usuario". Se devuelve true si la
	 * operación tuvo éxito y se asigna el id generado al DTO.
	 * </p>
	 *
	 * @param connection Conexión JDBC usada para la operación.
	 * @param usuario    DTO con la información del usuario a insertar.
	 * @return true si la inserción tuvo éxito (y se obtuvo un id generado), false
	 *         si el DTO era null o no se pudo insertar.
	 * @throws DataException Si ocurre un error SQL al insertar.
	 */
	@Override
	public boolean create(Connection connection, UsuarioDTO usuario) throws DataException {
		if (usuario == null) {
			logger.warn("create called with null Usuario.");
			return false;
		}
		String sql = "INSERT INTO usuario (nombre_usuario, contrasena, id_tipo_usuario, nombre, apellido1, apellido2, telefono, email) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			setUsuarioParameters(ps, usuario, false);
			if (ps.executeUpdate() > 0) {
				try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						usuario.setId(generatedKeys.getInt(1));
					}
				}
				logger.info("Usuario creado exitosamente, nombre_usuario: {}", usuario.getNombreUsuario());
				return true;
			}
		} catch (SQLException e) {
			logger.error("Error al crear Usuario: {}", usuario.getNombreUsuario(), e);
			throw new DataException("Error al crear Usuario: " + usuario.getNombreUsuario(), e);
		} finally {
			JDBCUtils.close(ps, null);
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Actualiza un registro existente de la tabla "usuario". Si el DTO o su id es
	 * null, devuelve false y registra advertencia.
	 * </p>
	 *
	 * @param connection Conexión JDBC usada para la operación.
	 * @param usuario    DTO con la información actualizada del usuario.
	 * @return true si la actualización tuvo éxito, false si el DTO es null, su id
	 *         es null o la actualización no afectó filas.
	 * @throws DataException Si ocurre un error SQL al actualizar.
	 */
	@Override
	public boolean update(Connection connection, UsuarioDTO usuario) throws DataException {
		if (usuario == null || usuario.getId() == null) {
			logger.warn("update called with null Usuario or id.");
			return false;
		}
		String sql = "UPDATE usuario SET nombre_usuario = ?, contrasena = ?, id_tipo_usuario = ?, nombre = ?, "
				+ "apellido1 = ?, apellido2 = ?, telefono = ?, email = ? WHERE id_usuario = ?";
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			setUsuarioParameters(ps, usuario, true);
			if (ps.executeUpdate() > 0) {
				logger.info("Usuario actualizado exitosamente, id: {}", usuario.getId());
				return true;
			}
		} catch (SQLException e) {
			logger.error("Error al actualizar Usuario: {}", usuario.getId(), e);
			throw new DataException("Error al actualizar Usuario: " + usuario.getId(), e);
		} finally {
			JDBCUtils.close(ps, null);
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Elimina un registro de la tabla "usuario". Si el DTO es null o su id es null,
	 * devuelve false y registra advertencia.
	 * </p>
	 *
	 * @param connection Conexión JDBC usada para la operación.
	 * @param usuario    DTO del usuario (puede usarse para auditoría).
	 * @param id         Identificador del usuario a eliminar (se ignora aquí, ya
	 *                   que se usa usuario.getId()).
	 * @return true si la eliminación tuvo éxito, false si el DTO o su id es null, o
	 *         si no se afectaron filas.
	 * @throws DataException Si ocurre un error SQL al eliminar.
	 */
	@Override
	public boolean delete(Connection connection, UsuarioDTO usuario, Integer id) throws DataException {
		if (usuario == null || usuario.getId() == null) {
			logger.warn("delete called with null Usuario or id.");
			return false;
		}
		String sql = "DELETE FROM usuario WHERE id_usuario = ?";
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, usuario.getId());
			if (ps.executeUpdate() > 0) {
				logger.info("Usuario eliminado, id: {}", usuario.getId());
				return true;
			}
		} catch (SQLException e) {
			logger.error("Error al eliminar Usuario: {}", usuario.getId(), e);
			throw new DataException("Error al eliminar Usuario: " + usuario.getId(), e);
		} finally {
			JDBCUtils.close(ps, null);
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Recupera todos los usuarios en la tabla "usuario".
	 * </p>
	 *
	 * @param connection Conexión JDBC usada para la operación.
	 * @return Lista de UsuarioDTO con todos los usuarios existentes.
	 * @throws DataException Si ocurre un error SQL al recuperar datos.
	 */
	@Override
	public List<UsuarioDTO> findAll(Connection connection) throws DataException {
		List<UsuarioDTO> usuarios = new ArrayList<>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(USUARIO_SELECT_BASE);
			rs = ps.executeQuery();
			while (rs.next()) {
				usuarios.add(loadUsuario(rs, false));
			}
		} catch (SQLException e) {
			logger.error("Error al obtener todos los Usuarios", e);
			throw new DataException("Error al obtener todos los Usuarios", e);
		} finally {
			JDBCUtils.close(ps, rs);
		}
		return usuarios;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Autentica a un usuario buscando por nombre de usuario y verificando la
	 * contraseña encriptada. Si el nombre o la contraseña en claro son null,
	 * devuelve null.
	 * </p>
	 *
	 * @param connection        Conexión JDBC usada para la operación.
	 * @param nombreUsuario     Nombre de usuario a autenticar.
	 * @param contrasenaEnClaro Contraseña en texto claro para comparar.
	 * @return UsuarioDTO con los datos del usuario autenticado, o null si falla la
	 *         autenticación o alguno de los parámetros es null.
	 * @throws DataException Si ocurre un error SQL al autenticar.
	 */
	@Override
	public UsuarioDTO autenticar(Connection connection, String nombreUsuario, String contrasenaEnClaro)
			throws DataException {
		if (nombreUsuario == null || contrasenaEnClaro == null) {
			logger.warn("autenticar called with null parameters.");
			return null;
		}
		String sql = USUARIO_SELECT_BASE + " WHERE nombre_usuario = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setString(1, nombreUsuario);
			rs = ps.executeQuery();
			if (rs.next() && passwordEncryptor.checkPassword(contrasenaEnClaro, rs.getString("contrasena"))) {
				logger.info("Usuario autenticado: {}", nombreUsuario);
				return loadUsuario(rs, true);
			}
		} catch (SQLException e) {
			logger.error("Error al autenticar Usuario: {}", nombreUsuario, e);
			throw new DataException("Error al autenticar Usuario: " + nombreUsuario, e);
		} finally {
			JDBCUtils.close(ps, rs);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Busca usuarios según criterios de paginación y filtros: nombre de usuario,
	 * nombre, apellidos, email y teléfono. Carga todos los resultados y luego
	 * aplica paginación en memoria.
	 * </p>
	 *
	 * @param connection Conexión JDBC usada para la operación.
	 * @param criteria   Objeto UsuarioCriteria con los filtros y la paginación.
	 * @return Objeto Results<UsuarioDTO> con la lista de usuarios paginada y la
	 *         información de total de registros.
	 * @throws DataException Si ocurre un error SQL al realizar la consulta.
	 */
	@Override
	public Results<UsuarioDTO> findByCriteria(Connection connection, UsuarioCriteria criteria) throws DataException {
		Results<UsuarioDTO> results = new Results<>();
		List<UsuarioDTO> listaCompleta = new ArrayList<>();

		StringBuilder sql = new StringBuilder(USUARIO_SELECT_BASE);
		sql.append(" WHERE 1=1 ");
		if (criteria.getNombreUsuario() != null && !criteria.getNombreUsuario().isEmpty()) {
			sql.append(" AND nombre_usuario LIKE ? ");
		}
		if (criteria.getNombre() != null && !criteria.getNombre().isEmpty()) {
			sql.append(" AND nombre LIKE ? ");
		}
		if (criteria.getApellido1() != null && !criteria.getApellido1().isEmpty()) {
			sql.append(" AND apellido1 LIKE ? ");
		}
		if (criteria.getApellido2() != null && !criteria.getApellido2().isEmpty()) {
			sql.append(" AND apellido2 LIKE ? ");
		}
		if (criteria.getEmail() != null && !criteria.getEmail().isEmpty()) {
			sql.append(" AND email LIKE ? ");
		}
		if (criteria.getTelefono() != null && !criteria.getTelefono().isEmpty()) {
			sql.append(" AND telefono LIKE ? ");
		}
		sql.append(" ORDER BY id_usuario ");

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql.toString());
			int index = 1;
			if (criteria.getNombreUsuario() != null && !criteria.getNombreUsuario().isEmpty()) {
				ps.setString(index++, "%" + criteria.getNombreUsuario() + "%");
			}
			if (criteria.getNombre() != null && !criteria.getNombre().isEmpty()) {
				ps.setString(index++, "%" + criteria.getNombre() + "%");
			}
			if (criteria.getApellido1() != null && !criteria.getApellido1().isEmpty()) {
				ps.setString(index++, "%" + criteria.getApellido1() + "%");
			}
			if (criteria.getApellido2() != null && !criteria.getApellido2().isEmpty()) {
				ps.setString(index++, "%" + criteria.getApellido2() + "%");
			}
			if (criteria.getEmail() != null && !criteria.getEmail().isEmpty()) {
				ps.setString(index++, "%" + criteria.getEmail() + "%");
			}
			if (criteria.getTelefono() != null && !criteria.getTelefono().isEmpty()) {
				ps.setString(index++, "%" + criteria.getTelefono() + "%");
			}

			rs = ps.executeQuery();
			while (rs.next()) {
				listaCompleta.add(loadUsuario(rs, false));
			}

			int page = criteria.getPageNumber() <= 0 ? 1 : criteria.getPageNumber();
			int size = criteria.getPageSize() <= 0 ? 25 : criteria.getPageSize();

			int totalRecords = listaCompleta.size();
			int offset = (page - 1) * size;
			int toIndex = Math.min(offset + size, totalRecords);
			List<UsuarioDTO> paginatedList = new ArrayList<>();
			if (offset < totalRecords && offset >= 0) {
				paginatedList = listaCompleta.subList(offset, toIndex);
			}

			results.setResults(paginatedList);
			results.setPageNumber(page);
			results.setPageSize(size);
			results.setTotalRecords(totalRecords);

			logger.info("findByCriteria de Usuario completado: Página {} (Tamaño: {}), Total registros: {}", page, size,
					totalRecords);

		} catch (SQLException e) {
			logger.error("Error en findByCriteria de Usuario", e);
			throw new DataException("Error en findByCriteria de Usuario", e);
		} finally {
			JDBCUtils.close(ps, rs);
		}
		return results;
	}

	/**
	 * Carga un objeto UsuarioDTO a partir del ResultSet actual. Si el parámetro
	 * authenticated es true, no asigna la contraseña en el DTO (para no exponerla).
	 *
	 * @param rs            ResultSet apuntando a la fila actual con datos de
	 *                      usuario.
	 * @param authenticated Si es true, omite la contraseña en la instancia DTO.
	 * @return UsuarioDTO recién creado con todos los campos (salvo contraseña si
	 *         authenticated).
	 * @throws SQLException Si ocurre un error al leer datos del ResultSet.
	 */
	private UsuarioDTO loadUsuario(ResultSet rs, boolean authenticated) throws SQLException {
		UsuarioDTO usuario = new UsuarioDTO();
		usuario.setId(rs.getInt("id_usuario"));
		usuario.setNombreUsuario(rs.getString("nombre_usuario"));
		usuario.setIdTipoUsuario(rs.getInt("id_tipo_usuario"));
		usuario.setNombre(rs.getString("nombre"));
		usuario.setApellido1(rs.getString("apellido1"));
		usuario.setApellido2(rs.getString("apellido2"));
		usuario.setTelefono(rs.getString("telefono"));
		usuario.setEmail(rs.getString("email"));
		usuario.setContrasena(authenticated ? null : rs.getString("contrasena"));
		return usuario;
	}

	/**
	 * Establece los parámetros del PreparedStatement según los datos del DTO. Si
	 * isUpdate es true, añade al final el id para la cláusula WHERE.
	 *
	 * @param ps       PreparedStatement preparado con la consulta SQL.
	 * @param usuario  UsuarioDTO que contiene los valores a asignar.
	 * @param isUpdate True si se va a ejecutar un UPDATE (añade parámetro id al
	 *                 final).
	 * @throws SQLException Si ocurre un error al asignar los parámetros.
	 */
	private void setUsuarioParameters(PreparedStatement ps, UsuarioDTO usuario, boolean isUpdate) throws SQLException {
		ps.setString(1, usuario.getNombreUsuario());
		ps.setString(2, passwordEncryptor.encryptPassword(usuario.getContrasena()));
		ps.setInt(3, usuario.getIdTipoUsuario());
		ps.setString(4, usuario.getNombre());
		ps.setString(5, usuario.getApellido1());
		ps.setString(6, usuario.getApellido2());
		ps.setString(7, usuario.getTelefono());
		ps.setString(8, usuario.getEmail());
		if (isUpdate) {
			ps.setInt(9, usuario.getId());
		}
	}
}
