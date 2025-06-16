package com.pinguela.rentexpres.dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.dao.ReservaDAO;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.ReservaDTO;
import com.pinguela.rentexpres.model.ReservaCriteria;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.util.JDBCUtils;

public class ReservaDAOImpl implements ReservaDAO {

	private static final Logger logger = LogManager.getLogger(ReservaDAOImpl.class);

	private static final String RESERVA_SELECT_BASE = "SELECT r.id_reserva, r.id_vehiculo, r.id_cliente, r.fecha_inicio, r.fecha_fin, r.id_usuario, "
			+ "r.id_estado_reserva, e.nombre_estado, v.marca, v.placa, v.modelo, v.precio_dia, "
			+ "c.nombre, c.telefono, c.apellido1 " + "FROM reserva r "
			+ "INNER JOIN vehiculo v ON r.id_vehiculo = v.id_vehiculo "
			+ "INNER JOIN cliente c ON r.id_cliente = c.id_cliente "
			+ "INNER JOIN estado_reserva e ON r.id_estado_reserva = e.id_estado_reserva";

	@Override
	public ReservaDTO findById(Connection connection, Integer id) throws DataException {
		if (id == null) {
			logger.warn("findById null id.");
			return null;
		}
		String sql = RESERVA_SELECT_BASE + " WHERE r.id_reserva = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				logger.info("Reserva id: {}", id);
				return loadReservaDTO(rs);
			} else {
				logger.warn("No se encontró Reserva con id: {}", id);
			}
		} catch (SQLException e) {
			logger.error("Error en findById for id: {}", id, e);
			throw new DataException("Error en findById Reserva", e);
		} finally {
			JDBCUtils.close(ps, rs);
		}
		return null;
	}

	@Override
	public List<ReservaDTO> findAll(Connection connection) throws DataException {
		List<ReservaDTO> lista = new ArrayList<>();
		String sql = RESERVA_SELECT_BASE + " ORDER BY r.fecha_inicio DESC";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				lista.add(loadReservaDTO(rs));
			}
			logger.info("Total Reservas found: {}", lista.size());
		} catch (SQLException e) {
			logger.error("Error en findAll Reserva", e);
			throw new DataException("Error en findAll Reserva", e);
		} finally {
			JDBCUtils.close(ps, rs);
		}
		return lista;
	}

	@Override
	public boolean create(Connection connection, ReservaDTO reserva) throws DataException {
		if (reserva == null) {
			logger.warn("create null Reserva.");
			return false;
		}

		String sql = "INSERT INTO reserva (id_vehiculo, id_cliente, fecha_inicio, fecha_fin, id_usuario, id_estado_reserva) VALUES (?, ?, ?, ?, ?, ?)";
		PreparedStatement ps = null;
		ResultSet generatedKeys = null;
		try {
			ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			setReservaParameters(ps, reserva, false);
			if (ps.executeUpdate() > 0) {
				generatedKeys = ps.getGeneratedKeys();
				if (generatedKeys.next()) {
					reserva.setId(generatedKeys.getInt(1));
				}
				logger.info("Reserva creada exitosamente, id: {}", reserva.getId());
				return true;
			}
		} catch (SQLException e) {
			logger.error("Error en create Reserva", e);
			throw new DataException("Error en create Reserva", e);
		} finally {
			JDBCUtils.close(ps, generatedKeys);
		}
		return false;
	}

	@Override
	public boolean update(Connection connection, ReservaDTO reserva) throws DataException {
		if (reserva == null || reserva.getId() == null) {
			logger.warn("update null Reserva or id.");
			return false;
		}
		String sql = "UPDATE reserva SET id_vehiculo = ?, id_cliente = ?, fecha_inicio = ?, fecha_fin = ?, id_usuario = ?, id_estado_reserva = ? WHERE id_reserva = ?";
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			setReservaParameters(ps, reserva, true);
			if (ps.executeUpdate() > 0) {
				logger.info("Reserva updated, id: {}", reserva.getId());
				return true;
			}
		} catch (SQLException e) {
			logger.error("Error en update Reserva", e);
			throw new DataException("Error en update Reserva", e);
		} finally {
			JDBCUtils.close(ps, null);
		}
		return false;
	}

	@Override
	public boolean delete(Connection connection, Integer id) throws DataException {
		if (id == null) {
			logger.warn("delete null id.");
			return false;
		}
		String sql = "DELETE FROM reserva WHERE id_reserva = ?";
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, id);
			if (ps.executeUpdate() > 0) {
				logger.info("Reserva deleted, id: {}", id);
				return true;
			}
		} catch (SQLException e) {
			logger.error("Error en delete Reserva", e);
			throw new DataException("Error en delete Reserva", e);
		} finally {
			JDBCUtils.close(ps, null);
		}
		return false;
	}

	@Override
	public Results<ReservaDTO> findByCriteria(Connection connection, ReservaCriteria criteria) throws DataException {
		Results<ReservaDTO> results = new Results<>();
		List<ReservaDTO> lista = new ArrayList<>();
		int pageNumber = criteria.getPageNumber();
		int pageSize = criteria.getPageSize();
		int offset = (pageNumber - 1) * pageSize;

		StringBuilder sql = new StringBuilder(RESERVA_SELECT_BASE);
		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM reserva r ");
		sql.append(" WHERE 1=1 ");
		sqlCount.append(" WHERE 1=1 ");

		List<Object> params = new ArrayList<>();

		if (criteria.getId() != null) {
			sql.append(" AND r.id_reserva = ? ");
			sqlCount.append(" AND r.id_reserva = ? ");
			params.add(criteria.getId());
		}
		if (criteria.getIdVehiculo() != null) {
			sql.append(" AND r.id_vehiculo = ? ");
			sqlCount.append(" AND r.id_vehiculo = ? ");
			params.add(criteria.getIdVehiculo());
		}
		if (criteria.getIdCliente() != null) {
			sql.append(" AND r.id_cliente = ? ");
			sqlCount.append(" AND r.id_cliente = ? ");
			params.add(criteria.getIdCliente());
		}
		if (criteria.getFechaInicio() != null && !criteria.getFechaInicio().isEmpty()) {
			sql.append(" AND r.fecha_inicio LIKE ? ");
			sqlCount.append(" AND r.fecha_inicio LIKE ? ");
			params.add("%" + criteria.getFechaInicio() + "%");
		}
		if (criteria.getFechaFin() != null && !criteria.getFechaFin().isEmpty()) {
			sql.append(" AND r.fecha_fin LIKE ? ");
			sqlCount.append(" AND r.fecha_fin LIKE ? ");
			params.add("%" + criteria.getFechaFin() + "%");
		}
		if (criteria.getMarca() != null && !criteria.getMarca().isEmpty()) {
			sql.append(" AND v.marca LIKE ? ");
			sqlCount.append(" AND r.id_vehiculo IN (SELECT id_vehiculo FROM vehiculo v WHERE marca LIKE ?) ");
			params.add("%" + criteria.getMarca() + "%");
		}
		if (criteria.getPlaca() != null && !criteria.getPlaca().isEmpty()) {
			sql.append(" AND v.placa LIKE ? ");
			sqlCount.append(" AND r.id_vehiculo IN (SELECT id_vehiculo FROM vehiculo v WHERE placa LIKE ?) ");
			params.add("%" + criteria.getPlaca() + "%");
		}
		if (criteria.getModelo() != null && !criteria.getModelo().isEmpty()) {
			sql.append(" AND v.modelo LIKE ? ");
			sqlCount.append(" AND r.id_vehiculo IN (SELECT id_vehiculo FROM vehiculo v WHERE modelo LIKE ?) ");
			params.add("%" + criteria.getModelo() + "%");
		}
		if (criteria.getPrecioDia() != null) {
			sql.append(" AND v.precio_dia <= ? ");
			sqlCount.append(" AND r.id_vehiculo IN (SELECT id_vehiculo FROM vehiculo v WHERE precio_dia <= ?) ");
			params.add(criteria.getPrecioDia());
		}
		if (criteria.getNombre() != null && !criteria.getNombre().isEmpty()) {
			sql.append(" AND c.nombre LIKE ? ");
			sqlCount.append(" AND r.id_cliente IN (SELECT id_cliente FROM cliente c WHERE nombre LIKE ?) ");
			params.add("%" + criteria.getNombre() + "%");
		}
		if (criteria.getTelefono() != null && !criteria.getTelefono().isEmpty()) {
			sql.append(" AND c.telefono LIKE ? ");
			sqlCount.append(" AND r.id_cliente IN (SELECT id_cliente FROM cliente c WHERE telefono LIKE ?) ");
			params.add("%" + criteria.getTelefono() + "%");
		}
		if (criteria.getApellido1() != null && !criteria.getApellido1().isEmpty()) {
			sql.append(" AND c.apellido1 LIKE ? ");
			sqlCount.append(" AND r.id_cliente IN (SELECT id_cliente FROM cliente c WHERE apellido1 LIKE ?) ");
			params.add("%" + criteria.getApellido1() + "%");
		}

		sql.append(" ORDER BY r.fecha_inicio DESC LIMIT ? OFFSET ?");
		params.add(pageSize);
		params.add(offset);

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql.toString());
			for (int i = 0; i < params.size(); i++) {
				ps.setObject(i + 1, params.get(i));
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				lista.add(loadReservaDTO(rs));
			}
			JDBCUtils.close(ps, rs);

			// COUNT
			ps = connection.prepareStatement(sqlCount.toString());
			for (int i = 0; i < params.size() - 2; i++) { // -2 porque LIMIT y OFFSET no van en el COUNT
				ps.setObject(i + 1, params.get(i));
			}
			rs = ps.executeQuery();
			int totalRecords = 0;
			if (rs.next()) {
				totalRecords = rs.getInt(1);
			}

			results.setResults(lista);
			results.setPageNumber(pageNumber);
			results.setPageSize(pageSize);
			results.setTotalRecords(totalRecords);

			logger.info("findByCriteria de Reserva completado: Página {} (Tamaño: {}), Total registros: {}",
					pageNumber, pageSize, totalRecords);
		} catch (SQLException e) {
			logger.error("Error en findByCriteria de Reserva", e);
			throw new DataException("Error en findByCriteria de Reserva", e);
		} finally {
			JDBCUtils.close(ps, rs);
		}
		return results;
	}

	private void setReservaParameters(PreparedStatement ps, ReservaDTO reserva, boolean isUpdate) throws SQLException {
		ps.setInt(1, reserva.getIdVehiculo());
		ps.setInt(2, reserva.getIdCliente());
		ps.setString(3, reserva.getFechaInicio());
		ps.setString(4, reserva.getFechaFin());
		ps.setInt(5, reserva.getIdUsuario());
		if (reserva.getIdEstadoReserva() != null) {
			ps.setInt(6, reserva.getIdEstadoReserva());
		} else {
			ps.setNull(6, java.sql.Types.INTEGER);
		}
		if (isUpdate) {
			ps.setInt(7, reserva.getId());
		}
	}

	private ReservaDTO loadReservaDTO(ResultSet rs) throws SQLException {
		ReservaDTO dto = new ReservaDTO();
		dto.setId(rs.getInt("id_reserva"));
		dto.setIdVehiculo(rs.getInt("id_vehiculo"));
		dto.setIdCliente(rs.getInt("id_cliente"));
		dto.setFechaInicio(rs.getString("fecha_inicio"));
		dto.setFechaFin(rs.getString("fecha_fin"));
		dto.setIdUsuario(rs.getInt("id_usuario"));
		dto.setIdEstadoReserva(rs.getInt("id_estado_reserva"));
		dto.setNombreEstadoReserva(rs.getString("nombre_estado"));
		dto.setMarca(rs.getString("marca"));
		dto.setPlaca(rs.getString("placa"));
		dto.setModelo(rs.getString("modelo"));
		dto.setPrecioDia(rs.getDouble("precio_dia"));
		dto.setNombre(rs.getString("nombre"));
		dto.setTelefono(rs.getString("telefono"));
		dto.setApellido1(rs.getString("apellido1"));
		return dto;
	}
}
