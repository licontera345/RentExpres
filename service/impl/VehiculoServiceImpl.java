package com.pinguela.rentexpres.service.impl;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.dao.VehiculoDAO;
import com.pinguela.rentexpres.dao.impl.VehiculoDAOImpl;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.model.VehiculoCriteria;
import com.pinguela.rentexpres.model.VehiculoDTO;
import com.pinguela.rentexpres.service.FileService;
import com.pinguela.rentexpres.service.VehiculoService;
import com.pinguela.rentexpres.util.JDBCUtils;

public class VehiculoServiceImpl implements VehiculoService {

	private static final Logger logger = LogManager.getLogger(VehiculoServiceImpl.class);
	private final VehiculoDAO vehiculoDAO;
	private final FileService fileService;

	public VehiculoServiceImpl() {
		this.vehiculoDAO = new VehiculoDAOImpl();
		this.fileService = new FileServiceImpl();
	}

	@Override
	public VehiculoDTO findById(Integer id) throws RentexpresException {
		Connection connection = null;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);

			VehiculoDTO vehiculo = vehiculoDAO.findById(connection, id);
			if (vehiculo != null) {
				List<String> imagePaths = fileService.getImagePaths(id);
				if (!imagePaths.isEmpty()) {
					vehiculo.setImagenPath(imagePaths.get(0));
				}
			}

