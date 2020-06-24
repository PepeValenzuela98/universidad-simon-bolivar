/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "kardex", schema = "universidad")
public class Kardex implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "pk_kardex")
    Long kardex;

    @Column(name = "promedio_calificacion")
    Double promedio;

    @Column(name = "veces_cursada")
    Integer vecesCursada;

    @Column(name = "semestre_curso")
    Integer semestreCurso;

    @Column(name = "numcontrol_alumno")
    String numeroDeControlAlumno;

    @Column(name = "codigo_materia")
    String codigoMateria;

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

    private static final long serialVersionUID = 6349208181595384132L;

}
