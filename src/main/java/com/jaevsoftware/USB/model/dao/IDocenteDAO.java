/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.model.dao;

import com.jaevsoftware.USB.model.entity.Docente;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 *
 * @author Pepe Valenzuela
 */
public interface IDocenteDAO extends PagingAndSortingRepository<Docente, String> {

    public Page<Docente> findAllByOrderByNombreAsc(Pageable pageable);

    public List<Docente> findByNumeroDeControlContaining(String fecha);
}
