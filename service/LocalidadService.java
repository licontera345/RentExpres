package com.pinguela.rentexpres.service;

import java.util.List;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.LocalidadDTO;

public interface LocalidadService {

	public LocalidadDTO findById(Integer id) throws RentexpresException;

	public List<LocalidadDTO> findAll() throws RentexpresException;

	public boolean create(LocalidadDTO localidad) throws RentexpresException;

	public boolean update(LocalidadDTO localidad) throws RentexpresException;

	public boolean delete(LocalidadDTO localidad) throws RentexpresException;

	List<LocalidadDTO> findByProvinciaId(Integer idProvincia) throws RentexpresException;
}
