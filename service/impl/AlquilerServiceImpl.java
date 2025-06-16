package com.pinguela.rentexpres.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.pinguela.rentexpres.dao.AlquilerDAO;
import com.pinguela.rentexpres.dao.impl.AlquilerDAOImpl;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.AlquilerCriteria;
import com.pinguela.rentexpres.model.AlquilerDTO;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.service.AlquilerService;
import com.pinguela.rentexpres.util.JDBCUtils;

public class AlquilerServiceImpl implements AlquilerService {

	private static final Logger logger = LogManager.getLogger(AlquilerServiceImpl.class);
	private AlquilerDAO alquilerDAO;
	Connection connection = null;

	public AlquilerServiceImpl() {
		this.alquilerDAO = new AlquilerDAOImpl();
	}

	@Override
	public AlquilerDTO findById(Integer id) throws RentexpresException {
		AlquilerDTO alquiler = null;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);
			alquiler = alquilerDAO.findById(connection, id);
			JDBCUtils.commitTransaction(connection);
			logger.info("Transacción findById de Alquiler completada. ID: {}", id);
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error("Error en findById de Alquiler: ", e);
			throw new RentexpresException("Error en findById de Alquiler", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return alquiler;
	}

	@Override
	public List<AlquilerDTO> findAll() throws RentexpresException {
		List<AlquilerDTO> lista = null;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);
			lista = alquilerDAO.findAll(connection);
			JDBCUtils.commitTransaction(connection);
			logger.info("Transacción findAll de Alquiler completada. Cantidad: {}", (lista != null ? lista.size() : 0));
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error("Error en findAll de Alquiler: ", e);
			throw new RentexpresException("Error en findAll de Alquiler", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return lista;
	}

	@Override
	public boolean create(AlquilerDTO alquiler) throws RentexpresException {
		boolean creado = false;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);
			creado = alquilerDAO.create(connection, alquiler);
			if (creado) {
				JDBCUtils.commitTransaction(connection);
				logger.info("Alquiler creado exitosamente. ID: {}", alquiler.getId());
			} else {
				JDBCUtils.rollbackTransaction(connection);
				logger.warn("No se pudo crear el Alquiler.");
			}
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error("Error en create Alquiler: ", e);
			throw new RentexpresException("Error en create Alquiler", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return creado;
	}

	@Override
	public boolean update(AlquilerDTO alquiler) throws RentexpresException {
		boolean actualizado = false;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);
			actualizado = alquilerDAO.update(connection, alquiler);
			if (actualizado) {
				JDBCUtils.commitTransaction(connection);
				logger.info("Alquiler actualizado exitosamente. ID: {}", alquiler.getId());
			} else {
				JDBCUtils.rollbackTransaction(connection);
				logger.warn("No se pudo actualizar el Alquiler. ID: {}", alquiler.getId());
			}
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error("Error en update Alquiler: ", e);
			throw new RentexpresException("Error en update Alquiler", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return actualizado;
	}

	@Override
	public boolean delete(Integer id) throws RentexpresException {
		boolean eliminado = false;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);
			eliminado = alquilerDAO.delete(connection, id);
			if (eliminado) {
				JDBCUtils.commitTransaction(connection);
				logger.info("Alquiler eliminado exitosamente. ID: {}", id);
			} else {
				JDBCUtils.rollbackTransaction(connection);
				logger.warn("No se pudo eliminar el Alquiler. ID: {}", id);
			}
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error("Error en delete Alquiler: ", e);
			throw new RentexpresException("Error en delete Alquiler", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return eliminado;
	}

	@Override
	public Results<AlquilerDTO> findByCriteria(AlquilerCriteria criteria) throws RentexpresException {
		Results<AlquilerDTO> results = null;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);
			results = alquilerDAO.findByCriteria(connection, criteria);
			JDBCUtils.commitTransaction(connection);
			logger.info("findByCriteria de Alquiler completado. Cantidad: {}",
					(results != null && results.getResults() != null ? results.getResults().size() : 0));
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error("Error en findByCriteria de Alquiler", e);
			throw new RentexpresException("Error en findByCriteria de Alquiler", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return results;
	}
	@Override
	public boolean existsByReserva(Integer idReserva) throws RentexpresException {
	    try {
	        return alquilerDAO.existsByReserva(idReserva);
	    } catch (DataException e) {
	        throw new RentexpresException("Error comprobando existencia de alquiler para reserva ID " + idReserva, e);
	    }
	}

}
