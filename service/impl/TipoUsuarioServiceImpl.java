package com.pinguela.rentexpres.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.pinguela.rentexpres.dao.TipoUsuarioDAO;
import com.pinguela.rentexpres.dao.impl.TipoUsuarioDAOImpl;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.TipoUsuarioDTO;
import com.pinguela.rentexpres.service.TipoUsuarioService;
import com.pinguela.rentexpres.util.JDBCUtils;

public class TipoUsuarioServiceImpl implements TipoUsuarioService {

	private static final Logger logger = LogManager.getLogger(TipoUsuarioServiceImpl.class);
	private TipoUsuarioDAO tipoUsuarioDAO;

	public TipoUsuarioServiceImpl() {
		this.tipoUsuarioDAO = new TipoUsuarioDAOImpl();
	}

	@Override
	public TipoUsuarioDTO findById(Integer id) throws RentexpresException {
		Connection connection = null;
		TipoUsuarioDTO tipo = null;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);
			try {
				tipo = tipoUsuarioDAO.findById(connection, id);
			} catch (DataException e) {
				e.printStackTrace();
			}
			JDBCUtils.commitTransaction(connection);
			logger.info("findById de TipoUsuario completado. ID: " + id);
		} catch (SQLException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error("Error en findById de TipoUsuario: ", e);
			throw new RentexpresException("Error en findById de TipoUsuario", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return tipo;
	}

	@Override
	public List<TipoUsuarioDTO> findAll() throws RentexpresException {
		Connection connection = null;
		List<TipoUsuarioDTO> lista = null;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);
			try {
				lista = tipoUsuarioDAO.findAll(connection);
			} catch (DataException e) {
				e.printStackTrace();
			}
			JDBCUtils.commitTransaction(connection);
			logger.info("findAll de TipoUsuario completado. Cantidad: " + (lista != null ? lista.size() : 0));
		} catch (SQLException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error("Error en findAll de TipoUsuario: ", e);
			throw new RentexpresException("Error en findAll de TipoUsuario", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return lista;
	}
}
