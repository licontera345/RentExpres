package com.pinguela.rentexpres.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.pinguela.rentexpres.dao.CategoriaVehiculoDAO;
import com.pinguela.rentexpres.dao.impl.CategoriaVehiculoDAOImpl;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.CategoriaVehiculoDTO;
import com.pinguela.rentexpres.service.CategoriaVehiculoService;
import com.pinguela.rentexpres.util.JDBCUtils;

public class CategoriaVehiculoServiceImpl implements CategoriaVehiculoService {

	private static final Logger logger = LogManager.getLogger(CategoriaVehiculoServiceImpl.class);
	private CategoriaVehiculoDAO categoriaDAO;

	public CategoriaVehiculoServiceImpl() {
		this.categoriaDAO = new CategoriaVehiculoDAOImpl();
	}

	@Override
	public CategoriaVehiculoDTO findById(Integer id) throws RentexpresException {
		Connection connection = null;
		CategoriaVehiculoDTO categoria = null;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);
			try {
				categoria = categoriaDAO.findById(connection, id);
			} catch (DataException e) {
				e.printStackTrace();
			}
			JDBCUtils.commitTransaction(connection);
			logger.info("findById de CategoriaVehiculo completado. ID: " + id);
		} catch (SQLException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error("Error en findById de CategoriaVehiculo: ", e);
			throw new RentexpresException("Error en findById de CategoriaVehiculo", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return categoria;
	}

	@Override
	public List<CategoriaVehiculoDTO> findAll() throws RentexpresException {
		Connection connection = null;
		List<CategoriaVehiculoDTO> lista = null;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);
			try {
				lista = categoriaDAO.findAll(connection);
			} catch (DataException e) {
				e.printStackTrace();
			}
			JDBCUtils.commitTransaction(connection);
			logger.info("findAll de CategoriaVehiculo completado. Cantidad: " + (lista != null ? lista.size() : 0));
		} catch (SQLException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error("Error en findAll de CategoriaVehiculo: ", e);
			throw new RentexpresException("Error en findAll de CategoriaVehiculo", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return lista;
	}
}
