/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.model.service.impl;

import com.jaevsoftware.USB.model.dao.IHorarioDeGrupoDAO;
import com.jaevsoftware.USB.model.entity.Grupo;
import com.jaevsoftware.USB.model.entity.HorarioDeGrupo;
import com.jaevsoftware.USB.model.entity.embedded.HorarioGrupoIdentity;
import com.jaevsoftware.USB.model.entity.wrapper.HorarioGrupoWrapper;
import com.jaevsoftware.USB.model.entity.wrapper.ListHorarioGrupoWrapper;
import com.jaevsoftware.USB.model.service.IHorarioDeGrupoService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Pepe Valenzuela
 */
@Service
public class IHorarioDeGrupoServiceImpl implements IHorarioDeGrupoService {

    private final Log log = LogFactory.getLog(this.getClass());

    @Autowired
    IHorarioDeGrupoDAO horarioDeGrupoDAO;

    @Transactional
    @Override
    public void save(HorarioDeGrupo horarioDeGrupo) {
        horarioDeGrupoDAO.save(horarioDeGrupo);
    }

    @Override
    public void delete(HorarioGrupoIdentity horarioGrupoIdentity) {
        horarioDeGrupoDAO.deleteById(horarioGrupoIdentity);
    }

    @Override
    public Boolean isRepeatHorarioDeGrupo(List<HorarioDeGrupo> horariosDeGrupo, String codigoGrupo) {
        Iterable<HorarioDeGrupo> horarios = horarioDeGrupoDAO.findAll();
        for (HorarioDeGrupo horario : horarios) {
            if (!horario.getHorarioGrupoIdentity().getGrupo().getCodigoGrupo().equals(codigoGrupo)) {
                for (int i = 0; i < horariosDeGrupo.size(); i++) {
                    Integer horaInicio = Integer.parseInt(horario.getHoraInicia().split(":")[0]);
                    Integer horaFin = Integer.parseInt(horario.getHoraFinal().split(":")[0]);
                    if (horario.getHorarioGrupoIdentity().getDia().equals(horariosDeGrupo.get(i).getHorarioGrupoIdentity().getDia())) {
                        if (horario.getHoraInicia().split(":")[0].equals(horariosDeGrupo.get(i).getHoraInicia().split(":")[0])
                                && horario.getSalon().equals(horariosDeGrupo.get(i).getSalon())
                                || horario.getHoraInicia().split(":")[0].equals(horariosDeGrupo.get(i).getHoraInicia().split(":")[0])
                                && horario.getHorarioGrupoIdentity().getGrupo().getDocente().equals(horariosDeGrupo.get(i).getHorarioGrupoIdentity().getGrupo().getDocente())
                                || horaInicio >= Integer.parseInt(horariosDeGrupo.get(i).getHoraInicia().split(":")[0])
                                && horaFin <= Integer.parseInt(horariosDeGrupo.get(i).getHoraFinal().split(":")[0]) && horario.getSalon().equals(horariosDeGrupo.get(i).getSalon())) {
                            return true;
                        }
                    }
                }
            }

        }
        return false;
    }

    @Override
    public List<HorarioDeGrupo> makeHorarioDeGrupo(Grupo grupo, ListHorarioGrupoWrapper listHorarioGrupo) {
        List<HorarioDeGrupo> horarios = new ArrayList();
        for (int i = 0; i < listHorarioGrupo.getHorarios().size(); i++) {
            if (!listHorarioGrupo.getHorarios().get(i).getHoraInicio().isEmpty() || !listHorarioGrupo.getHorarios().get(i).getHoraFin().isEmpty() || !listHorarioGrupo.getHorarios().get(i).getSalon().isEmpty()) {
                Integer horaInicio = Integer.parseInt(listHorarioGrupo.getHorarios().get(i).getHoraInicio().split(":")[0]);
                Integer horaFin = Integer.parseInt(listHorarioGrupo.getHorarios().get(i).getHoraFin().split(":")[0]);
                if (horaInicio < horaFin && listHorarioGrupo.getHorarios().get(i).getSalon().matches("^[A-Z]\\d$")) {
                    HorarioDeGrupo horario = new HorarioDeGrupo();
                    HorarioGrupoIdentity horarioGrupoIdentity = new HorarioGrupoIdentity();
                    switch (i) {
                        case 0:
                            horarioGrupoIdentity.setDia("Lunes");
                            break;
                        case 1:
                            horarioGrupoIdentity.setDia("Martes");
                            break;
                        case 2:
                            horarioGrupoIdentity.setDia("Miercoles");
                            break;
                        case 3:
                            horarioGrupoIdentity.setDia("Jueves");
                            break;
                        case 4:
                            horarioGrupoIdentity.setDia("Viernes");
                            break;
                    }
                    horarioGrupoIdentity.setGrupo(grupo);
                    horario.setHorarioGrupoIdentity(horarioGrupoIdentity);
                    horario.setHoraInicia(listHorarioGrupo.getHorarios().get(i).getHoraInicio());
                    horario.setHoraFinal(listHorarioGrupo.getHorarios().get(i).getHoraFin());
                    horario.setSalon(listHorarioGrupo.getHorarios().get(i).getSalon());
                    horarios.add(horario);
                } else {
                    return null;
                }
            }

        }
        return horarios;
    }

