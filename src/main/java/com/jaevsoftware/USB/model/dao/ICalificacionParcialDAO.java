/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.model.dao;

import com.jaevsoftware.USB.model.entity.CalificacionParcial;
import com.jaevsoftware.USB.model.entity.embedded.CalificacionParcialIdentity;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Pepe Valenzuela
 */
public interface ICalificacionParcialDAO extends CrudRepository<CalificacionParcial, CalificacionParcialIdentity> {

    @Query("select c from CalificacionParcial c join c.calificacionParcialIdentity.grupoAlumno.alumno a join c.calificacionParcialIdentity.grupoAlumno.grupo g where a.numeroDeControl like ?1 and g.codigoGrupo like ?2")
    public List<CalificacionParcial> findByAlumnoAndGrupo(String numeroDeControlAlumno, String codigoGrupo);

    @Query("select c from CalificacionParcial c join c.calificacionParcialIdentity.grupoAlumno.alumno a where a.numeroDeControl like ?1")
    public List<CalificacionParcial> findByAlumno(String numeroDeControlAlumno);
}
