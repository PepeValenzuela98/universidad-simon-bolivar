/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.view.pdf.calificaciones;

import com.jaevsoftware.USB.model.entity.Alumno;
import com.jaevsoftware.USB.model.entity.wrapper.ListCalificacionParcialWrapper;
import com.jaevsoftware.USB.util.BoletaTemplatePDF;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

@Component("/calificacionParcial/ver.pdf")
public class BoletaIndividualPdfView extends AbstractPdfView {

    private final Log log = LogFactory.getLog(this.getClass());

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Alumno alumno = (Alumno) model.get("alumno");
        Integer unidadesMaximo = (Integer) model.get("unidadesMaximo");
        ListCalificacionParcialWrapper listCalificacionesWrapper = (ListCalificacionParcialWrapper) model.get("calificacionesWrapper");

//        response.addHeader("Content-Disposition", "attachment; filename=Boleta de calificaciones de " + alumno.getNombre() + " " + alumno.getApellido() + " - USB.pdf");
        document.addTitle("Boleta de calificaciones de " + alumno.getNombre() + " " + alumno.getApellido() + " - USB");
        
        BoletaTemplatePDF boletaTemplatePdf = new BoletaTemplatePDF();
        boletaTemplatePdf.setAlumno(alumno);
        boletaTemplatePdf.setUnidadesMaximo(unidadesMaximo);
        boletaTemplatePdf.setListCalificacionesWrapper(listCalificacionesWrapper);
        boletaTemplatePdf.setDocument(document);
        boletaTemplatePdf.generateBoleta();
    }
}
