package com.pinguela.rentexpres.service;

import java.util.List;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.ReservationStatusDTO;

public interface EstadoReservaService {
	public ReservationStatusDTO findById(Integer id) throws RentexpresException;

	public List<ReservationStatusDTO> findAll() throws RentexpresException;
}
