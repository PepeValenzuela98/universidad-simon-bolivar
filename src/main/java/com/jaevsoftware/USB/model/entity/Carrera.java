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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "carreras", schema = "universidad")
public class Carrera implements Serializable {

    @Id
    @Column(name = "pk_codigo_carrera")
    String codigoCarrera;

    @NotEmpty
    @Column(unique = true)
    String nombre;

    @NotNull
    @Column(name = "semestre")
    Integer semestres;

    @OneToOne()
    @JoinColumn(name = "fk_numcontrol_jefe", referencedColumnName = "pk_numero_control")
    JefeDepartamento jefeDepartamento;

    @OneToOne()
    @JoinColumn(name = "fk_numcontrol_secretaria", referencedColumnName = "pk_numero_control")
    Secretaria secretaria;

    //OJO PARA FUTURAS IMPLEMENTACION DE RELACIONES EN JPA:
    //MappedBy hace referencia al nombre del ATRIBUTO de la clase que requiere su informacion
    @OneToMany(mappedBy = "carrera", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Alumno> alumnos;

    public String getCodigoCarrera() {
        return codigoCarrera;
    }

    public void setCodigoCarrera(String codigoCarrera) {
        this.codigoCarrera = codigoCarrera;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getSemestres() {
        return semestres;
    }

    public void setSemestres(Integer semestres) {
        this.semestres = semestres;
    }

    public JefeDepartamento getJefeDepartamento() {
        return jefeDepartamento;
    }

    public void setJefeDepartamento(JefeDepartamento jefeDepartamento) {
        this.jefeDepartamento = jefeDepartamento;
    }

    public Secretaria getSecretaria() {
        return secretaria;
    }

    public void setSecretaria(Secretaria secretaria) {
        this.secretaria = secretaria;
    }

    public List<Alumno> getAlumnos() {
        return alumnos;
    }

    public void setAlumnos(List<Alumno> alumnos) {
        this.alumnos = alumnos;
    }

    private static final long serialVersionUID = -9061247051004990072L;

}
