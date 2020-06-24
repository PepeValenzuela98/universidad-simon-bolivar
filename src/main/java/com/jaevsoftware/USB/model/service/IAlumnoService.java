/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.model.service;

import com.jaevsoftware.USB.model.entity.Alumno;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author Pepe Valenzuela
 */
public interface IAlumnoService {
    public void save(Alumno alumno);
    
    public void delete(String numeroDeControl);
    
    public Alumno generateNewAlumno(String codigoCarrera);
    
    public Optional<Alumno> findOne(String numeroDeControl);
    
    public List<Alumno> findByNumeroDeControlContaining(String numeroDeControl,String codigoCarrera);
    
    public Page<Alumno> findAll(Pageable pageable,String codigoCarrera);
    
    public List<Alumno> findIfExistsInGrupoAlumno(String codigoCarrera);
}
