package com.pinguela.rentexpres.service;

import java.io.File;
import java.util.List;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.model.VehiculoCriteria;
import com.pinguela.rentexpres.model.VehiculoDTO;

public interface VehiculoService {

	public VehiculoDTO findById(Integer id) throws RentexpresException;

	public List<VehiculoDTO> findAll() throws RentexpresException;

	public boolean delete(Integer id) throws RentexpresException;

	public Results<VehiculoDTO> findByCriteria(VehiculoCriteria criteria) throws RentexpresException;

	boolean create(VehiculoDTO vehiculo, File imagen) throws RentexpresException;

	boolean update(VehiculoDTO vehiculo, File nuevaImagen) throws RentexpresException;

	List<String> getVehicleImages(Integer idVehiculo) throws RentexpresException;

	boolean updateVehicleImage(Integer idVehiculo, File nuevaImagen) throws RentexpresException;

}
