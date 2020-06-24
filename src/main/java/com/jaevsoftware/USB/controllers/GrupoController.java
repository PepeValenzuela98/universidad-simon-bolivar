/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.controllers;

import com.jaevsoftware.USB.model.dao.IDocenteDAO;
import com.jaevsoftware.USB.model.dao.IGrupoDAO;
import com.jaevsoftware.USB.model.dao.IMateriaDAO;
import com.jaevsoftware.USB.model.entity.Docente;
import com.jaevsoftware.USB.model.entity.Grupo;
import com.jaevsoftware.USB.model.entity.GrupoAlumno;
import com.jaevsoftware.USB.model.entity.HorarioDeGrupo;
import com.jaevsoftware.USB.model.entity.Materia;
import com.jaevsoftware.USB.model.entity.wrapper.ListHorarioGrupoWrapper;
import com.jaevsoftware.USB.model.entity.wrapper.Usuario;
import com.jaevsoftware.USB.model.service.IGrupoAlumnoService;
import com.jaevsoftware.USB.model.service.IHorarioDeGrupoService;
import com.jaevsoftware.USB.util.PageRender;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/grupos")
@SessionAttributes("grupo")
public class GrupoController {

    private final Log log = LogFactory.getLog(this.getClass());

    @Autowired
    IGrupoDAO grupoDAO;

    @Autowired
    IMateriaDAO materiaDAO;

    @Autowired
    IDocenteDAO docenteDAO;

    @Autowired
    IHorarioDeGrupoService horarioDeGrupoService;

    @Autowired
    IGrupoAlumnoService grupoAlumnoService;

