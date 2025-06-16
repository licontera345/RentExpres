package com.pinguela.rentexpres.service;

import java.util.List;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.ReservaCriteria;
import com.pinguela.rentexpres.model.ReservaDTO;
import com.pinguela.rentexpres.model.Results;

public interface ReservaService {

	public ReservaDTO findById(Integer id) throws RentexpresException;

	public List<ReservaDTO> findAll() throws RentexpresException;

	public boolean create(ReservaDTO reserva) throws RentexpresException;

	public boolean update(ReservaDTO reserva) throws RentexpresException;

	public boolean delete(Integer id) throws RentexpresException;

	public Results<ReservaDTO> findByCriteria(ReservaCriteria criteria) throws RentexpresException;

}
