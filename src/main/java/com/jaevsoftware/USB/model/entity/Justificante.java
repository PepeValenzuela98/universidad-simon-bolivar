/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.model.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "justificantes", schema = "universidad")
public class Justificante implements Serializable {

    @Id
    @Column(name = "pk_codigo_justificante")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer codigoJustificante;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "fk_numcontrol_alumno")
    Alumno alumno;

    @NotNull
    @Column(name = "fecha_inicio_valida")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date fechaInicioValida;

    @NotNull
    @Column(name = "fecha_fin_valida")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date fechaFinValida;

    @NotNull
    @Column(name = "motivo")
    String motivo;

    public Integer getCodigoJustificante() {
        return codigoJustificante;
    }

    public void setCodigoJustificante(Integer codigoJustificante) {
        this.codigoJustificante = codigoJustificante;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public Date getFechaInicioValida() {
        return fechaInicioValida;
    }

    public void setFechaInicioValida(Date fechaInicioValida) {
        this.fechaInicioValida = fechaInicioValida;
    }

    public Date getFechaFinValida() {
        return fechaFinValida;
    }

    public void setFechaFinValida(Date fechaFinValida) {
        this.fechaFinValida = fechaFinValida;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    private static final long serialVersionUID = -4801761591259590697L;

}
