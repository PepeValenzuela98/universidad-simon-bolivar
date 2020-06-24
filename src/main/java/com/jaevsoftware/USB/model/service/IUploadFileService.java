/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.model.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Pepe Valenzuela
 */
public interface IUploadFileService {

    public Resource loadAlumnoFile(String filename) throws MalformedURLException;

    public Resource loadCarruselFile(String filename) throws MalformedURLException;

    public List<String> loadCarruselFiles() throws MalformedURLException;

    public String copyAlumnoFile(MultipartFile file) throws IOException;

    public String copyCarruselFile(MultipartFile file) throws IOException;

    public boolean deleteAlumnoFile(String filename);

    public boolean deleteCarruselFile(String filename);

    public void createDirectory();
    
}
