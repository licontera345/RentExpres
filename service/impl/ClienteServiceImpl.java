package com.pinguela.rentexpres.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.pinguela.rentexpres.dao.ClienteDAO;
import com.pinguela.rentexpres.dao.impl.ClienteDAOImpl;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.ClienteDTO;
import com.pinguela.rentexpres.model.ClienteCriteria;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.service.ClienteService;
import com.pinguela.rentexpres.util.JDBCUtils;

public class ClienteServiceImpl implements ClienteService {

    private static final Logger logger = LogManager.getLogger(ClienteServiceImpl.class);
    private ClienteDAO clienteDAO;

    public ClienteServiceImpl() {
        this.clienteDAO = new ClienteDAOImpl();
    }

    @Override
    public ClienteDTO findById(Integer id) throws RentexpresException {
        Connection connection = null;
        ClienteDTO cliente = null;
        try {
            connection = JDBCUtils.getConnection();
            JDBCUtils.beginTransaction(connection);
            cliente = clienteDAO.findById(connection, id);
            JDBCUtils.commitTransaction(connection);
            logger.info("findById de Cliente completado. ID: {}", id);
        } catch (SQLException | DataException e) {
            JDBCUtils.rollbackTransaction(connection);
            logger.error("Error en findById de Cliente: ", e);
            throw new RentexpresException("Error en findById de Cliente", e);
        } finally {
            JDBCUtils.close(connection);
        }
        return cliente;
    }

    @Override
    public List<ClienteDTO> findAll() throws RentexpresException {
        Connection connection = null;
        List<ClienteDTO> lista = null;
        try {
            connection = JDBCUtils.getConnection();
            JDBCUtils.beginTransaction(connection);
            lista = clienteDAO.findAll(connection);
            JDBCUtils.commitTransaction(connection);
            logger.info("findAll de Cliente completado. Cantidad: {}", (lista != null ? lista.size() : 0));
        } catch (SQLException | DataException e) {
            JDBCUtils.rollbackTransaction(connection);
            logger.error("Error en findAll de Cliente: ", e);
            throw new RentexpresException("Error en findAll de Cliente", e);
        } finally {
            JDBCUtils.close(connection);
        }
        return lista;
    }

    @Override
    public boolean create(ClienteDTO cliente) throws RentexpresException {
        Connection connection = null;
        boolean creado = false;
        try {
            connection = JDBCUtils.getConnection();
            JDBCUtils.beginTransaction(connection);
            creado = clienteDAO.create(connection, cliente);
            if (creado) {
                JDBCUtils.commitTransaction(connection);
                logger.info("Cliente creado exitosamente. ID: {}", cliente.getId());
            } else {
                JDBCUtils.rollbackTransaction(connection);
                logger.warn("No se pudo crear el Cliente.");
            }
        } catch (SQLException | DataException e) {
            JDBCUtils.rollbackTransaction(connection);
            logger.error("Error en create de Cliente: ", e);
            throw new RentexpresException("Error en create de Cliente", e);
        } finally {
            JDBCUtils.close(connection);
        }
        return creado;
    }

    @Override
    public boolean update(ClienteDTO cliente) throws RentexpresException {
        Connection connection = null;
        boolean actualizado = false;
        try {
            connection = JDBCUtils.getConnection();
            JDBCUtils.beginTransaction(connection);
            actualizado = clienteDAO.update(connection, cliente);
            if (actualizado) {
                JDBCUtils.commitTransaction(connection);
                logger.info("Cliente actualizado exitosamente. ID: {}", cliente.getId());
            } else {
                JDBCUtils.rollbackTransaction(connection);
                logger.warn("No se pudo actualizar el Cliente. ID: {}", cliente.getId());
            }
        } catch (SQLException | DataException e) {
            JDBCUtils.rollbackTransaction(connection);
            logger.error("Error en update de Cliente: ", e);
            throw new RentexpresException("Error en update de Cliente", e);
        } finally {
            JDBCUtils.close(connection);
        }
        return actualizado;
    }

    @Override
    public boolean delete(Integer id) throws RentexpresException {
        Connection connection = null;
        boolean eliminado = false;
        try {
            connection = JDBCUtils.getConnection();
            JDBCUtils.beginTransaction(connection);
            eliminado = clienteDAO.delete(connection, id);
            if (eliminado) {
                JDBCUtils.commitTransaction(connection);
                logger.info("Cliente eliminado exitosamente. ID: {}", id);
            } else {
                JDBCUtils.rollbackTransaction(connection);
                logger.warn("No se pudo eliminar el Cliente. ID: {}", id);
            }
        } catch (SQLException | DataException e) {
            JDBCUtils.rollbackTransaction(connection);
            logger.error("Error en delete de Cliente: ", e);
            throw new RentexpresException("Error en delete de Cliente", e);
        } finally {
            JDBCUtils.close(connection);
        }
        return eliminado;
    }

    @Override
    public Results<ClienteDTO> findByCriteria(ClienteCriteria criteria) throws RentexpresException {
        Results<ClienteDTO> results = null;
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            JDBCUtils.beginTransaction(connection);
            results = clienteDAO.findByCriteria(connection, criteria);
            JDBCUtils.commitTransaction(connection);
            logger.info("findByCriteria de Cliente completado: Página {} (Tamaño: {}), Total registros: {}",
                    criteria.getPageNumber(), criteria.getPageSize(), results.getTotalRecords());
        } catch (SQLException | DataException e) {
            JDBCUtils.rollbackTransaction(connection);
            logger.error("Error en findByCriteria de Cliente: ", e);
            throw new RentexpresException("Error en findByCriteria de Cliente", e);
        } finally {
            JDBCUtils.close(connection);
        }
        return results;
    }
}
