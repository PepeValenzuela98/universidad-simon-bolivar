/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.model.entity;

import com.jaevsoftware.USB.model.entity.embedded.HorarioGrupoIdentity;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "horarios_de_grupo", schema = "universidad")
public class HorarioDeGrupo implements Serializable {

    @EmbeddedId
    HorarioGrupoIdentity horarioGrupoIdentity;

    @Column(name = "hora_inicio")
    @NotEmpty
    String horaInicia;

    @Column(name = "hora_final")
    @NotEmpty
    String horaFinal;

    @Column(name = "salon")
    @NotEmpty
    String salon;

    public HorarioGrupoIdentity getHorarioGrupoIdentity() {
        return horarioGrupoIdentity;
    }

    public void setHorarioGrupoIdentity(HorarioGrupoIdentity horarioGrupoIdentity) {
        this.horarioGrupoIdentity = horarioGrupoIdentity;
    }

    public String getHoraInicia() {
        return horaInicia;
    }

    public void setHoraInicia(String horaInicia) {
        this.horaInicia = horaInicia;
    }

    public String getHoraFinal() {
        return horaFinal;
    }

    public void setHoraFinal(String horaFinal) {
        this.horaFinal = horaFinal;
    }

    public String getSalon() {
        return salon;
    }

    public void setSalon(String salon) {
        this.salon = salon;
    }

    private static final long serialVersionUID = -7270487894795551394L;

}
