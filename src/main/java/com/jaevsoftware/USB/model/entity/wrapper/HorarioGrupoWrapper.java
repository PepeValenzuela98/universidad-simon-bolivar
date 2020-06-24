/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.model.entity.wrapper;

import org.springframework.stereotype.Component;

@Component
public class HorarioGrupoWrapper {

    String horaInicio;
    String horaFin;
    String salon;

    public HorarioGrupoWrapper() {
    }
    
    public HorarioGrupoWrapper(String horaInicio, String horaFin, String salon) {
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.salon = salon;
    }
    
    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public String getSalon() {
        return salon;
    }

    public void setSalon(String salon) {
        this.salon = salon;
    }

}
