package com.pinguela.rentexpres.dao;

import java.sql.Connection;

import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.AddressDTO;

public interface DireccionDAO {

	public AddressDTO findById(Connection connection, Integer id)throws DataException;
	public boolean create(Connection connection, AddressDTO direccion)throws DataException;
	public boolean update(Connection connection, AddressDTO direccion)throws DataException;
	public boolean delete(Connection connection, AddressDTO direccion, Integer id)throws DataException;
}
