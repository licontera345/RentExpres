package com.pinguela.rentexpres.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.service.FileService;

/**
 * Implementación del servicio de gestión de archivos que permite almacenar,
 * actualizar y eliminar imágenes en el sistema de ficheros.
 */
public class FileServiceImpl implements FileService {

    private static final Logger LOGGER = LogManager.getLogger(FileServiceImpl.class);
    private static final Path UPLOAD_DIR = Paths.get(System.getProperty("user.dir"), "uploads");
    private static final long MAX_FILE_SIZE = 2L * 1024L * 1024L; // 2 MB
    private static final String[] ALLOWED_EXTENSIONS = { "jpg", "jpeg", "png" };

    @Override
    public String saveFile(InputStream fileStream, String fileName, String folder, Long entityId) {
        if (fileStream == null) {
            LOGGER.warn("El flujo de entrada del archivo es nulo");
            return null;
        }
        if (fileName == null || fileName.isBlank()) {
            LOGGER.warn("El nombre del archivo es inválido");
            return null;
        }
        if (folder == null || folder.isBlank()) {
            LOGGER.warn("La carpeta de destino es inválida");
            return null;
        }
        if (entityId == null) {
            LOGGER.warn("El identificador de la entidad es nulo");
            return null;
        }

        String extension = getFileExtension(fileName);
        if (!isExtensionAllowed(extension)) {
            LOGGER.warn("Extensión de archivo no permitida: {}", extension);
            return null;
        }

        Path targetDirectory = resolveFolder(folder, entityId);
        try {
            Files.createDirectories(targetDirectory);
        } catch (IOException e) {
            LOGGER.error("No se pudieron crear las carpetas de destino", e);
            return null;
        }

        byte[] fileBytes;
        try {
            fileBytes = readBytesWithLimit(fileStream);
            if (fileBytes == null) {
                LOGGER.warn("El archivo supera el tamaño máximo permitido de {} bytes", MAX_FILE_SIZE);
                return null;
            }
        } catch (IOException e) {
            LOGGER.error("Error al leer el archivo", e);
            return null;
        }

        String uniqueFileName = buildUniqueFileName(entityId, extension);
        Path destination = targetDirectory.resolve(uniqueFileName);
        try {
            Files.write(destination, fileBytes);
            LOGGER.info("Archivo guardado en {}", destination);
            return UPLOAD_DIR.relativize(destination).toString().replace('\\', '/');
        } catch (IOException e) {
            LOGGER.error("Error al guardar el archivo", e);
            return null;
        }
    }

    @Override
    public boolean deleteFile(String filePath) {
        if (filePath == null || filePath.isBlank()) {
            LOGGER.warn("La ruta del archivo a eliminar es inválida");
            return false;
        }

        Path pathToDelete = resolvePath(filePath);
        try {
            if (!Files.exists(pathToDelete)) {
                LOGGER.warn("El archivo no existe: {}", pathToDelete);
                return false;
            }
            Files.delete(pathToDelete);
            LOGGER.info("Archivo eliminado: {}", pathToDelete);
            return true;
        } catch (IOException e) {
            LOGGER.error("No se pudo eliminar el archivo: {}", pathToDelete, e);
            return false;
        }
    }

    @Override
    public String updateFile(InputStream fileStream, String fileName, String folder, Long entityId, String oldFilePath) {
        String newFilePath = saveFile(fileStream, fileName, folder, entityId);
        if (newFilePath == null) {
            LOGGER.warn("No se pudo guardar el nuevo archivo, se mantiene el anterior");
            return null;
        }

        if (oldFilePath != null && !oldFilePath.isBlank()) {
            if (!deleteFile(oldFilePath)) {
                LOGGER.warn("No se pudo eliminar el archivo anterior: {}", oldFilePath);
            }
        }

        return newFilePath;
    }

    @Override
    public Path getFilePath(String fileName, String folder, Long entityId) {
        if (fileName == null || fileName.isBlank()) {
            LOGGER.warn("El nombre del archivo es inválido");
            return null;
        }
        if (folder == null || folder.isBlank()) {
            LOGGER.warn("La carpeta es inválida");
            return null;
        }

        Path directory = resolveFolder(folder, entityId);
        return directory.resolve(fileName).normalize();
    }

    @Override
    public List<String> listFiles(String folder, Long entityId) {
        if (folder == null || folder.isBlank()) {
            LOGGER.warn("La carpeta para listar archivos es inválida");
            return Collections.emptyList();
        }
        if (entityId == null) {
            LOGGER.warn("El identificador de la entidad es nulo");
            return Collections.emptyList();
        }

        Path directory = resolveFolder(folder, entityId);
        if (!Files.exists(directory) || !Files.isDirectory(directory)) {
            LOGGER.info("No existen archivos en {}", directory);
            return Collections.emptyList();
        }

        try (Stream<Path> stream = Files.list(directory)) {
            return stream.filter(Files::isRegularFile)
                    .map(path -> UPLOAD_DIR.relativize(path).toString().replace('\\', '/'))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            LOGGER.error("Error al listar archivos en {}", directory, e);
            return Collections.emptyList();
        }
    }

    private Path resolveFolder(String folder, Long entityId) {
        Path folderPath = UPLOAD_DIR.resolve(folder).normalize();
        if (entityId != null) {
            folderPath = folderPath.resolve(String.valueOf(entityId)).normalize();
        }
        return folderPath;
    }

    private Path resolvePath(String filePath) {
        Path path = Paths.get(filePath);
        if (!path.isAbsolute()) {
            path = UPLOAD_DIR.resolve(path).normalize();
        }
        return path;
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex >= 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toLowerCase(Locale.ROOT);
        }
        return "";
    }

    private boolean isExtensionAllowed(String extension) {
        for (String allowed : ALLOWED_EXTENSIONS) {
            if (allowed.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }

    private String buildUniqueFileName(Long entityId, String extension) {
        String uuid = UUID.randomUUID().toString();
        return entityId + "_" + uuid + (extension.isEmpty() ? "" : "." + extension);
    }

    private byte[] readBytesWithLimit(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[8192];
        int bytesRead;
        long totalRead = 0;
        while ((bytesRead = inputStream.read(data)) != -1) {
            totalRead += bytesRead;
            if (totalRead > MAX_FILE_SIZE) {
                return null;
            }
            buffer.write(data, 0, bytesRead);
        }
        return buffer.toByteArray();
    }
}
