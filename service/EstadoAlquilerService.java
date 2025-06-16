package com.pinguela.rentexpres.service;

import java.util.List;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.EstadoAlquilerDTO;

public interface EstadoAlquilerService {

	public EstadoAlquilerDTO findById( Integer id) throws RentexpresException;

	public List<EstadoAlquilerDTO> findAll() throws RentexpresException;

}
