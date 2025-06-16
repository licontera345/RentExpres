package com.pinguela.rentexpres.dao;

import java.sql.Connection;
import java.util.List;

import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.ReservaCriteria;
import com.pinguela.rentexpres.model.ReservaDTO;
import com.pinguela.rentexpres.model.Results;

public interface ReservaDAO {

	public ReservaDTO findById(Connection connection, Integer id) throws DataException;

	public List<ReservaDTO> findAll(Connection connection) throws DataException;

	public boolean create(Connection connection, ReservaDTO reserva) throws DataException;

	public boolean update(Connection connection, ReservaDTO reserva) throws DataException;

	public boolean delete(Connection connection, Integer id) throws DataException;

	public Results<ReservaDTO> findByCriteria(Connection connection, ReservaCriteria criteria) throws DataException;
}
