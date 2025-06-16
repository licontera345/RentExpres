package com.pinguela.rentexpres.service;

import java.util.List;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.EstadoReservaDTO;

public interface EstadoReservaService {
	public EstadoReservaDTO findById(Integer id) throws RentexpresException;

	public List<EstadoReservaDTO> findAll() throws RentexpresException;
}
