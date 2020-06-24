/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.model.service;

import com.jaevsoftware.USB.model.entity.Docente;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author Pepe Valenzuela
 */
public interface IDocenteService {
    public void save(Docente docente);
    
    public void delete(String numeroDeControl);
    
    public Docente generateNewDocente();
    
    public Optional<Docente> findOne(String numeroDeControl);
    
    public List<Docente> findByNumeroDeControlContaining(String numeroDeControl);
    
    public Page<Docente> findAll(Pageable pageable);
}
