package com.pinguela.rentexpres.dao;

import java.sql.Connection;
import java.util.List;

import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.model.VehiculoCriteria;
import com.pinguela.rentexpres.model.VehiculoDTO;

public interface VehiculoDAO {

	public VehiculoDTO findById(Connection connection, Integer id) throws DataException;

	public List<VehiculoDTO> findAll(Connection connection) throws DataException;

	public boolean create(Connection connection, VehiculoDTO vehiculo) throws DataException;

	public boolean update(Connection connection, VehiculoDTO vehiculo) throws DataException;

	public boolean delete(Connection connection, Integer id) throws DataException;

	public Results<VehiculoDTO> findByCriteria(Connection connection, VehiculoCriteria criteria) throws DataException;

}
