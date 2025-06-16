package com.pinguela.rentexpres.service;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.DireccionDTO;

public interface DireccionService {
	
	
	public DireccionDTO findById(Integer id) throws RentexpresException;

	public boolean create(DireccionDTO direccion) throws RentexpresException;

	public boolean update(DireccionDTO direccion) throws RentexpresException;

	public boolean delete(DireccionDTO direccion) throws RentexpresException;
}