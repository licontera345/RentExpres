package com.pinguela.rentexpres.dao;

import java.sql.Connection;
import java.util.List;

import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.EstadoVehiculoDTO;

public interface EstadoVehiculoDAO {

	public EstadoVehiculoDTO findById(Connection connection, Integer id)throws DataException;

	public List<EstadoVehiculoDTO> findAll(Connection connection)throws DataException;

}
