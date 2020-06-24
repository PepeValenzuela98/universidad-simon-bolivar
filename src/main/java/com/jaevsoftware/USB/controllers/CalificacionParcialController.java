/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.controllers;

import com.jaevsoftware.USB.model.dao.IGrupoDAO;
import com.jaevsoftware.USB.model.entity.Alumno;
import com.jaevsoftware.USB.model.entity.Grupo;
import com.jaevsoftware.USB.model.entity.CalificacionParcial;
import com.jaevsoftware.USB.model.entity.wrapper.ListCalificacionParcialWrapper;
import com.jaevsoftware.USB.model.entity.wrapper.Usuario;
import com.jaevsoftware.USB.model.service.IAlumnoService;
import com.jaevsoftware.USB.model.service.ICalificacionParcialService;
import com.jaevsoftware.USB.model.service.IGrupoAlumnoService;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/calificacionesParciales")
public class CalificacionParcialController {

    private final Log log = LogFactory.getLog(this.getClass());

    @Autowired
    IGrupoAlumnoService grupoAlumnoService;

    @Autowired
    IAlumnoService alumnoService;

    @Autowired
    IGrupoDAO grupoDAO;

    @Autowired
    ICalificacionParcialService calificacionParcialService;

    @Secured("ROLE_DOCENTE")
    @GetMapping("/listar/{codigoGrupo}")
    public String listar(@PathVariable String codigoGrupo, RedirectAttributes flash, Model model) {

        Grupo grupo = grupoDAO.findById(codigoGrupo).orElse(null);
        if (grupo == null) {
            flash.addFlashAttribute("warning", "Porfavor seleccion un grupo");
            return "redirect:/grupos/listar";
        }
        ListCalificacionParcialWrapper calificacionParcial = calificacionParcialService.createWrapperOneGrupo(codigoGrupo);

        model.addAttribute("titulo", "Calificaciones");
        model.addAttribute("tituloCard", "Calificaciones - " + grupo.getMateria().getNombre());
        model.addAttribute("grupo", grupo);
        model.addAttribute("calificacionParcial", calificacionParcial);
        return "/calificacionParcial/listar";
    }

    @Secured("ROLE_DOCENTE")
    @PostMapping("/save")
    public String save(@ModelAttribute ListCalificacionParcialWrapper calificacionParcial, @RequestParam String codigoGrupo, RedirectAttributes flash) {
        Grupo grupo = grupoDAO.findById(codigoGrupo).orElse(null);
        if (grupo == null) {
            flash.addFlashAttribute("warning", "Porfavor seleccion un grupo");
            return "redirect:/grupos/listar";
        }
        if (calificacionParcial.getCalificaciones().isEmpty() || calificacionParcial == null) {
            flash.addFlashAttribute("warning", "Porfavor seleccion un grupo");
            return "redirect:/grupos/listar";
        }
        calificacionParcialService.saveByWrapper(calificacionParcial, grupo.getMateria().getCodigoMateria());
        flash.addFlashAttribute("success", "Calificaciones guardadas con exito");
        return "redirect:/grupos/ver/" + codigoGrupo;
    }

    @Secured({"ROLE_ALUMNO","ROLE_JEFE","ROLE_SECRETARIA"})
    @GetMapping("/ver/{numeroDeControlAlumno}")
    public String ver(@PathVariable String numeroDeControlAlumno, Model model, RedirectAttributes flash) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) auth.getPrincipal();
        List<CalificacionParcial> calificacionesParciales = calificacionParcialService.findByAlumno(numeroDeControlAlumno);
        Alumno alumno = alumnoService.findOne(numeroDeControlAlumno).orElse(null);
        if (calificacionesParciales.isEmpty()) {
            flash.addFlashAttribute("warning", "Este alumno no tiene materias cargadas");
            return "redirect:/alumnos/ver/" + numeroDeControlAlumno;
        }
        if (alumno == null) {
            flash.addFlashAttribute("warning", "Porfavor seleccion un alumno existente");
            return "redirect:/alumnos/listar";
        }
        for (GrantedAuthority role : usuario.getAuthorities()) {
            if (role.getAuthority().equals("ROLE_JEFE") || role.getAuthority().equals("ROLE_SECRETARIA") ) {
                if (!alumno.getCarrera().getCodigoCarrera().equals(usuario.getCodigoCarrera())) {
                    flash.addFlashAttribute("error", "No tienes permido para acceder a este alumno");
                    return "redirect:/alumnos/listar";
                }
            }
        }
        ListCalificacionParcialWrapper calificaciones = calificacionParcialService.createWrapperOneAlumno(numeroDeControlAlumno);
        Integer unidadesMaximo = calificacionParcialService.getUnidadesMaximo(calificacionesParciales);
        model.addAttribute("titulo", "Calificaciones");
        model.addAttribute("tituloCard", "Calificaciones parciales");
        model.addAttribute("unidadesMaximo", unidadesMaximo);
        model.addAttribute("calificacionesWrapper", calificaciones);
        model.addAttribute("alumno", alumno);
        return "/calificacionParcial/ver";
    }

    @Secured({"ROLE_JEFE", "ROLE_SECRETARIA"})
    @GetMapping("/verPorCarrera/{codigoCarrera}")
    public String verPorCarrera(@PathVariable String codigoCarrera, Model model, RedirectAttributes flash) {
        
        List<Alumno> alumnos = alumnoService.findIfExistsInGrupoAlumno(codigoCarrera);
        if (alumnos.isEmpty()) {
            flash.addFlashAttribute("warning", "Esta carrera no tiene alumnos con materias cargadas");
            return "redirect:/generar/calificaciones";
        }
        model.addAttribute("alumnos", alumnos);
        model.addAttribute("codigoCarrera", codigoCarrera);
        return "/calificacionParcial/verPorCarrera";
    }
}
