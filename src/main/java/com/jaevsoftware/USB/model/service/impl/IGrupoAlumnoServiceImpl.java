
    /*
     * To change this license header, choose License Headers in Project Properties.
     * To change this template file, choose Tools | Templates
     * and open the template in the editor.
 */
package com.jaevsoftware.USB.model.service.impl;

import com.jaevsoftware.USB.model.dao.IGrupoAlumnoDAO;
import com.jaevsoftware.USB.model.dao.IKardexDAO;
import com.jaevsoftware.USB.model.dao.IMateriaDAO;
import com.jaevsoftware.USB.model.entity.Alumno;
import com.jaevsoftware.USB.model.entity.GrupoAlumno;
import com.jaevsoftware.USB.model.entity.HorarioDeGrupo;
import com.jaevsoftware.USB.model.entity.Materia;
import com.jaevsoftware.USB.model.service.IGrupoAlumnoService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jaevsoftware.USB.model.entity.Kardex;
import com.jaevsoftware.USB.model.service.IAlumnoService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Service
public class IGrupoAlumnoServiceImpl implements IGrupoAlumnoService {

    private final Log log = LogFactory.getLog(this.getClass());

    @Autowired
    IGrupoAlumnoDAO grupoAlumnoDAO;

    @Autowired
    IKardexDAO kardexDAO;

    @Autowired
    IMateriaDAO materiaDAO;

    @Autowired
    IAlumnoService alumnoService;

    @Override
    public void save(GrupoAlumno grupoAlumno) {
        grupoAlumnoDAO.save(grupoAlumno);
    }

    @Override
    public void delete(String codigoGrupoAlumno) {
        grupoAlumnoDAO.deleteById(codigoGrupoAlumno);
    }

    @Override
    public Optional<GrupoAlumno> findOne(String codigoGrupoAlumno) {
        return grupoAlumnoDAO.findById(codigoGrupoAlumno);
    }

    @Override
    public List<GrupoAlumno> findByAlumno(String numeroDeControlAlumno) {
        return grupoAlumnoDAO.findByAlumno(numeroDeControlAlumno);
    }

    @Override
    public List<GrupoAlumno> findByGrupo(String codigoGrupo) {
        return grupoAlumnoDAO.findByGrupo(codigoGrupo);
    }

    @Override
    public List<Materia> findMateriasToLoadOfTheAlumno(String numeroDeControlAlumno) {
        Alumno alumno = alumnoService.findOne(numeroDeControlAlumno).orElse(null);
        List<Materia> materiasCarga = new ArrayList();
        List<Kardex> kardexAlumno = kardexDAO.findByNumeroDeControlAlumnoOrderBySemestreCursoAsc(numeroDeControlAlumno);
        List<Materia> materiasCarrera = materiaDAO.findByCodigoMateriaStartingWith(alumno.getCarrera().getCodigoCarrera());
        List<GrupoAlumno> gruposAlumnoCargados = findByAlumno(numeroDeControlAlumno);
        List<Materia> materiasCargadasActualmente = new ArrayList<>();
        gruposAlumnoCargados.forEach(grupo -> {
            materiasCargadasActualmente.add(grupo.getGrupo().getMateria());
        });

        kardexAlumno.forEach(kardex -> {
            //Si reprobo agregar
            Materia materia = materiaDAO.findById(kardex.getCodigoMateria()).orElse(null);
            if (kardex.getPromedio() < 70 && !materiasCargadasActualmente.contains(materia)) {
                materiasCarga.add(materia);
            }
        });

        materiasCarrera.forEach(materia -> {
            //Si es de su semestre agregar
            if (alumno.getSemestre() == materia.getSemestre()) {
                if (!materiasCarga.contains(materia) && !materiasCargadasActualmente.contains(materia)) {
                    materiasCarga.add(materia);
                }
                //Si de un semestre menor al del alumno
            } else if (materia.getSemestre() < alumno.getSemestre()) {
                //Si no esta en su kardex agregar
                Kardex kardex = kardexDAO.findByNumeroDeControlAlumnoAndCodigoMateria(numeroDeControlAlumno, materia.getCodigoMateria()).orElse(null);
                if (kardex == null) {
                    if (!materiasCarga.contains(materia) && !materiasCargadasActualmente.contains(materia)) {
                        materiasCarga.add(materia);
                    }
                }
            }
        });
        return materiasCarga;
    }

    @Override
    public Boolean isRepeatHorario(GrupoAlumno grupoAlumnoACargar) {
        Boolean seRepite = false;
        List<GrupoAlumno> gruposAlumno = findByAlumno(grupoAlumnoACargar.getAlumno().getNumeroDeControl());
        for (GrupoAlumno grupoAlumno : gruposAlumno) {
            for (HorarioDeGrupo horario : grupoAlumno.getGrupo().getHorariosDeGrupo()) {
                for (HorarioDeGrupo horarioACargar : grupoAlumnoACargar.getGrupo().getHorariosDeGrupo()) {
                    if (horario.getHorarioGrupoIdentity().getDia().equals(horarioACargar.getHorarioGrupoIdentity().getDia())) {
                        if (horario.getHoraInicia().equals(horarioACargar.getHoraInicia())
                                || Integer.parseInt(horario.getHoraInicia().split(":")[0]) >= Integer.parseInt(horarioACargar.getHoraInicia().split(":")[0])
                                && Integer.parseInt(horario.getHoraFinal().split(":")[0]) <= Integer.parseInt(horarioACargar.getHoraFinal().split(":")[0])) {
                            seRepite = true;
                            return seRepite;
                        }
                    }
                }
            }
        }
        return seRepite;
    }
}
