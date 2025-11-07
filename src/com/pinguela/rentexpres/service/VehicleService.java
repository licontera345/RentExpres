package com.pinguela.rentexpres.service;

import java.util.List;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.model.VehicleCriteria;
import com.pinguela.rentexpres.model.VehicleDTO;

/**
 * Service interface for managing vehicles and their images.
 */
public interface VehicleService {

	VehicleDTO findById(Integer id) throws RentexpresException;

	List<VehicleDTO> findAll() throws RentexpresException;

	boolean create(VehicleDTO vehicle) throws RentexpresException;

	boolean update(VehicleDTO vehicle) throws RentexpresException;

	boolean delete(Integer id) throws RentexpresException;

        Results<VehicleDTO> findByCriteria(VehicleCriteria criteria) throws RentexpresException;

        /**
         * Permite solicitar la búsqueda indicando la página y el tamaño explícitos.
         * El criteria recibido se reutiliza (o se crea uno nuevo si llega null) y se
         * configura con los valores indicados antes de delegar en
         * {@link #findByCriteria(VehicleCriteria)}.
         *
         * @param criteria criterios de filtrado (opcional)
         * @param page     número de página solicitado (1 en adelante)
         * @param pageSize tamaño de página (> 0)
         * @return resultados paginados de vehículos
         * @throws RentexpresException si ocurre algún problema de negocio o datos
         */
        Results<VehicleDTO> findBy(VehicleCriteria criteria, int page, int pageSize) throws RentexpresException;

}
