package com.pinguela.rentexpres.service;

import java.util.List;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.ProvinceDTO;

public interface ProvinciaService {

	public ProvinceDTO findById(Integer id) throws RentexpresException;

	public List<ProvinceDTO> findAll() throws RentexpresException;

	public boolean create(ProvinceDTO provincia) throws RentexpresException;

	public boolean update(ProvinceDTO provincia) throws RentexpresException;

	public boolean delete(ProvinceDTO provincia) throws RentexpresException;

}
