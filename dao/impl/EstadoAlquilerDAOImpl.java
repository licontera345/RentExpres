package com.pinguela.rentexpres.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.dao.EstadoAlquilerDAO;
import com.pinguela.rentexpres.model.EstadoAlquilerDTO;
import com.pinguela.rentexpres.util.JDBCUtils;

public class EstadoAlquilerDAOImpl implements EstadoAlquilerDAO {

    private static final Logger logger = LogManager.getLogger(EstadoAlquilerDAOImpl.class);


    @Override
    public EstadoAlquilerDTO findById(Connection connection, Integer id) {
        EstadoAlquilerDTO ea = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = connection.prepareStatement("SELECT id_estado_alquiler, nombre_estado FROM estado_alquiler WHERE id_estado_alquiler = ?");
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                ea = loadEstadoAlquiler(rs);
                logger.info("EstadoAlquiler encontrado con ID: " + id);
            }
        } catch (SQLException e) {
            logger.error("Error al buscar EstadoAlquiler por ID: " + e.getMessage(), e);
        } finally {
			JDBCUtils.close(ps, rs);
        }
        return ea;
    }
    @Override
    public List<EstadoAlquilerDTO> findAll(Connection connection) {
        List<EstadoAlquilerDTO> lista = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement("SELECT id_estado_alquiler, nombre_estado FROM estado_alquiler")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(loadEstadoAlquiler(rs));
                }
                logger.info("Total EstadosAlquiler: " + lista.size());
            }
        } catch (SQLException e) {
            logger.error("Error al obtener todos los EstadoAlquiler: " + e.getMessage(), e);
        }
        return lista;
    }

    private EstadoAlquilerDTO loadEstadoAlquiler(ResultSet rs) throws SQLException {
        EstadoAlquilerDTO ea = new EstadoAlquilerDTO();
        ea.setId(rs.getInt("id_estado_alquiler"));
        ea.setNombreEstado(rs.getString("nombre_estado"));

        return ea;
    }
}
