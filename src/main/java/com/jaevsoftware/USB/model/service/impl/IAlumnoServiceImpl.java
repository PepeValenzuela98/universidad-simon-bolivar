/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.model.service.impl;

import com.jaevsoftware.USB.model.dao.IAlumnoDAO;
import com.jaevsoftware.USB.model.dao.ICarreraDAO;
import com.jaevsoftware.USB.model.entity.Alumno;
import com.jaevsoftware.USB.model.service.IAlumnoService;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Pepe Valenzuela
 */
@Service
public class IAlumnoServiceImpl implements IAlumnoService {

    @Autowired
    IAlumnoDAO alumnoDAO;

    @Autowired
    ICarreraDAO carreraDAO;

    @Transactional
    @Override
    public void save(Alumno alumno) {
        alumnoDAO.save(alumno);
    }

    @Transactional
    @Override
    public void delete(String numeroDeControl) {
        alumnoDAO.deleteById(numeroDeControl);
    }

    @Override
    @Transactional(readOnly = true)
    public Alumno generateNewAlumno(String codigoCarra) {
        Alumno nuevoAlumno = new Alumno();
        nuevoAlumno.setNumeroDeControl(generateNumControl());
        nuevoAlumno.setContraseña(generatePassword());
        nuevoAlumno.setSemestre(1);
        nuevoAlumno.setCarrera(carreraDAO.findById(codigoCarra).orElse(null));
        return nuevoAlumno;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Alumno> findOne(String numeroDeControl) {
        return alumnoDAO.findById(numeroDeControl);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Alumno> findByNumeroDeControlContaining(String numeroDeControl,String codigoCarrera) {
        return alumnoDAO.findByNumeroDeControlAndCarreraContaining(numeroDeControl,codigoCarrera);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Alumno> findAll(Pageable pageable,String codigoCarrera) {
        return alumnoDAO.findAllByOrderBySemestreAsc(pageable,codigoCarrera);
    }

    private String generatePassword() {
        RandomStringGenerator passwordGenerator = new RandomStringGenerator.Builder().withinRange('1', '9').build();
        return passwordGenerator.generate(3);
    }

    private String generateNumControl() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd");
        DecimalFormat decimalFormat = new DecimalFormat("0000");
        Date date = new Date(System.currentTimeMillis());
        List<Alumno> alumnosInsertadosHoy = alumnoDAO.findByNumeroDeControl(formatter.format(date));
        Integer numeroDeAlumno = 1;
        if (!alumnosInsertadosHoy.isEmpty()) {
            Integer numerosDeAlumno = alumnosInsertadosHoy.size() - 1;
            numeroDeAlumno = Integer.parseInt(alumnosInsertadosHoy.get(numerosDeAlumno).getNumeroDeControl().substring(0, 4)) + 1;
        }
        return decimalFormat.format(numeroDeAlumno).concat(formatter.format(date)).concat("ES");
    }

    @Override
    public List<Alumno> findIfExistsInGrupoAlumno(String codigoCarrera) {
        return alumnoDAO.findIfExistsInGrupoAlumno(codigoCarrera);
    }
    
    
}
