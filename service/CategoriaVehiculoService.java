package com.pinguela.rentexpres.service;

import java.util.List;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.CategoriaVehiculoDTO;

public interface CategoriaVehiculoService {
	public CategoriaVehiculoDTO findById(Integer id) throws RentexpresException;

	public List<CategoriaVehiculoDTO> findAll() throws RentexpresException;
}
