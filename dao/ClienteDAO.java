package com.pinguela.rentexpres.dao;

import java.sql.Connection;
import java.util.List;

import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.ClienteCriteria;
import com.pinguela.rentexpres.model.ClienteDTO;
import com.pinguela.rentexpres.model.Results;

public interface ClienteDAO {

	public ClienteDTO findById(Connection connection, Integer id) throws DataException;

	public List<ClienteDTO> findAll(Connection connection) throws DataException;

	public boolean create(Connection connection, ClienteDTO cliente) throws DataException;

	public boolean update(Connection connection, ClienteDTO cliente) throws DataException;

	public boolean delete(Connection connection, Integer id) throws DataException;

	public Results<ClienteDTO> findByCriteria(Connection connection, ClienteCriteria criteria) throws DataException;

}
