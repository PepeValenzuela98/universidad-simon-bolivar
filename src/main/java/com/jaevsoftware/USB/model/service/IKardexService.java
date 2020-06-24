/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.model.service;

import com.jaevsoftware.USB.model.entity.Kardex;
import com.jaevsoftware.USB.model.entity.wrapper.KardexWrapper;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Pepe Valenzuela
 */
public interface IKardexService {

    public void save(Kardex kardex);

    public void delete(Kardex kardex);

    public List<KardexWrapper> findByNumeroDeControlAlumnoOrderBySemestreCursoAsc(String numeroDeControlAlumno);

    public Optional<Kardex> findByNumeroDeControlAlumnoAndCodigoMateria(String numeroDeControlAlumno, String codigoMateria);

    public List<KardexWrapper> findByNumeroDeControlAlumnoAndSemestreCurso(String numeroDeControlAlumno, Integer semestre);

}
