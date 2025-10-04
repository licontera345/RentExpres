package com.pinguela.rentexpres.dao;

import java.sql.Connection;
import java.util.List;

import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.CustomerCriteria;
import com.pinguela.rentexpres.model.CustomerDTO;
import com.pinguela.rentexpres.model.Results;

public interface ClienteDAO {

	public CustomerDTO findById(Connection connection, Integer id) throws DataException;

	public List<CustomerDTO> findAll(Connection connection) throws DataException;

	public boolean create(Connection connection, CustomerDTO cliente) throws DataException;

	public boolean update(Connection connection, CustomerDTO cliente) throws DataException;

	public boolean delete(Connection connection, Integer id) throws DataException;

	public Results<CustomerDTO> findByCriteria(Connection connection, CustomerCriteria criteria) throws DataException;

}
