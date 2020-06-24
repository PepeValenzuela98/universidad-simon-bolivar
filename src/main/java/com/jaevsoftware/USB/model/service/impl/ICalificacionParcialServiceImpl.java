/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.model.service.impl;

import com.jaevsoftware.USB.model.dao.ICalificacionParcialDAO;
import com.jaevsoftware.USB.model.entity.CalificacionParcial;
import com.jaevsoftware.USB.model.entity.GrupoAlumno;
import com.jaevsoftware.USB.model.entity.embedded.CalificacionParcialIdentity;
import com.jaevsoftware.USB.model.entity.wrapper.CalificacionParcialData;
import com.jaevsoftware.USB.model.entity.wrapper.CalificacionParcialWrapper;
import com.jaevsoftware.USB.model.entity.wrapper.ListCalificacionParcialWrapper;
import com.jaevsoftware.USB.model.service.ICalificacionParcialService;
import com.jaevsoftware.USB.model.service.IGrupoAlumnoService;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ICalificacionParcialServiceImpl implements ICalificacionParcialService {

    private final Log log = LogFactory.getLog(this.getClass());

    @Autowired
    ICalificacionParcialDAO calificacionParcialDAO;

    @Autowired
    IGrupoAlumnoService grupoAlumnoService;

    @Transactional
    @Override
    public void save(CalificacionParcial calificacion) {
        calificacionParcialDAO.save(calificacion);
    }

    @Transactional
    @Override
    public void delete(CalificacionParcialIdentity calificacionIdentity) {
        calificacionParcialDAO.deleteById(calificacionIdentity);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CalificacionParcial> findByAlumnoAndGrupo(String numeroDeControlAlumno, String codigoGrupo) {
        return calificacionParcialDAO.findByAlumnoAndGrupo(numeroDeControlAlumno, codigoGrupo);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CalificacionParcial> findByAlumno(String numeroDeControlAlumno) {
        return calificacionParcialDAO.findByAlumno(numeroDeControlAlumno);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<CalificacionParcial> findOne(CalificacionParcialIdentity calificacionParcialIdentity) {
        return calificacionParcialDAO.findById(calificacionParcialIdentity);
    }

    
    @Override
    public Iterable<CalificacionParcial> findAll() {
        return calificacionParcialDAO.findAll();
    }

    @Override
    public ListCalificacionParcialWrapper createWrapperOneGrupo(String codigoGrupo) {
        List<GrupoAlumno> grupoAlumnos = grupoAlumnoService.findByGrupo(codigoGrupo);
        ListCalificacionParcialWrapper calificacionesWrapper = new ListCalificacionParcialWrapper();
        grupoAlumnos.forEach(grupoAlumno -> {
            List<CalificacionParcial> calificacionesAlumno = findByAlumnoAndGrupo(grupoAlumno.getAlumno().getNumeroDeControl(), grupoAlumno.getGrupo().getCodigoGrupo());

            CalificacionParcialWrapper calificacionWrapper = new CalificacionParcialWrapper();
            calificacionWrapper.setNumeroDeControlAlumno(grupoAlumno.getAlumno().getNumeroDeControl());
            calificacionWrapper.setNombreAlumno(grupoAlumno.getAlumno().getNombre().concat(" ").concat(grupoAlumno.getAlumno().getApellido()));

            List<CalificacionParcialData> calificacionData = new ArrayList();
            calificacionesAlumno.forEach(calificacionAlumno -> {
                CalificacionParcialData calificacionParcialData = new CalificacionParcialData();
                calificacionParcialData.setCalificacion(calificacionAlumno.getCalificacion());
                calificacionParcialData.setUnidad(calificacionAlumno.getCalificacionParcialIdentity().getUnidad());
                calificacionData.add(calificacionParcialData);
            });

            calificacionWrapper.setCalificacionesPorUnidad(calificacionData);

            calificacionesWrapper.addCalificacion(calificacionWrapper);
        });
        return calificacionesWrapper;
    }

    @Override
    public ListCalificacionParcialWrapper createWrapperOneAlumno(String numeroDeControlAlumno) {
        List<GrupoAlumno> grupoAlumnos = grupoAlumnoService.findByAlumno(numeroDeControlAlumno);
        ListCalificacionParcialWrapper listCalificacionParcialWrapper = new ListCalificacionParcialWrapper();

        grupoAlumnos.forEach(grupoAlumno -> {
            List<CalificacionParcial> calificacionesAlumno = findByAlumnoAndGrupo(grupoAlumno.getAlumno().getNumeroDeControl(), grupoAlumno.getGrupo().getCodigoGrupo());

            CalificacionParcialWrapper calificacionWrapper = new CalificacionParcialWrapper();
            calificacionWrapper.setNombreMateria(grupoAlumno.getGrupo().getMateria().getNombre());

            List<CalificacionParcialData> calificacionData = new ArrayList();
            calificacionesAlumno.forEach(calificacionAlumno -> {
                CalificacionParcialData calificacionParcialData = new CalificacionParcialData();
                calificacionParcialData.setCalificacion(calificacionAlumno.getCalificacion());
                calificacionParcialData.setUnidad(calificacionAlumno.getCalificacionParcialIdentity().getUnidad());
                calificacionData.add(calificacionParcialData);
            });

            calificacionWrapper.setCalificacionesPorUnidad(calificacionData);

            listCalificacionParcialWrapper.addCalificacion(calificacionWrapper);
        });
        return listCalificacionParcialWrapper;
    }

    @Override
    public Integer getUnidadesMaximo(List<CalificacionParcial> calificacionesParciales) {
        Comparator<CalificacionParcial> byUnidedes = (CalificacionParcial calificacion1, CalificacionParcial calificacion2) -> calificacion1.getCalificacionParcialIdentity().getGrupoAlumno().getGrupo().getMateria().getNumeroUnidades().compareTo(calificacion1.getCalificacionParcialIdentity().getGrupoAlumno().getGrupo().getMateria().getNumeroUnidades());
        Collections.sort(calificacionesParciales, byUnidedes);
        return calificacionesParciales.get(0).getCalificacionParcialIdentity().getGrupoAlumno().getGrupo().getMateria().getNumeroUnidades();
    }

    @Override
    public void saveByWrapper(ListCalificacionParcialWrapper calificacionesWrapper, String codigoMateria) {

        calificacionesWrapper.getCalificaciones().forEach(calificacionWrapper -> {
            calificacionWrapper.getCalificacionesPorUnidad().forEach(calificacionPorUnidad -> {

                CalificacionParcialIdentity calificacionParcialIdentity = new CalificacionParcialIdentity();
                GrupoAlumno grupoAlumno = grupoAlumnoService.findOne(codigoMateria.concat("/").concat(calificacionWrapper.getNumeroDeControlAlumno())).orElse(null);
                if (grupoAlumno == null) {
                    throw new NullPointerException("Grupo alumno nulo");
                }
                calificacionParcialIdentity.setUnidad(calificacionPorUnidad.getUnidad());
                calificacionParcialIdentity.setGrupoAlumno(grupoAlumno);

                CalificacionParcial calificacionParcial = findOne(calificacionParcialIdentity).orElse(null);
                if (calificacionPorUnidad.getCalificacion() < 0 || calificacionPorUnidad.getCalificacion() > 100) {
                    throw new NullPointerException("Calificacion no valida");
                }
                calificacionParcial.setCalificacion(calificacionPorUnidad.getCalificacion());
                save(calificacionParcial);

            });

        });
    }

}
