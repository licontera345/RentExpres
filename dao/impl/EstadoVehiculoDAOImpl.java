package com.pinguela.rentexpres.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.dao.EstadoVehiculoDAO;
import com.pinguela.rentexpres.model.VehicleStatusDTO;
import com.pinguela.rentexpres.util.JDBCUtils;

public class EstadoVehiculoDAOImpl implements EstadoVehiculoDAO {

    private static final Logger logger = LogManager.getLogger(EstadoVehiculoDAOImpl.class);

    @Override
    public VehicleStatusDTO findById(Connection connection, Integer id) {
        VehicleStatusDTO ev = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = connection.prepareStatement("SELECT id_estado_vehiculo, nombre_estado FROM estado_vehiculo WHERE id_estado_vehiculo = ?");
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                ev = loadEstadoVehiculo(rs);
                logger.info("EstadoVehiculo encontrado con ID: " + id);
            }
        } catch (SQLException e) {
            logger.error("Error al buscar EstadoVehiculo por ID: " + e.getMessage(), e);
        } finally {
			JDBCUtils.close(ps, rs);
        }
        return ev;
    }

    @Override
    public List<VehicleStatusDTO> findAll(Connection connection) {
        List<VehicleStatusDTO> lista = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement("SELECT id_estado_vehiculo, nombre_estado FROM estado_vehiculo")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(loadEstadoVehiculo(rs));
                }
                logger.info("Estados de vehículo encontrados: " + lista.size());
            }
        } catch (SQLException e) {
            logger.error("Error al obtener todos los EstadoVehiculo: " + e.getMessage(), e);
        }
        return lista;
    }

    private VehicleStatusDTO loadEstadoVehiculo(ResultSet rs) throws SQLException {
        VehicleStatusDTO ev = new VehicleStatusDTO();
        ev.setId(rs.getInt("id_estado_vehiculo"));
        ev.setNombreEstado(rs.getString("nombre_estado"));
        return ev;
    }
}
