package com.pinguela.rentexpres.service;

import java.util.List;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.CityDTO;

public interface LocalidadService {

	public CityDTO findById(Integer id) throws RentexpresException;

	public List<CityDTO> findAll() throws RentexpresException;

	public boolean create(CityDTO localidad) throws RentexpresException;

	public boolean update(CityDTO localidad) throws RentexpresException;

	public boolean delete(CityDTO localidad) throws RentexpresException;

	List<CityDTO> findByProvinciaId(Integer idProvincia) throws RentexpresException;
}
