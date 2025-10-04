package com.pinguela.rentexpres.dao.impl;

import com.pinguela.rentexpres.dao.VehiculoDAO;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.model.VehicleCriteria;
import com.pinguela.rentexpres.model.VehicleDTO;
import com.pinguela.rentexpres.util.JDBCUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehiculoDAOImpl implements VehiculoDAO {

	private static final Logger logger = LogManager.getLogger(VehiculoDAOImpl.class);

	private static final String VEHICULO_SELECT_BASE = "SELECT v.id_vehiculo, v.marca, v.modelo, v.anio_fabricacion, v.precio_dia, "
			+ "v.placa, v.numero_bastidor, v.kilometraje_actual, v.id_estado_vehiculo, "
			+ "ev.nombre_estado AS nombreEstadoVehiculo, v.id_categoria, c.nombre_categoria AS nombreCategoria "
			+ "FROM vehiculo v " + "INNER JOIN estado_vehiculo ev ON v.id_estado_vehiculo = ev.id_estado_vehiculo "
			+ "INNER JOIN categoria_vehiculo c ON v.id_categoria = c.id_categoria";

	@Override
	public VehicleDTO findById(Connection connection, Integer id) throws DataException {
		VehicleDTO vehiculo = null;
		String sql = VEHICULO_SELECT_BASE + " WHERE v.id_vehiculo = ?";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					vehiculo = loadVehicleDTO(rs);
					logger.info("Vehículo encontrado con ID: {}", id);
				}
			}
		} catch (SQLException e) {
			logger.error("Error al buscar Vehículo por ID: {}", id, e);
			throw new DataException("Error al buscar Vehículo por ID: " + id, e);
		}
		return vehiculo;
	}

	@Override
	public List<VehicleDTO> findAll(Connection connection) throws DataException {
		List<VehicleDTO> lista = new ArrayList<>();
		String sql = VEHICULO_SELECT_BASE;
		try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				lista.add(loadVehicleDTO(rs));
			}
			logger.info("Total de Vehículos encontrados: {}", lista.size());
		} catch (SQLException e) {
			logger.error("Error en findAll de Vehículo", e);
			throw new DataException("Error en findAll de Vehículo", e);
		}
		return lista;
	}

	@Override
	public boolean create(Connection connection, VehicleDTO vehiculo) throws DataException {
		String sql = "INSERT INTO vehiculo (marca, modelo, anio_fabricacion, id_categoria, id_estado_vehiculo, "
				+ "placa, precio_dia, numero_bastidor, kilometraje_actual) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			setVehiculoParameters(ps, vehiculo);
			int rows = ps.executeUpdate();
			if (rows > 0) {
				try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						vehiculo.setId(generatedKeys.getInt(1));
					}
				}
				logger.info("Vehículo creado exitosamente. ID: {}", vehiculo.getId());
				return true;
			}
		} catch (SQLException e) {
			logger.error("Error al crear Vehículo", e);
			throw new DataException("Error al crear Vehículo", e);
		} finally {
			JDBCUtils.close(ps, null);
		}
		return false;
	}

	@Override
	public boolean update(Connection connection, VehicleDTO vehiculo) throws DataException {
		String sql = "UPDATE vehiculo SET marca = ?, modelo = ?, anio_fabricacion = ?, id_categoria = ?, "
				+ "id_estado_vehiculo = ?, placa = ?, precio_dia = ?, numero_bastidor = ?, "
				+ "kilometraje_actual = ?, WHERE id_vehiculo = ?";
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			setVehiculoParameters(ps, vehiculo);
			ps.setInt(11, vehiculo.getId());
			int rows = ps.executeUpdate();
			if (rows > 0) {
				logger.info("Vehículo actualizado exitosamente. ID: {}", vehiculo.getId());
				return true;
			}
		} catch (SQLException e) {
			logger.error("Error al actualizar Vehículo", e);
			throw new DataException("Error al actualizar Vehículo", e);
		} finally {
			JDBCUtils.close(ps, null);
		}
		return false;
	}

	@Override
	public boolean delete(Connection connection, Integer id) throws DataException {
		String sql = "DELETE FROM vehiculo WHERE id_vehiculo = ?";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, id);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			logger.error("Error al eliminar Vehículo", e);
			throw new DataException("Error al eliminar Vehículo", e);
		}
	}

	@Override
	public Results<VehicleDTO> findByCriteria(Connection connection, VehicleCriteria criteria) throws DataException {
		Results<VehicleDTO> results = new Results<>();
		List<VehicleDTO> lista = new ArrayList<>();
		int pageNumber = criteria.getPageNumber();
		int pageSize = criteria.getPageSize();
		int offset = (pageNumber - 1) * pageSize;

		StringBuilder sql = new StringBuilder(VEHICULO_SELECT_BASE);
		sql.append(" WHERE 1=1 ");

		if (criteria.getMarca() != null && !criteria.getMarca().isEmpty()) {
			sql.append(" AND v.marca LIKE ? ");
		}
		if (criteria.getModelo() != null && !criteria.getModelo().isEmpty()) {
			sql.append(" AND v.modelo LIKE ? ");
		}
		if (criteria.getAnioDesde() != null) {
			sql.append(" AND v.anio_fabricacion >= ? ");
		}
		if (criteria.getAnioHasta() != null) {
			sql.append(" AND v.anio_fabricacion <= ? ");
		}
		if (criteria.getPrecioMax() != null) {
			sql.append(" AND v.precio_dia <= ? ");
		}
		if (criteria.getIdEstadoVehiculo() != null) {
			sql.append(" AND v.id_estado_vehiculo = ? ");
		}
		if (criteria.getIdCategoria() != null) {
			sql.append(" AND v.id_categoria = ? ");
		}
		if (criteria.getPlaca() != null && !criteria.getPlaca().isEmpty()) {
			sql.append(" AND v.placa LIKE ? ");
		}
		if (criteria.getNumeroBastidor() != null && !criteria.getNumeroBastidor().isEmpty()) {
			sql.append(" AND v.numero_bastidor LIKE ? ");
		}

		sql.append(" ORDER BY v.marca, v.modelo ");

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			int index = 1;

			if (criteria.getMarca() != null && !criteria.getMarca().isEmpty()) {
				ps.setString(index++, "%" + criteria.getMarca() + "%");
			}
			if (criteria.getModelo() != null && !criteria.getModelo().isEmpty()) {
				ps.setString(index++, "%" + criteria.getModelo() + "%");
			}
			if (criteria.getAnioDesde() != null) {
				ps.setInt(index++, criteria.getAnioDesde());
			}
			if (criteria.getAnioHasta() != null) {
				ps.setInt(index++, criteria.getAnioHasta());
			}
			if (criteria.getPrecioMax() != null) {
				ps.setDouble(index++, criteria.getPrecioMax());
			}
			if (criteria.getIdEstadoVehiculo() != null) {
				ps.setInt(index++, criteria.getIdEstadoVehiculo());
			}
			if (criteria.getIdCategoria() != null) {
				ps.setInt(index++, criteria.getIdCategoria());
			}
			if (criteria.getPlaca() != null && !criteria.getPlaca().isEmpty()) {
				ps.setString(index++, "%" + criteria.getPlaca() + "%");
			}
			if (criteria.getNumeroBastidor() != null && !criteria.getNumeroBastidor().isEmpty()) {
				ps.setString(index++, "%" + criteria.getNumeroBastidor() + "%");
			}

			rs = ps.executeQuery();

			if (rs.absolute(offset + 1)) {
				int count = 0;
				do {
					lista.add(loadVehicleDTO(rs));
					count++;
				} while (count < pageSize && rs.next());
			}
			rs.last();
			int totalRecords = rs.getRow();

			results.setResults(lista);
			results.setPageNumber(pageNumber);
			results.setPageSize(pageSize);
			results.setTotalRecords(totalRecords);

			logger.info("findByCriteria de Vehículo completado: Página {} (Tamaño: {}), Total registros: {}",
					pageNumber, pageSize, totalRecords);
		} catch (SQLException e) {
			logger.error("Error en findByCriteria de Vehículo", e);
			throw new DataException("Error en findByCriteria de Vehículo", e);
		} finally {
			JDBCUtils.close(ps, rs);
		}
		return results;
	}

	private void setVehiculoParameters(PreparedStatement ps, VehicleDTO vehiculo) throws SQLException {
		ps.setString(1, vehiculo.getMarca());
		ps.setString(2, vehiculo.getModelo());
		ps.setInt(3, vehiculo.getAnioFabricacion());
		ps.setInt(4, vehiculo.getIdCategoria());
		ps.setInt(5, vehiculo.getIdEstadoVehiculo());
		ps.setString(6, vehiculo.getPlaca());
		ps.setDouble(7, vehiculo.getPrecioDia());
		ps.setString(8, vehiculo.getNumeroBastidor());
		ps.setInt(9, vehiculo.getKilometrajeActual());
	}

	private VehicleDTO loadVehicleDTO(ResultSet rs) throws SQLException {
		VehicleDTO vehiculo = new VehicleDTO();
		vehiculo.setId(rs.getInt("id_vehiculo"));
		vehiculo.setMarca(rs.getString("marca"));
		vehiculo.setModelo(rs.getString("modelo"));
		vehiculo.setAnioFabricacion(rs.getInt("anio_fabricacion"));
		vehiculo.setPrecioDia(rs.getDouble("precio_dia"));
		vehiculo.setPlaca(rs.getString("placa"));
		vehiculo.setNumeroBastidor(rs.getString("numero_bastidor"));
		vehiculo.setKilometrajeActual(rs.getInt("kilometraje_actual"));
		vehiculo.setIdEstadoVehiculo(rs.getInt("id_estado_vehiculo"));
		vehiculo.setNombreEstadoVehiculo(rs.getString("nombreEstadoVehiculo"));
		vehiculo.setIdCategoria(rs.getInt("id_categoria"));
		vehiculo.setNombreCategoria(rs.getString("nombreCategoria"));
		return vehiculo;
	}
}