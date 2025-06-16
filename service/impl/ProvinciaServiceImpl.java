package com.pinguela.rentexpres.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.pinguela.rentexpres.dao.ProvinciaDAO;
import com.pinguela.rentexpres.dao.impl.ProvinciaDAOImpl;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.ProvinciaDTO;
import com.pinguela.rentexpres.service.ProvinciaService;
import com.pinguela.rentexpres.util.JDBCUtils;

public class ProvinciaServiceImpl implements ProvinciaService {

	private static final Logger logger = LogManager.getLogger(ProvinciaServiceImpl.class);
	private ProvinciaDAO provinciaDAO;

	public ProvinciaServiceImpl() {
		this.provinciaDAO = new ProvinciaDAOImpl();
	}

	@Override
	public ProvinciaDTO findById(Integer id) throws RentexpresException {
		Connection connection = null;
		ProvinciaDTO provincia = null;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);
			provincia = provinciaDAO.findById(connection, id);
			JDBCUtils.commitTransaction(connection);
			logger.info("findById de Provincia completado. ID: {}", id);
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error("Error en findById de Provincia: ", e);
			throw new RentexpresException("Error en findById de Provincia", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return provincia;
	}

	@Override
	public List<ProvinciaDTO> findAll() throws RentexpresException {
		Connection connection = null;
		List<ProvinciaDTO> lista = null;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);
			lista = provinciaDAO.findAll(connection);
			JDBCUtils.commitTransaction(connection);
			logger.info("findAll de Provincia completado. Cantidad: {}", (lista != null ? lista.size() : 0));
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error("Error en findAll de Provincia: ", e);
			throw new RentexpresException("Error en findAll de Provincia", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return lista;
	}

	@Override
	public boolean create(ProvinciaDTO provincia) throws RentexpresException {
		Connection connection = null;
		boolean creado = false;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);
			creado = provinciaDAO.create(connection, provincia);
			if (creado) {
				JDBCUtils.commitTransaction(connection);
				logger.info("Provincia creada exitosamente. ID: {}", provincia.getId());
			} else {
				JDBCUtils.rollbackTransaction(connection);
				logger.warn("No se pudo crear la Provincia.");
			}
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error("Error en create de Provincia: ", e);
			throw new RentexpresException("Error en create de Provincia", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return creado;
	}

	@Override
	public boolean update(ProvinciaDTO provincia) throws RentexpresException {
		Connection connection = null;
		boolean actualizado = false;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);
			actualizado = provinciaDAO.update(connection, provincia);
			if (actualizado) {
				JDBCUtils.commitTransaction(connection);
				logger.info("Provincia actualizada exitosamente. ID: {}", provincia.getId());
			} else {
				JDBCUtils.rollbackTransaction(connection);
				logger.warn("No se pudo actualizar la Provincia. ID: {}", provincia.getId());
			}
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error("Error en update de Provincia: ", e);
			throw new RentexpresException("Error en update de Provincia", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return actualizado;
	}

	@Override
	public boolean delete(ProvinciaDTO provincia) throws RentexpresException {
		Connection connection = null;
		boolean eliminado = false;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);
			eliminado = provinciaDAO.delete(connection, provincia);
			if (eliminado) {
				JDBCUtils.commitTransaction(connection);
				logger.info("Provincia eliminada exitosamente. ID: {}", provincia.getId());
			} else {
				JDBCUtils.rollbackTransaction(connection);
				logger.warn("No se pudo eliminar la Provincia. ID: {}", provincia.getId());
			}
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error("Error en delete de Provincia: ", e);
			throw new RentexpresException("Error en delete de Provincia", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return eliminado;
	}
}
