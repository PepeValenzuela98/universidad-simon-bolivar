/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.model.dao;

import com.jaevsoftware.USB.model.entity.Grupo;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 *
 * @author Pepe Valenzuela
 */
public interface IGrupoDAO extends PagingAndSortingRepository<Grupo, String> {

    @Query("select g from Grupo g where g.codigoGrupo like %?1%")
    public Page<Grupo> findAllByCarrera(Pageable pageable, String codigoCarrera);

    public List<Grupo> findByCodigoGrupo(String codigoGrupo);

    @Query("select g from Grupo g join g.materia where g.materia.codigoMateria like ?1")
    public List<Grupo> findBycodigoMateria(String codigoMateria);

    @Query("select g from Grupo g join g.docente d where d.numeroDeControl like ?1")
    public List<Grupo> findByDocente(String numeroDeControlDocente);

}
