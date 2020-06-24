/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.model.service;

import com.jaevsoftware.USB.model.entity.Grupo;
import com.jaevsoftware.USB.model.entity.HorarioDeGrupo;
import com.jaevsoftware.USB.model.entity.embedded.HorarioGrupoIdentity;
import com.jaevsoftware.USB.model.entity.wrapper.ListHorarioGrupoWrapper;
import java.util.List;
import java.util.Optional;

public interface IHorarioDeGrupoService {

    public void save(HorarioDeGrupo horarioDeGrupo);

    public void delete(HorarioGrupoIdentity horarioGrupoIdentity);

    public Boolean isRepeatHorarioDeGrupo(List<HorarioDeGrupo> horariosDeGrupo, String codigoGrupo);

    public List<HorarioDeGrupo> makeHorarioDeGrupo(Grupo grupo, ListHorarioGrupoWrapper listHorarioGrupo);

    public ListHorarioGrupoWrapper setHorarioExistente(List<HorarioDeGrupo> horariosDeGrupoExistente);

    public List<HorarioDeGrupo> sortHorarioDeGrupo(List<HorarioDeGrupo> horariosDesordenado);

    public ListHorarioGrupoWrapper getListHorarioGrupoWrapper();

    public Optional<HorarioDeGrupo> findOne(HorarioGrupoIdentity horarioGrupoIdentity);

    public List<HorarioDeGrupo> findByDia(String dia);

    public Iterable<HorarioDeGrupo> findAll();
    
    public List<HorarioDeGrupo> findByGrupo(String codigoGrupo);
}
