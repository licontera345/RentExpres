package com.pinguela.rentexpres.dao;

import java.sql.Connection;
import java.util.List;

import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.AlquilerCriteria;
import com.pinguela.rentexpres.model.AlquilerDTO;
import com.pinguela.rentexpres.model.Results;

public interface AlquilerDAO {

	public AlquilerDTO findById(Connection connection, Integer id) throws DataException;

	public List<AlquilerDTO> findAll(Connection connection) throws DataException;

	public boolean create(Connection connection, AlquilerDTO alquiler) throws DataException;

	public boolean update(Connection connection, AlquilerDTO alquiler) throws DataException;

	public boolean delete(Connection connection, Integer id) throws DataException;

	public Results<AlquilerDTO> findByCriteria(Connection connection, AlquilerCriteria criteria) throws DataException;

	boolean existsByReserva(Integer idReserva) throws DataException;
}
