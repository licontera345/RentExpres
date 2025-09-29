package com.pinguela.rentexpres.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.config.ConfigManager;
import com.pinguela.rentexpres.service.FileService;

public class FileServiceImpl implements FileService {

        private static final Logger logger = LogManager.getLogger(FileServiceImpl.class);
        private static final String BASE_IMAGE_PATH = ConfigManager.getStringValue("base.image.path");
        private static final Pattern FILE_NAME_PATTERN = Pattern
                        .compile("^[a-zA-Z][a-zA-Z0-9_-]*\\.(jpg|png|jpeg)$", Pattern.CASE_INSENSITIVE);

        @Override
        public String uploadImage(File imagen, Integer entityId, String entityDirectory) throws IOException {
                if (imagen == null || !imagen.exists()) {
                        logger.warn("El archivo de imagen es nulo o no existe");
                        return null;
                }

                if (entityId == null || entityDirectory == null || entityDirectory.isBlank()) {
                        logger.warn("No se puede almacenar la imagen porque el identificador o directorio es nulo");
                        return null;
                }

                Path directorioDestino = resolveEntityDirectory(entityDirectory, entityId);
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

                String relativePath = buildRelativePath(entityDirectory, entityId, nombreUnico);
                logger.info("Imagen guardada en: {}", destino);

                return relativePath;
        }

        @Override
        public List<String> uploadImages(List<File> imagenes, Integer entityId, String entityDirectory) throws IOException {
                List<String> relativePaths = new ArrayList<>();
                if (imagenes == null || imagenes.isEmpty()) {
                        return relativePaths;
                }

                for (File imagen : imagenes) {
                        String relativePath = uploadImage(imagen, entityId, entityDirectory);
                        if (relativePath != null) {
                                relativePaths.add(relativePath);
                        }
                }

                return relativePaths;
        }

        @Override
        public List<String> getImagePaths(Integer entityId, String entityDirectory) {
                List<String> imagePaths = new ArrayList<>();
                if (entityId == null || entityDirectory == null || entityDirectory.isBlank()) {
                        return imagePaths;
                }

                Path directorio = resolveEntityDirectory(entityDirectory, entityId);
                File directorioImagenes = directorio.toFile();

                if (directorioImagenes.exists() && directorioImagenes.isDirectory()) {
                        File[] archivos = directorioImagenes.listFiles();
                        if (archivos != null) {
                                for (File archivo : archivos) {
                                        if (archivo.isFile() && esImagen(archivo.getName())) {
                                                imagePaths.add(buildRelativePath(entityDirectory, entityId, archivo.getName()));
                                        }
                                }
                        }
                } else {
                        logger.info("No se encontraron imágenes para {} con ID: {}", entityDirectory, entityId);
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

        @Override
        public boolean deleteAllImages(Integer entityId, String entityDirectory) throws IOException {
                if (entityId == null || entityDirectory == null || entityDirectory.isBlank()) {
                        return false;
                }

                Path directorio = resolveEntityDirectory(entityDirectory, entityId);
                if (!Files.exists(directorio)) {
                        return false;
                }

                final boolean[] success = { true };
                try (Stream<Path> paths = Files.walk(directorio)) {
                        paths.sorted(Comparator.reverseOrder()).forEach(path -> {
                                File file = path.toFile();
                                if (!file.delete() && file.exists()) {
                                        success[0] = false;
                                        logger.warn("No se pudo eliminar el archivo o directorio: {}",
                                                        file.getAbsolutePath());
                                }
                        });
                }
                return success[0];
        }

        private boolean validarNombreArchivo(String nombreArchivo) {
                Matcher matcher = FILE_NAME_PATTERN.matcher(nombreArchivo);
                return matcher.matches();
        }

        private boolean esImagen(String nombreArchivo) {
                if (nombreArchivo == null) {
                        return false;
                }
                String lowerName = nombreArchivo.toLowerCase();
                return lowerName.endsWith(".jpg") || lowerName.endsWith(".png") || lowerName.endsWith(".jpeg");
        }

        private String generarNombreUnico(String nombreArchivo) {
                String timestamp = String.valueOf(System.currentTimeMillis());
                int dotIndex = nombreArchivo.lastIndexOf('.');
                if (dotIndex > 0) {
                        return nombreArchivo.substring(0, dotIndex) + "_" + timestamp + nombreArchivo.substring(dotIndex);
                }
                return nombreArchivo + "_" + timestamp;
        }

        private Path resolveEntityDirectory(String entityDirectory, Integer entityId) {
                return Paths.get(BASE_IMAGE_PATH, entityDirectory, String.valueOf(entityId));
        }

        private String buildRelativePath(String entityDirectory, Integer entityId, String fileName) {
                return entityDirectory + File.separator + entityId + File.separator + fileName;
        }
}