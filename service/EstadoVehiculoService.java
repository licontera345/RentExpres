package com.pinguela.rentexpres.service;


import java.util.List;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.EstadoVehiculoDTO;

public interface EstadoVehiculoService {

    public EstadoVehiculoDTO findById(Integer id) throws RentexpresException;

	public List<EstadoVehiculoDTO> findAll() throws RentexpresException;

}
