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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "grupos", schema = "universidad")
public class Grupo implements Serializable {

    @Id
    @NotEmpty
    @Column(name = "pk_grupo")
    String codigoGrupo;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_codigo_materia", referencedColumnName = "pk_codigo_materia")
    Materia materia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_numcontrol_docente", referencedColumnName = "pk_numero_control")
    Docente docente;

    @OneToMany(mappedBy = "horarioGrupoIdentity.grupo", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<HorarioDeGrupo> horariosDeGrupo;

    @OneToMany(mappedBy = "grupo", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<GrupoAlumno> grupoAlumnos;

    public String getCodigoGrupo() {
        return codigoGrupo;
    }

    public void setCodigoGrupo(String codigoGrupo) {
        this.codigoGrupo = codigoGrupo;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    public List<HorarioDeGrupo> getHorariosDeGrupo() {
        return horariosDeGrupo;
    }

    public void setHorariosDeGrupo(List<HorarioDeGrupo> horariosDeGrupo) {
        this.horariosDeGrupo = horariosDeGrupo;
    }

    public List<GrupoAlumno> getGrupoAlumnos() {
        return grupoAlumnos;
    }

    public void setGrupoAlumnos(List<GrupoAlumno> grupoAlumnos) {
        this.grupoAlumnos = grupoAlumnos;
    }
    
    public String getNumeroGrupo(){
        return codigoGrupo.substring(6);
    }

    private static final long serialVersionUID = 3863040382401952279L;

}
