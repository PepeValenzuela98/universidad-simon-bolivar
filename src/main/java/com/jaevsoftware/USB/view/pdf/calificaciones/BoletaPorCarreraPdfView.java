/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.view.pdf.calificaciones;

import com.jaevsoftware.USB.model.entity.Alumno;
import com.jaevsoftware.USB.model.entity.CalificacionParcial;
import com.jaevsoftware.USB.model.entity.wrapper.ListCalificacionParcialWrapper;
import com.jaevsoftware.USB.model.service.ICalificacionParcialService;
import com.jaevsoftware.USB.util.BoletaTemplatePDF;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

@Component("/calificacionParcial/verPorCarrera.pdf")
public class BoletaPorCarreraPdfView extends AbstractPdfView {

    @Autowired
    ICalificacionParcialService calificacionParcialService;

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Alumno> alumnos = (List<Alumno>) model.get("alumnos");
        String codigoCarrera = (String) model.get("codigoCarrera");
        document.addTitle("Boleta de calificaciones " + codigoCarrera);
        BoletaTemplatePDF boletaTemplatePdf = new BoletaTemplatePDF();
        alumnos.forEach(alumno -> {
            List<CalificacionParcial> calificacionesParciales = calificacionParcialService.findByAlumno(alumno.getNumeroDeControl());
            ListCalificacionParcialWrapper listCalificacionesWrapper = calificacionParcialService.createWrapperOneAlumno(alumno.getNumeroDeControl());
            Integer unidadesMaximo = calificacionParcialService.getUnidadesMaximo(calificacionesParciales);
            boletaTemplatePdf.setAlumno(alumno);
            boletaTemplatePdf.setUnidadesMaximo(unidadesMaximo);
            boletaTemplatePdf.setListCalificacionesWrapper(listCalificacionesWrapper);
            boletaTemplatePdf.setDocument(document);
            boletaTemplatePdf.generateBoleta();
            document.newPage();
        });
    }

}
