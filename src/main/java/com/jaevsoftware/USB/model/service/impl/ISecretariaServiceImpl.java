/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.model.service.impl;

import com.jaevsoftware.USB.model.dao.ISecretariaDAO;
import com.jaevsoftware.USB.model.entity.Secretaria;
import com.jaevsoftware.USB.model.service.ISecretariaService;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ISecretariaServiceImpl implements ISecretariaService {

    @Autowired
    ISecretariaDAO secretariaDAO;

    @Override
    public void save(Secretaria secretaria) {
        secretariaDAO.save(secretaria);
    }

    @Override
    public void delete(String numeroDeControl) {
        secretariaDAO.deleteById(numeroDeControl);
    }

    @Override
    @Transactional(readOnly = true)
    public Secretaria generateNewSecretaria() {
        Secretaria nuevaSecretaria = new Secretaria();
        nuevaSecretaria.setNumeroDeControl(generateNumControl());
        nuevaSecretaria.setContraseña(generatePassword());
        return nuevaSecretaria;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Secretaria> findOne(String numeroDeControl) {
        return secretariaDAO.findById(numeroDeControl);
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<Secretaria> findAll() {
        return secretariaDAO.findAll();
    }

    private String generatePassword() {
        RandomStringGenerator passwordGenerator = new RandomStringGenerator.Builder().withinRange('1', '9').build();
        return passwordGenerator.generate(3);
    }

    private String generateNumControl() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd");
        DecimalFormat decimalFormat = new DecimalFormat("0000");
        Date date = new Date(System.currentTimeMillis());
        List<Secretaria> secretariasInsertadasHoy = secretariaDAO.findByNumeroDeControlContaining(formatter.format(date));
        Integer numeroDeSecretaria = 1;
        if (!secretariasInsertadasHoy.isEmpty()) {
            Integer numeroDeSecretarias = secretariasInsertadasHoy.size() - 1;
            numeroDeSecretaria = Integer.parseInt(secretariasInsertadasHoy.get(numeroDeSecretarias).getNumeroDeControl().substring(0, 4)) + 1;
        }
        return decimalFormat.format(numeroDeSecretaria).concat(formatter.format(date)).concat("SE");
    }
}
