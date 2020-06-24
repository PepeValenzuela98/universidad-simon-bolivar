/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.model.entity.wrapper;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 *
 * @author Pepe Valenzuela
 */
public class Usuario extends User {

    String nombreCompleto;

    String codigoCarrera;

    public Usuario(String username, String password, String nombreCompleto, String codigoCarrera, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.nombreCompleto = nombreCompleto;
        this.codigoCarrera = codigoCarrera;

    }

    @Override
    public String toString() {
        return super.toString(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int hashCode() {
        return super.hashCode(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean equals(Object rhs) {
        return super.equals(rhs); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return super.isCredentialsNonExpired(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isAccountNonLocked() {
        return super.isAccountNonLocked(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isAccountNonExpired() {
        return super.isAccountNonExpired(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isEnabled() {
        return super.isEnabled(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getUsername() {
        return super.getUsername(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getPassword() {
        return super.getPassword(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return super.getAuthorities(); //To change body of generated methods, choose Tools | Templates.
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getCodigoCarrera() {
        return codigoCarrera;
    }

    public void setCodigoCarrera(String codigoCarrera) {
        this.codigoCarrera = codigoCarrera;
    }

}
