/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.model.service;

import com.jaevsoftware.USB.model.entity.GrupoAlumno;
import com.jaevsoftware.USB.model.entity.Materia;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Pepe Valenzuela
 */
public interface IGrupoAlumnoService {
    
    public void save(GrupoAlumno grupoAlumno);
    
    public void delete(String codigoGrupoAlumno);
    
    public Optional<GrupoAlumno> findOne(String codigoGrupoAlumno);
    
    public List<GrupoAlumno> findByAlumno(String numeroDeControlAlumno);
    
    public List<GrupoAlumno> findByGrupo(String codigoGrupo);
    
    public List<Materia> findMateriasToLoadOfTheAlumno(String numeroDeControlAlumno);
    
    public Boolean isRepeatHorario(GrupoAlumno grupoAlumno);
}