    @Override
    public ListHorarioGrupoWrapper setHorarioExistente(List<HorarioDeGrupo> horariosDeGrupoExistente) {
        ListHorarioGrupoWrapper horariosWrapper = getListHorarioGrupoWrapper();
        horariosDeGrupoExistente.forEach(horarioExistente -> {
            for (int i = 0; i < 5; i++) {
                switch (i) {
                    case 0:
                        if ("Lunes".equals(horarioExistente.getHorarioGrupoIdentity().getDia())) {
                            horariosWrapper.getHorarios().get(i).setHoraInicio(horarioExistente.getHoraInicia());
                            horariosWrapper.getHorarios().get(i).setHoraFin(horarioExistente.getHoraFinal());
                            horariosWrapper.getHorarios().get(i).setSalon(horarioExistente.getSalon());
                        }
                        break;
                    case 1:
                        if ("Martes".equals(horarioExistente.getHorarioGrupoIdentity().getDia())) {
                            horariosWrapper.getHorarios().get(i).setHoraInicio(horarioExistente.getHoraInicia());
                            horariosWrapper.getHorarios().get(i).setHoraFin(horarioExistente.getHoraFinal());
                            horariosWrapper.getHorarios().get(i).setSalon(horarioExistente.getSalon());
                        }
                        break;
                    case 2:
                        if ("Miercoles".equals(horarioExistente.getHorarioGrupoIdentity().getDia())) {
                            horariosWrapper.getHorarios().get(i).setHoraInicio(horarioExistente.getHoraInicia());
                            horariosWrapper.getHorarios().get(i).setHoraFin(horarioExistente.getHoraFinal());
                            horariosWrapper.getHorarios().get(i).setSalon(horarioExistente.getSalon());
                        }
                        break;
                    case 3:
                        if ("Jueves".equals(horarioExistente.getHorarioGrupoIdentity().getDia())) {
                            horariosWrapper.getHorarios().get(i).setHoraInicio(horarioExistente.getHoraInicia());
                            horariosWrapper.getHorarios().get(i).setHoraFin(horarioExistente.getHoraFinal());
                            horariosWrapper.getHorarios().get(i).setSalon(horarioExistente.getSalon());
                        }
                        break;
                    case 4:
                        if ("Viernes".equals(horarioExistente.getHorarioGrupoIdentity().getDia())) {
                            horariosWrapper.getHorarios().get(i).setHoraInicio(horarioExistente.getHoraInicia());
                            horariosWrapper.getHorarios().get(i).setHoraFin(horarioExistente.getHoraFinal());
                            horariosWrapper.getHorarios().get(i).setSalon(horarioExistente.getSalon());
                        }
                        break;
                }
            }
        });
        return horariosWrapper;
    }

