/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.model.entity.embedded;

import com.jaevsoftware.USB.model.entity.Grupo;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;

@Embeddable
public class HorarioGrupoIdentity implements Serializable {

    @NotEmpty
    @Column(name = "pk_dia")
    String dia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_grupo", referencedColumnName = "pk_grupo")
    Grupo grupo;

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.dia);
        hash = 37 * hash + Objects.hashCode(this.grupo);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final HorarioGrupoIdentity other = (HorarioGrupoIdentity) obj;
        if (!Objects.equals(this.dia, other.dia)) {
            return false;
        }
        if (!Objects.equals(this.grupo, other.grupo)) {
            return false;
        }
        return true;
    }
    
    

    private static final long serialVersionUID = -8215247725329472567L;

}
