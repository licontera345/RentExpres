package com.pinguela.rentexpres.dao;

import java.sql.Connection;
import java.util.List;

import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.RentalStatusDTO;

public interface EstadoAlquilerDAO {
	public RentalStatusDTO findById(Connection connection, Integer id)throws DataException;

	public List<RentalStatusDTO> findAll(Connection connection)throws DataException;

}
