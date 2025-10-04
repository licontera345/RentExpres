package com.pinguela.rentexpres.dao;

import java.sql.Connection;
import java.util.List;

import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.UserTypeDTO;

public interface TipoUsuarioDAO {

	public UserTypeDTO findById(Connection connection, Integer id)throws DataException;

	public List<UserTypeDTO> findAll(Connection connection)throws DataException;

}
