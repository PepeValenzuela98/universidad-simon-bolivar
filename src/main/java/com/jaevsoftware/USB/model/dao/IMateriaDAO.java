/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.model.dao;

import com.jaevsoftware.USB.model.entity.Materia;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Pepe Valenzuela
 */
public interface IMateriaDAO extends CrudRepository<Materia, String> {

    public List<Materia> findByNombreContainingAndCodigoMateriaContaining(String nombre,String codigoMateria);

    public Materia findByNombre(String nombre);
    
    public List<Materia> findByCodigoMateriaStartingWith(String codigoCarrera);
}
