package com.pinguela.rentexpres.dao;

import java.sql.Connection;
import java.util.List;

import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.RentalCriteria;
import com.pinguela.rentexpres.model.RentalDTO;
import com.pinguela.rentexpres.model.Results;

public interface AlquilerDAO {

	public RentalDTO findById(Connection connection, Integer id) throws DataException;

	public List<RentalDTO> findAll(Connection connection) throws DataException;

	public boolean create(Connection connection, RentalDTO alquiler) throws DataException;

	public boolean update(Connection connection, RentalDTO alquiler) throws DataException;

	public boolean delete(Connection connection, Integer id) throws DataException;

	public Results<RentalDTO> findByCriteria(Connection connection, RentalCriteria criteria) throws DataException;

	boolean existsByReserva(Integer idReserva) throws DataException;
}
