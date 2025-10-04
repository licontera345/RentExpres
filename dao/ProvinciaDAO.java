package com.pinguela.rentexpres.dao;

import java.sql.Connection;
import java.util.List;

import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.ProvinceDTO;

public interface ProvinciaDAO {

	public ProvinceDTO findById(Connection connection, Integer id) throws DataException;

	public List<ProvinceDTO> findAll(Connection connection) throws DataException;

	public boolean create(Connection connection, ProvinceDTO provincia) throws DataException;

	public boolean update(Connection connection, ProvinceDTO provincia) throws DataException;

	public boolean delete(Connection connection, ProvinceDTO provincia) throws DataException;

}
