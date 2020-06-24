/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.model.dao;

import com.jaevsoftware.USB.model.entity.Kardex;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Pepe Valenzuela
 */
public interface IKardexDAO extends CrudRepository<Kardex, Long> {

    public List<Kardex> findByNumeroDeControlAlumnoOrderBySemestreCursoAsc(String numeroDeControlAlumno);

    public Optional<Kardex> findByNumeroDeControlAlumnoAndCodigoMateria(String numeroDeControlAlumno, String codigoMateria);

    public List<Kardex> findByNumeroDeControlAlumnoAndSemestreCurso(String numeroDeControlAlumno, Integer semestre);
}
