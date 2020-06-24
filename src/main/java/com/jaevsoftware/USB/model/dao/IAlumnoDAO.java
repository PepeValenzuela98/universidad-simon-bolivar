/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.model.dao;

import com.jaevsoftware.USB.model.entity.Alumno;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 *
 * @author Pepe Valenzuela
 */
public interface IAlumnoDAO extends PagingAndSortingRepository<Alumno, String> {

    @Query("select a from Alumno a where a.carrera.codigoCarrera like ?1 order by a.semestre asc")
    public Page<Alumno> findAllByOrderBySemestreAsc(Pageable pageable, String codigoCarrera);

    @Query("select a from Alumno a where a.numeroDeControl like %?1% and a.carrera.codigoCarrera like ?2")
    public List<Alumno> findByNumeroDeControlAndCarreraContaining(String numeroDeControl, String codigoCarrera);
    
    public List<Alumno> findByNumeroDeControl(String numeroDeControl);

    @Query("select a from Alumno a where exists(select g from GrupoAlumno g WHERE g.alumno.numeroDeControl like a.numeroDeControl) and a.carrera.codigoCarrera like ?1 order by a.semestre asc")
    public List<Alumno> findIfExistsInGrupoAlumno(String carrera);
}
