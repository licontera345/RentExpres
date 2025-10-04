package com.pinguela.rentexpres.service;

import java.util.List;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.UserTypeDTO;

public interface TipoUsuarioService {

	public UserTypeDTO findById(Integer id) throws RentexpresException;

	public List<UserTypeDTO> findAll() throws RentexpresException;

	
}