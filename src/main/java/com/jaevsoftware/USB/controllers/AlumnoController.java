/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.controllers;

import com.jaevsoftware.USB.model.entity.Alumno;
import com.jaevsoftware.USB.model.entity.GrupoAlumno;
import com.jaevsoftware.USB.model.entity.wrapper.Usuario;
import com.jaevsoftware.USB.model.service.IAlumnoService;
import com.jaevsoftware.USB.model.service.IGrupoAlumnoService;
import com.jaevsoftware.USB.model.service.IHorarioDeGrupoService;
import com.jaevsoftware.USB.util.PageRender;
import java.util.List;
import java.util.Locale;
import javax.validation.Valid;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/alumnos")
@SessionAttributes("alumno")
public class AlumnoController {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    IAlumnoService alumnoService;

    @Autowired
    IGrupoAlumnoService grupoAlumnoService;

    @Autowired
    IHorarioDeGrupoService horarioDeGrupoService;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    private final Log log = LogFactory.getLog(this.getClass());

    @Secured("ROLE_JEFE")
    @GetMapping("/listar")
    public String listar(@RequestParam(name = "page", defaultValue = "0") Integer page, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) auth.getPrincipal();
        Pageable paginaRequerida = PageRequest.of(page, 5);
        Page<Alumno> alumnos = alumnoService.findAll(paginaRequerida, usuario.getCodigoCarrera());
        PageRender<Alumno> pageRender = new PageRender("/alumnos/listar", alumnos);
        model.addAttribute("titulo", "Alumnos");
        model.addAttribute("tituloCard", "Alumnos");
        model.addAttribute("alumnos", alumnos);
        model.addAttribute("alumnoFiltro", new Alumno());
        model.addAttribute("page", pageRender);
        return "/alumno/listar";
    }

    @Secured("ROLE_JEFE")
    @GetMapping("/filtro")
    public String listarFiltro(Alumno alumnoFiltro, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) auth.getPrincipal();
        List<Alumno> alumnosFiltro = alumnoService.findByNumeroDeControlContaining(alumnoFiltro.getNumeroDeControl(), usuario.getCodigoCarrera());
        model.addAttribute("titulo", "Alumnos");
        model.addAttribute("tituloCard", "Alumnos");
        model.addAttribute("alumnos", alumnosFiltro);
        model.addAttribute("alumnoFiltro", alumnoFiltro);
        return "/alumno/listar";
    }

    @Secured({"ROLE_JEFE", "ROLE_ALUMNO","ROLE_SECRETARIA"})
    @GetMapping("/ver/{numeroDeControl}")
    public String ver(@PathVariable("numeroDeControl") String numeroDeControl, Model model, RedirectAttributes flash) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) auth.getPrincipal();
        for (GrantedAuthority role : usuario.getAuthorities()) {
            if (role.getAuthority().equals("ROLE_ALUMNO")) {
                if (!usuario.getUsername().equals(numeroDeControl)) {
                    flash.addFlashAttribute("error", "No tienes permido para acceder a este alumno");
                    return "redirect:/";
                }
            }
        }
        Alumno alumno = alumnoService.findOne(numeroDeControl).orElse(null);
        if (alumno == null) {
            flash.addFlashAttribute("warning", "Porfavor seleccione un alumno");
            return "redirect:/alumnos/listar";
        }
        for (GrantedAuthority role : usuario.getAuthorities()) {
            if (role.getAuthority().equals("ROLE_JEFE") || role.getAuthority().equals("ROLE_SECRETARIA")) {
                if (!alumno.getCarrera().getCodigoCarrera().equals(usuario.getCodigoCarrera())) {
                    flash.addFlashAttribute("error", "No tienes permido para acceder a este alumno");
                    return "redirect:/alumnos/listar";
                }
            }
        }
        List<GrupoAlumno> gruposAlumno = grupoAlumnoService.findByAlumno(numeroDeControl);
        gruposAlumno.forEach(grupoAlumno -> {
            grupoAlumno.getGrupo().setHorariosDeGrupo(horarioDeGrupoService.sortHorarioDeGrupo(grupoAlumno.getGrupo().getHorariosDeGrupo()));
        });
        model.addAttribute("titulo", "Alumnos");
        model.addAttribute("tituloCard", "Alumno - " + alumno.getNumeroDeControl());
        model.addAttribute("alumno", alumno);
        model.addAttribute("gruposAlumno", gruposAlumno);
        return "/alumno/ver";
    }

    @Secured("ROLE_JEFE")
    @GetMapping("/form")
    public String crear(Model model, RedirectAttributes flash) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) auth.getPrincipal();
        Alumno nuevoAlumno = alumnoService.generateNewAlumno(usuario.getCodigoCarrera());
        if (nuevoAlumno == null) {
            flash.addFlashAttribute("warning", "Carrera no disponible");
            return "redirect:/alumnos/listar";
        }
        model.addAttribute("titulo", "Alumnos");
        model.addAttribute("tituloCard", "Alumnos - Alta");
        model.addAttribute("alumno", nuevoAlumno);
        return "/alumno/form";
    }

    @Secured("ROLE_JEFE")
    @PostMapping("/form")
    public String guardar(@Valid Alumno alumno, BindingResult result,
            Model model, RedirectAttributes flash,
            SessionStatus status) {
        if (result.hasErrors()) {
            flash.addFlashAttribute("error", "Error al cargar los datos verifique el formulario");
            List<FieldError> errores = result.getFieldErrors();
            errores.forEach((error) -> {
                flash.addFlashAttribute(error.getField().replace("ñ", "n") + "Error", messageSource.getMessage(error, Locale.ENGLISH));
            });
            flash.addFlashAttribute("errorField", "errorField");
            return "redirect:/alumnos/form";
        }
        alumnoService.save(alumno);
        status.setComplete();
        flash.addFlashAttribute("success", "Se ah guardado el alumno con exito");
        return "redirect:/alumnos/listar";
    }

    @Secured("ROLE_JEFE")
    @GetMapping("/form/{numeroDeControl}")
    public String editar(@PathVariable("numeroDeControl") String numeroDeControl, Model model,
            RedirectAttributes flash) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) auth.getPrincipal();
        Alumno alumno = alumnoService.findOne(numeroDeControl).orElse(null);
        if (alumno == null) {
            flash.addFlashAttribute("warning", "Porfavor seleccione un alumno a editar");
            return "redirect:/alumnos/listar";
        }
        for (GrantedAuthority role : usuario.getAuthorities()) {
            if (role.getAuthority().equals("ROLE_JEFE")) {
                if (!alumno.getCarrera().getCodigoCarrera().equals(usuario.getCodigoCarrera())) {
                    log.info(usuario.getCodigoCarrera());
                    flash.addFlashAttribute("error", "No tienes permido para acceder a este alumno");
                    return "redirect:/alumnos/listar";
                }
            }
        }
        model.addAttribute("titulo", "Alumnos");
        model.addAttribute("tituloCard", "Alumnos - Editar");
        model.addAttribute("alumno", alumno);
        return "/alumno/form";
    }

    @Secured("ROLE_JEFE")
    @GetMapping("/eliminar/{numeroDeControl}")
    public String eliminar(@PathVariable("numeroDeControl") String numeroDeControl, RedirectAttributes flash
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) auth.getPrincipal();
        Alumno alumno = alumnoService.findOne(numeroDeControl).orElse(null);
        if (alumno == null) {
            flash.addFlashAttribute("warning", "Porfavor seleccione un alumno a eliminar");
            return "redirect:/alumnos/listar";
        }
        for (GrantedAuthority role : usuario.getAuthorities()) {
            if (role.getAuthority().equals("ROLE_JEFE")) {
                if (!alumno.getCarrera().getCodigoCarrera().equals(usuario.getCodigoCarrera())) {
                    flash.addFlashAttribute("error", "No tienes permido para acceder a este alumno");
                    return "redirect:/alumnos/listar";
                }
            }
        }
        alumnoService.delete(numeroDeControl);
        flash.addFlashAttribute("success", "Alumno eliminado con exito");
        return "redirect:/alumnos/listar";
    }
}
