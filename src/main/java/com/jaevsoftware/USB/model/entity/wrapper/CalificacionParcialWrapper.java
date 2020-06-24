package com.jaevsoftware.USB.model.entity.wrapper;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CalificacionParcialWrapper {

    String numeroDeControlAlumno;

    String nombreAlumno;

    String nombreMateria;

    List<CalificacionParcialData> calificacionesPorUnidad;

    public String getNombreMateria() {
        return nombreMateria;
    }

    public void setNombreMateria(String nombreMateria) {
        this.nombreMateria = nombreMateria;
    }

    public String getNumeroDeControlAlumno() {
        return numeroDeControlAlumno;
    }

    public void setNumeroDeControlAlumno(String numeroDeControlAlumno) {
        this.numeroDeControlAlumno = numeroDeControlAlumno;
    }

    public String getNombreAlumno() {
        return nombreAlumno;
    }

    public void setNombreAlumno(String nombreAlumno) {
        this.nombreAlumno = nombreAlumno;
    }

    public List<CalificacionParcialData> getCalificacionesPorUnidad() {
        return calificacionesPorUnidad;
    }

    public void setCalificacionesPorUnidad(List<CalificacionParcialData> calificaciones) {
        this.calificacionesPorUnidad = calificaciones;
    }

}
