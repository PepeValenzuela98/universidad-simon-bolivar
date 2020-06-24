/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.model.entity;

import com.jaevsoftware.USB.model.entity.embedded.CalificacionParcialIdentity;
import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "calificaciones_parciales", schema = "universidad")
public class CalificacionParcial implements Serializable {

    @EmbeddedId
    CalificacionParcialIdentity calificacionParcialIdentity;

    Integer calificacion;

    public CalificacionParcialIdentity getCalificacionParcialIdentity() {
        return calificacionParcialIdentity;
    }

    public void setCalificacionParcialIdentity(CalificacionParcialIdentity calificacionParcialIdentity) {
        this.calificacionParcialIdentity = calificacionParcialIdentity;
    }

    public Integer getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Integer calificacion) {
        this.calificacion = calificacion;
    }

    private static final long serialVersionUID = 1333777207516509013L;

}
