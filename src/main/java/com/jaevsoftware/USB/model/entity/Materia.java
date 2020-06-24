/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "materias", schema = "universidad")
public class Materia implements Serializable {

    @Id
    @Column(name = "pk_codigo_materia")
    String codigoMateria;

    @NotEmpty
    String nombre;

    @NotNull
    Integer numeroUnidades;

    @NotNull
    Integer semestre;

    @OneToMany(mappedBy = "materia", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    List<Grupo> grupos;

    public String getCodigoMateria() {
        return codigoMateria;
    }

    public void setCodigoMateria(String codigoMateria) {
        this.codigoMateria = codigoMateria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getNumeroUnidades() {
        return numeroUnidades;
    }

    public void setNumeroUnidades(Integer numeroUnidades) {
        this.numeroUnidades = numeroUnidades;
    }

    public Integer getSemestre() {
        return semestre;
    }

    public void setSemestre(Integer semestre) {
        this.semestre = semestre;
    }

    public List<Grupo> getGrupos() {
        return grupos;
    }

    public void setGrupos(List<Grupo> grupos) {
        this.grupos = grupos;
    }

    private static final long serialVersionUID = -8338646335939799192L;

}
