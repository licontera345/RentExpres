package com.pinguela.rentexpres.dao;

import java.sql.Connection;
import java.util.List;

import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.TipoUsuarioDTO;

public interface TipoUsuarioDAO {

	public TipoUsuarioDTO findById(Connection connection, Integer id)throws DataException;

	public List<TipoUsuarioDTO> findAll(Connection connection)throws DataException;

}
