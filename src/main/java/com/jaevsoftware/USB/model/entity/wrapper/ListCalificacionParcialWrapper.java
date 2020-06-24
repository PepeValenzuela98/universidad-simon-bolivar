/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.model.entity.wrapper;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Pepe Valenzuela
 */
public class ListCalificacionParcialWrapper {

    public List<CalificacionParcialWrapper> calificaciones;

    public ListCalificacionParcialWrapper() {
        this.calificaciones = new ArrayList<>();
    }

    public ListCalificacionParcialWrapper(List<CalificacionParcialWrapper> calificaciones) {
        this.calificaciones = calificaciones;
    }

    public void addCalificacion(CalificacionParcialWrapper calificacion) {
        calificaciones.add(calificacion);
    }

    public List<CalificacionParcialWrapper> getCalificaciones() {
        return calificaciones;
    }

    public void setCalificaciones(List<CalificacionParcialWrapper> calificaciones) {
        this.calificaciones = calificaciones;
    }

}
