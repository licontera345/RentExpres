package com.pinguela.rentexpres.service;

import java.io.File;
import java.util.List;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.model.VehicleCriteria;
import com.pinguela.rentexpres.model.VehicleDTO;

public interface VehiculoService {

	public VehicleDTO findById(Integer id) throws RentexpresException;

	public List<VehicleDTO> findAll() throws RentexpresException;

	public boolean delete(Integer id) throws RentexpresException;

	public Results<VehicleDTO> findByCriteria(VehicleCriteria criteria) throws RentexpresException;

	boolean create(VehicleDTO vehiculo, File imagen) throws RentexpresException;

	boolean update(VehicleDTO vehiculo, File nuevaImagen) throws RentexpresException;

	List<String> getVehicleImages(Integer idVehiculo) throws RentexpresException;

	boolean updateVehicleImage(Integer idVehiculo, File nuevaImagen) throws RentexpresException;

}
