package com.pinguela.rentexpres.service;

import java.util.List;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.CustomerCriteria;
import com.pinguela.rentexpres.model.CustomerDTO;
import com.pinguela.rentexpres.model.Results;

public interface ClienteService {

	public CustomerDTO findById(Integer id) throws RentexpresException;

	public List<CustomerDTO> findAll() throws RentexpresException;

	public boolean create(CustomerDTO cliente) throws RentexpresException;

	public boolean update(CustomerDTO cliente) throws RentexpresException;

	public boolean delete(Integer id) throws RentexpresException;

	public Results<CustomerDTO> findByCriteria(CustomerCriteria criteria) throws RentexpresException;

}
