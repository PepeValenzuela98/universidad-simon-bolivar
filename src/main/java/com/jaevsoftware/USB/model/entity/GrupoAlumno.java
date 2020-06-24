/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.model.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "grupo_alumnos", schema = "universidad")
public class GrupoAlumno implements Serializable {

    @Id
    @Column(name="pk_codigo_grupo_alumno")
    String codigoGrupoAlumno;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_numcontrol_alumno", referencedColumnName = "pk_numero_control")
    Alumno alumno;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_grupo", referencedColumnName = "pk_grupo")
    Grupo grupo;

    @NotNull
    @Column(name = "promedio_calificacion")
    Double promedio;

    @OneToMany(mappedBy = "calificacionParcialIdentity.grupoAlumno", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<CalificacionParcial> calificacionesParciales;

    public String getCodigoGrupoAlumno() {
        return codigoGrupoAlumno;
    }

    public void setCodigoGrupoAlumno(String codigoGrupoAlumno) {
        this.codigoGrupoAlumno = codigoGrupoAlumno;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public Double getPromedio() {
        return promedio;
    }

    public void setPromedio(Double promedio) {
        this.promedio = promedio;
    }

    public List<CalificacionParcial> getCalificacionesParciales() {
        return calificacionesParciales;
    }

    public void setCalificacionesParciales(List<CalificacionParcial> calificacionesParciales) {
        this.calificacionesParciales = calificacionesParciales;
    }

    private static final long serialVersionUID = 5299848982629448761L;

}
