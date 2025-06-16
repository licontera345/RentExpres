package com.pinguela.rentexpres.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.dao.TipoUsuarioDAO;
import com.pinguela.rentexpres.model.TipoUsuarioDTO;
import com.pinguela.rentexpres.util.JDBCUtils;

public class TipoUsuarioDAOImpl implements TipoUsuarioDAO {

	private static final Logger logger = LogManager.getLogger(TipoUsuarioDAOImpl.class);

	@Override
    public TipoUsuarioDTO findById(Connection connection, Integer id) {
        TipoUsuarioDTO tu = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = connection.prepareStatement("SELECT id_tipo_usuario, nombre_tipo FROM tipo_usuario WHERE id_tipo_usuario = ?");
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                tu = loadTipoUsuario(rs);
                logger.info("TipoUsuario encontrado con ID: " + id);
            }
        } catch (SQLException e) {
            logger.error("Error al buscar TipoUsuario por ID: " + e.getMessage(), e);
        } finally {
            JDBCUtils.close(ps, rs);
        }
        return tu;
    }

	@Override
	public List<TipoUsuarioDTO> findAll(Connection connection) {
		List<TipoUsuarioDTO> lista = new ArrayList<>();

		try (PreparedStatement ps = connection
				.prepareStatement("SELECT id_tipo_usuario, nombre_tipo FROM tipo_usuario")) {
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					lista.add(loadTipoUsuario(rs));
				}
				logger.info("Total TipoUsuarios found: " + lista.size());
			}
		} catch (SQLException e) {
			logger.error("Error retrieving all TipoUsuarios: " + e.getMessage(), e);
		} 

		return lista;
	}

	private TipoUsuarioDTO loadTipoUsuario(ResultSet rs) throws SQLException {
		TipoUsuarioDTO tu = new TipoUsuarioDTO();
		tu.setId(rs.getInt("id_tipo_usuario"));
		tu.setNombreTipo(rs.getString("nombre_tipo"));
		return tu;
	}
}
