package com.pinguela.rentexpres.service;


import java.util.List;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.VehicleStatusDTO;

public interface EstadoVehiculoService {

    public VehicleStatusDTO findById(Integer id) throws RentexpresException;

	public List<VehicleStatusDTO> findAll() throws RentexpresException;

}
