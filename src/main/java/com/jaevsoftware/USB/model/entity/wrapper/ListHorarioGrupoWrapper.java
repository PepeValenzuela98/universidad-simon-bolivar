/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.model.entity.wrapper;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 *
 * @author Pepe Valenzuela
 */
@Component
public class ListHorarioGrupoWrapper {

    private List<HorarioGrupoWrapper> horarios;

    public ListHorarioGrupoWrapper() {
        horarios = new ArrayList();
    }

    public ListHorarioGrupoWrapper(List<HorarioGrupoWrapper> horarios) {
        this.horarios = horarios;
    }

    public void addHorario(HorarioGrupoWrapper horario) {
        this.horarios.add(horario);
    }

    public List<HorarioGrupoWrapper> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<HorarioGrupoWrapper> horarios) {
        this.horarios = horarios;
    }

}
