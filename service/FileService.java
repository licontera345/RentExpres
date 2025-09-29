package com.pinguela.rentexpres.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface FileService {

        String VEHICLE_DIRECTORY = "vehiculos";
        String USER_DIRECTORY = "usuarios";

        List<String> getImagePaths(Integer entityId, String entityDirectory);

        boolean deleteImage(String imagePath);

        String uploadImage(File imagen, Integer entityId, String entityDirectory) throws IOException;

        List<String> uploadImages(List<File> imagenes, Integer entityId, String entityDirectory) throws IOException;

        boolean deleteAllImages(Integer entityId, String entityDirectory) throws IOException;

}
 