			JDBCUtils.commitTransaction(connection);
			logger.info("Vehículo encontrado con ID: {}", id);
			return vehiculo;
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error("Error al buscar vehículo por ID: {}", id, e);
			throw new RentexpresException("Error al buscar vehículo por ID: " + id, e);
		} finally {
			JDBCUtils.close(connection);
		}
	}

	@Override
	public List<VehiculoDTO> findAll() throws RentexpresException {
		Connection connection = null;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);

			List<VehiculoDTO> vehiculos = vehiculoDAO.findAll(connection);
			for (VehiculoDTO vehiculo : vehiculos) {
				List<String> imagePaths = fileService.getImagePaths(vehiculo.getId());
				if (!imagePaths.isEmpty()) {
					vehiculo.setImagenPath(imagePaths.get(0));
				}
			}

			JDBCUtils.commitTransaction(connection);
			return vehiculos;
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			throw new RentexpresException("Error al obtener todos los vehículos", e);
		} finally {
			JDBCUtils.close(connection);
		}
	}

	@Override
	public boolean create(VehiculoDTO vehiculo, File imagen) throws RentexpresException {
		Connection connection = null;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);

			// 1. Crear el vehículo en la base de datos
			boolean creado = vehiculoDAO.create(connection, vehiculo);
			if (!creado) {
				JDBCUtils.rollbackTransaction(connection);
				logger.warn("No se pudo crear el vehículo en la base de datos");
				return false;
			}

			// 2. Manejar la imagen si existe
			if (imagen != null) {
				try {
					String imagePath = fileService.uploadImage(imagen, vehiculo.getId());
					if (imagePath != null) {
						// 3. Actualizar el vehículo con el path de la imagen
						vehiculo.setImagenPath(imagePath);
						boolean actualizado = vehiculoDAO.update(connection, vehiculo);
						if (!actualizado) {
							JDBCUtils.rollbackTransaction(connection);
							logger.warn("Vehículo creado pero no se pudo actualizar con la imagen");
							return false;
						}
					}
				} catch (IOException e) {
					logger.error("Error al guardar la imagen del vehículo, pero se creó el vehículo", e);
					// Continuamos sin hacer rollback porque el vehículo sí se creó
				}
			}

			JDBCUtils.commitTransaction(connection);
			logger.info("Vehículo creado exitosamente con ID: {}", vehiculo.getId());
			return true;
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error("Error al crear vehículo", e);
			throw new RentexpresException("Error al crear vehículo", e);
		} finally {
			JDBCUtils.close(connection);
		}
	}

	@Override
	public boolean update(VehiculoDTO vehiculo, File nuevaImagen) throws RentexpresException {
		Connection connection = null;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);

			// 1. Manejar la imagen si se proporciona una nueva
			if (nuevaImagen != null) {
				try {
					// Eliminar imagen anterior si existe
					if (vehiculo.getImagenPath() != null) {
						fileService.deleteImage(vehiculo.getImagenPath());
					}

					// Subir nueva imagen
					String imagePath = fileService.uploadImage(nuevaImagen, vehiculo.getId());
					vehiculo.setImagenPath(imagePath);
				} catch (IOException e) {
					logger.error("Error al actualizar la imagen del vehículo", e);
					// Continuamos con la actualización sin la imagen
				}
			}

			// 2. Actualizar el vehículo en la base de datos
			boolean actualizado = vehiculoDAO.update(connection, vehiculo);
			if (!actualizado) {
				JDBCUtils.rollbackTransaction(connection);
				logger.warn("No se pudo actualizar el vehículo con ID: {}", vehiculo.getId());
				return false;
			}

			JDBCUtils.commitTransaction(connection);
			logger.info("Vehículo actualizado exitosamente. ID: {}", vehiculo.getId());
			return true;
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error("Error al actualizar vehículo ID: {}", vehiculo.getId(), e);
			throw new RentexpresException("Error al actualizar vehículo", e);
		} finally {
			JDBCUtils.close(connection);
		}
	}

	@Override
	public boolean delete(Integer id) throws RentexpresException {
		Connection connection = null;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);

			// 1. Primero obtener el vehículo para manejar sus imágenes
			VehiculoDTO vehiculo = vehiculoDAO.findById(connection, id);
			if (vehiculo == null) {
				JDBCUtils.rollbackTransaction(connection);
				logger.warn("No se encontró el vehículo con ID: {} para eliminar", id);
				return false;
			}

			// 2. Eliminar las imágenes asociadas si existen
			if (vehiculo.getImagenPath() != null) {
				fileService.deleteImage(vehiculo.getImagenPath());
			}

			// 3. Eliminar el vehículo de la base de datos
			boolean eliminado = vehiculoDAO.delete(connection, id);
			if (!eliminado) {
				JDBCUtils.rollbackTransaction(connection);
				logger.warn("No se pudo eliminar el vehículo con ID: {}", id);
				return false;
			}

			JDBCUtils.commitTransaction(connection);
			logger.info("Vehículo eliminado exitosamente. ID: {}", id);
			return true;
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error("Error al eliminar vehículo ID: {}", id, e);
			throw new RentexpresException("Error al eliminar vehículo", e);
		} finally {
			JDBCUtils.close(connection);
		}
	}

	@Override
	public Results<VehiculoDTO> findByCriteria(VehiculoCriteria criteria) throws RentexpresException {
		Connection connection = null;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);

			// Validar y establecer valores por defecto para la paginación
			if (criteria == null) {
				criteria = new VehiculoCriteria();
			}
			if (criteria.getPageNumber() == null || criteria.getPageNumber() < 1) {
				criteria.setPageNumber(1);
			}
			if (criteria.getPageSize() == null || criteria.getPageSize() < 1) {
				criteria.setPageSize(10);
			}

			// Buscar vehículos según criterios
			Results<VehiculoDTO> results = vehiculoDAO.findByCriteria(connection, criteria);

			// Para cada vehículo, obtener su imagen principal
			if (results != null && results.getResults() != null) {
				for (VehiculoDTO vehiculo : results.getResults()) {
					List<String> imagePaths = fileService.getImagePaths(vehiculo.getId());
					if (!imagePaths.isEmpty()) {
						vehiculo.setImagenPath(imagePaths.get(0));
					}
				}
			}

			JDBCUtils.commitTransaction(connection);
			logger.info("Búsqueda por criterios completada. Encontrados {} vehículos",
					(results != null && results.getResults() != null) ? results.getResults().size() : 0);
			return results;
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error("Error en búsqueda por criterios", e);
			throw new RentexpresException("Error en búsqueda por criterios", e);
		} finally {
			JDBCUtils.close(connection);
		}
	}

	@Override
	public List<String> getVehicleImages(Integer idVehiculo) throws RentexpresException {
		try {
			return fileService.getImagePaths(idVehiculo);
		} catch (Exception e) {
			logger.error("Error al obtener imágenes del vehículo ID: {}", idVehiculo, e);
			throw new RentexpresException("Error al obtener imágenes del vehículo", e);
		}
	}

	@Override
	public boolean updateVehicleImage(Integer idVehiculo, File nuevaImagen) throws RentexpresException {
		Connection connection = null;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);

			// 1. Obtener el vehículo actual
			VehiculoDTO vehiculo = vehiculoDAO.findById(connection, idVehiculo);
			if (vehiculo == null) {
				JDBCUtils.rollbackTransaction(connection);
				logger.warn("No se encontró el vehículo con ID: {} para actualizar imagen", idVehiculo);
				return false;
			}

			// 2. Manejar la nueva imagen
			if (nuevaImagen != null) {
				try {
					// Eliminar imagen anterior si existe
					if (vehiculo.getImagenPath() != null) {
						fileService.deleteImage(vehiculo.getImagenPath());
					}

					// Subir nueva imagen
					String imagePath = fileService.uploadImage(nuevaImagen, idVehiculo);
					vehiculo.setImagenPath(imagePath);

					// Actualizar el vehículo con el nuevo path
					boolean actualizado = vehiculoDAO.update(connection, vehiculo);
					if (!actualizado) {
						JDBCUtils.rollbackTransaction(connection);
						logger.warn("No se pudo actualizar la imagen del vehículo ID: {}", idVehiculo);
						return false;
					}
				} catch (IOException e) {
					JDBCUtils.rollbackTransaction(connection);
					logger.error("Error al actualizar la imagen del vehículo ID: {}", idVehiculo, e);
					throw new RentexpresException("Error al actualizar la imagen del vehículo", e);
				}
			}

			JDBCUtils.commitTransaction(connection);
			logger.info("Imagen del vehículo actualizada exitosamente. ID: {}", idVehiculo);
			return true;
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error("Error al actualizar imagen del vehículo ID: {}", idVehiculo, e);
			throw new RentexpresException("Error al actualizar imagen del vehículo", e);
		} finally {
			JDBCUtils.close(connection);
		}
	}
}