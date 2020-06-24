/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.model.dao;

import com.jaevsoftware.USB.model.entity.HorarioDeGrupo;
import com.jaevsoftware.USB.model.entity.embedded.HorarioGrupoIdentity;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Pepe Valenzuela
 */
public interface IHorarioDeGrupoDAO extends CrudRepository<HorarioDeGrupo, HorarioGrupoIdentity>{
    @Query("select h from HorarioDeGrupo h WHERE h.horarioGrupoIdentity.dia LIKE ?1")
    public List<HorarioDeGrupo> findByDia(String dia);
    
    @Query("select h from HorarioDeGrupo h WHERE h.horarioGrupoIdentity.grupo.codigoGrupo like ?1")
    public List<HorarioDeGrupo> findByGrupo(String codigoGrupo);
}
