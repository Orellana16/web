package com.jerezsurinmobiliaria.web.service;

import com.jerezsurinmobiliaria.web.model.Inmueble;
import com.jerezsurinmobiliaria.web.model.PropiedadAdicional;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;
import java.awt.Color;
import java.io.IOException;
import java.util.List;

@Service
public class InmueblePdfExporter {

    public void exportar(HttpServletResponse response, Inmueble inmueble, List<PropiedadAdicional> propiedades)
            throws IOException {

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        Font tituloF = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, new Color(4, 59, 137));
        Font subtituloF = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, Color.DARK_GRAY);
        Font normalF = FontFactory.getFont(FontFactory.HELVETICA, 11);
        Font negritaF = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);

        // --- TÍTULO Y DATOS GENERALES ---
        Paragraph titulo = new Paragraph("Ficha del Inmueble #" + inmueble.getId(), tituloF);
        titulo.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(titulo);
        document.add(new Paragraph(" "));

        PdfPTable tablaInfo = new PdfPTable(2);
        tablaInfo.setWidthPercentage(100f);
        tablaInfo.setSpacingBefore(10);

        agregarCelda(tablaInfo, "Dirección:", negritaF, Color.LIGHT_GRAY);
        agregarCelda(tablaInfo, inmueble.getDireccion(), normalF, Color.WHITE);
        agregarCelda(tablaInfo, "Tipo:", negritaF, Color.LIGHT_GRAY);
        agregarCelda(tablaInfo, inmueble.getTipoVivienda(), normalF, Color.WHITE);
        agregarCelda(tablaInfo, "Precio:", negritaF, Color.LIGHT_GRAY);
        agregarCelda(tablaInfo, inmueble.getPrecio() + " €", normalF, Color.WHITE);
        agregarCelda(tablaInfo, "Hab / Baños:", negritaF, Color.LIGHT_GRAY);
        agregarCelda(tablaInfo, inmueble.getNumHab() + " hab / " + inmueble.getNumBanos() + " baños", normalF,
                Color.WHITE);
        agregarCelda(tablaInfo, "Metros:", negritaF, Color.LIGHT_GRAY);
        agregarCelda(tablaInfo, inmueble.getMetrosCuadrados() + " m²", normalF, Color.WHITE);

        document.add(tablaInfo);
        document.add(new Paragraph(" "));

        // --- PROPIEDADES ADICIONALES ---
        if (propiedades != null && !propiedades.isEmpty()) {
            document.add(new Paragraph("Propiedades Adicionales", subtituloF));

            PdfPTable tablaProps = new PdfPTable(3);
            tablaProps.setWidthPercentage(100f);
            tablaProps.setSpacingBefore(10);
            tablaProps.setWidths(new float[] { 4.0f, 3.0f, 3.0f });

            agregarCabecera(tablaProps, "Tipo", Color.DARK_GRAY);
            agregarCabecera(tablaProps, "Derrama", Color.DARK_GRAY);
            agregarCabecera(tablaProps, "IBI", Color.DARK_GRAY);

            for (PropiedadAdicional pa : propiedades) {
                tablaProps.addCell(pa.getTipo() != null ? pa.getTipo() : "-");
                tablaProps.addCell(pa.getDerrama() != null ? pa.getDerrama() + " €" : "-");
                tablaProps.addCell(pa.getIbi() != null ? pa.getIbi() : "-");
            }
            document.add(tablaProps);
            document.add(new Paragraph(" "));
        }
        document.close();
    }

    private void agregarCelda(PdfPTable tabla, String texto, Font font, Color backgroundColor) {
        PdfPCell celda = new PdfPCell(new Phrase(texto != null ? texto : "-", font));
        celda.setPadding(5);
        celda.setBackgroundColor(backgroundColor);
        tabla.addCell(celda);
    }

    private void agregarCabecera(PdfPTable tabla, String titulo, Color backgroundColor) {
        PdfPCell celda = new PdfPCell(
                new Phrase(titulo, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE)));
        celda.setBackgroundColor(backgroundColor);
        celda.setPadding(6);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla.addCell(celda);
    }
}