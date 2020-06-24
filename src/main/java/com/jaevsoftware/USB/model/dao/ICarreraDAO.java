/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.model.dao;

import com.jaevsoftware.USB.model.entity.Carrera;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Pepe Valenzuela
 */
public interface ICarreraDAO extends CrudRepository<Carrera, String>{

    @Query("select c from Carrera c join c.jefeDepartamento where c.secretaria.numeroDeControl like ?1")       
    public Carrera findBySecretaria(String numeroControlSecretaria);
    
    @Query("select c from Carrera c join c.secretaria where c.jefeDepartamento.numeroDeControl like ?1")       
    public Carrera findByJefeDepartamento(String numeroControlJefeDepartamento);
    
    public Iterable<Carrera> findAllByOrderByNombreAsc();
}
