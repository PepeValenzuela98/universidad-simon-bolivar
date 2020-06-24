/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.model.entity.wrapper;

/**
 *
 * @author Pepe Valenzuela
 */
public class KardexWrapper {

    Long kardex;

    Double promedio;

    Integer vecesCursada;

    Integer semestreCurso;

    String numeroDeControlAlumno;

    String codigoMateria;

    String nombreMateria;

    public Long getKardex() {
        return kardex;
    }

    public void setKardex(Long kardex) {
        this.kardex = kardex;
    }

    public Double getPromedio() {
        return promedio;
    }

    public void setPromedio(Double promedio) {
        this.promedio = promedio;
    }

    public Integer getVecesCursada() {
        return vecesCursada;
    }

    public void setVecesCursada(Integer vecesCursada) {
        this.vecesCursada = vecesCursada;
    }

    public Integer getSemestreCurso() {
        return semestreCurso;
    }

    public void setSemestreCurso(Integer semestreCurso) {
        this.semestreCurso = semestreCurso;
    }

    public String getNumeroDeControlAlumno() {
        return numeroDeControlAlumno;
    }

    public void setNumeroDeControlAlumno(String numeroDeControlAlumno) {
        this.numeroDeControlAlumno = numeroDeControlAlumno;
    }

    public String getCodigoMateria() {
        return codigoMateria;
    }

    public void setCodigoMateria(String codigoMateria) {
        this.codigoMateria = codigoMateria;
    }

    public String getNombreMateria() {
        return nombreMateria;
    }

    public void setNombreMateria(String nombreMateria) {
        this.nombreMateria = nombreMateria;
    }

}
