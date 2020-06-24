/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.model.dao;

import com.jaevsoftware.USB.model.entity.Secretaria;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Pepe Valenzuela
 */
public interface ISecretariaDAO extends CrudRepository<Secretaria, String> {

    public List<Secretaria> findByNumeroDeControlContaining(String fecha);
    
    
}
