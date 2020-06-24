/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.view.pdf;

import com.jaevsoftware.USB.model.entity.Alumno;
import com.jaevsoftware.USB.model.entity.GrupoAlumno;
import com.jaevsoftware.USB.model.entity.Justificante;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

/**
 *
 * @author Pepe Valenzuela
 */
@Component("/justificantes/ver.pdf")
public class JustificantePdfView extends AbstractPdfView {

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Alumno alumno = (Alumno) model.get("alumno");
        List<GrupoAlumno> grupoAlumno = (List<GrupoAlumno>) model.get("grupos");
        Justificante justificante = (Justificante) model.get("justificante");

        document.add(header());

        Date fechaActual = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Paragraph encabezado = new Paragraph("Ciudad de México, CDMX " + format.format(fechaActual));
        encabezado.setAlignment(Element.ALIGN_RIGHT);

        Paragraph docentesEncabezado = new Paragraph("DOCENTES DE LA CARRERA DE " + alumno.getCarrera().getCodigoCarrera());
        docentesEncabezado.getFont().setStyle("bold");
        docentesEncabezado.setSpacingAfter(15);

        PdfPTable grupos = new PdfPTable(1);
        grupos.setHorizontalAlignment(Element.ALIGN_LEFT);
        grupos.setWidthPercentage(50);
        Integer iteratorGrupoAlumno = 0;
        for (GrupoAlumno grupo : grupoAlumno) {
            PdfPCell celdaGrupo = new PdfPCell();
            celdaGrupo.setPadding(0);
            if (iteratorGrupoAlumno % 2 == 0) {
                celdaGrupo.setBackgroundColor(new Color(219, 219, 219));
            } else {
                celdaGrupo.setBackgroundColor(Color.WHITE);
            }
            celdaGrupo.setBorder(Rectangle.NO_BORDER);
            celdaGrupo.setFixedHeight(33);
            Phrase codigoMateria = new Phrase(grupo.getGrupo().getMateria().getCodigoMateria());
            codigoMateria.getFont().setStyle("bold");
            codigoMateria.getFont().setSize(10);
            Phrase nombreMateria = new Phrase();
            nombreMateria.add(grupo.getGrupo().getMateria().getNombre());
            nombreMateria.getFont().setSize(10);
            Phrase docente = new Phrase();
            docente.add(grupo.getGrupo().getDocente().getNombre() + " " + grupo.getGrupo().getDocente().getApellido());
            docente.getFont().setSize(10);

            Paragraph parrafoCelda = new Paragraph();
            parrafoCelda.setLeading(10);
            parrafoCelda.add(codigoMateria);
            parrafoCelda.add(Chunk.NEWLINE);
            parrafoCelda.add(nombreMateria);
            parrafoCelda.add(Chunk.NEWLINE);
            parrafoCelda.add(docente);

            celdaGrupo.addElement(parrafoCelda);

            grupos.addCell(celdaGrupo);
            iteratorGrupoAlumno++;
        }

        document.add(encabezado);
        document.add(docentesEncabezado);
        document.add(grupos);

        Phrase nombreAlumno = new Phrase(alumno.getNombre() + " " + alumno.getApellido());
        nombreAlumno.getFont().setStyle("bold");
        Phrase numeroDeControl = new Phrase(alumno.getNumeroDeControl());
        numeroDeControl.getFont().setStyle("bold");
        Phrase carrera = new Phrase(alumno.getCarrera().getNombre());
        carrera.getFont().setStyle("bold");

        String[] fechaInicioFormat = format.format(justificante.getFechaInicioValida()).split("/");
        String fechaInicioFix = fechaInicioFormat[0] + " de " + getMonth(Integer.parseInt(fechaInicioFormat[1])) + " del " + fechaInicioFormat[2];

        String[] fechaFinFormat = format.format(justificante.getFechaFinValida()).split("/");
        String fechaFinFix = fechaFinFormat[0] + " de " + getMonth(Integer.parseInt(fechaFinFormat[1])) + " del " + fechaFinFormat[2];

        Phrase fecha = new Phrase();
        fecha.add(fechaInicioFix + " al " + fechaFinFix);
        fecha.getFont().setStyle("bold");
        Phrase motivo = new Phrase();
        motivo.add(justificante.getMotivo());
        motivo.getFont().setStyle("bold");

        Paragraph justificanteTexto = new Paragraph();
        justificanteTexto.setAlignment(Element.ALIGN_JUSTIFIED);
        justificanteTexto.add(
                "Por este medio reciban un cordial saludo y de la manera mas atenta y respetuosa, me dirijo a usted para solicitarle"
                + " le justifiquen las inasistencias, del alumno(a) "
        );
        justificanteTexto.add(nombreAlumno);
        justificanteTexto.add(
                " con numero de control "
        );
        justificanteTexto.add(numeroDeControl);
        justificanteTexto.add(
                " de la carrera de "
        );
        justificanteTexto.add(carrera);
        justificanteTexto.add(
                ", dias a justificar son del "
        );
        justificanteTexto.add(fecha);
        justificanteTexto.add(
                ", por motivos de "
        );
        justificanteTexto.add(motivo);
        justificanteTexto.add(
                ". donde se solicito tome en consideracion reprogramacion exámenes, tareas, exposiciones según sea su caso"
                + " de ser posible evaluar con als actividades realizadas a la fecha. Agradeciendo de antemando sus finas"
                + " atenciones, quedo de ustedes."
        );

        document.add(justificanteTexto);

        document.add(footer());
    }

    private PdfPTable header() throws BadElementException, IOException {
        Image logo = Image.getInstance("USERSUPLOADS/Logo.png");
        logo.scaleAbsolute(120, 70);

        Paragraph titulo = new Paragraph("Justificante");
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

    public String getMonth(int month) {
        return new DateFormatSymbols(new Locale("es", "ES")).getMonths()[month - 1];
    }
}
