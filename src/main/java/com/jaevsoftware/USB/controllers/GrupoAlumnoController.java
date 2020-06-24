/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.controllers;

import com.jaevsoftware.USB.model.dao.ICalificacionParcialDAO;
import com.jaevsoftware.USB.model.dao.IGrupoDAO;
import com.jaevsoftware.USB.model.dao.IKardexDAO;
import com.jaevsoftware.USB.model.dao.IMateriaDAO;
import com.jaevsoftware.USB.model.entity.Alumno;
import com.jaevsoftware.USB.model.entity.CalificacionParcial;
import com.jaevsoftware.USB.model.entity.Grupo;
import com.jaevsoftware.USB.model.entity.GrupoAlumno;
import com.jaevsoftware.USB.model.entity.Kardex;
import com.jaevsoftware.USB.model.entity.Materia;
import com.jaevsoftware.USB.model.entity.embedded.CalificacionParcialIdentity;
import com.jaevsoftware.USB.model.service.IAlumnoService;
import com.jaevsoftware.USB.model.service.IGrupoAlumnoService;
import com.jaevsoftware.USB.model.service.IHorarioDeGrupoService;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/gruposAlumno")
@Secured("ROLE_JEFE")
public class GrupoAlumnoController {

    private final Log log = LogFactory.getLog(this.getClass());

    @Autowired
    IAlumnoService alumnoService;

    @Autowired
    IGrupoDAO grupoDAO;

    @Autowired
    IMateriaDAO materiaDAO;

    @Autowired
    IGrupoAlumnoService grupoAlumnoService;

    @Autowired
    IHorarioDeGrupoService horarioGrupoService;

    @Autowired
    IKardexDAO kardexDAO;

    @Autowired
    ICalificacionParcialDAO calificacionParcialDAO;

    @GetMapping("/formList/{numeroDeControl}")
    public String listarCargaMaterias(@PathVariable String numeroDeControl, Model model, RedirectAttributes flash) {
        Alumno alumno = alumnoService.findOne(numeroDeControl).orElse(null);
        if (alumno == null) {
            flash.addFlashAttribute("warning", "Seleccione un alumno para cargar materia");
            return "redirect:/alumnos/listar";
        }
        List<Materia> materiasACargar = grupoAlumnoService.findMateriasToLoadOfTheAlumno(numeroDeControl);
        List<GrupoAlumno> gruposAlumnoCargados = grupoAlumnoService.findByAlumno(numeroDeControl);
        gruposAlumnoCargados.forEach(grupoAlumno -> {
            grupoAlumno.getGrupo().setHorariosDeGrupo(horarioGrupoService.sortHorarioDeGrupo(grupoAlumno.getGrupo().getHorariosDeGrupo()));
        });
        model.addAttribute("titulo", "Carga de materias");
        model.addAttribute("tituloCard", "Carga de materias - " + alumno.getNombre() + " " + alumno.getApellido());
        model.addAttribute("alumno", alumno);
        model.addAttribute("materiasACargar", materiasACargar);
        model.addAttribute("gruposAlumnoCargados", gruposAlumnoCargados);
        return "/grupoAlumno/listar";
    }

    @GetMapping("/formList/{numeroDeControl}/{codigoMateria}")
    public String listarGruposMaterias(@PathVariable String numeroDeControl, @PathVariable String codigoMateria, RedirectAttributes flash, Model model) {
        Materia materia = materiaDAO.findById(codigoMateria).orElse(null);
        Alumno alumno = alumnoService.findOne(numeroDeControl).orElse(null);
        if (alumno == null) {
            flash.addFlashAttribute("warning", "Seleccione un alumno para cargar materia");
            return "redirect:/alumnos/listar";
        }
        if (materia == null) {
            flash.addFlashAttribute("warning", "Seleccione una materia a cargar");
            return "redirect:/gruposAlumno/formList/" + numeroDeControl;
        }
        List<Materia> materiasACargar = grupoAlumnoService.findMateriasToLoadOfTheAlumno(numeroDeControl);

        if (!materiasACargar.contains(materia)) {
            flash.addFlashAttribute("warning", "No se puede cargar una materia que el alumno no tiene disponible aun");
            return "redirect:/gruposAlumno/formList/" + numeroDeControl;
        }
        List<Grupo> grupos = grupoDAO.findBycodigoMateria(codigoMateria);
        grupos.forEach(grupo -> {
            grupo.setHorariosDeGrupo(horarioGrupoService.sortHorarioDeGrupo(grupo.getHorariosDeGrupo()));
        });
        model.addAttribute("titulo", "Carga de materias");
        model.addAttribute("tituloCard", "Carga de materias - " + alumno.getNombre() + " " + alumno.getApellido() + " - " + materia.getNombre());
        model.addAttribute("alumno", alumno);
        model.addAttribute("materia", materia);
        model.addAttribute("grupos", grupos);
        return "/grupoAlumno/form";
    }

