package com.pinguela.rentexpres.dao;

import java.sql.Connection;
import java.util.List;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.ReservationStatusDTO;

public interface EstadoReservaDAO {
	public ReservationStatusDTO findById(Connection connection, Integer id) throws DataException;

	public List<ReservationStatusDTO> findAll(Connection connection) throws DataException;
	
	
}
