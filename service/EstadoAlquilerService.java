package com.pinguela.rentexpres.service;

import java.util.List;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.RentalStatusDTO;

public interface EstadoAlquilerService {

	public RentalStatusDTO findById( Integer id) throws RentexpresException;

	public List<RentalStatusDTO> findAll() throws RentexpresException;

}
