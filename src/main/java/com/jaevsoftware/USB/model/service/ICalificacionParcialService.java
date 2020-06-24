/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.model.service;

import com.jaevsoftware.USB.model.entity.CalificacionParcial;
import com.jaevsoftware.USB.model.entity.embedded.CalificacionParcialIdentity;
import com.jaevsoftware.USB.model.entity.wrapper.ListCalificacionParcialWrapper;
import java.util.List;
import java.util.Optional;

public interface ICalificacionParcialService {

    public void save(CalificacionParcial calificacion);

    public void delete(CalificacionParcialIdentity calificacionIdentity);

    public List<CalificacionParcial> findByAlumnoAndGrupo(String numeroDeControlAlumno, String codigoGrupo);
    
    public List<CalificacionParcial> findByAlumno(String numeroDeControlAlumno);
    
    public Optional<CalificacionParcial> findOne(CalificacionParcialIdentity calificacionParcialIdentity);

    public Iterable<CalificacionParcial> findAll();

    public ListCalificacionParcialWrapper createWrapperOneGrupo(String codigoGrupo);
    
    public ListCalificacionParcialWrapper createWrapperOneAlumno(String numeroDeControlAlumno);
    
    public Integer getUnidadesMaximo(List<CalificacionParcial> calificacionesParciales);

    public void saveByWrapper(ListCalificacionParcialWrapper calificacionesWrapper, String codigoGrupo);

}
