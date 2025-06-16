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

import com.pinguela.rentexpres.dao.LocalidadDAO;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.LocalidadDTO;

public class LocalidadDAOImpl implements LocalidadDAO {

	private static final Logger logger = LogManager.getLogger(LocalidadDAOImpl.class);

	private static final String BASE_SELECT = "SELECT id_localidad, nombre_localidad, id_provincia FROM localidad";

	@Override
	public LocalidadDTO findById(Connection c, Integer id) throws DataException {
		String sql = BASE_SELECT + " WHERE id_localidad = ?";
		try (PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? load(rs) : null;
			}
		} catch (SQLException ex) {
			logger.error("findById {}", id, ex);
			throw new DataException("Error buscando localidad", ex);
		}
	}

	@Override
	public List<LocalidadDTO> findAll(Connection c) throws DataException {
		try (PreparedStatement ps = c.prepareStatement(BASE_SELECT); ResultSet rs = ps.executeQuery()) {

			List<LocalidadDTO> out = new ArrayList<>();
			while (rs.next())
				out.add(load(rs));
			return out;

		} catch (SQLException ex) {
			logger.error("findAll", ex);
			throw new DataException("Error listando localidades", ex);
		}
	}

	@Override
	public List<LocalidadDTO> findByProvinciaId(Connection c, Integer idProvincia) throws DataException {
		String sql = BASE_SELECT + " WHERE id_provincia = ? ORDER BY nombre_localidad";
		try (PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, idProvincia);
			try (ResultSet rs = ps.executeQuery()) {
				List<LocalidadDTO> out = new ArrayList<>();
				while (rs.next())
					out.add(load(rs));
				return out;
			}
		} catch (SQLException ex) {
			logger.error("findByProvinciaId {}", idProvincia, ex);
			throw new DataException("Error buscando localidades por provincia", ex);
		}
	}

	
	@Override
	public boolean create(Connection c, LocalidadDTO l) throws DataException {
		String sql = "INSERT INTO localidad (nombre_localidad, id_provincia) VALUES (?, ?)";
		try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			set(ps, l, false);
			if (ps.executeUpdate() > 0) {
				try (ResultSet keys = ps.getGeneratedKeys()) {
					if (keys.next())
						l.setId(keys.getInt(1));
				}
				return true;
			}
			return false;
		} catch (SQLException ex) {
			throw new DataException("Error insertando localidad", ex);
		}
	}

	@Override
	public boolean update(Connection c, LocalidadDTO l) throws DataException {
		String sql = "UPDATE localidad SET nombre_localidad = ?, id_provincia = ? WHERE id_localidad = ?";
		try (PreparedStatement ps = c.prepareStatement(sql)) {
			set(ps, l, true);
			return ps.executeUpdate() > 0;
		} catch (SQLException ex) {
			throw new DataException("Error actualizando localidad", ex);
		}
	}

	@Override
	public boolean delete(Connection c, LocalidadDTO l) throws DataException {
		String sql = "DELETE FROM localidad WHERE id_localidad = ?";
		try (PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, l.getId());
			return ps.executeUpdate() > 0;
		} catch (SQLException ex) {
			throw new DataException("Error eliminando localidad", ex);
		}
	}

	
	private static void set(PreparedStatement ps, LocalidadDTO l, boolean update) throws SQLException {
		ps.setString(1, l.getNombre());
		ps.setInt(2, l.getIdProvincia());
		if (update)
			ps.setInt(3, l.getId());
	}

	private static LocalidadDTO load(ResultSet rs) throws SQLException {
		LocalidadDTO l = new LocalidadDTO();
		l.setId(rs.getInt("id_localidad"));
		l.setNombre(rs.getString("nombre_localidad"));
		l.setIdProvincia(rs.getInt("id_provincia"));
		return l;
	}
}
