/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.controllers;

import com.jaevsoftware.USB.model.dao.ICarreraDAO;
import com.jaevsoftware.USB.model.entity.Carrera;
import com.jaevsoftware.USB.model.entity.Secretaria;
import com.jaevsoftware.USB.model.entity.wrapper.Usuario;
import com.jaevsoftware.USB.model.service.ISecretariaService;
import java.util.List;
import java.util.Locale;
import javax.validation.Valid;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.validation.FieldError;

@Controller
@RequestMapping("/secretarias")
@SessionAttributes("secretaria")

//Asegurado para jefe de departamento
public class SecretariaController {

    @Autowired
    private MessageSource messageSource;

    private final Log log = LogFactory.getLog(this.getClass());

    @Autowired
    ICarreraDAO carreraDAO;

    @Autowired
    ISecretariaService secretarioService;

    @Secured("ROLE_JEFE")
    @GetMapping("/listar")
    public String listar(Model model) {
        model.addAttribute("titulo", "Secretarias");
        model.addAttribute("tituloCard", "Secretarias");
        model.addAttribute("carreras", carreraDAO.findAllByOrderByNombreAsc());
        return "/secretaria/listar";
    }

    @Secured({"ROLE_JEFE", "ROLE_SECRETARIA"})
    @GetMapping("/ver/{numeroControl}")
    public String ver(@PathVariable("numeroControl") String numeroControl, Model model, RedirectAttributes flash) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) auth.getPrincipal();
        for (GrantedAuthority role : usuario.getAuthorities()) {
            if (role.getAuthority().equals("ROLE_SECRETARIA")) {
                if (!usuario.getUsername().equals(numeroControl)) {
                    flash.addFlashAttribute("error", "No tienes permido para acceder a esta secretaria");
                    return "redirect:/";
                }
            }
        }
        Carrera carrera = carreraDAO.findBySecretaria(numeroControl);
        if (carrera == null) {
            flash.addFlashAttribute("warning", "Porfavor seleccione una secretaria existente");
            return "redirect:/secretarias/listar";
        }
        model.addAttribute("titulo", "Secretarias");
        model.addAttribute("tituloCard", "Secretaria - " + carrera.getCodigoCarrera());
        model.addAttribute("secretaria", carrera.getSecretaria());
        model.addAttribute("nombreCarrera", carrera.getNombre());
        return "/secretaria/ver";
    }

    @Secured("ROLE_JEFE")
    @GetMapping("/form/{codigoCarrera}")
    public String crear(@PathVariable("codigoCarrera") String codigoCarrera, Model model, RedirectAttributes flash) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) auth.getPrincipal();
        for (GrantedAuthority role : usuario.getAuthorities()) {
            if (role.getAuthority().equals("ROLE_JEFE")) {
                if (!codigoCarrera.equals(usuario.getCodigoCarrera())) {
                    flash.addFlashAttribute("error", "No tienes permido para acceder a esta secretaria");
                    return "redirect:/secretarias/listar";
                }
            }
        }
        Carrera carrera = carreraDAO.findById(codigoCarrera).orElse(null);
        if (carrera == null) {
            flash.addFlashAttribute("warning", "Porfavor seleccione una secretaria a remplazar");
            return "redirect:/secretarias/listar";
        }
        model.addAttribute("titulo", "Secretarias");
        model.addAttribute("tituloCard", "Secretarias - Alta");
        model.addAttribute("secretaria", secretarioService.generateNewSecretaria());
        model.addAttribute("carrera", carrera);
        return "/secretaria/form";
    }

    @Secured("ROLE_JEFE")
    @PostMapping("/form")
    public String guardar(@RequestParam("codigoCarrera") String codigoCarrera, @Valid Secretaria secretaria, BindingResult result, Model model, RedirectAttributes flash, SessionStatus status) {
        if (result.hasErrors()) {
            flash.addFlashAttribute("error", "Error al cargar los datos verifique el formulario");
            List<FieldError> errores = result.getFieldErrors();
            errores.forEach((error) -> {
                flash.addFlashAttribute(error.getField().replace("ñ", "n") + "Error", messageSource.getMessage(error, Locale.ENGLISH));
            });
            flash.addFlashAttribute("errorField", "errorField");
            return "redirect:/secretarias/form/" + codigoCarrera;
        }
        secretarioService.save(secretaria);
        Carrera carrera = carreraDAO.findById(codigoCarrera).get();
        Secretaria secretariaAnterior = carrera.getSecretaria();
        if (!secretaria.getNumeroDeControl().equals(secretariaAnterior.getNumeroDeControl())) {
            carrera.setSecretaria(secretaria);
            carreraDAO.save(carrera);
            secretarioService.delete(secretariaAnterior.getNumeroDeControl());
        }
        status.setComplete();
        flash.addFlashAttribute("success", "Se han guardado los cambios con exito!!!");
        model.addAttribute("titulo", "Secretarias");
        model.addAttribute("tituloCard", "Secretarias");
        return "redirect:/secretarias/listar";
    }

    @Secured("ROLE_JEFE")
    @GetMapping("/form/{codigoCarrera}/{numeroDeControl}")
    public String editar(@PathVariable("codigoCarrera") String codigoCarrera,
            @PathVariable("numeroDeControl") String numeroDeControl, Model model, RedirectAttributes flash) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) auth.getPrincipal();
        for (GrantedAuthority role : usuario.getAuthorities()) {
            if (role.getAuthority().equals("ROLE_JEFE")) {
                if (!codigoCarrera.equals(usuario.getCodigoCarrera())) {
                    flash.addFlashAttribute("error", "No tienes permido para acceder a esta secretaria");
                    return "redirect:/secretarias/listar";
                }
            }
        }
        Secretaria secretaria = secretarioService.findOne(numeroDeControl).orElse(null);
        if (secretaria == null) {
            flash.addFlashAttribute("warning", "Porfavor seleccione una secretaria a editar");
            return "redirect:/secretarias/listar";
        }
        Carrera carrera = carreraDAO.findById(codigoCarrera).orElse(null);
        if (carrera == null) {
            flash.addFlashAttribute("warning", "Porfavor seleccione una secretaria a editar");
            return "redirect:/secretarias/listar";
        }
        model.addAttribute("titulo", "Secretarias");
        model.addAttribute("tituloCard", "Secretarias - Alta");
        model.addAttribute("secretaria", secretaria);
        model.addAttribute("carrera", carrera);
        return "/secretaria/form";
    }
}
