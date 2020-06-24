/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.model.dao;

import com.jaevsoftware.USB.model.entity.JefeDepartamento;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Pepe Valenzuela
 */
public interface IJefeDepartamentoDAO extends CrudRepository<JefeDepartamento, String>{
    
    public List<JefeDepartamento> findByNumeroDeControlContaining(String fecha);
}
