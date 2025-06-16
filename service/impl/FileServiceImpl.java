package com.pinguela.rentexpres.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.config.ConfigManager;
import com.pinguela.rentexpres.service.FileService;

public class FileServiceImpl implements FileService {

	private static final Logger logger = LogManager.getLogger(FileServiceImpl.class);
	private static final String BASE_IMAGE_PATH = ConfigManager.getStringValue("base.image.path");

	@Override
	public String uploadImage(File imagen, Integer idVehiculo) throws IOException {
		if (imagen == null || !imagen.exists()) {
			logger.warn("El archivo de imagen es nulo o no existe");
			return null;
		}

		String carpetaImagenes = BASE_IMAGE_PATH + File.separator + "vehiculos" + File.separator + idVehiculo;
		Path directorioDestino = Paths.get(carpetaImagenes);

		if (!Files.exists(directorioDestino)) {
			Files.createDirectories(directorioDestino);
		}

		String nombreArchivo = imagen.getName();
		if (!validarNombreArchivo(nombreArchivo)) {
			logger.warn("El archivo no cumple con el formato requerido: {}", nombreArchivo);
			return null;
		}

		String nombreUnico = generarNombreUnico(nombreArchivo);
		Path destino = directorioDestino.resolve(nombreUnico);
		Files.copy(imagen.toPath(), destino);

		String relativePath = "vehiculos" + File.separator + idVehiculo + File.separator + nombreUnico;
		logger.info("Imagen guardada en: {}", destino.toString());

		return relativePath;
	}

	@Override
	public List<String> getImagePaths(Integer idVehiculo) {
		String carpetaImagenes = BASE_IMAGE_PATH + File.separator + "vehiculos" + File.separator + idVehiculo;
		File directorioImagenes = new File(carpetaImagenes);
		List<String> imagePaths = new ArrayList<>();

		if (directorioImagenes.exists() && directorioImagenes.isDirectory()) {
			File[] archivos = directorioImagenes.listFiles();
			if (archivos != null) {
				for (File archivo : archivos) {
					if (archivo.isFile()) {
						String nombre = archivo.getName().toLowerCase();
						if (nombre.endsWith(".jpg") || nombre.endsWith(".png") || nombre.endsWith(".jpeg")) {
							String relativePath = "vehiculos" + File.separator + idVehiculo + File.separator
									+ archivo.getName();
							imagePaths.add(relativePath);
						}
					}
				}
			}
		} else {
			logger.info("No se encontraron imágenes para el vehículo ID: {}", idVehiculo);
		}

		return imagePaths;
	}

	@Override
	public boolean deleteImage(String imagePath) {
		if (imagePath == null || imagePath.isEmpty()) {
			return false;
		}

		File imageFile = new File(BASE_IMAGE_PATH + File.separator + imagePath);
		if (!imageFile.exists()) {
			return false;
		}

		return imageFile.delete();
	}

	private boolean validarNombreArchivo(String nombreArchivo) {
		String regex = "^[a-zA-Z][a-zA-Z0-9_-]*\\.(jpg|png|jpeg)$";
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(nombreArchivo);
		return matcher.matches();
	}

	private String generarNombreUnico(String nombreArchivo) {
		String timestamp = String.valueOf(System.currentTimeMillis());
		int dotIndex = nombreArchivo.lastIndexOf('.');
		if (dotIndex > 0) {
			return nombreArchivo.substring(0, dotIndex) + "_" + timestamp + nombreArchivo.substring(dotIndex);
		}
		return nombreArchivo + "_" + timestamp;
	}

	public void uploadImages(List<File> imagenes, Integer id, Integer id2) {
		// TODO Auto-generated method stub
		
	}
}