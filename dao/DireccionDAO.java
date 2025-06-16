package com.pinguela.rentexpres.dao;

import java.sql.Connection;

import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.DireccionDTO;

public interface DireccionDAO {

	public DireccionDTO findById(Connection connection, Integer id)throws DataException;
	public boolean create(Connection connection, DireccionDTO direccion)throws DataException;
	public boolean update(Connection connection, DireccionDTO direccion)throws DataException;
	public boolean delete(Connection connection, DireccionDTO direccion, Integer id)throws DataException;
}
