/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.model.service.impl;

import com.jaevsoftware.USB.model.service.IUploadFileService;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Pepe Valenzuela
 */
@Service
public class IUploadFileServiceImpl implements IUploadFileService {

    public final org.slf4j.Logger log = LoggerFactory.getLogger(getClass());
    private static final String UPLOADS_FOLDER = "USERSUPLOADS";
    private static final String UPLOADS_ALUMNOS_IMAGES = "USERSUPLOADS/ALUMNOS";
    private static final String UPLOADS_CARRUSEL_IMAGES = "USERSUPLOADS/CARRUSEL";

    public Path getPathAlumnos(String filename) {
        return Paths.get(UPLOADS_ALUMNOS_IMAGES).resolve(filename);
    }

    public Path getPathCarrusel(String filename) {
        return Paths.get(UPLOADS_CARRUSEL_IMAGES).resolve(filename);
    }

    @Override
    public Resource loadAlumnoFile(String filename) throws MalformedURLException {
        Path pathFoto = getPathAlumnos(filename);
        log.info("path:" + pathFoto);
        Resource recurso = null;

        recurso = new UrlResource(pathFoto.toUri());
        if (!recurso.exists() && !recurso.isReadable()) {
            throw new RuntimeException("Error: no se pudo cargar la imagen: " + pathFoto.toString());
        }
        return recurso;
    }

    @Override
    public Resource loadCarruselFile(String filename) throws MalformedURLException {
        Path pathFoto = getPathCarrusel(filename);
        Resource recurso = null;

        recurso = new UrlResource(pathFoto.toUri());
        if (!recurso.exists() && !recurso.isReadable()) {
            throw new RuntimeException("Error: no se pudo cargar la imagen: " + pathFoto.toString());
        }
        return recurso;
    }

    @Override
    public List<String> loadCarruselFiles() throws MalformedURLException {
        List<String> images = new ArrayList();

        for (File file : new File(UPLOADS_CARRUSEL_IMAGES).listFiles()) {
            Path pathFoto = getPathCarrusel(file.getName());
            Resource recurso = null;

            recurso = new UrlResource(pathFoto.toUri());
            if (!recurso.exists() && !recurso.isReadable()) {
                throw new RuntimeException("Error: no se pudo cargar la imagen: " + pathFoto.toString());
            }
            images.add(recurso.getFilename());
        }

        return images;
    }

    @Override
    public String copyAlumnoFile(MultipartFile file) throws IOException {
        String uniqueFilename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path rootPath = getPathAlumnos(uniqueFilename);
        byte[] bytes = file.getBytes();
        Files.write(rootPath, bytes);
        return uniqueFilename;
    }

    @Override
    public String copyCarruselFile(MultipartFile file) throws IOException {
        if (new File(UPLOADS_CARRUSEL_IMAGES).list().length > 5) {
            throw new IOException("No hay mas espacio para insertar imagenes");
        } else {
            String uniqueFilename = new File(UPLOADS_CARRUSEL_IMAGES).list().length + "_" + file.getOriginalFilename();
            Path rootPath = getPathCarrusel(uniqueFilename);
            byte[] bytes = file.getBytes();
            Files.write(rootPath, bytes);
            return uniqueFilename;
        }

    }

    @Override
    public boolean deleteAlumnoFile(String filename) {
        Path rootPath = getPathAlumnos(filename);
        File archivo = rootPath.toFile();
        if (archivo.exists() && archivo.canRead()) {
            if (archivo.delete()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteCarruselFile(String filename) {
        Path rootPath = getPathCarrusel(filename);
        File archivo = rootPath.toFile();
        if (archivo.exists() && archivo.canRead()) {
            if (archivo.delete()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void createDirectory() {
        try {
            if (!Files.exists(Paths.get(UPLOADS_FOLDER))) {
                Files.createDirectory(Paths.get(UPLOADS_FOLDER));
                log.info("El folder " + UPLOADS_FOLDER + " fue creado");
            } else {
                log.info("El folder " + UPLOADS_FOLDER + " ya existe");
            }
            if (!Files.exists(Paths.get(UPLOADS_ALUMNOS_IMAGES))) {
                Files.createDirectory(Paths.get(UPLOADS_ALUMNOS_IMAGES));
                log.info("El folder " + UPLOADS_ALUMNOS_IMAGES + " fue creado");
            } else {
                log.info("El folder " + UPLOADS_ALUMNOS_IMAGES + " ya existe");
            }

            if (!Files.exists(Paths.get(UPLOADS_CARRUSEL_IMAGES))) {
                Files.createDirectory(Paths.get(UPLOADS_CARRUSEL_IMAGES));
                log.info("El folder " + UPLOADS_CARRUSEL_IMAGES + " fue creado");
            } else {
                log.info("El folder " + UPLOADS_CARRUSEL_IMAGES + " ya existe");
            }
        } catch (IOException ex) {
            Logger.getLogger(IUploadFileServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
