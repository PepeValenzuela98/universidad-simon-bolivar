/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.controllers;

import com.jaevsoftware.USB.model.entity.Alumno;
import com.jaevsoftware.USB.model.entity.wrapper.KardexWrapper;
import com.jaevsoftware.USB.model.service.IAlumnoService;
import com.jaevsoftware.USB.model.service.IKardexService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/kardex")
@Secured({"ROLE_JEFE", "ROLE_ALUMNO"})
public class KardexController {

    @Autowired
    IAlumnoService alumnoService;

    @Autowired
    IKardexService kardexService;

    
    @GetMapping("/ver/{numeroDeControlAlumno}")
    public String ver(@PathVariable String numeroDeControlAlumno, Model model, RedirectAttributes flash) {
        Alumno alumno = alumnoService.findOne(numeroDeControlAlumno).orElse(null);
        if (alumno == null) {
            flash.addFlashAttribute("warning", "Porfavor seleccione un alumno");
            return "redirect:/alumnos/listar";
        }
        model.addAttribute("titulo", "Kardex");
        model.addAttribute("tituloCard", "Kardex - " + numeroDeControlAlumno);
        model.addAttribute("numeroDeControlAlumno", numeroDeControlAlumno);
        model.addAttribute("semestresCursados", alumno.getSemestre() - 1);
        return "/kardex/ver";
    }

    @GetMapping(value = "/cargar-kardex/{numeroDeControlAlumno}/{semestre}", produces = {"application/json"})
    public @ResponseBody
    List<KardexWrapper> cargarKardex(@PathVariable String numeroDeControlAlumno, @PathVariable String semestre) {
        return semestre.equals("*") ? kardexService.findByNumeroDeControlAlumnoOrderBySemestreCursoAsc(numeroDeControlAlumno) : kardexService.findByNumeroDeControlAlumnoAndSemestreCurso(numeroDeControlAlumno, Integer.valueOf(semestre));
    }
}
