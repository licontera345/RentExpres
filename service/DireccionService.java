package com.pinguela.rentexpres.service;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.AddressDTO;

public interface DireccionService {
	
	
	public AddressDTO findById(Integer id) throws RentexpresException;

	public boolean create(AddressDTO direccion) throws RentexpresException;

	public boolean update(AddressDTO direccion) throws RentexpresException;

	public boolean delete(AddressDTO direccion) throws RentexpresException;
}