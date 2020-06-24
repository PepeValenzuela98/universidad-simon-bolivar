/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.util;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;

/**
 *
 * @author Pepe Valenzuela
 * @param <T>
 */
public class PageRender<T> {

    private String url;
    private Page<T> page;

    private Integer totalPaginas;

    private Integer numElementosPorPaginas;

    private Integer paginaActual;

    private List<PageItem> paginas;

    public PageRender(String url, Page<T> page) {
        this.url = url;
        this.page = page;
        paginas = new ArrayList<>();

        totalPaginas = page.getTotalPages();
        numElementosPorPaginas = page.getSize();
        paginaActual = page.getNumber() + 1;

        Integer primeraPagina;
        Integer ultimaPagina;
        if (totalPaginas <= numElementosPorPaginas) {
            primeraPagina = 1;
            ultimaPagina = totalPaginas;
        } else {
            if (paginaActual <= numElementosPorPaginas / 2) {
                primeraPagina = 1;
                ultimaPagina = numElementosPorPaginas;
            } else if (paginaActual >= totalPaginas - numElementosPorPaginas / 2) {
                primeraPagina = totalPaginas - numElementosPorPaginas + 1;
                ultimaPagina = numElementosPorPaginas;
            } else {
                primeraPagina = paginaActual - numElementosPorPaginas / 2;
                ultimaPagina = numElementosPorPaginas;
            }
        }

        for (int i = 0; i < ultimaPagina; i++) {
            paginas.add(new PageItem(primeraPagina + i, paginaActual == primeraPagina + i));
        }
    }

    public String getUrl() {
        return url;
    }

    public Page<T> getPage() {
        return page;
    }

    public Integer getTotalPaginas() {
        return totalPaginas;
    }

    public Integer getNumElementosPorPaginas() {
        return numElementosPorPaginas;
    }

    public Integer getPaginaActual() {
        return paginaActual;
    }

    public List<PageItem> getPaginas() {
        return paginas;
    }

    public boolean isFirst() {
        return page.isFirst();
    }

    public boolean isLast() {
        return page.isLast();
    }

    public boolean isHasNext() {
        return page.hasNext();
    }

    public boolean isHasPrevious() {
        return page.hasPrevious();
    }

}
