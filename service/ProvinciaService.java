package com.pinguela.rentexpres.service;

import java.util.List;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.ProvinciaDTO;

public interface ProvinciaService {

	public ProvinciaDTO findById(Integer id) throws RentexpresException;

	public List<ProvinciaDTO> findAll() throws RentexpresException;

	public boolean create(ProvinciaDTO provincia) throws RentexpresException;

	public boolean update(ProvinciaDTO provincia) throws RentexpresException;

	public boolean delete(ProvinciaDTO provincia) throws RentexpresException;

}