    @Override
    public List<HorarioDeGrupo> sortHorarioDeGrupo(List<HorarioDeGrupo> horariosDesordenado) {
        List<HorarioDeGrupo> horariosDeGrupo = Arrays.asList(new HorarioDeGrupo(), new HorarioDeGrupo(), new HorarioDeGrupo(), new HorarioDeGrupo(), new HorarioDeGrupo());
        horariosDesordenado.forEach(horarioDesacomodado -> {
            for (int i = 0; i < horariosDeGrupo.size(); i++) {
                switch (i) {
                    case 0:
                        if ("Lunes".equals(horarioDesacomodado.getHorarioGrupoIdentity().getDia())) {
                            horariosDeGrupo.get(i).setHorarioGrupoIdentity(horarioDesacomodado.getHorarioGrupoIdentity());
                            horariosDeGrupo.get(i).setHoraInicia(horarioDesacomodado.getHoraInicia());
                            horariosDeGrupo.get(i).setHoraFinal(horarioDesacomodado.getHoraFinal());
                            horariosDeGrupo.get(i).setSalon(horarioDesacomodado.getSalon());
                        }
                        break;
                    case 1:
                        if ("Martes".equals(horarioDesacomodado.getHorarioGrupoIdentity().getDia())) {
                            horariosDeGrupo.get(i).setHorarioGrupoIdentity(horarioDesacomodado.getHorarioGrupoIdentity());
                            horariosDeGrupo.get(i).setHoraInicia(horarioDesacomodado.getHoraInicia());
                            horariosDeGrupo.get(i).setHoraFinal(horarioDesacomodado.getHoraFinal());
                            horariosDeGrupo.get(i).setSalon(horarioDesacomodado.getSalon());
                        }
                        break;
                    case 2:
                        if ("Miercoles".equals(horarioDesacomodado.getHorarioGrupoIdentity().getDia())) {
                            horariosDeGrupo.get(i).setHorarioGrupoIdentity(horarioDesacomodado.getHorarioGrupoIdentity());
                            horariosDeGrupo.get(i).setHoraInicia(horarioDesacomodado.getHoraInicia());
                            horariosDeGrupo.get(i).setHoraFinal(horarioDesacomodado.getHoraFinal());
                            horariosDeGrupo.get(i).setSalon(horarioDesacomodado.getSalon());
                        }
                        break;
                    case 3:
                        if ("Jueves".equals(horarioDesacomodado.getHorarioGrupoIdentity().getDia())) {
                            horariosDeGrupo.get(i).setHorarioGrupoIdentity(horarioDesacomodado.getHorarioGrupoIdentity());
                            horariosDeGrupo.get(i).setHoraInicia(horarioDesacomodado.getHoraInicia());
                            horariosDeGrupo.get(i).setHoraFinal(horarioDesacomodado.getHoraFinal());
                            horariosDeGrupo.get(i).setSalon(horarioDesacomodado.getSalon());
                        }
                        break;
                    case 4:
                        if ("Viernes".equals(horarioDesacomodado.getHorarioGrupoIdentity().getDia())) {
                            horariosDeGrupo.get(i).setHorarioGrupoIdentity(horarioDesacomodado.getHorarioGrupoIdentity());
                            horariosDeGrupo.get(i).setHoraInicia(horarioDesacomodado.getHoraInicia());
                            horariosDeGrupo.get(i).setHoraFinal(horarioDesacomodado.getHoraFinal());
                            horariosDeGrupo.get(i).setSalon(horarioDesacomodado.getSalon());
                        }
                        break;
                }
            }
        });
        return horariosDeGrupo;
    }

    @Override
    public ListHorarioGrupoWrapper getListHorarioGrupoWrapper() {
        ListHorarioGrupoWrapper horarios = new ListHorarioGrupoWrapper();
        for (int i = 0; i < 5; i++) {
            horarios.addHorario(new HorarioGrupoWrapper());
        }
        return horarios;
    }

    @Override
    public Optional<HorarioDeGrupo> findOne(HorarioGrupoIdentity horarioGrupoIdentity) {
        return horarioDeGrupoDAO.findById(horarioGrupoIdentity);
    }

    @Override
    public List<HorarioDeGrupo> findByDia(String dia) {
        return horarioDeGrupoDAO.findByDia(dia);
    }

    @Override
    public Iterable<HorarioDeGrupo> findAll() {
        return horarioDeGrupoDAO.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<HorarioDeGrupo> findByGrupo(String codigoGrupo){
        return horarioDeGrupoDAO.findByGrupo(codigoGrupo);
    }

}
