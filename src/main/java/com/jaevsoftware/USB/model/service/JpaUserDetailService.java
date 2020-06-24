/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.model.service;

import com.jaevsoftware.USB.model.dao.ICarreraDAO;
import com.jaevsoftware.USB.model.dao.IJefeDepartamentoDAO;
import com.jaevsoftware.USB.model.entity.Alumno;
import com.jaevsoftware.USB.model.entity.Carrera;
import com.jaevsoftware.USB.model.entity.Docente;
import com.jaevsoftware.USB.model.entity.JefeDepartamento;
import com.jaevsoftware.USB.model.entity.Secretaria;
import com.jaevsoftware.USB.model.entity.wrapper.Usuario;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service()
public class JpaUserDetailService implements UserDetailsService {

    private final Log log = LogFactory.getLog(this.getClass());

    @Autowired
    IJefeDepartamentoDAO jefeDepartamentoDAO;

    @Autowired
    ISecretariaService secretariaService;

    @Autowired
    IDocenteService docenteService;

    @Autowired
    IAlumnoService alumnoService;
    
    @Autowired
    ICarreraDAO carreraDAO;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String numeroDeControl) throws UsernameNotFoundException {
        String userName = "";
        String contraseña = "";
        String nombreCompleto = "";
        String codigoCarrera = "";
        List<GrantedAuthority> authorities = new ArrayList();

        if (numeroDeControl.endsWith("JD")) {
            JefeDepartamento jefe = jefeDepartamentoDAO.findById(numeroDeControl).orElse(null);
            
            if (jefe == null) {
                log.error("No existe el usuario " + numeroDeControl);
                throw new UsernameNotFoundException("El username ".concat(numeroDeControl).concat(" no existe"));
            }
            Carrera carrera = carreraDAO.findByJefeDepartamento(numeroDeControl);
            authorities.add(new SimpleGrantedAuthority("ROLE_JEFE"));

            userName = jefe.getNumeroDeControl();
            contraseña = jefe.getContraseña();
            nombreCompleto = jefe.getNombre() + " " + jefe.getApellido();
            codigoCarrera = carrera.getCodigoCarrera();
        } else if (numeroDeControl.endsWith("SE")) {
            Secretaria secretaria = secretariaService.findOne(numeroDeControl).orElse(null);
            if (secretaria == null) {
                log.error("No existe el usuario " + numeroDeControl);
                throw new UsernameNotFoundException("El username ".concat(numeroDeControl).concat(" no existe"));
            }
            Carrera carrera = carreraDAO.findBySecretaria(numeroDeControl);
            authorities.add(new SimpleGrantedAuthority("ROLE_SECRETARIA"));

            userName = secretaria.getNumeroDeControl();
            contraseña = secretaria.getContraseña();
            nombreCompleto = secretaria.getNombre() + " " + secretaria.getApellido();
            codigoCarrera = carrera.getCodigoCarrera();
            
        } else if (numeroDeControl.endsWith("DO")) {
            Docente docente = docenteService.findOne(numeroDeControl).orElse(null);
            if (docente == null) {
                log.error("No existe el usuario " + numeroDeControl);
                throw new UsernameNotFoundException("El username ".concat(numeroDeControl).concat(" no existe"));
            }
            authorities.add(new SimpleGrantedAuthority("ROLE_DOCENTE"));

            userName = docente.getNumeroDeControl();
            contraseña = docente.getContraseña();
            nombreCompleto = docente.getNombre() + " " + docente.getApellido();
        } else if (numeroDeControl.endsWith("ES")) {
            Alumno alumno = alumnoService.findOne(numeroDeControl).orElse(null);
            if (alumno == null) {
                log.error("No existe el usuario " + numeroDeControl);
                throw new UsernameNotFoundException("El username ".concat(numeroDeControl).concat(" no existe"));
            }
            authorities.add(new SimpleGrantedAuthority("ROLE_ALUMNO"));

            userName = alumno.getNumeroDeControl();
            contraseña = alumno.getContraseña();
            nombreCompleto = alumno.getNombre() + " " + alumno.getApellido();
            codigoCarrera = alumno.getCarrera().getCodigoCarrera();
        }

        return new Usuario(
                userName,
                contraseña,
                nombreCompleto,
                codigoCarrera,
                true, true, true, true, authorities);
    }

}
