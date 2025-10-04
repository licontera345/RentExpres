package com.pinguela.rentexpres.dao;

import java.sql.Connection;
import java.util.List;

import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.VehicleStatusDTO;

public interface EstadoVehiculoDAO {

	public VehicleStatusDTO findById(Connection connection, Integer id)throws DataException;

	public List<VehicleStatusDTO> findAll(Connection connection)throws DataException;

}
