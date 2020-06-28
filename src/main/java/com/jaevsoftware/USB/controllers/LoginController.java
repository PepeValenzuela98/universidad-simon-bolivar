/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.controllers;

import java.security.Principal;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    private final Log log = LogFactory.getLog(this.getClass());

    @GetMapping("/login")
    public String login(@RequestParam(name = "logout", required = false) String logout, @RequestParam(value = "error", required = false) String error, Model model, RedirectAttributes flash, Principal principal) {
        model.addAttribute("titulo", "Iniciar sesion");
        model.addAttribute("tituloCard", "Iniciar sesion");
        if (principal != null) {
            log.info(principal.getName());
            flash.addFlashAttribute("warning", "Usted ya ha iniciado sesion");
            return "redirect:/";
        }
        if (error != null) {
            model.addAttribute("error", "Error al iniciar sesion usuario o contraseña incorrectas");
            return "login";
        }
        if (logout != null) {
            flash.addFlashAttribute("success", "Sesion cerrada con exito");
            return "redirect:/";
        }
        return "login";
    }
}
