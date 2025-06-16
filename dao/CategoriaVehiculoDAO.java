package com.pinguela.rentexpres.dao;

import java.sql.Connection;
import java.util.List;

import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.CategoriaVehiculoDTO;

public interface CategoriaVehiculoDAO {

	public CategoriaVehiculoDTO findById(Connection connection, Integer id)throws DataException;

	public List<CategoriaVehiculoDTO> findAll(Connection connection)throws DataException;

}
