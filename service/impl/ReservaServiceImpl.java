package com.pinguela.rentexpres.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.pinguela.rentexpres.dao.ReservaDAO;
import com.pinguela.rentexpres.dao.impl.ReservaDAOImpl;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.ReservaCriteria;
import com.pinguela.rentexpres.model.ReservaDTO;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.service.ReservaService;
import com.pinguela.rentexpres.util.JDBCUtils;

public class ReservaServiceImpl implements ReservaService {

	private static final Logger logger = LogManager.getLogger(ReservaServiceImpl.class);
	private ReservaDAO reservaDAO;

	public ReservaServiceImpl() {
		this.reservaDAO = new ReservaDAOImpl();
	}

	@Override
	public ReservaDTO findById(Integer id) throws RentexpresException {
		Connection connection = null;
		ReservaDTO reserva = null;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);
			reserva = reservaDAO.findById(connection, id);
			JDBCUtils.commitTransaction(connection);
			logger.info("Transacción findById de Reserva completada. ID: {}", id);
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error("Error en findById de Reserva: ", e);
			throw new RentexpresException("Error en findById de Reserva", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return reserva;
	}

	@Override
	public List<ReservaDTO> findAll() throws RentexpresException {
		Connection connection = null;
		List<ReservaDTO> reservas = null;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);
			reservas = reservaDAO.findAll(connection);
			JDBCUtils.commitTransaction(connection);
			logger.info("Transacción findAll de Reserva completada. Cantidad: {}",
					reservas != null ? reservas.size() : 0);
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error("Error en findAll de Reserva: ", e);
			throw new RentexpresException("Error en findAll de Reserva", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return reservas;
	}

	@Override
	public boolean create(ReservaDTO reserva) throws RentexpresException {
		Connection connection = null;
		boolean creado = false;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);
			creado = reservaDAO.create(connection, reserva);
			if (creado) {
				JDBCUtils.commitTransaction(connection);
				logger.info("Reserva creada exitosamente. ID: {}", reserva.getId());
			} else {
				JDBCUtils.rollbackTransaction(connection);
				logger.warn("No se pudo crear la Reserva.");
			}
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error("Error en create Reserva: ", e);
			throw new RentexpresException("Error en create Reserva", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return creado;
	}

	@Override
	public boolean update(ReservaDTO reserva) throws RentexpresException {
		Connection connection = null;
		boolean actualizado = false;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);
			actualizado = reservaDAO.update(connection, reserva);
			if (actualizado) {
				JDBCUtils.commitTransaction(connection);
				logger.info("Reserva actualizada exitosamente. ID: {}", reserva.getId());
			} else {
				JDBCUtils.rollbackTransaction(connection);
				logger.warn("No se pudo actualizar la Reserva. ID: {}", reserva.getId());
			}
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error("Error en update Reserva: ", e);
			throw new RentexpresException("Error en update Reserva", e);
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
			eliminado = reservaDAO.delete(connection, id);
			if (eliminado) {
				JDBCUtils.commitTransaction(connection);
				logger.info("Reserva eliminada exitosamente. ID: {}", id);
			} else {
				JDBCUtils.rollbackTransaction(connection);
				logger.warn("No se pudo eliminar la Reserva. ID: {}", id);
			}
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error("Error en delete Reserva: ", e);
			throw new RentexpresException("Error en delete Reserva", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return eliminado;
	}

	@Override
	public Results<ReservaDTO> findByCriteria(ReservaCriteria criteria) throws RentexpresException {
		Connection connection = null;
		Results<ReservaDTO> results = null;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);
			results = reservaDAO.findByCriteria(connection, criteria);
			JDBCUtils.commitTransaction(connection);
			logger.info("findByCriteria de Reserva completado: Página {} (Tamaño: {}), Total registros: {}",
					criteria.getPageNumber(), criteria.getPageSize(), results.getTotalRecords());
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error("Error en findByCriteria de Reserva: ", e);
			throw new RentexpresException("Error en findByCriteria de Reserva", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return results;
	}

}
