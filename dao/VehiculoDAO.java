package com.pinguela.rentexpres.dao;

import java.sql.Connection;
import java.util.List;

import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.model.VehicleCriteria;
import com.pinguela.rentexpres.model.VehicleDTO;

public interface VehiculoDAO {

	public VehicleDTO findById(Connection connection, Integer id) throws DataException;

	public List<VehicleDTO> findAll(Connection connection) throws DataException;

	public boolean create(Connection connection, VehicleDTO vehiculo) throws DataException;

	public boolean update(Connection connection, VehicleDTO vehiculo) throws DataException;

	public boolean delete(Connection connection, Integer id) throws DataException;

	public Results<VehicleDTO> findByCriteria(Connection connection, VehicleCriteria criteria) throws DataException;

}
