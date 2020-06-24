/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.controllers;

import com.jaevsoftware.USB.model.service.IUploadFileService;
import com.jaevsoftware.USB.util.ImageCheck;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Pepe Valenzuela
 */
@Controller
public class IndexController {

    private final Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private IUploadFileService uploadService;

    @Autowired
    ImageCheck imageCheck;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String inicio(Model modelo) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        auth.getAuthorities().forEach(autho ->{
            log.info(autho.getAuthority());
        });
        modelo.addAttribute("titulo", "Inicio");
        try {
            List<String> imagenes = uploadService.loadCarruselFiles();
            modelo.addAttribute("carruselImagenes", imagenes);
        } catch (MalformedURLException ex) {
            Logger.getLogger(IndexController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "index";
    }

    @Secured({"ROLE_JEFE","ROLE_SECRETARIA"})
    @PostMapping("/uploadImageCarrousel")
    //Asegurado para jefe de departamento y secretaria
    public String subirFotoCarrusel(@RequestParam(name = "file") MultipartFile foto, RedirectAttributes flash) {
        try {
            if (imageCheck.checkImageExtension(foto)) {
                if (imageCheck.checkImageResolution(foto, 1920, 400)) {
                    flash.addFlashAttribute("warning", "Se recomienda imagenes de resolucion 1920x400 para lograr una vista optima");
                }
                String nameFile = uploadService.copyCarruselFile(foto);
                flash.addFlashAttribute("success", nameFile + " subida con exito!!!");
            } else {
                flash.addFlashAttribute("warning", "Solo se aceptan imagenes de formato JPG y PNG");
            }
        } catch (NullPointerException | IOException ex) {
            flash.addFlashAttribute("error", ex.getMessage());
            log.error(ex.getMessage(), ex);

            return "redirect:/";
        }

        return "redirect:/";
    }

    @GetMapping(value = "/UsersUploads/{nombreArchivo:.+}")
    public ResponseEntity<Resource> mostrarFotoCarrusel(@PathVariable String nombreArchivo) {
        Resource recurso = null;
        try {
            recurso = uploadService.loadCarruselFile(nombreArchivo);
        } catch (MalformedURLException ex) {
            Logger.getLogger(IndexController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ResponseEntity.ok().
                header(HttpHeaders.CONTENT_DISPOSITION, "attachment; nombreArchivo=\"" + recurso.getFilename() + "\"")
                .body(recurso);
    }

    @Secured({"ROLE_JEFE","ROLE_SECRETARIA"})
    @PostMapping(value = "/deleteImageCarrousel")
    //Asegurado para jefe de departamento y secretaria
    public String eliminarFotoCarrusel(@RequestParam(name = "numberImage") String numberImage, RedirectAttributes flash) {
        try {
            List<String> imagenes = uploadService.loadCarruselFiles();
            for (int i = 0; i < imagenes.size(); i++) {
                if (i == Integer.parseInt(numberImage)) {
                    uploadService.deleteCarruselFile(imagenes.get(i));
                    break;
                }
            }
        } catch (MalformedURLException ex) {
            flash.addFlashAttribute("error", "Error al eliminar la imagen");
            return "redirect:/";
        }
        flash.addFlashAttribute("success", "Imagen eliminada con exito");
        return "redirect:/";
    }

}
