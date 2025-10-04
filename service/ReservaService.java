package com.pinguela.rentexpres.service;

import java.util.List;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.ReservationCriteria;
import com.pinguela.rentexpres.model.ReservationDTO;
import com.pinguela.rentexpres.model.Results;

public interface ReservaService {

	public ReservationDTO findById(Integer id) throws RentexpresException;

	public List<ReservationDTO> findAll() throws RentexpresException;

	public boolean create(ReservationDTO reserva) throws RentexpresException;

	public boolean update(ReservationDTO reserva) throws RentexpresException;

	public boolean delete(Integer id) throws RentexpresException;

	public Results<ReservationDTO> findByCriteria(ReservationCriteria criteria) throws RentexpresException;

}
