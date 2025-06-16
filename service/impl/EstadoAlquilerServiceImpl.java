package com.pinguela.rentexpres.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.pinguela.rentexpres.dao.EstadoAlquilerDAO;
import com.pinguela.rentexpres.dao.impl.EstadoAlquilerDAOImpl;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.EstadoAlquilerDTO;
import com.pinguela.rentexpres.service.EstadoAlquilerService;
import com.pinguela.rentexpres.util.JDBCUtils;

public class EstadoAlquilerServiceImpl implements EstadoAlquilerService {

	private static final Logger logger = LogManager.getLogger(EstadoAlquilerServiceImpl.class);
	private EstadoAlquilerDAO estadoAlquilerDAO;

	public EstadoAlquilerServiceImpl() {
		this.estadoAlquilerDAO = new EstadoAlquilerDAOImpl();
	}

	@Override
	public EstadoAlquilerDTO findById(Integer id) throws RentexpresException {
		Connection connection = null;
		EstadoAlquilerDTO estado = null;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);
			try {
				estado = estadoAlquilerDAO.findById(connection, id);
			} catch (DataException e) {
				e.printStackTrace();
			}
			JDBCUtils.commitTransaction(connection);
			logger.info("findById de EstadoAlquiler completado. ID: " + id);
		} catch (SQLException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error("Error en findById de EstadoAlquiler: ", e);
			throw new RentexpresException("Error en findById de EstadoAlquiler", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return estado;
	}

	@Override
	public List<EstadoAlquilerDTO> findAll() throws RentexpresException {
		Connection connection = null;
		List<EstadoAlquilerDTO> lista = null;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);
			try {
				lista = estadoAlquilerDAO.findAll(connection);
			} catch (DataException e) {
				e.printStackTrace();
			}
			JDBCUtils.commitTransaction(connection);
			logger.info("findAll de EstadoAlquiler completado. Cantidad: " + (lista != null ? lista.size() : 0));
		} catch (SQLException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error("Error en findAll de EstadoAlquiler: ", e);
			throw new RentexpresException("Error en findAll de EstadoAlquiler", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return lista;
	}
}
