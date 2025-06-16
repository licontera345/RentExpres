package com.pinguela.rentexpres.dao;

import java.sql.Connection;
import java.util.List;

import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.LocalidadDTO;

public interface LocalidadDAO {

	public LocalidadDTO findById(Connection connection, Integer id) throws DataException;

	List<LocalidadDTO> findAll(Connection connection) throws DataException;

	List<LocalidadDTO> findByProvinciaId(Connection c, Integer idProvincia) throws DataException;

	public boolean create(Connection connection, LocalidadDTO localidad) throws DataException;

	public boolean update(Connection connection, LocalidadDTO localidad) throws DataException;

	public boolean delete(Connection connection, LocalidadDTO localidad) throws DataException;

}
