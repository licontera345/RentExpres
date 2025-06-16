package com.pinguela.rentexpres.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.pinguela.rentexpres.dao.DireccionDAO;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.DireccionDTO;
import com.pinguela.rentexpres.util.JDBCUtils;

public class DireccionDAOImpl implements DireccionDAO {

	private static final Logger logger = LogManager.getLogger(DireccionDAOImpl.class);

	@Override
	public DireccionDTO findById(Connection connection, Integer id) throws DataException {
		DireccionDTO d = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT d.id_direccion, d.id_localidad, d.calle, d.numero, "
					+ "l.nombre_localidad, p.nombre_provincia " + "FROM direccion d "
					+ "JOIN localidad l ON d.id_localidad = l.id_localidad "
					+ "JOIN provincia p ON l.id_provincia = p.id_provincia " + "WHERE d.id_direccion = ?";
			ps = connection.prepareStatement(sql);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				d = loadDireccion(rs);
				logger.info("Dirección encontrada, id: {}", id);
			}
		} catch (SQLException e) {
			logger.error("Error al buscar la dirección por id: {}", id, e);
			throw new DataException("Error al buscar la dirección por id: " + id, e);
		} finally {
			JDBCUtils.close(ps, rs);
		}
		return d;
	}

	@Override
	public boolean create(Connection connection, DireccionDTO direccion) throws DataException {
		if (direccion == null) {
			logger.warn("create llamado con Direccion nula.");
			return false;
		}
		String sql = "INSERT INTO direccion (id_localidad, calle, numero) VALUES (?, ?, ?)";
		PreparedStatement ps = null;
		ResultSet generatedKeys = null;
		try {
			ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			setDireccionParameters(ps, direccion, false);
			if (ps.executeUpdate() > 0) {
				generatedKeys = ps.getGeneratedKeys();
				if (generatedKeys.next()) {
					direccion.setId(generatedKeys.getInt(1));
				}
				logger.info("Dirección creada con éxito, id: {}", direccion.getId());
				return true;
			}
		} catch (SQLException e) {
			logger.error("Error al crear la dirección: {}", e.getMessage(), e);
			throw new DataException("Error al crear la dirección", e);
		} finally {
			JDBCUtils.close(ps, generatedKeys);
		}
		return false;
	}

	@Override
	public boolean update(Connection connection, DireccionDTO direccion) throws DataException {
		if (direccion == null || direccion.getId() == null) {
			logger.warn("update llamado con dirección nula o sin id.");
			return false;
		}
		String sql = "UPDATE direccion SET id_localidad = ?, calle = ?, numero = ? WHERE id_direccion = ?";
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			setDireccionParameters(ps, direccion, true);
			int rows = ps.executeUpdate();
			if (rows > 0) {
				logger.info("Dirección actualizada con éxito, id: {}", direccion.getId());
				return true;
			}
		} catch (SQLException e) {
			logger.error("Error al actualizar la dirección: {}", e.getMessage(), e);
			throw new DataException("Error al actualizar la dirección", e);
		} finally {
			JDBCUtils.close(ps, null);
		}
		return false;
	}

	@Override
	public boolean delete(Connection connection, DireccionDTO direccion, Integer id) throws DataException {
		if (id == null) {
			logger.warn("delete llamado con id nulo.");
			return false;
		}
		String sql = "DELETE FROM direccion WHERE id_direccion = ?";
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, id);
			if (ps.executeUpdate() > 0) {
				logger.info("Dirección eliminada, id: {}", id);
				return true;
			}
		} catch (SQLException e) {
			logger.error("Error al eliminar la dirección: {}", e.getMessage(), e);
			throw new DataException("Error al eliminar la dirección", e);
		} finally {
			JDBCUtils.close(ps, null);
		}
		return false;
	}

	private DireccionDTO loadDireccion(ResultSet rs) throws SQLException {
		DireccionDTO d = new DireccionDTO();
		d.setId(rs.getInt("id_direccion"));
		d.setIdLocalidad(rs.getInt("id_localidad"));
		d.setCalle(rs.getString("calle"));
		d.setNumero(rs.getString("numero"));
		d.setNombreLocalidad(rs.getString("nombre_localidad"));
		d.setNombreProvincia(rs.getString("nombre_provincia"));
		return d;
	}

	private void setDireccionParameters(PreparedStatement ps, DireccionDTO direccion, boolean isUpdate)
			throws SQLException {
		ps.setInt(1, direccion.getIdLocalidad());
		ps.setString(2, direccion.getCalle());
		ps.setString(3, direccion.getNumero());
		if (isUpdate) {
			ps.setInt(4, direccion.getId());
		}
	}
}
