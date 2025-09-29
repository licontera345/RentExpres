package com.pinguela.rentexpres.service;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

/**
 * Servicio encargado de gestionar el almacenamiento y eliminación de archivos
 * en el sistema.
 */
public interface FileService {

    /**
     * Guarda un archivo de imagen en el sistema de ficheros utilizando un
     * nombre único construido a partir del identificador de la entidad y un
     * UUID.
     *
     * @param fileStream flujo de datos de la imagen a guardar
     * @param fileName   nombre original del archivo recibido
     * @param folder     carpeta de destino (por ejemplo, "usuarios" o "vehiculos")
     * @param entityId   identificador de la entidad a la que pertenece la imagen
     * @return la ruta relativa del archivo guardado o {@code null} si se produce un error
     */
    String saveFile(InputStream fileStream, String fileName, String folder, Long entityId);

    /**
     * Elimina un archivo del sistema de almacenamiento.
     *
     * @param filePath ruta del archivo que se desea eliminar (relativa o absoluta)
     * @return {@code true} si el archivo se eliminó correctamente, {@code false} en caso contrario
     */
    boolean deleteFile(String filePath);

    /**
     * Reemplaza una imagen existente por una nueva, eliminando el archivo
     * anterior y guardando el nuevo contenido.
     *
     * @param fileStream  flujo de datos del nuevo archivo
     * @param fileName    nombre original del nuevo archivo
     * @param folder      carpeta donde se almacenará el archivo
     * @param entityId    identificador de la entidad relacionada con el archivo
     * @param oldFilePath ruta del archivo anterior que se debe eliminar
     * @return la ruta relativa del nuevo archivo guardado o {@code null} si ocurre un error
     */
    String updateFile(InputStream fileStream, String fileName, String folder, Long entityId, String oldFilePath);

    /**
     * Obtiene la ruta absoluta en el sistema de ficheros para un archivo
     * específico.
     *
     * @param fileName nombre del archivo
     * @param folder   carpeta donde se encuentra el archivo
     * @param entityId identificador de la entidad, utilizado para construir la ruta
     * @return la ruta absoluta del archivo o {@code null} si los parámetros son inválidos
     */
    Path getFilePath(String fileName, String folder, Long entityId);

    /**
     * Obtiene una lista con las rutas relativas de todos los archivos
     * almacenados para una entidad concreta dentro de una carpeta.
     *
     * @param folder   carpeta donde se buscan los archivos
     * @param entityId identificador de la entidad asociada
     * @return lista de rutas relativas de los archivos encontrados. Si no hay
     *         archivos o se produce un error, se devuelve una lista vacía
     */
    List<String> listFiles(String folder, Long entityId);
}
