/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.controllers;

import com.jaevsoftware.USB.model.dao.IJustificanteDAO;
import com.jaevsoftware.USB.model.entity.Alumno;
import com.jaevsoftware.USB.model.entity.GrupoAlumno;
import com.jaevsoftware.USB.model.entity.Justificante;
import com.jaevsoftware.USB.model.entity.wrapper.Usuario;
import com.jaevsoftware.USB.model.service.IAlumnoService;
import com.jaevsoftware.USB.model.service.IGrupoAlumnoService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller()
@RequestMapping("/generar")
@Secured({"ROLE_JEFE", "ROLE_SECRETARIA"})
public class GenerarController {

    @Autowired
    IJustificanteDAO justificanteDAO;

    @Autowired
    IAlumnoService alumnoService;

    @Autowired
    IGrupoAlumnoService grupoAlumnoService;

    
    @GetMapping("/calificaciones")
    public String calificaciones(Model model) {
        model.addAttribute("titulo", "Generar");
        model.addAttribute("tituloCard", "Calificaciones");
        return "/generar/calificaciones";
    }

    @GetMapping("/justificante")
    public String crearJustificante(Model model) {
        model.addAttribute("titulo", "Generar");
        model.addAttribute("tituloCard", "Justificante ");
        model.addAttribute("justificante", new Justificante());
        return "/generar/justificantes";
    }

    @PostMapping("/justificante")
    public String guardarJustificante(@Valid Justificante justificante, RedirectAttributes flash, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) auth.getPrincipal();
        Alumno alumno = alumnoService.findOne(justificante.getAlumno().getNumeroDeControl()).orElse(null);
        if (alumno == null) {
            flash.addFlashAttribute("warning", "El numero de control insertado no existe");
            return "redirect:/generar/justificante";
        }
        for (GrantedAuthority role : usuario.getAuthorities()) {
            if (role.getAuthority().equals("ROLE_JEFE") || role.getAuthority().equals("ROLE_SECRETARIA")) {
                if (!alumno.getCarrera().getCodigoCarrera().equals(usuario.getCodigoCarrera())) {
                    flash.addFlashAttribute("error", "No tienes permido para acceder a este alumno");
                    return "redirect:/alumnos/listar";
                }
            }
        }
        justificante.setAlumno(alumno);
//        justificanteDAO.save(justificante);
        List<GrupoAlumno> gruposAlumno = grupoAlumnoService.findByAlumno(alumno.getNumeroDeControl());
        model.addAttribute("alumno", alumno);
        model.addAttribute("grupos", gruposAlumno);
        model.addAttribute("justificante", justificante);
        return "/justificantes/ver";
    }

    @GetMapping("/horarios")
    public String horarios(Model model) {
        model.addAttribute("titulo", "Generar");
        model.addAttribute("tituloCard", "Horarios");
        return "/generar/horarios";
    }
}
