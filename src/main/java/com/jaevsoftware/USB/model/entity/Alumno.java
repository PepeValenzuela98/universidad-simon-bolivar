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
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "alumnos", schema = "universidad")
public class Alumno implements Serializable {

    @Id
    @Column(name = "pk_numero_control")
    @Pattern(regexp = "^\\d{4}\\d{6}ES$",message = "Numero de control no valido")
    @NotEmpty
    String numeroDeControl;

    @NotEmpty
    @Pattern(regexp = "^[A-Za-z\\s]+$",message = "Nombre no valido")
    String nombre;

    @NotEmpty
    @Pattern(regexp = "^[A-Za-z\\s]+$",message = "Apellido no valido")
    String apellido;

    @NotEmpty
    @Pattern(regexp = "^\\d{3}-\\d{3}-\\d{4}$",message = "Telefono no valido")
    String telefono;

    @Column(name = "correo_electronico")
    @Email
    @NotEmpty
    String correoElectronico;

    @Column(name = "nombre_emergencia")
    @NotEmpty
    @Pattern(regexp = "^[A-Za-z\\s]+$",message = "Nombre no valido")
    String nombreEmergencia;

    @Column(name = "apellido_emergencia")
    @NotEmpty
    @Pattern(regexp = "^[A-Za-z\\s]+$",message = "Apellido no valido")
    String apellidoEmergencia;

    @Column(name = "telefono_emergencia")
    @NotEmpty
    @Pattern(regexp = "^\\d{3}-\\d{3}-\\d{4}$",message = "Telefono no valido")
    String telefonoEmergencia;

    @NotNull
    Integer semestre;

    @NotEmpty
    String contraseña;

    //OJO PARA FUTURAS IMPLEMENTACION DE RELACIONES EN JPA:
    //En dado caso que el nombre del atributo no sea el mismo en la BD usar JoinColumn para especificarlo y opcionalmente poner el nombre del
    //atributo al cual hace referencia con referencedColumnName
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_carrera", referencedColumnName = "pk_codigo_carrera")
    Carrera carrera;

    @OneToMany(mappedBy = "alumno", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<GrupoAlumno> grupos;

    @OneToMany(mappedBy = "alumno", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Justificante> justificantes;
    
    public String getNumeroDeControl() {
        return numeroDeControl;
    }

    public void setNumeroDeControl(String numeroDeControl) {
        this.numeroDeControl = numeroDeControl;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getNombreEmergencia() {
        return nombreEmergencia;
    }

    public void setNombreEmergencia(String nombreEmergencia) {
        this.nombreEmergencia = nombreEmergencia;
    }

    public String getApellidoEmergencia() {
        return apellidoEmergencia;
    }

    public void setApellidoEmergencia(String apellidoEmergencia) {
        this.apellidoEmergencia = apellidoEmergencia;
    }

    public String getTelefonoEmergencia() {
        return telefonoEmergencia;
    }

    public void setTelefonoEmergencia(String telefonoEmergencia) {
        this.telefonoEmergencia = telefonoEmergencia;
    }

    public Integer getSemestre() {
        return semestre;
    }

    public void setSemestre(Integer semestre) {
        this.semestre = semestre;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public Carrera getCarrera() {
        return carrera;
    }

    public void setCarrera(Carrera carrera) {
        this.carrera = carrera;
    }

    public List<GrupoAlumno> getHorarios() {
        return grupos;
    }

    public void setHorarios(List<GrupoAlumno> grupos) {
        this.grupos = grupos;
    }

    public List<Justificante> getJustificantes() {
        return justificantes;
    }

    public void setJustificantes(List<Justificante> justificantes) {
        this.justificantes = justificantes;
    }

    public List<GrupoAlumno> getGrupos() {
        return grupos;
    }

    public void setGrupos(List<GrupoAlumno> grupos) {
        this.grupos = grupos;
    } 
    
    public String getContrasena(){
        return contraseña;
    }

    private static final long serialVersionUID = -8326479140542391604L;
}
