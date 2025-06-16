package com.pinguela.rentexpres.dao;

import java.sql.Connection;
import java.util.List;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.EstadoReservaDTO;

public interface EstadoReservaDAO {
	public EstadoReservaDTO findById(Connection connection, Integer id) throws DataException;

	public List<EstadoReservaDTO> findAll(Connection connection) throws DataException;
	
	
}
