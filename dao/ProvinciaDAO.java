package com.pinguela.rentexpres.dao;

import java.sql.Connection;
import java.util.List;

import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.ProvinciaDTO;

public interface ProvinciaDAO {

	public ProvinciaDTO findById(Connection connection, Integer id) throws DataException;

	public List<ProvinciaDTO> findAll(Connection connection) throws DataException;

	public boolean create(Connection connection, ProvinciaDTO provincia) throws DataException;

	public boolean update(Connection connection, ProvinciaDTO provincia) throws DataException;

	public boolean delete(Connection connection, ProvinciaDTO provincia) throws DataException;

}
