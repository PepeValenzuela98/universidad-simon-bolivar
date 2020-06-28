/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.controllers;

import com.jaevsoftware.USB.model.dao.IGrupoDAO;
import com.jaevsoftware.USB.model.entity.Docente;
import com.jaevsoftware.USB.model.entity.Grupo;
import com.jaevsoftware.USB.model.entity.wrapper.Usuario;
import com.jaevsoftware.USB.model.service.IDocenteService;
import com.jaevsoftware.USB.model.service.IHorarioDeGrupoService;
import com.jaevsoftware.USB.util.PageRender;
import java.util.List;
import java.util.Locale;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
@RequestMapping("/docentes")
@SessionAttributes("docente")
public class DocenteController {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    IDocenteService docenteService;

    @Autowired
    IGrupoDAO grupoDAO;

    @Autowired
    IHorarioDeGrupoService horarioDeGrupoService;

    @Secured("ROLE_JEFE")
    @GetMapping("/listar")
    public String listar(@RequestParam(name = "page", defaultValue = "0") Integer page, Model model) {

        Pageable paginaRequerida = PageRequest.of(page, 5);
        Page<Docente> docentes = docenteService.findAll(paginaRequerida);
        PageRender<Docente> pageRender = new PageRender<>("/docentes/listar", docentes);
        model.addAttribute("titulo", "Docentes");
        model.addAttribute("tituloCard", "Docentes");
        model.addAttribute("docentes", docentes);
        model.addAttribute("docenteFiltro", new Docente());
        model.addAttribute("page", pageRender);
        return "/docente/listar";
    }

    @Secured("ROLE_JEFE")
    @GetMapping("/filtro")
    public String listarFiltro(Docente docenteFiltro, Model model) {

        List<Docente> docentesFiltro = docenteService.findByNumeroDeControlContaining(docenteFiltro.getNumeroDeControl());
        model.addAttribute("titulo", "Docentes");
        model.addAttribute("tituloCard", "Docentes");
        model.addAttribute("docentes", docentesFiltro);
        model.addAttribute("docenteFiltro", docenteFiltro);
        return "/docente/listar";
    }

    @Secured({"ROLE_JEFE", "ROLE_DOCENTE"})
    @GetMapping("/ver/{numeroControl}")
    public String ver(@PathVariable("numeroControl") String numeroControl, Model model, RedirectAttributes flash) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) auth.getPrincipal();
        for (GrantedAuthority role : usuario.getAuthorities()) {
            if (role.getAuthority().equals("ROLE_DOCENTE")) {
                if (!usuario.getUsername().equals(numeroControl)) {
                    flash.addFlashAttribute("error", "No tienes permido para acceder a este docente");
                    return "redirect:/";
                }
            }
        }
        Docente docente = docenteService.findOne(numeroControl).orElse(null);
        if (docente == null) {
            flash.addFlashAttribute("warning", "Porfavor seleccione un docente existente");
            return "redirect:/docentes/listar";
        }
        List<Grupo> gruposDocente = grupoDAO.findByDocente(numeroControl);
        gruposDocente.forEach(grupo -> {
            grupo.setHorariosDeGrupo(horarioDeGrupoService.sortHorarioDeGrupo(grupo.getHorariosDeGrupo()));
        });
        model.addAttribute("titulo", "Docentes");
        model.addAttribute("tituloCard", "Docente - " + docente.getNumeroDeControl());
        model.addAttribute("docente", docente);
        model.addAttribute("gruposDocente", gruposDocente);
        return "/docente/ver";
    }

    @Secured("ROLE_JEFE")
    @GetMapping("/form")
    public String crear(Model model, RedirectAttributes flash) {

        Docente nuevoDocente = docenteService.generateNewDocente();
        model.addAttribute("titulo", "Docente");
        model.addAttribute("tituloCard", "Docentes - Alta");
        model.addAttribute("docente", nuevoDocente);
        return "/docente/form";
    }

    @Secured("ROLE_JEFE")
    @PostMapping("/form")
    public String guardar(@Valid Docente docente, BindingResult result, Model model, RedirectAttributes flash, SessionStatus status) {
        if (result.hasErrors()) {
            flash.addFlashAttribute("error", "Error al cargar los datos verifique el formulario");
            List<FieldError> errores = result.getFieldErrors();
            errores.forEach((error) -> {
                flash.addFlashAttribute(error.getField().replace("ñ", "n") + "Error", messageSource.getMessage(error, Locale.ENGLISH));
            });
            flash.addFlashAttribute("errorField", "errorField");
            return "redirect:/docentes/form";
        }
        docenteService.save(docente);
        status.setComplete();
        flash.addFlashAttribute("success", "Se ha guardado el docente con exito");
        return "redirect:/docentes/listar";
    }

    @Secured("ROLE_JEFE")
    @GetMapping("/form/{numeroDeControl}")
    public String editar(@PathVariable("numeroDeControl") String numeroDeControl, Model model, RedirectAttributes flash) {

        Docente docente = docenteService.findOne(numeroDeControl).orElse(null);
        if (docente == null) {
            flash.addFlashAttribute("warning", "Porfavor seleccione un docente a editar");
            return "redirect:/docentes/listar";
        }
        model.addAttribute("titulo", "Docentes");
        model.addAttribute("tituloCard", "Docentes - Editar");
        model.addAttribute("docente", docente);
        return "/docente/form";
    }

    @Secured("ROLE_JEFE")
    @GetMapping("/eliminar/{numeroDeControl}")
    public String eliminar(@PathVariable("numeroDeControl") String numeroDeControl, RedirectAttributes flash) {
        Docente docente = docenteService.findOne(numeroDeControl).orElse(null);
        if (docente == null) {
            flash.addFlashAttribute("warning", "Porfavor seleccione un docente a eliminar");
            return "redirect:/docentes/listar";
        }
        if(!docente.getGrupos().isEmpty()){
            flash.addFlashAttribute("warning", "No se puede eliminar este docente mientras tenga grupos");
            return "redirect:/docentes/listar";
        }
        docenteService.delete(numeroDeControl);
        flash.addFlashAttribute("success", "Docente eliminado con exito");
        return "redirect:/docentes/listar";
    }

}
