package com.pinguela.rentexpres.service;

import java.util.List;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.TipoUsuarioDTO;

public interface TipoUsuarioService {

	public TipoUsuarioDTO findById(Integer id) throws RentexpresException;

	public List<TipoUsuarioDTO> findAll() throws RentexpresException;

	
}