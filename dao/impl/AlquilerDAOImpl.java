package com.pinguela.rentexpres.dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.dao.AlquilerDAO;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.RentalDTO;
import com.pinguela.rentexpres.model.RentalCriteria;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.util.JDBCUtils;

public class AlquilerDAOImpl implements AlquilerDAO {

	private static final Logger logger = LogManager.getLogger(AlquilerDAOImpl.class);

	private static final String SELECT_BASE = "SELECT a.id_alquiler, a.id_reserva, a.fecha_inicio_efectivo, a.fecha_fin_efectivo, "
			+ "a.km_inicial, a.km_final, a.id_estado_alquiler, a.coste_total, e.nombre_estado AS nombreEstado, "
			+ "r.id_vehiculo, v.placa, v.marca, v.modelo, "
			+ "r.id_cliente, c.nombre AS nombreCliente, c.apellido1, c.telefono " + "FROM alquiler a "
			+ "INNER JOIN estado_alquiler e ON a.id_estado_alquiler = e.id_estado_alquiler "
			+ "INNER JOIN reserva r ON a.id_reserva = r.id_reserva "
			+ "INNER JOIN vehiculo v ON r.id_vehiculo = v.id_vehiculo "
			+ "INNER JOIN cliente c ON r.id_cliente = c.id_cliente";

