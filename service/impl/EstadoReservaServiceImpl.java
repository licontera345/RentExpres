package com.pinguela.rentexpres.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.pinguela.rentexpres.dao.EstadoReservaDAO;
import com.pinguela.rentexpres.dao.impl.EstadoReservaDAOImpl;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.EstadoReservaDTO;
import com.pinguela.rentexpres.service.EstadoReservaService;
import com.pinguela.rentexpres.util.JDBCUtils;

public class EstadoReservaServiceImpl implements EstadoReservaService {
    private static final Logger logger = LogManager.getLogger(EstadoReservaServiceImpl.class);
    private EstadoReservaDAO estadoReservaDAO;
    
    public EstadoReservaServiceImpl() {
        this.estadoReservaDAO = new EstadoReservaDAOImpl();
    }
    
    @Override
    public EstadoReservaDTO findById(Integer id) throws RentexpresException {
        Connection connection = null;
        EstadoReservaDTO er = null;
        try {
            connection = JDBCUtils.getConnection();
            JDBCUtils.beginTransaction(connection);
            er = estadoReservaDAO.findById(connection, id);
            JDBCUtils.commitTransaction(connection);
            logger.info("findById de EstadoReserva completado. ID: " + id);
        } catch (SQLException | DataException e) {
            JDBCUtils.rollbackTransaction(connection);
            logger.error("Error en findById de EstadoReserva: ", e);
            throw new RentexpresException("Error en findById de EstadoReserva", e);
        } finally {
            JDBCUtils.close(connection);
        }
        return er;
    }
    
    @Override
    public List<EstadoReservaDTO> findAll() throws RentexpresException {
        Connection connection = null;
        List<EstadoReservaDTO> lista = null;
        try {
            connection = JDBCUtils.getConnection();
            JDBCUtils.beginTransaction(connection);
            lista = estadoReservaDAO.findAll(connection);
            JDBCUtils.commitTransaction(connection);
            logger.info("findAll de EstadoReserva completado. Cantidad: " + (lista != null ? lista.size() : 0));
        } catch (SQLException | DataException e) {
            JDBCUtils.rollbackTransaction(connection);
            logger.error("Error en findAll de EstadoReserva: ", e);
            throw new RentexpresException("Error en findAll de EstadoReserva", e);
        } finally {
            JDBCUtils.close(connection);
        }
        return lista;
    }
}
