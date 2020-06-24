/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.view.pdf;

import com.jaevsoftware.USB.model.entity.Alumno;
import com.jaevsoftware.USB.model.entity.GrupoAlumno;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

@Component("/alumno/ver.pdf")
public class HorarioPdfView extends AbstractPdfView {

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Alumno alumno = (Alumno) model.get("alumno");
        List<GrupoAlumno> gruposAlumno = (List<GrupoAlumno>) model.get("gruposAlumno");
        document.addTitle("Horario de " + alumno.getNombre() + " " + alumno.getApellido());
        float width = 30f * 72f / 25.4f;
        float height = 25f * 72f / 25.4f;
        Rectangle rect = new Rectangle(width, height);
        document.setPageSize(rect);
        document.add(header());
        Paragraph nombreAlumno = new Paragraph("Nombre del alumno:" + alumno.getNombre() + " " + alumno.getApellido());
        nombreAlumno.setSpacingAfter(10);
        document.add(nombreAlumno);
        Paragraph semestre = new Paragraph("Semestre:°" + alumno.getSemestre());
        semestre.setSpacingAfter(10);
        document.add(semestre);

        PdfPTable content = new PdfPTable(7);
        content.setWidthPercentage(100);
        
        content.addCell(addCellHeader("Materias"));
        content.addCell(addCellHeader("Grupo"));
        content.addCell(addCellHeader("Lunes"));
        content.addCell(addCellHeader("Martes"));
        content.addCell(addCellHeader("Miercoles"));
        content.addCell(addCellHeader("Jueves"));
        content.addCell(addCellHeader("Viernes"));

        gruposAlumno.forEach(grupoAlumno -> {
            content.addCell(addCellBody(grupoAlumno.getGrupo().getMateria().getNombre()));
            content.addCell(addCellBody(grupoAlumno.getGrupo().getNumeroGrupo()));
            grupoAlumno.getGrupo().getHorariosDeGrupo().forEach(horario -> {
                content.addCell(addCellBody(horario.getHoraInicia() == null ? "" : horario.getHoraInicia() + " - " + horario.getHoraFinal() + " : " + horario.getSalon()));
            });
        });

        document.add(content);
        document.add(footer());

    }

    private PdfPCell addCellHeader(String text) {
        PdfPCell cellHeader = new PdfPCell();
        cellHeader.setBackgroundColor(new Color(52, 58, 64));
        Paragraph textHeader = new Paragraph(text);
        textHeader.getFont().setColor(Color.WHITE);
        textHeader.getFont().setStyle("bold");
        textHeader.setAlignment(Element.ALIGN_CENTER);
        cellHeader.addElement(textHeader);
        return cellHeader;
    }

    private PdfPCell addCellBody(String text) {
        PdfPCell cellBody = new PdfPCell();
        cellBody.setHorizontalAlignment(Element.ALIGN_CENTER);
        Paragraph calificacionMateria = new Paragraph(text);
        calificacionMateria.getFont().setStyle("bold");
        calificacionMateria.setAlignment(Element.ALIGN_CENTER);
        cellBody.addElement(calificacionMateria);
        return cellBody;
    }

    private PdfPTable header() throws BadElementException, IOException {
        Image logo = Image.getInstance("USERSUPLOADS/Logo.png");
        logo.scaleAbsolute(120, 70);

        Paragraph titulo = new Paragraph("Horario");
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
}
