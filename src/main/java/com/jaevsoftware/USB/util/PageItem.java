/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.util;

public class PageItem {

    Integer numeroPagina;
    Boolean paginaActual;

    public PageItem(Integer numeroPagina, Boolean paginaActual) {
        this.numeroPagina = numeroPagina;
        this.paginaActual = paginaActual;
    }

    public Integer getNumeroPagina() {
        return numeroPagina;
    }

    public void setNumeroPagina(Integer numeroPagina) {
        this.numeroPagina = numeroPagina;
    }

    public Boolean getPaginaActual() {
        return paginaActual;
    }

    public void setPaginaActual(Boolean paginaActual) {
        this.paginaActual = paginaActual;
    }

}
