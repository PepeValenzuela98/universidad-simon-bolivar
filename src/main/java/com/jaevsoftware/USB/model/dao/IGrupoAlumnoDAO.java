/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.model.dao;

import com.jaevsoftware.USB.model.entity.GrupoAlumno;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Pepe Valenzuela
 */
public interface IGrupoAlumnoDAO extends CrudRepository<GrupoAlumno, String>{
    @Query("select g from GrupoAlumno g join g.alumno where g.alumno.numeroDeControl like ?1")
    public List<GrupoAlumno> findByAlumno(String numeroDeControlAlumno);
 
    @Query("select g from GrupoAlumno g join g.grupo where g.grupo.codigoGrupo like ?1")
    public List<GrupoAlumno> findByGrupo(String codigoGrupo);
}
