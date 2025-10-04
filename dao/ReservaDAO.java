package com.pinguela.rentexpres.dao;

import java.sql.Connection;
import java.util.List;

import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.ReservationCriteria;
import com.pinguela.rentexpres.model.ReservationDTO;
import com.pinguela.rentexpres.model.Results;

public interface ReservaDAO {

	public ReservationDTO findById(Connection connection, Integer id) throws DataException;

	public List<ReservationDTO> findAll(Connection connection) throws DataException;

	public boolean create(Connection connection, ReservationDTO reserva) throws DataException;

	public boolean update(Connection connection, ReservationDTO reserva) throws DataException;

	public boolean delete(Connection connection, Integer id) throws DataException;

	public Results<ReservationDTO> findByCriteria(Connection connection, ReservationCriteria criteria) throws DataException;
}