	@Override
	public RentalDTO findById(Connection connection, Integer id) throws DataException {
		if (id == null) {
			logger.warn("findById de Alquiler llamado con id nulo.");
			return null;
		}
		String sql = SELECT_BASE + " WHERE a.id_alquiler = ?";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					logger.info("Alquiler encontrado, id: {}", id);
					return loadAlquiler(rs);
				} else {
					logger.warn("No se encontró Alquiler con id: {}", id);
				}
			}
		} catch (SQLException e) {
			logger.error("Error en findById de Alquiler para id: {}", id, e);
			throw new DataException("Error en findById de Alquiler", e);
		}
		return null;
	}

	@Override
	public List<RentalDTO> findAll(Connection connection) throws DataException {
		List<RentalDTO> lista = new ArrayList<>();
		String sql = SELECT_BASE + " ORDER BY a.fecha_inicio_efectivo DESC";
		try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				lista.add(loadAlquiler(rs));
			}
			logger.info("Total de Alquileres encontrados: {}", lista.size());
		} catch (SQLException e) {
			logger.error("Error en findAll de Alquiler", e);
			throw new DataException("Error en findAll de Alquiler", e);
		}
		return lista;
	}

	@Override
	public boolean create(Connection connection, RentalDTO alquiler) throws DataException {
		if (alquiler == null) {
			logger.warn("create null Alquiler.");
			return false;
		}
		String sql = "INSERT INTO alquiler (id_reserva, fecha_inicio_efectivo, fecha_fin_efectivo, km_inicial, km_final, id_estado_alquiler, coste_total) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			setAlquilerParameters(ps, alquiler, false);
			if (ps.executeUpdate() > 0) {
				try (ResultSet gen = ps.getGeneratedKeys()) {
					if (gen.next()) {
						alquiler.setId(gen.getInt(1));
					}
				}
				logger.info("Alquiler creado exitosamente. Nuevo ID: {}", alquiler.getId());
				return true;
			}
		} catch (SQLException e) {
			logger.error("Error al crear Alquiler", e);
			throw new DataException("Error al crear Alquiler", e);
		}
		return false;
	}

	@Override
	public boolean update(Connection connection, RentalDTO alquiler) throws DataException {
		if (alquiler == null || alquiler.getId() == null) {
			logger.warn("update null Alquiler o id nulo.");
			return false;
		}
		String sql = "UPDATE alquiler SET fecha_inicio_efectivo = ?, fecha_fin_efectivo = ?, km_inicial = ?, km_final = ?, id_estado_alquiler = ?, coste_total = ? "
				+ "WHERE id_alquiler = ?";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			setAlquilerParameters(ps, alquiler, true);
			if (ps.executeUpdate() > 0) {
				logger.info("Alquiler actualizado exitosamente. ID: {}", alquiler.getId());
				return true;
			}
		} catch (SQLException e) {
			logger.error("Error al actualizar Alquiler", e);
			throw new DataException("Error al actualizar Alquiler", e);
		}
		return false;
	}

	@Override
	public boolean delete(Connection connection, Integer id) throws DataException {
		if (id == null) {
			logger.warn("delete null id de Alquiler.");
			return false;
		}
		String sql = "DELETE FROM alquiler WHERE id_alquiler = ?";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, id);
			if (ps.executeUpdate() > 0) {
				logger.info("Alquiler eliminado. ID: {}", id);
				return true;
			}
		} catch (SQLException e) {
			logger.error("Error al eliminar Alquiler con ID: {}", id, e);
			throw new DataException("Error al eliminar Alquiler con ID: " + id, e);
		}
		return false;
	}

	@Override
	public Results<RentalDTO> findByCriteria(Connection connection, RentalCriteria criteria) throws DataException {
		Results<RentalDTO> results = new Results<>();
		List<RentalDTO> lista = new ArrayList<>();
		int pageNumber = criteria.getPageNumber();
		int pageSize = criteria.getPageSize();
		int offset = (pageNumber - 1) * pageSize;

		StringBuilder sql = new StringBuilder(SELECT_BASE);
		sql.append(" WHERE 1=1 ");
		if (criteria.getIdAlquiler() != null)
			sql.append(" AND a.id_alquiler = ? ");
		if (criteria.getIdReserva() != null)
			sql.append(" AND a.id_reserva = ? ");
		if (criteria.getFechaInicioEfectivo() != null && !criteria.getFechaInicioEfectivo().isEmpty())
			sql.append(" AND a.fecha_inicio_efectivo LIKE ? ");
		if (criteria.getFechaFinEfectivo() != null && !criteria.getFechaFinEfectivo().isEmpty())
			sql.append(" AND a.fecha_fin_efectivo LIKE ? ");
		if (criteria.getKmInicial() != null)
			sql.append(" AND a.km_inicial >= ? ");
		if (criteria.getKmFinal() != null)
			sql.append(" AND a.km_final <= ? ");
		if (criteria.getIdEstadoAlquiler() != null)
			sql.append(" AND a.id_estado_alquiler = ? ");
		sql.append(" ORDER BY a.fecha_inicio_efectivo DESC LIMIT ? OFFSET ?");

		try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
			int idx = 1;
			if (criteria.getIdAlquiler() != null)
				ps.setInt(idx++, criteria.getIdAlquiler());
			if (criteria.getIdReserva() != null)
				ps.setInt(idx++, criteria.getIdReserva());
			if (criteria.getFechaInicioEfectivo() != null && !criteria.getFechaInicioEfectivo().isEmpty())
				ps.setString(idx++, "%" + criteria.getFechaInicioEfectivo() + "%");
			if (criteria.getFechaFinEfectivo() != null && !criteria.getFechaFinEfectivo().isEmpty())
				ps.setString(idx++, "%" + criteria.getFechaFinEfectivo() + "%");
			if (criteria.getKmInicial() != null)
				ps.setInt(idx++, criteria.getKmInicial());
			if (criteria.getKmFinal() != null)
				ps.setInt(idx++, criteria.getKmFinal());
			if (criteria.getIdEstadoAlquiler() != null)
				ps.setInt(idx++, criteria.getIdEstadoAlquiler());
			ps.setInt(idx++, pageSize);
			ps.setInt(idx++, offset);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					lista.add(loadAlquiler(rs));
				}
			}
		} catch (SQLException e) {
			logger.error("Error ejecutando búsqueda paginada de alquileres", e);
			throw new DataException("Error ejecutando búsqueda", e);
		}

		int totalRecords = 0;
		StringBuilder countSql = new StringBuilder("SELECT COUNT(*) FROM alquiler a ");
		countSql.append("INNER JOIN estado_alquiler e ON a.id_estado_alquiler = e.id_estado_alquiler ");
		countSql.append("INNER JOIN reserva r ON a.id_reserva = r.id_reserva ");
		countSql.append("INNER JOIN vehiculo v ON r.id_vehiculo = v.id_vehiculo ");
		countSql.append("INNER JOIN cliente c ON r.id_cliente = c.id_cliente ");
		countSql.append(" WHERE 1=1 ");
		if (criteria.getIdAlquiler() != null)
			countSql.append(" AND a.id_alquiler = ? ");
		if (criteria.getIdReserva() != null)
			countSql.append(" AND a.id_reserva = ? ");
		if (criteria.getFechaInicioEfectivo() != null && !criteria.getFechaInicioEfectivo().isEmpty())
			countSql.append(" AND a.fecha_inicio_efectivo LIKE ? ");
		if (criteria.getFechaFinEfectivo() != null && !criteria.getFechaFinEfectivo().isEmpty())
			countSql.append(" AND a.fecha_fin_efectivo LIKE ? ");
		if (criteria.getKmInicial() != null)
			countSql.append(" AND a.km_inicial >= ? ");
		if (criteria.getKmFinal() != null)
			countSql.append(" AND a.km_final <= ? ");
		if (criteria.getIdEstadoAlquiler() != null)
			countSql.append(" AND a.id_estado_alquiler = ? ");

		try (PreparedStatement countPs = connection.prepareStatement(countSql.toString())) {
			int idx = 1;
			if (criteria.getIdAlquiler() != null)
				countPs.setInt(idx++, criteria.getIdAlquiler());
			if (criteria.getIdReserva() != null)
				countPs.setInt(idx++, criteria.getIdReserva());
			if (criteria.getFechaInicioEfectivo() != null && !criteria.getFechaInicioEfectivo().isEmpty())
				countPs.setString(idx++, "%" + criteria.getFechaInicioEfectivo() + "%");
			if (criteria.getFechaFinEfectivo() != null && !criteria.getFechaFinEfectivo().isEmpty())
				countPs.setString(idx++, "%" + criteria.getFechaFinEfectivo() + "%");
			if (criteria.getKmInicial() != null)
				countPs.setInt(idx++, criteria.getKmInicial());
			if (criteria.getKmFinal() != null)
				countPs.setInt(idx++, criteria.getKmFinal());
			if (criteria.getIdEstadoAlquiler() != null)
				countPs.setInt(idx++, criteria.getIdEstadoAlquiler());

			try (ResultSet rs = countPs.executeQuery()) {
				if (rs.next()) {
					totalRecords = rs.getInt(1);
				}
			}
		} catch (SQLException e) {
			logger.error("Error ejecutando conteo total de alquileres", e);
			throw new DataException("Error ejecutando conteo total", e);
		}

		results.setResults(lista);
		results.setPageNumber(pageNumber);
		results.setPageSize(pageSize);
		results.setTotalRecords(totalRecords);
		return results;
	}

	private void setAlquilerParameters(PreparedStatement ps, RentalDTO alquiler, boolean isUpdate)
			throws SQLException {
		if (!isUpdate) {
			ps.setInt(1, alquiler.getIdReserva());
			ps.setString(2, alquiler.getFechaInicioEfectivo());
			ps.setString(3, alquiler.getFechaFinEfectivo());
			ps.setInt(4, alquiler.getKmInicial());
			ps.setInt(5, alquiler.getKmFinal());
			ps.setInt(6, alquiler.getIdEstadoAlquiler());
			ps.setInt(7, alquiler.getCostetotal());
		} else {
			ps.setString(1, alquiler.getFechaInicioEfectivo());
			ps.setString(2, alquiler.getFechaFinEfectivo());
			ps.setInt(3, alquiler.getKmInicial());
			ps.setInt(4, alquiler.getKmFinal());
			ps.setInt(5, alquiler.getIdEstadoAlquiler());
			ps.setInt(6, alquiler.getCostetotal());
			ps.setInt(7, alquiler.getId());
		}
	}

	private RentalDTO loadAlquiler(ResultSet rs) throws SQLException {
		RentalDTO dto = new RentalDTO();
		dto.setId(rs.getInt("id_alquiler"));
		dto.setIdReserva(rs.getInt("id_reserva"));
		dto.setFechaInicioEfectivo(rs.getString("fecha_inicio_efectivo"));
		dto.setFechaFinEfectivo(rs.getString("fecha_fin_efectivo"));
		dto.setKmInicial(rs.getInt("km_inicial"));
		dto.setKmFinal(rs.getInt("km_final"));
		dto.setIdEstadoAlquiler(rs.getInt("id_estado_alquiler"));
		dto.setCostetotal(rs.getInt("coste_total"));
		dto.setNombreEstado(rs.getString("nombreEstado"));
		dto.setIdVehiculo(rs.getInt("id_vehiculo"));
		dto.setPlaca(rs.getString("placa"));
		dto.setMarca(rs.getString("marca"));
		dto.setModelo(rs.getString("modelo"));
		dto.setIdCliente(rs.getInt("id_cliente"));
		dto.setNombre(rs.getString("nombreCliente"));
		dto.setApellido1(rs.getString("apellido1"));
		dto.setTelefono(rs.getString("telefono"));
		return dto;
	}

	@Override
	public boolean existsByReserva(Integer idReserva) throws DataException {
		final String sql = "SELECT COUNT(*) FROM alquiler WHERE id_reserva = ?";

		try (Connection conn = JDBCUtils.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, idReserva);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1) > 0;
				}
				return false;
			}
		} catch (SQLException e) {
			logger.error("Error comprobando existencia de alquiler por reserva", e);
			throw new DataException(e);
		}
	}

}
