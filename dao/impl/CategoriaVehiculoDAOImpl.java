package com.pinguela.rentexpres.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.dao.CategoriaVehiculoDAO;
import com.pinguela.rentexpres.model.CategoriaVehiculoDTO;
import com.pinguela.rentexpres.util.JDBCUtils;

public class CategoriaVehiculoDAOImpl implements CategoriaVehiculoDAO {

    private static final Logger logger = LogManager.getLogger(CategoriaVehiculoDAOImpl.class);

    @Override
    public CategoriaVehiculoDTO findById(Connection connection, Integer id) {
        CategoriaVehiculoDTO cv = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT id_categoria, nombre_categoria FROM categoria_vehiculo WHERE id_categoria = ?";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                cv = loadCategoriaVehiculo(rs);
                logger.info("CategoriaVehiculo encontrada con ID: {}", id);
            }
        } catch (SQLException e) {
            logger.error("Error al buscar CategoriaVehiculo por ID: {}", e.getMessage(), e);
        } finally {
			JDBCUtils.close(ps, rs);
        }
        return cv;
    }

    @Override
    public List<CategoriaVehiculoDTO> findAll(Connection connection) {
        List<CategoriaVehiculoDTO> lista = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT id_categoria, nombre_categoria FROM categoria_vehiculo";
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(loadCategoriaVehiculo(rs));
            }
            logger.info("Total categorias de vehiculo: {}", lista.size());
        } catch (SQLException e) {
            logger.error("Error al obtener todas las CategoriaVehiculo: {}", e.getMessage(), e);
        } finally {
            JDBCUtils.close(ps, rs);
        }
        return lista;
    }

    private CategoriaVehiculoDTO loadCategoriaVehiculo(ResultSet rs) throws SQLException {
        CategoriaVehiculoDTO cv = new CategoriaVehiculoDTO();
        cv.setId(rs.getInt("id_categoria"));
        cv.setNombreCategoria(rs.getString("nombre_categoria"));
        return cv;
    }
}
