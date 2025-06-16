package com.pinguela.rentexpres.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.pinguela.rentexpres.dao.DireccionDAO;
import com.pinguela.rentexpres.dao.impl.DireccionDAOImpl;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.DireccionDTO;
import com.pinguela.rentexpres.service.DireccionService;
import com.pinguela.rentexpres.util.JDBCUtils;

public class DireccionServiceImpl implements DireccionService {

    private static final Logger logger = LogManager.getLogger(DireccionServiceImpl.class);
    private DireccionDAO direccionDAO;

    public DireccionServiceImpl() {
        this.direccionDAO = new DireccionDAOImpl();
    }

    @Override
    public DireccionDTO findById(Integer id) throws RentexpresException {
        Connection connection = null;
        DireccionDTO direccion = null;
        try {
            connection = JDBCUtils.getConnection();
            JDBCUtils.beginTransaction(connection);
            direccion = direccionDAO.findById(connection, id);
            JDBCUtils.commitTransaction(connection);
            logger.info("findById de Dirección completado. ID: {}", id);
        } catch (SQLException | DataException e) {
            JDBCUtils.rollbackTransaction(connection);
            logger.error("Error en findById de Dirección: ", e);
            throw new RentexpresException("Error en findById de Dirección", e);
        } finally {
            JDBCUtils.close(connection);
        }
        return direccion;
    }

    @Override
    public boolean create(DireccionDTO direccion) throws RentexpresException {
        Connection connection = null;
        boolean creado = false;
        try {
            connection = JDBCUtils.getConnection();
            JDBCUtils.beginTransaction(connection);
            creado = direccionDAO.create(connection, direccion);
            if (creado) {
                JDBCUtils.commitTransaction(connection);
                logger.info("Dirección creada exitosamente. ID: {}", direccion.getId());
            } else {
                JDBCUtils.rollbackTransaction(connection);
                logger.warn("No se pudo crear la Dirección.");
            }
        } catch (SQLException | DataException e) {
            JDBCUtils.rollbackTransaction(connection);
            logger.error("Error en create de Dirección: ", e);
            throw new RentexpresException("Error en create de Dirección", e);
        } finally {
            JDBCUtils.close(connection);
        }
        return creado;
    }

    @Override
    public boolean update(DireccionDTO direccion) throws RentexpresException {
        Connection connection = null;
        boolean actualizado = false;
        try {
            connection = JDBCUtils.getConnection();
            JDBCUtils.beginTransaction(connection);
            actualizado = direccionDAO.update(connection, direccion);
            if (actualizado) {
                JDBCUtils.commitTransaction(connection);
                logger.info("Dirección actualizada exitosamente. ID: {}", direccion.getId());
            } else {
                JDBCUtils.rollbackTransaction(connection);
                logger.warn("No se pudo actualizar la Dirección. ID: {}", direccion.getId());
            }
        } catch (SQLException | DataException e) {
            JDBCUtils.rollbackTransaction(connection);
            logger.error("Error en update de Dirección: ", e);
            throw new RentexpresException("Error en update de Dirección", e);
        } finally {
            JDBCUtils.close(connection);
        }
        return actualizado;
    }

    @Override
    public boolean delete(DireccionDTO direccion) throws RentexpresException {
        Connection connection = null;
        boolean eliminado = false;
        try {
            connection = JDBCUtils.getConnection();
            JDBCUtils.beginTransaction(connection);
            eliminado = direccionDAO.delete(connection, direccion, direccion.getId());
            if (eliminado) {
                JDBCUtils.commitTransaction(connection);
                logger.info("Dirección eliminada exitosamente. ID: {}", direccion.getId());
            } else {
                JDBCUtils.rollbackTransaction(connection);
                logger.warn("No se pudo eliminar la Dirección. ID: {}", direccion.getId());
            }
        } catch (SQLException | DataException e) {
            JDBCUtils.rollbackTransaction(connection);
            logger.error("Error en delete de Dirección: ", e);
            throw new RentexpresException("Error en delete de Dirección", e);
        } finally {
            JDBCUtils.close(connection);
        }
        return eliminado;
    }
}
