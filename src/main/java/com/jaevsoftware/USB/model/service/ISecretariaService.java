/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.model.service;

import com.jaevsoftware.USB.model.entity.Secretaria;
import java.util.Optional;

/**
 *
 * @author Pepe Valenzuela
 */
public interface ISecretariaService {
    
    public void save(Secretaria secretaria);
    
    public void delete(String numeroDeControl);
    
    public Secretaria generateNewSecretaria();
    
    public Optional<Secretaria> findOne(String numeroDeControl);
    
    public Iterable<Secretaria> findAll();
}
