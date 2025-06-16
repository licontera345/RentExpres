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
import com.pinguela.rentexpres.dao.ProvinciaDAO;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.ProvinciaDTO;
import com.pinguela.rentexpres.util.JDBCUtils;

public class ProvinciaDAOImpl implements ProvinciaDAO {

	private static final Logger logger = LogManager.getLogger(ProvinciaDAOImpl.class);

	@Override
	public ProvinciaDTO findById(Connection connection, Integer id) throws DataException {
		ProvinciaDTO prov = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT id_provincia, nombre_provincia FROM provincia WHERE id_provincia = ?";
			ps = connection.prepareStatement(sql);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				prov = loadProvincia(rs);
				logger.info("Provincia encontrada, id: {}", id);
			}
		} catch (SQLException e) {
			logger.error("Error al buscar Provincia por ID: {}", id, e);
			throw new DataException("Error al buscar Provincia por ID: " + id, e);
		} finally {
			JDBCUtils.close(ps, rs);
		}
		return prov;
	}

	@Override
	public List<ProvinciaDTO> findAll(Connection connection) throws DataException {
		List<ProvinciaDTO> lista = new ArrayList<>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT id_provincia, nombre_provincia FROM provincia";
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				lista.add(loadProvincia(rs));
			}
			logger.info("Total de Provincias encontradas: {}", lista.size());
		} catch (SQLException e) {
			logger.error("Error al obtener todas las Provincias", e);
			throw new DataException("Error al obtener todas las Provincias", e);
		} finally {
			JDBCUtils.close(ps, rs);
		}
		return lista;
	}

	@Override
	public boolean create(Connection connection, ProvinciaDTO provincia) throws DataException {
		if (provincia == null) {
			logger.warn("create llamado con Provincia nula.");
			return false;
		}
		String sql = "INSERT INTO provincia (nombre_provincia) VALUES (?)";
		PreparedStatement ps = null;
		ResultSet generatedKeys = null;
		try {
			ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			setProvinciaParameters(ps, provincia, false);
			if (ps.executeUpdate() > 0) {
				generatedKeys = ps.getGeneratedKeys();
				if (generatedKeys.next()) {
					provincia.setId(generatedKeys.getInt(1));
				}
				logger.info("Provincia creada con éxito, id: {}", provincia.getId());
				return true;
			}
		} catch (SQLException e) {
			logger.error("Error al crear Provincia", e);
			throw new DataException("Error al crear Provincia", e);
		} finally {
			JDBCUtils.close(ps, generatedKeys);
		}
		return false;
	}

	@Override
	public boolean update(Connection connection, ProvinciaDTO provincia) throws DataException {
		if (provincia == null || provincia.getId() == null) {
			logger.warn("update llamado con Provincia nula o sin id.");
			return false;
		}
		String sql = "UPDATE provincia SET nombre_provincia = ? WHERE id_provincia = ?";
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			setProvinciaParameters(ps, provincia, true);
			int rows = ps.executeUpdate();
			if (rows > 0) {
				logger.info("Provincia actualizada con éxito, id: {}", provincia.getId());
				return true;
			}
		} catch (SQLException e) {
			logger.error("Error al actualizar Provincia: {}", e.getMessage(), e);
			throw new DataException("Error al actualizar Provincia", e);
		} finally {
			JDBCUtils.close(ps, null);
		}
		return false;
	}

	@Override
	public boolean delete(Connection connection, ProvinciaDTO provincia) throws DataException {
		if (provincia == null || provincia.getId() == null) {
			logger.warn("delete llamado con Provincia nula o sin id.");
			return false;
		}
		String sql = "DELETE FROM provincia WHERE id_provincia = ?";
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, provincia.getId());
			if (ps.executeUpdate() > 0) {
				logger.info("Provincia eliminada, id: {}", provincia.getId());
				return true;
			}
		} catch (SQLException e) {
			logger.error("Error al eliminar Provincia: {}", e.getMessage(), e);
			throw new DataException("Error al eliminar Provincia", e);
		} finally {
			JDBCUtils.close(ps, null);
		}
		return false;
	}

	private void setProvinciaParameters(PreparedStatement ps, ProvinciaDTO provincia, boolean isUpdate)
			throws SQLException {
		ps.setString(1, provincia.getNombre());
		if (isUpdate) {
			ps.setInt(2, provincia.getId());
		}
	}

	private ProvinciaDTO loadProvincia(ResultSet rs) throws SQLException {
		ProvinciaDTO p = new ProvinciaDTO();
		p.setId(rs.getInt("id_provincia"));
		p.setNombre(rs.getString("nombre_provincia"));
		return p;
	}
}