    @PostMapping("/formList")
    public String guardar(@RequestParam(defaultValue = "") String numeroDeControlAlumno, @RequestParam(defaultValue = "") String codigoGrupo, RedirectAttributes flash, Model model) {
        Alumno alumno = alumnoService.findOne(numeroDeControlAlumno).orElse(null);
        Grupo grupo = grupoDAO.findById(codigoGrupo).orElse(null);
        if (alumno == null) {
            flash.addFlashAttribute("warning", "Seleccione un alumno para cargar materia");
            return "redirect:/alumnos/listar";
        }
        if (grupo == null) {
            flash.addFlashAttribute("warning", "Seleccione un grupo para cargar materia");
            return "redirect:/gruposAlumno/formList/" + numeroDeControlAlumno;
        }
        GrupoAlumno grupoAlumno = new GrupoAlumno();
        grupoAlumno.setCodigoGrupoAlumno(grupo.getMateria().getCodigoMateria() + "/" + alumno.getNumeroDeControl());
        grupoAlumno.setAlumno(alumno);
        grupoAlumno.setGrupo(grupo);
        grupoAlumno.setPromedio(0.0d);
        List<GrupoAlumno> gruposAlumno = grupoAlumnoService.findByAlumno(numeroDeControlAlumno);
        List<Materia> materiasCargadas = new ArrayList<>();
        gruposAlumno.forEach(grupoAlumnoCargado -> {
            materiasCargadas.add(grupoAlumnoCargado.getGrupo().getMateria());
        });
        List<Kardex> kardexAlumno = kardexDAO.findByNumeroDeControlAlumnoOrderBySemestreCursoAsc(numeroDeControlAlumno);

        if (gruposAlumno.size() == 5) {
            for (Kardex kardex : kardexAlumno) {
                Materia materia = materiaDAO.findById(kardex.getCodigoMateria()).orElse(null);
                if (kardex.getPromedio() < 70 && !materiasCargadas.contains(materia)) {
                    if (!grupoAlumno.getGrupo().getMateria().equals(materia)) {
                        flash.addFlashAttribute("warning", "Se debe cargar obligatoriamente la materia: " + materia.getNombre());
                        return "redirect:/gruposAlumno/formList/" + numeroDeControlAlumno;
                    }

                }
            }
        }

        if (gruposAlumno.size() == 6) {
            flash.addFlashAttribute("warning", "No se pueden cargar mas materias a menos que elimine una de las ya seleccionadas");
            return "redirect:/gruposAlumno/formList/" + numeroDeControlAlumno;
        }
        if (!grupoAlumnoService.isRepeatHorario(grupoAlumno)) {
            grupoAlumnoService.save(grupoAlumno);
            flash.addFlashAttribute("success", "Materia cargada con exito!!");
            for (Integer unidad = 1; unidad <= grupo.getMateria().getNumeroUnidades(); unidad++) {
                CalificacionParcial calificacionParcial = new CalificacionParcial();
                CalificacionParcialIdentity calificacionParcialIdentity = new CalificacionParcialIdentity();
                calificacionParcialIdentity.setGrupoAlumno(grupoAlumno);
                calificacionParcialIdentity.setUnidad(unidad);
                calificacionParcial.setCalificacionParcialIdentity(calificacionParcialIdentity);
                calificacionParcial.setCalificacion(0);
                calificacionParcialDAO.save(calificacionParcial);
            }
            return "redirect:/gruposAlumno/formList/" + numeroDeControlAlumno;
        } else {
            flash.addFlashAttribute("warning", "Compruebe que el horario de el grupo seleccionado no choque con otro");
            return "redirect:/gruposAlumno/formList/" + numeroDeControlAlumno;
        }
    }

    @GetMapping("/eliminar/{codigoMateria}/{numeroDeControlAlumno}")
    public String eliminar(@PathVariable String codigoMateria, @PathVariable String numeroDeControlAlumno, RedirectAttributes flash) {
        GrupoAlumno grupoAlumno = grupoAlumnoService.findOne(codigoMateria + "/" + numeroDeControlAlumno).orElse(null);
        if (grupoAlumno == null) {
            flash.addFlashAttribute("warning", "Por favor seleccione el grupo de un alumno");
            return "redirect:/alumnos/listar";
        }
        grupoAlumnoService.delete(grupoAlumno.getCodigoGrupoAlumno());
        flash.addFlashAttribute("success", "Grupo de alumno eliminado con exito");
        return "redirect:/gruposAlumno/formList/" + grupoAlumno.getAlumno().getNumeroDeControl();
    }
}
