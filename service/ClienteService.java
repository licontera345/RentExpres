package com.pinguela.rentexpres.service;

import java.util.List;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.ClienteCriteria;
import com.pinguela.rentexpres.model.ClienteDTO;
import com.pinguela.rentexpres.model.Results;

public interface ClienteService {

	public ClienteDTO findById(Integer id) throws RentexpresException;

	public List<ClienteDTO> findAll() throws RentexpresException;

	public boolean create(ClienteDTO cliente) throws RentexpresException;

	public boolean update(ClienteDTO cliente) throws RentexpresException;

	public boolean delete(Integer id) throws RentexpresException;

	public Results<ClienteDTO> findByCriteria(ClienteCriteria criteria) throws RentexpresException;

}
