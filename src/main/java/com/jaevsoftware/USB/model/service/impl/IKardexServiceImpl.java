/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.model.service.impl;

import com.jaevsoftware.USB.model.dao.IKardexDAO;
import com.jaevsoftware.USB.model.dao.IMateriaDAO;
import com.jaevsoftware.USB.model.entity.Kardex;
import com.jaevsoftware.USB.model.entity.wrapper.KardexWrapper;
import com.jaevsoftware.USB.model.service.IKardexService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IKardexServiceImpl implements IKardexService {

    @Autowired
    IKardexDAO kardexDAO;

    @Autowired
    IMateriaDAO materiaDAO;

    @Override
    public void save(Kardex kardex) {
        kardexDAO.save(kardex);
    }

    @Override
    public void delete(Kardex kardex) {
        kardexDAO.delete(kardex);
    }

    @Override
    public List<KardexWrapper> findByNumeroDeControlAlumnoOrderBySemestreCursoAsc(String numeroDeControlAlumno) {
        List<Kardex> kardex = kardexDAO.findByNumeroDeControlAlumnoOrderBySemestreCursoAsc(numeroDeControlAlumno);
        List<KardexWrapper> kardexWrapper = new ArrayList();
        kardex.forEach(k -> {
            KardexWrapper kWrapper = new KardexWrapper();
            kWrapper.setKardex(k.getKardex());
            kWrapper.setPromedio(k.getPromedio());
            kWrapper.setVecesCursada(k.getVecesCursada());
            kWrapper.setSemestreCurso(k.getSemestreCurso());
            kWrapper.setNumeroDeControlAlumno(k.getNumeroDeControlAlumno());
            kWrapper.setCodigoMateria(k.getCodigoMateria());
            kWrapper.setNombreMateria(materiaDAO.findById(k.getCodigoMateria()).orElse(null).getNombre());
            kardexWrapper.add(kWrapper);
        });
        return kardexWrapper;
    }

    @Override
    public Optional<Kardex> findByNumeroDeControlAlumnoAndCodigoMateria(String numeroDeControlAlumno, String codigoMateria) {
        return kardexDAO.findByNumeroDeControlAlumnoAndCodigoMateria(numeroDeControlAlumno, codigoMateria);
    }

    @Override
    public List<KardexWrapper> findByNumeroDeControlAlumnoAndSemestreCurso(String numeroDeControlAlumno, Integer semestre) {
        List<Kardex> kardex = kardexDAO.findByNumeroDeControlAlumnoAndSemestreCurso(numeroDeControlAlumno, semestre);
        List<KardexWrapper> kardexWrapper = new ArrayList();
        kardex.forEach(k -> {
            KardexWrapper kWrapper = new KardexWrapper();
            kWrapper.setKardex(k.getKardex());
            kWrapper.setPromedio(k.getPromedio());
            kWrapper.setVecesCursada(k.getVecesCursada());
            kWrapper.setSemestreCurso(k.getSemestreCurso());
            kWrapper.setNumeroDeControlAlumno(k.getNumeroDeControlAlumno());
            kWrapper.setCodigoMateria(k.getCodigoMateria());
            kWrapper.setNombreMateria(materiaDAO.findById(k.getCodigoMateria()).orElse(null).getNombre());
            kardexWrapper.add(kWrapper);
        });
        return kardexWrapper;
    }

}
