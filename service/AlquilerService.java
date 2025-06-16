package com.pinguela.rentexpres.service;

import java.util.List;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.AlquilerCriteria;
import com.pinguela.rentexpres.model.AlquilerDTO;
import com.pinguela.rentexpres.model.Results;

public interface AlquilerService {

	public AlquilerDTO findById(Integer id) throws RentexpresException;

	public List<AlquilerDTO> findAll() throws RentexpresException;;

	public boolean create(AlquilerDTO alquiler) throws RentexpresException;

	public boolean update(AlquilerDTO alquiler) throws RentexpresException;

	public boolean delete(Integer id) throws RentexpresException;

	public Results<AlquilerDTO> findByCriteria(AlquilerCriteria criteria) throws RentexpresException;

	boolean existsByReserva(Integer idReserva) throws RentexpresException;

}
