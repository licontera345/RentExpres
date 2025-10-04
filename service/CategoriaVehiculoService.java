package com.pinguela.rentexpres.service;

import java.util.List;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.CategoriaVehicleDTO;

public interface CategoriaVehiculoService {
	public CategoriaVehicleDTO findById(Integer id) throws RentexpresException;

	public List<CategoriaVehicleDTO> findAll() throws RentexpresException;
}
