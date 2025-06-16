package com.pinguela.rentexpres.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.pinguela.rentexpres.dao.EstadoVehiculoDAO;
import com.pinguela.rentexpres.dao.impl.EstadoVehiculoDAOImpl;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.EstadoVehiculoDTO;
import com.pinguela.rentexpres.service.EstadoVehiculoService;
import com.pinguela.rentexpres.util.JDBCUtils;

public class EstadoVehiculoServiceImpl implements EstadoVehiculoService {

    private static final Logger logger = LogManager.getLogger(EstadoVehiculoServiceImpl.class);
    private EstadoVehiculoDAO estadoVehiculoDAO;

    public EstadoVehiculoServiceImpl() {
        this.estadoVehiculoDAO = new EstadoVehiculoDAOImpl();
    }

    @Override
    public EstadoVehiculoDTO findById(Integer id) throws RentexpresException {
        Connection connection = null;
        EstadoVehiculoDTO estado = null;
        try {
            connection = JDBCUtils.getConnection();
            JDBCUtils.beginTransaction(connection);
            try {
				estado = estadoVehiculoDAO.findById(connection, id);
			} catch (DataException e) {
				e.printStackTrace();
			}
            JDBCUtils.commitTransaction(connection);
            logger.info("findById de EstadoVehiculo completado. ID: " + id);
        } catch (SQLException e) {
            JDBCUtils.rollbackTransaction(connection);
            logger.error("Error en findById de EstadoVehiculo: ", e);
            throw new RentexpresException("Error en findById de EstadoVehiculo", e);
        } finally {
            JDBCUtils.close(connection);
        }
        return estado;
    }

    @Override
    public List<EstadoVehiculoDTO> findAll() throws RentexpresException {
        Connection connection = null;
        List<EstadoVehiculoDTO> lista = null;
        try {
            connection = JDBCUtils.getConnection();
            JDBCUtils.beginTransaction(connection);
            try {
				lista = estadoVehiculoDAO.findAll(connection);
			} catch (DataException e) {
				e.printStackTrace();
			}
            JDBCUtils.commitTransaction(connection);
            logger.info("findAll de EstadoVehiculo completado. Cantidad: " + (lista != null ? lista.size() : 0));
        } catch (SQLException e) {
            JDBCUtils.rollbackTransaction(connection);
            logger.error("Error en findAll de EstadoVehiculo: ", e);
            throw new RentexpresException("Error en findAll de EstadoVehiculo", e);
        } finally {
            JDBCUtils.close(connection);
        }
        return lista;
    }
}
