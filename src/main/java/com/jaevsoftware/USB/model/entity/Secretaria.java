/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.model.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 *
 * @author Pepe Valenzuela
 */
@Entity
@Table(name = "secretarias", schema = "universidad")
public class Secretaria implements Serializable {

    @Id
    @Column(name = "pk_numero_control")
    @Pattern(regexp = "^\\d{4}\\d{6}SE$", message = "Numero de control no valido")
    @NotEmpty()
    String numeroDeControl;

    @NotEmpty()
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Nombre no valido")
    String nombre;

    @NotEmpty
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Apellido no valido")
    String apellido;

    @NotEmpty
    @Pattern(regexp = "^\\d{3}-\\d{3}-\\d{4}$", message = "Numero de telefono no valido")
    String telefono;

    @Column(name = "correo_electronico")
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

    public String getContrasena(){
        return contraseña;
    }
    private static final long serialVersionUID = 468813789081545482L;
}
