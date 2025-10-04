package com.pinguela.rentexpres.dao;

import java.sql.Connection;
import java.util.List;

import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.CategoriaVehicleDTO;

public interface CategoriaVehiculoDAO {

	public CategoriaVehicleDTO findById(Connection connection, Integer id)throws DataException;

	public List<CategoriaVehicleDTO> findAll(Connection connection)throws DataException;

}
