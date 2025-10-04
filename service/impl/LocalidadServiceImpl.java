package com.pinguela.rentexpres.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.dao.LocalidadDAO;
import com.pinguela.rentexpres.dao.impl.LocalidadDAOImpl;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.CityDTO;
import com.pinguela.rentexpres.service.LocalidadService;
import com.pinguela.rentexpres.util.JDBCUtils;

/**
 * Implementación de LocalidadService.  
 * Gestiona la transacción y delega la lógica de persistencia al DAO.
 */
public class LocalidadServiceImpl implements LocalidadService {

	/* --------------------------------------------------------- */
	private static final Logger logger = LogManager.getLogger(LocalidadServiceImpl.class);

	private final LocalidadDAO localidadDAO = new LocalidadDAOImpl();

	/* --------------------------------------------------------- */
	@Override
	public CityDTO findById(Integer id) throws RentexpresException {
		try (Connection c = JDBCUtils.getConnection()) {
			JDBCUtils.beginTransaction(c);
			CityDTO dto = localidadDAO.findById(c, id);
			JDBCUtils.commitTransaction(c);
			logger.info("findById Localidad OK (id={})", id);
			return dto;
		} catch (SQLException | DataException ex) {
			logger.error("findById Localidad ERROR", ex);
			throw new RentexpresException("Error buscando localidad", ex);
		}
	}

	@Override
	public List<CityDTO> findAll() throws RentexpresException {
		try (Connection c = JDBCUtils.getConnection()) {
			JDBCUtils.beginTransaction(c);
			List<CityDTO> list = localidadDAO.findAll(c);
			JDBCUtils.commitTransaction(c);
			logger.info("findAll Localidad OK ({} filas)", list.size());
			return list;
		} catch (SQLException | DataException ex) {
			logger.error("findAll Localidad ERROR", ex);
			throw new RentexpresException("Error listando localidades", ex);
		}
	}

	/* -------- NUEVO -------- */
	@Override
	public List<CityDTO> findByProvinciaId(Integer idProvincia) throws RentexpresException {
		try (Connection c = JDBCUtils.getConnection()) {
			JDBCUtils.beginTransaction(c);
			List<CityDTO> list = localidadDAO.findByProvinciaId(c, idProvincia);
			JDBCUtils.commitTransaction(c);
			logger.info("findByProvinciaId Localidad OK (prov={}, {} filas)", idProvincia, list.size());
			return list;
		} catch (SQLException | DataException ex) {
			logger.error("findByProvinciaId Localidad ERROR", ex);
			throw new RentexpresException("Error buscando localidades por provincia", ex);
		}
	}

	@Override
	public boolean create(CityDTO l) throws RentexpresException {
		try (Connection c = JDBCUtils.getConnection()) {
			JDBCUtils.beginTransaction(c);
			boolean ok = localidadDAO.create(c, l);
			if (ok) JDBCUtils.commitTransaction(c); else JDBCUtils.rollbackTransaction(c);
			return ok;
		} catch (SQLException | DataException ex) {
			logger.error("create Localidad ERROR", ex);
			throw new RentexpresException("Error creando localidad", ex);
		}
	}

	@Override
	public boolean update(CityDTO l) throws RentexpresException {
		try (Connection c = JDBCUtils.getConnection()) {
			JDBCUtils.beginTransaction(c);
			boolean ok = localidadDAO.update(c, l);
			if (ok) JDBCUtils.commitTransaction(c); else JDBCUtils.rollbackTransaction(c);
			return ok;
		} catch (SQLException | DataException ex) {
			logger.error("update Localidad ERROR", ex);
			throw new RentexpresException("Error actualizando localidad", ex);
		}
	}

	@Override
	public boolean delete(CityDTO l) throws RentexpresException {
		try (Connection c = JDBCUtils.getConnection()) {
			JDBCUtils.beginTransaction(c);
			boolean ok = localidadDAO.delete(c, l);
			if (ok) JDBCUtils.commitTransaction(c); else JDBCUtils.rollbackTransaction(c);
			return ok;
		} catch (SQLException | DataException ex) {
			logger.error("delete Localidad ERROR", ex);
			throw new RentexpresException("Error eliminando localidad", ex);
		}
	}
}
