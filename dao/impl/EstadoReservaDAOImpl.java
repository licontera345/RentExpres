package com.pinguela.rentexpres.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.pinguela.rentexpres.dao.EstadoReservaDAO;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.EstadoReservaDTO;
import com.pinguela.rentexpres.util.JDBCUtils;

public class EstadoReservaDAOImpl implements EstadoReservaDAO {
    private static final Logger logger = LogManager.getLogger(EstadoReservaDAOImpl.class);
    
    @Override
    public EstadoReservaDTO findById(Connection connection, Integer id) throws DataException {
        EstadoReservaDTO er = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT id_estado_reserva, nombre_estado FROM estado_reserva WHERE id_estado_reserva = ?";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                er = new EstadoReservaDTO();
                er.setId(rs.getInt("id_estado_reserva"));
                er.setNombreEstado(rs.getString("nombre_estado"));
                logger.info("EstadoReserva encontrado, id: " + id);
            }
        } catch (SQLException e) {
            logger.error("Error al buscar EstadoReserva por ID: " + id, e);
            throw new DataException("Error al buscar EstadoReserva por ID: " + id, e);
        } finally {
            JDBCUtils.close(ps, rs);
        }
        return er;
    }
    
    @Override
    public List<EstadoReservaDTO> findAll(Connection connection) throws DataException {
        List<EstadoReservaDTO> lista = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT id_estado_reserva, nombre_estado FROM estado_reserva";
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                EstadoReservaDTO er = new EstadoReservaDTO();
                er.setId(rs.getInt("id_estado_reserva"));
                er.setNombreEstado(rs.getString("nombre_estado"));
                lista.add(er);
            }
            logger.info("Total de EstadoReserva encontrados: " + lista.size());
        } catch (SQLException e) {
            logger.error("Error al obtener EstadoReserva", e);
            throw new DataException("Error al obtener EstadoReserva", e);
        } finally {
            JDBCUtils.close(ps, rs);
        }
        return lista;
    }
}
