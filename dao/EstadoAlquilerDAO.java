package com.pinguela.rentexpres.dao;

import java.sql.Connection;
import java.util.List;

import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.EstadoAlquilerDTO;

public interface EstadoAlquilerDAO {
	public EstadoAlquilerDTO findById(Connection connection, Integer id)throws DataException;

	public List<EstadoAlquilerDTO> findAll(Connection connection)throws DataException;

}
