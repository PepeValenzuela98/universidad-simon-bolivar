/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.model.service.impl;

import com.jaevsoftware.USB.model.dao.IDocenteDAO;
import com.jaevsoftware.USB.model.entity.Alumno;
import com.jaevsoftware.USB.model.entity.Docente;
import com.jaevsoftware.USB.model.service.IDocenteService;
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

/**
 *
 * @author Pepe Valenzuela
 */
@Service
public class IDocenteServiceImpl implements IDocenteService {

    @Autowired
    IDocenteDAO docenteDAO;

    @Override
    public void save(Docente docente) {
        docenteDAO.save(docente);
    }

    @Override
    public void delete(String numeroDeControl) {
        docenteDAO.deleteById(numeroDeControl);
    }

    @Override
    public Docente generateNewDocente() {
        Docente docente = new Docente();
        docente.setNumeroDeControl(generateNumControl());
        docente.setContraseña(generatePassword());
        return docente;
    }

    @Override
    public Optional<Docente> findOne(String numeroDeControl) {
        return docenteDAO.findById(numeroDeControl);
    }

    @Override
    public List<Docente> findByNumeroDeControlContaining(String numeroDeControl) {
        return docenteDAO.findByNumeroDeControlContaining(numeroDeControl);
    }

    @Override
    public Page<Docente> findAll(Pageable pageable) {
        return docenteDAO.findAll(pageable);
    }

    private String generatePassword() {
        RandomStringGenerator passwordGenerator = new RandomStringGenerator.Builder().withinRange('1', '9').build();
        return passwordGenerator.generate(3);
    }

    private String generateNumControl() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd");
        DecimalFormat decimalFormat = new DecimalFormat("0000");
        Date date = new Date(System.currentTimeMillis());
        List<Docente> docentesInsertadosHoy = docenteDAO.findByNumeroDeControlContaining(formatter.format(date));
        Integer numeroDeDocentes = 1;
        if (!docentesInsertadosHoy.isEmpty()) {
            Integer numerosDeDocentes = docentesInsertadosHoy.size() - 1;
            numeroDeDocentes = Integer.parseInt(docentesInsertadosHoy.get(numerosDeDocentes).getNumeroDeControl().substring(0, 4)) + 1;
        }
        return decimalFormat.format(numeroDeDocentes).concat(formatter.format(date)).concat("DO");
    }
}
