/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.model.entity.embedded;

import com.jaevsoftware.USB.model.entity.GrupoAlumno;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class CalificacionParcialIdentity implements Serializable {

    Integer unidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_codigo_grupo_alumno", referencedColumnName = "pk_codigo_grupo_alumno")
    GrupoAlumno grupoAlumno;

    public Integer getUnidad() {
        return unidad;
    }

    public void setUnidad(Integer unidad) {
        this.unidad = unidad;
    }

    public GrupoAlumno getGrupoAlumno() {
        return grupoAlumno;
    }

    public void setGrupoAlumno(GrupoAlumno grupoAlumno) {
        this.grupoAlumno = grupoAlumno;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CalificacionParcialIdentity other = (CalificacionParcialIdentity) obj;
        if (!Objects.equals(this.unidad, other.unidad)) {
            return false;
        }
        if (!Objects.equals(this.grupoAlumno, other.grupoAlumno)) {
            return false;
        }
        return true;
    }

    private static final long serialVersionUID = 8379703710057437132L;

}
