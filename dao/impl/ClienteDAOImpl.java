package com.pinguela.rentexpres.dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.pinguela.rentexpres.dao.ClienteDAO;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.ClienteDTO;
import com.pinguela.rentexpres.model.ClienteCriteria;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.util.JDBCUtils;

public class ClienteDAOImpl implements ClienteDAO {

	private static final Logger logger = LogManager.getLogger(ClienteDAOImpl.class);
	private static final String CLIENTE_SELECT_BASE = "SELECT c.id_cliente, c.nombre, c.apellido1, c.apellido2, c.telefono, c.email, "
			+ "d.calle, d.numero, l.nombre_localidad, p.nombre_provincia " + "FROM cliente c "
			+ "LEFT JOIN direccion d ON c.id_direccion = d.id_direccion "
			+ "LEFT JOIN localidad l ON d.id_localidad = l.id_localidad "
			+ "LEFT JOIN provincia p ON l.id_provincia = p.id_provincia";

	@Override
	public ClienteDTO findById(Connection connection, Integer id) throws DataException {
		if (id == null) {
			logger.warn("findById null id.");
			return null;
		}
		String sql = CLIENTE_SELECT_BASE + " WHERE c.id_cliente = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				logger.info("Cliente found with id: {}", id);
				return loadCliente(rs);
			}
		} catch (SQLException e) {
			logger.error("Error en findById for id: {}", id, e);
			throw new DataException("Error en findById Cliente", e);
		} finally {
			JDBCUtils.close(ps, rs);
		}
		return null;
	}

	@Override
	public List<ClienteDTO> findAll(Connection connection) throws DataException {
		List<ClienteDTO> lista = new ArrayList<>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(CLIENTE_SELECT_BASE);
			rs = ps.executeQuery();
			while (rs.next()) {
				lista.add(loadCliente(rs));
			}
			logger.info("Total Clientes BD: {}", lista.size());
		} catch (SQLException e) {
			logger.error("Error en findAll Cliente", e);
			throw new DataException("Error en findAll Cliente", e);
		} finally {
			JDBCUtils.close(ps, rs);
		}
		return lista;
	}

	@Override
	public boolean create(Connection connection, ClienteDTO cliente) throws DataException {
		if (cliente == null) {
			logger.warn("create llamada con Cliente nulo.");
			return false;
		}
		String sql = "INSERT INTO cliente (nombre, apellido1, apellido2, fecha_nacimiento, email, telefono, id_direccion) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement ps = null;
		ResultSet generatedKeys = null;
		try {
			ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			setClienteParameters(ps, cliente, false);
			if (ps.executeUpdate() > 0) {
				generatedKeys = ps.getGeneratedKeys();
				if (generatedKeys.next()) {
					cliente.setId(generatedKeys.getInt(1));
				}
				logger.info("Cliente creado con éxito, id: {}", cliente.getId());
				return true;
			}
		} catch (SQLException e) {
			logger.error("Error en create Cliente", e);
			throw new DataException("Error en create Cliente", e);
		} finally {
			JDBCUtils.close(ps, generatedKeys);
		}
		return false;
	}

	@Override
	public boolean update(Connection connection, ClienteDTO cliente) throws DataException {
		if (cliente == null || cliente.getId() == null) {
			logger.warn("update llamado con Cliente o id nulo.");
			return false;
		}
		String sql = "UPDATE cliente SET nombre = ?, apellido1 = ?, apellido2 = ?, fecha_nacimiento = ?, "
				+ "email = ?, telefono = ?, id_direccion = ? WHERE id_cliente = ?";
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			setClienteParameters(ps, cliente, true);
			if (ps.executeUpdate() > 0) {
				logger.info("Cliente actualizado correctamente, id: {}", cliente.getId());
				return true;
			}
		} catch (SQLException e) {
			logger.error("Error en update Cliente", e);
			throw new DataException("Error en update Cliente", e);
		} finally {
			JDBCUtils.close(ps, null);
		}
		return false;
	}

	@Override
	public boolean delete(Connection connection, Integer id) throws DataException {
		if (id == null) {
			logger.warn("delete llamado con null id.");
			return false;
		}
		String sql = "DELETE FROM cliente WHERE id_cliente = ?";
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, id);
			if (ps.executeUpdate() > 0) {
				logger.info("Cliente eliminado, id: {}", id);
				return true;
			}
		} catch (SQLException e) {
			logger.error("Error en eliminar Cliente", e);
			throw new DataException("Error en eliminar Cliente", e);
		} finally {
			JDBCUtils.close(ps, null);
		}
		return false;
	}

	@Override
	public Results<ClienteDTO> findByCriteria(Connection connection, ClienteCriteria criteria) throws DataException {
		Results<ClienteDTO> results = new Results<>();
		List<ClienteDTO> lista = new ArrayList<>();
		int pageNumber = criteria.getPageNumber();
		int pageSize = criteria.getPageSize();
		int offset = (pageNumber - 1) * pageSize;

		StringBuilder sql = new StringBuilder();
		sql.append(CLIENTE_SELECT_BASE);
		sql.append(" WHERE 1=1 ");

		// FILTRO POR ID
		if (criteria.getId() != null && criteria.getId() > 0) {
			sql.append(" AND c.id_cliente = ? ");
		}
		// Resto de filtros (nombre, apellidos, email, tel, calle, numero, localidad,
		// provincia)
		if (criteria.getNombre() != null && !criteria.getNombre().isEmpty()) {
			sql.append(" AND c.nombre LIKE ? ");
		}
		if (criteria.getApellido1() != null && !criteria.getApellido1().isEmpty()) {
			sql.append(" AND c.apellido1 LIKE ? ");
		}
		if (criteria.getApellido2() != null && !criteria.getApellido2().isEmpty()) {
			sql.append(" AND c.apellido2 LIKE ? ");
		}
		if (criteria.getEmail() != null && !criteria.getEmail().isEmpty()) {
			sql.append(" AND c.email LIKE ? ");
		}
		if (criteria.getTelefono() != null && !criteria.getTelefono().isEmpty()) {
			sql.append(" AND c.telefono LIKE ? ");
		}
		if (criteria.getCalle() != null && !criteria.getCalle().isEmpty()) {
			sql.append(" AND d.calle LIKE ? ");
		}
		if (criteria.getNumero() != null && !criteria.getNumero().isEmpty()) {
			sql.append(" AND d.numero LIKE ? ");
		}
		if (criteria.getNombreLocalidad() != null && !criteria.getNombreLocalidad().isEmpty()) {
			sql.append(" AND l.nombre_localidad LIKE ? ");
		}
		if (criteria.getNombreProvincia() != null && !criteria.getNombreProvincia().isEmpty()) {
			sql.append(" AND p.nombre_provincia LIKE ? ");
		}
		sql.append(" ORDER BY c.nombre ");

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			int idx = 1;
			if (criteria.getId() != null && criteria.getId() > 0) {
				ps.setInt(idx++, criteria.getId());
			}
			if (criteria.getNombre() != null && !criteria.getNombre().isEmpty()) {
				ps.setString(idx++, "%" + criteria.getNombre() + "%");
			}
			if (criteria.getApellido1() != null && !criteria.getApellido1().isEmpty()) {
				ps.setString(idx++, "%" + criteria.getApellido1() + "%");
			}
			if (criteria.getApellido2() != null && !criteria.getApellido2().isEmpty()) {
				ps.setString(idx++, "%" + criteria.getApellido2() + "%");
			}
			if (criteria.getEmail() != null && !criteria.getEmail().isEmpty()) {
				ps.setString(idx++, "%" + criteria.getEmail() + "%");
			}
			if (criteria.getTelefono() != null && !criteria.getTelefono().isEmpty()) {
				ps.setString(idx++, "%" + criteria.getTelefono() + "%");
			}
			if (criteria.getCalle() != null && !criteria.getCalle().isEmpty()) {
				ps.setString(idx++, "%" + criteria.getCalle() + "%");
			}
			if (criteria.getNumero() != null && !criteria.getNumero().isEmpty()) {
				ps.setString(idx++, "%" + criteria.getNumero() + "%");
			}
			if (criteria.getNombreLocalidad() != null && !criteria.getNombreLocalidad().isEmpty()) {
				ps.setString(idx++, "%" + criteria.getNombreLocalidad() + "%");
			}
			if (criteria.getNombreProvincia() != null && !criteria.getNombreProvincia().isEmpty()) {
				ps.setString(idx++, "%" + criteria.getNombreProvincia() + "%");
			}

			rs = ps.executeQuery();
			if (rs.absolute(offset + 1)) {
				int count = 0;
				do {
					ClienteDTO dto = new ClienteDTO();
					dto.setId(rs.getInt("id_cliente"));
					dto.setNombre(rs.getString("nombre"));
					dto.setApellido1(rs.getString("apellido1"));
					dto.setApellido2(rs.getString("apellido2"));
					dto.setTelefono(rs.getString("telefono"));
					dto.setEmail(rs.getString("email"));
					dto.setCalle(rs.getString("calle"));
					dto.setNumero(rs.getString("numero"));
					dto.setNombreLocalidad(rs.getString("nombre_localidad"));
					dto.setNombreProvincia(rs.getString("nombre_provincia"));
					lista.add(dto);
					count++;
				} while (count < pageSize && rs.next());
			}
			rs.last();
			int totalRecords = rs.getRow();

			results.setResults(lista);
			results.setPageNumber(pageNumber);
			results.setPageSize(pageSize);
			results.setTotalRecords(totalRecords);

			logger.info("findByCriteria de Cliente completado: Página {} (Tamaño: {}), Total registros: {}", pageNumber,
					pageSize, totalRecords);
		} catch (SQLException e) {
			logger.error("Error en findByCriteria de Cliente", e);
			throw new DataException("Error en findByCriteria de Cliente", e);
		} finally {
			JDBCUtils.close(ps, rs);
		}
		return results;
	}

	private ClienteDTO loadCliente(ResultSet rs) throws SQLException {
		ClienteDTO c = new ClienteDTO();
		c.setId(rs.getInt("id_cliente"));
		c.setNombre(rs.getString("nombre"));
		c.setApellido1(rs.getString("apellido1"));
		c.setApellido2(rs.getString("apellido2"));
		c.setFechaNacimiento(rs.getString("fecha_nacimiento"));
		c.setEmail(rs.getString("email"));
		c.setTelefono(rs.getString("telefono"));
		c.setIdDireccion(rs.getInt("id_direccion"));
		c.setCalle(rs.getString("calle"));
		c.setNumero(rs.getString("numero"));
		c.setNombreLocalidad(rs.getString("nombre_localidad"));
		c.setNombreProvincia(rs.getString("nombre_provincia"));
		return c;
	}

	private void setClienteParameters(PreparedStatement ps, ClienteDTO cliente, boolean isUpdate) throws SQLException {
		ps.setString(1, cliente.getNombre());
		ps.setString(2, cliente.getApellido1());
		ps.setString(3, cliente.getApellido2());
		ps.setString(4, cliente.getFechaNacimiento());
		ps.setString(5, cliente.getEmail());
		ps.setString(6, cliente.getTelefono());
		ps.setInt(7, cliente.getIdDireccion());
		if (isUpdate) {
			ps.setInt(8, cliente.getId());
		}
	}
}
