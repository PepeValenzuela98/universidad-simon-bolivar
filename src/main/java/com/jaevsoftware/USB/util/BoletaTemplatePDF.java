/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.util;

import com.jaevsoftware.USB.model.entity.Alumno;
import com.jaevsoftware.USB.model.entity.wrapper.CalificacionParcialData;
import com.jaevsoftware.USB.model.entity.wrapper.ListCalificacionParcialWrapper;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BoletaTemplatePDF {

    Alumno alumno;

    Integer unidadesMaximo;

    ListCalificacionParcialWrapper listCalificacionesWrapper;

    Document document;

    public void generateBoleta() {
        try {
            document.add(header());

            PdfPTable content = new PdfPTable(unidadesMaximo + 2);
            content.setWidthPercentage(100);

            Paragraph nombreAlumno = new Paragraph("Nombre del alumno:" + alumno.getNombre() + " " + alumno.getApellido());
            nombreAlumno.setSpacingAfter(10);
            document.add(nombreAlumno);
            Paragraph semestre = new Paragraph("Semestre:" + alumno.getSemestre() + "°");
            semestre.setSpacingAfter(10);
            document.add(semestre);

            PdfPCell cellHeaderMateria = new PdfPCell();
            cellHeaderMateria.setBackgroundColor(new Color(52, 58, 64));

            cellHeaderMateria.setHorizontalAlignment(Element.ALIGN_CENTER);
            Paragraph materiaHeader = new Paragraph("Materias");
            materiaHeader.getFont().setColor(Color.WHITE);
            materiaHeader.getFont().setStyle("bold");
            materiaHeader.setAlignment(Element.ALIGN_CENTER);

            cellHeaderMateria.addElement(materiaHeader);

            content.addCell(cellHeaderMateria);

            for (int i = 1; i <= unidadesMaximo + 1; i++) {

                PdfPCell cellHeader = new PdfPCell();
                cellHeader.setBackgroundColor(new Color(52, 58, 64));

                cellHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
                Paragraph unidadHeader = i == unidadesMaximo + 1 ? new Paragraph("Calificacion") : new Paragraph("Unidad " + i);
                unidadHeader.getFont().setColor(Color.WHITE);
                unidadHeader.getFont().setStyle("bold");
                unidadHeader.setAlignment(Element.ALIGN_CENTER);

                cellHeader.addElement(unidadHeader);

                content.addCell(cellHeader);
            }

            List<Integer> promediosMaterias = new ArrayList();

            listCalificacionesWrapper.getCalificaciones().forEach(calificacionWrapper -> {
                List<PdfPCell> celdas = new ArrayList();

                PdfPCell cellMateria = new PdfPCell();
                cellMateria.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellMateria.setFixedHeight(50);
                Paragraph nombreMateria = new Paragraph(calificacionWrapper.getNombreMateria());
                nombreMateria.getFont().setStyle("bold");
                nombreMateria.setAlignment(Element.ALIGN_CENTER);
                nombreMateria.getFont().setSize(10);
                cellMateria.addElement(nombreMateria);
                celdas.add(cellMateria);
                Integer promedioMateria = 0;

                for (CalificacionParcialData calificacionPorUnidad : calificacionWrapper.getCalificacionesPorUnidad()) {
                    if (calificacionPorUnidad.getCalificacion() >= 70) {
                        PdfPCell cellCalificacion = new PdfPCell();
                        cellCalificacion.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cellCalificacion.setFixedHeight(50);
                        Paragraph calificacionParcial = new Paragraph(calificacionPorUnidad.getCalificacion().toString());
                        calificacionParcial.getFont().setStyle("bold");
                        calificacionParcial.setAlignment(Element.ALIGN_CENTER);
                        cellCalificacion.addElement(calificacionParcial);

                        promedioMateria += calificacionPorUnidad.getCalificacion();

                        celdas.add(cellCalificacion);
                    } else {
                        break;
                    }

                }
                if (calificacionWrapper.getCalificacionesPorUnidad().size() < content.getNumberOfColumns() - 2) {
                    for (int i = calificacionWrapper.getCalificacionesPorUnidad().size() + 1; i <= content.getNumberOfColumns() - 2; i++) {
                        PdfPCell emptyCell = new PdfPCell(new Phrase());
                        celdas.add(emptyCell);
                    }
                }
                PdfPCell cellPromedioMateria = new PdfPCell();
                cellPromedioMateria.setHorizontalAlignment(Element.ALIGN_CENTER);
                if (promedioMateria != 0) {
                    promedioMateria /= calificacionWrapper.getCalificacionesPorUnidad().size();
                }
                Paragraph calificacionMateria = new Paragraph(promedioMateria == 0 ? "0" : promedioMateria.toString());
                calificacionMateria.getFont().setStyle("bold");
                calificacionMateria.setAlignment(Element.ALIGN_CENTER);
                cellPromedioMateria.addElement(calificacionMateria);
                celdas.add(cellPromedioMateria);

                if (promedioMateria > 70) {
                    promediosMaterias.add(promedioMateria);
                    celdas.forEach(celda -> content.addCell(celda));

                }
            });
            for (int i = 1; i <= content.getNumberOfColumns() - 2; i++) {
                PdfPCell emptyCell = new PdfPCell(new Phrase());
                emptyCell.setBorder(Rectangle.NO_BORDER);
                content.addCell(emptyCell);
            }

            PdfPCell cellDatoPromedioGeneral = new PdfPCell();
            cellDatoPromedioGeneral.setHorizontalAlignment(Element.ALIGN_MIDDLE);
            cellDatoPromedioGeneral.setBackgroundColor(new Color(99, 97, 97));
            Paragraph datoPromedio = new Paragraph("Promedio final:");
            datoPromedio.setAlignment(Element.ALIGN_MIDDLE);
            datoPromedio.getFont().setColor(Color.WHITE);
            cellDatoPromedioGeneral.addElement(datoPromedio);

            PdfPCell cellPromedioGeneral = new PdfPCell();
            cellPromedioGeneral.setVerticalAlignment(Element.ALIGN_CENTER);
            Integer sumaPromedioGeneral = promediosMaterias.stream().mapToInt(a -> a).sum();
            Integer promedioGeneral = sumaPromedioGeneral == 0 ? 0 : sumaPromedioGeneral / promediosMaterias.size();
            cellPromedioGeneral.setBackgroundColor(promedioGeneral < 70 ? new Color(220, 53, 69) : new Color(40, 167, 69));

            Paragraph promedio = new Paragraph(promedioGeneral.toString() + "%");
            promedio.setAlignment(Element.ALIGN_CENTER);
            promedio.getFont().setStyle("bold");
            promedio.getFont().setStyle("underline");
            promedio.getFont().setColor(Color.WHITE);

            cellPromedioGeneral.addElement(promedio);
            content.addCell(cellDatoPromedioGeneral);
            content.addCell(cellPromedioGeneral);

            document.add(content);

            document.add(footer());
        } catch (BadElementException ex) {
            Logger.getLogger(BoletaTemplatePDF.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BoletaTemplatePDF.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private PdfPTable header() throws BadElementException, IOException {
        Image logo = Image.getInstance("USERSUPLOADS/Logo.png");
        logo.scaleAbsolute(120, 70);

        Paragraph titulo = new Paragraph("Boleta de calificaciones");
        titulo.getFont().setStyle("bold");
        titulo.getFont().setStyle("underline");
        titulo.getFont().setSize(24);

        PdfPTable header = new PdfPTable(2);
        header.setWidthPercentage(100);
        header.setWidths(new int[]{1, 2});

        PdfPCell celdaLogo = new PdfPCell(logo, true);
        celdaLogo.setBorder(Rectangle.NO_BORDER);
        celdaLogo.setPadding(32);
        header.addCell(celdaLogo);

        PdfPCell celdaTitulo = new PdfPCell(titulo);
        celdaTitulo.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celdaTitulo.setBorder(Rectangle.NO_BORDER);

        header.addCell(celdaTitulo);

        return header;
    }

    private Image footer() throws BadElementException, IOException {
        Image piePagina = Image.getInstance("USERSUPLOADS/sepFooter.png");
        piePagina.scaleAbsolute(PageSize.A4.getWidth(), 80);
        piePagina.setAbsolutePosition(piePagina.getAbsoluteX(), 0);
        return piePagina;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public Integer getUnidadesMaximo() {
        return unidadesMaximo;
    }

    public void setUnidadesMaximo(Integer unidadesMaximo) {
        this.unidadesMaximo = unidadesMaximo;
    }

    public ListCalificacionParcialWrapper getListCalificacionesWrapper() {
        return listCalificacionesWrapper;
    }

    public void setListCalificacionesWrapper(ListCalificacionParcialWrapper listCalificacionesWrapper) {
        this.listCalificacionesWrapper = listCalificacionesWrapper;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

}
