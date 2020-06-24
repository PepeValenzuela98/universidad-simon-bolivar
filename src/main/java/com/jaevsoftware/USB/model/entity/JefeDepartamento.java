/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.model.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "jefes_departamento", schema = "universidad")
public class JefeDepartamento implements Serializable {
      
    @Id
    @Column(name = "pk_numero_control")
    String numeroDeControl;
    
    @NotEmpty
    String nombre;
    
    @NotEmpty
    String apellido;
    
    @NotEmpty
    String telefono;
    
    @Column(name="correo_electronico")
    @Email
    @NotEmpty
    String correoElectronico;
    
    @NotEmpty
    String contraseña;
     
    
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

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    } 
    
    private static final long serialVersionUID = -5280932223472764780L;
}