    @Secured("ROLE_JEFE")
    @GetMapping("/listar")
    public String listar(@RequestParam(name = "page", defaultValue = "0") Integer page, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) auth.getPrincipal();
        Pageable paginaRequerida = PageRequest.of(page, 5);
        Page<Grupo> grupos = grupoDAO.findAllByCarrera(paginaRequerida, usuario.getCodigoCarrera());
        PageRender<Grupo> pageRender = new PageRender<>("/grupos/listar", grupos);
        model.addAttribute("titulo", "Grupos");
        model.addAttribute("tituloCard", "Grupos");
        model.addAttribute("grupos", grupos);
        model.addAttribute("grupoFiltro", new Grupo());
        model.addAttribute("page", pageRender);
        return "/grupo/listar";
    }

    @Secured("ROLE_JEFE")
    @GetMapping("/filtro")
    public String listarFiltro(Grupo grupoFiltro, Model model) {
        List<Grupo> gruposFiltro = grupoDAO.findByCodigoGrupo(grupoFiltro.getCodigoGrupo());
        model.addAttribute("titulo", "Grupos");
        model.addAttribute("tituloCard", "Grupos");
        model.addAttribute("grupos", gruposFiltro);
        model.addAttribute("grupoFiltro", grupoFiltro);
        return "/grupo/listar";
    }

    @Secured({"ROLE_JEFE", "ROLE_DOCENTE"})
    @GetMapping("/ver/{codigoGrupo}")
    public String ver(@PathVariable String codigoGrupo, Model model, RedirectAttributes flash) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) auth.getPrincipal();
        Grupo grupo = grupoDAO.findById(codigoGrupo).orElse(null);
        if (grupo == null) {
            flash.addFlashAttribute("warning", "Porfavor seleccione un grupo existente");
            return "redirect:/grupos/listar";
        }
        for (GrantedAuthority role : usuario.getAuthorities()) {
            if (role.getAuthority().equals("ROLE_JEFE")) {
                if (!grupo.getCodigoGrupo().contains(usuario.getCodigoCarrera())) {
                    flash.addFlashAttribute("error", "No tienes permido para acceder a este grupo");
                    return "redirect:/grupos/listar";
                }
            }
        }
        model.addAttribute("titulo", "Grupos");
        model.addAttribute("tituloCard", "Grupos - " + grupo.getCodigoGrupo());
        model.addAttribute("grupo", grupo);
        List<HorarioDeGrupo> horariosDeGrupo = grupo.getHorariosDeGrupo();
        List<GrupoAlumno> grupoAlumnos = grupoAlumnoService.findByGrupo(codigoGrupo);
        model.addAttribute("horariosGrupo", horarioDeGrupoService.setHorarioExistente(horariosDeGrupo));
        model.addAttribute("grupoAlumnos", grupoAlumnos);
        return "/grupo/ver";
    }

    @Secured("ROLE_JEFE")
    @GetMapping("/form")
    public String crear(Model model) {
        model.addAttribute("titulo", "Grupos");
        model.addAttribute("tituloCard", "Grupos - Guardar");
        model.addAttribute("docentes", docenteDAO.findAll());
        model.addAttribute("materia", new Materia());
        model.addAttribute("docenteSelected", "");
        model.addAttribute("grupo", new Grupo());
        model.addAttribute("horariosGrupo", horarioDeGrupoService.getListHorarioGrupoWrapper());
        model.addAttribute("edit", "false");
        return "/grupo/form";
    }

    @Secured("ROLE_JEFE")
    @GetMapping(value = "/cargar-materias/{nombre}", produces = {"application/json"})
    public @ResponseBody
    List<Materia> cargarMateria(@PathVariable String nombre) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) auth.getPrincipal();
        return materiaDAO.findByNombreContainingAndCodigoMateriaContaining(nombre, usuario.getCodigoCarrera());
    }

    @Secured("ROLE_JEFE")
    //Al crear como nuevo un grupo con una materia que ya tiene grupo y redirecciona con error se actualiza la que tenia anteriormente
    @PostMapping("/form")
    public String guardar(@RequestParam(defaultValue = "") String codigoMateria, @RequestParam(defaultValue = "") String nombreMateria,
            @RequestParam(defaultValue = "") String numeroControlDocente, RedirectAttributes flash, Model model,
            @ModelAttribute ListHorarioGrupoWrapper horariosGrupo, @RequestParam String codigoGrupo) {
        Materia materia = materiaDAO.findById(codigoMateria).orElse(materiaDAO.findByNombre(nombreMateria));
        Docente docente = docenteDAO.findById(numeroControlDocente).orElse(null);
        if (docente == null || materia == null) {
            Grupo grupo = new Grupo();
            grupo.setCodigoGrupo(codigoGrupo);
            redirectGuardarWithErrors(model, grupo, new Materia(), new Docente(), horariosGrupo, "Seleccione un docente y una materia");
            return "/grupo/form";
        }

        if (horariosGrupo.getHorarios().isEmpty()) {
            Grupo grupo = new Grupo();
            grupo.setCodigoGrupo(codigoGrupo);
            redirectGuardarWithErrors(model, grupo, new Materia(), new Docente(), horariosGrupo, "Asigne un horario para el gruoi");
            return "/grupo/form";
        }

        Grupo grupo = new Grupo();
        Boolean isNew = codigoGrupo.isEmpty();
        if (isNew) {
            List<Grupo> gruposConMateria = grupoDAO.findBycodigoMateria(materia.getCodigoMateria());
            codigoGrupo = materia.getCodigoMateria() + "-" + (gruposConMateria.isEmpty() ? "1" : (Integer.parseInt(gruposConMateria.get(gruposConMateria.size() - 1).getCodigoGrupo().substring(6)) + 1));
        }

        grupo.setCodigoGrupo(codigoGrupo);
        grupo.setMateria(materia);
        grupo.setDocente(docente);
        List<HorarioDeGrupo> horariosDeGrupo = horarioDeGrupoService.makeHorarioDeGrupo(grupo, horariosGrupo);
        if (horariosDeGrupo == null) {
            redirectGuardarWithErrors(model, grupo, materia, docente, horariosGrupo, "Compruebe que las horas o salones de el horario sean validos");
            return "/grupo/form";
        }
        if (!horarioDeGrupoService.isRepeatHorarioDeGrupo(horariosDeGrupo, codigoGrupo)) {
            grupoDAO.save(grupo);
            if (!isNew) {
                List<HorarioDeGrupo> horarioExistentes = horarioDeGrupoService.findByGrupo(codigoGrupo);
                horarioExistentes.forEach(horario -> {
                    if (!horariosDeGrupo.contains(horario)) {
                        horarioDeGrupoService.delete(horario.getHorarioGrupoIdentity());
                    }
                });
            }
            horariosDeGrupo.forEach((horario) -> {
                horarioDeGrupoService.save(horario);
            });
        } else {
            redirectGuardarWithErrors(model, grupo, materia, docente, horariosGrupo, "Compruebe que el horario, los salones o docentes seleccionados no se repitan con otro grupo");
            return "/grupo/form";
        }

        flash.addFlashAttribute("success", "El grupo se ah guardado con exito");
        return "redirect:/grupos/listar";
    }

    @Secured("ROLE_JEFE")
    @GetMapping("/form/{codigoGrupo}")
    public String editar(@PathVariable String codigoGrupo, Model model, RedirectAttributes flash) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) auth.getPrincipal();
        Grupo grupo = grupoDAO.findById(codigoGrupo).orElse(null);
        if (grupo == null) {
            flash.addFlashAttribute("warning", "Porfavor seleccione un grupo a editar");
            return "redirect:/grupos/listar";
        }
        for (GrantedAuthority role : usuario.getAuthorities()) {
            if (role.getAuthority().equals("ROLE_JEFE")) {
                if (!grupo.getCodigoGrupo().contains(usuario.getCodigoCarrera())) {
                    flash.addFlashAttribute("error", "No tienes permido para acceder a este grupo");
                    return "redirect:/grupos/listar";
                }
            }
        }
        model.addAttribute("titulo", "Grupos");
        model.addAttribute("tituloCard", "Grupos - Guardar");
        model.addAttribute("docentes", docenteDAO.findAll());
        model.addAttribute("materia", grupo.getMateria());
        model.addAttribute("docenteSelected", grupo.getDocente().getNumeroDeControl());
        model.addAttribute("grupo", grupo);
        List<HorarioDeGrupo> horariosDeGrupo = grupo.getHorariosDeGrupo();
        model.addAttribute("horariosGrupo", horarioDeGrupoService.setHorarioExistente(horariosDeGrupo));
        return "/grupo/form";
    }

    @Secured("ROLE_JEFE")
    @GetMapping("/eliminar/{codigoGrupo}")
    public String eliminar(@PathVariable String codigoGrupo, RedirectAttributes flash) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) auth.getPrincipal();
        Grupo grupo = grupoDAO.findById(codigoGrupo).orElse(null);
        if (grupo == null) {
            flash.addFlashAttribute("warning", "Porfavor seleccione un grupo a eliminar");
            return "redirect:/grupos/listar";
        }
        for (GrantedAuthority role : usuario.getAuthorities()) {
            if (role.getAuthority().equals("ROLE_JEFE")) {
                if (!grupo.getCodigoGrupo().contains(usuario.getCodigoCarrera())) {
                    flash.addFlashAttribute("error", "No tienes permido para acceder a este grupo");
                    return "redirect:/grupos/listar";
                }
            }
        }
        if (!grupo.getGrupoAlumnos().isEmpty()) {
            flash.addFlashAttribute("warning", "No se puede eliminar este grupo mientras tenga alumnos");
            return "redirect:/grupos/listar";
        }
        grupoDAO.delete(grupo);
        flash.addFlashAttribute("success", "Grupo eliminado con exito");
        return "redirect:/grupos/listar";
    }

    public void redirectGuardarWithErrors(Model model, Grupo grupo, Materia materia, Docente docente, ListHorarioGrupoWrapper horariosGrupo, String mensajeFlash) {
        model.addAttribute("titulo", "Grupos");
        model.addAttribute("tituloCard", "Grupos - Guardar");
        model.addAttribute("docentes", docenteDAO.findAll());
        model.addAttribute("materia", materia);
        model.addAttribute("docenteSelected", docente.getNumeroDeControl());
        model.addAttribute("horariosGrupo", horariosGrupo);
        model.addAttribute("grupo", grupo);
        model.addAttribute("warning", mensajeFlash);
    }
}
