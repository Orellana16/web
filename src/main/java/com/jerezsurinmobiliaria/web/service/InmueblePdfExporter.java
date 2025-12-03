package com.jerezsurinmobiliaria.web.service;

import com.jerezsurinmobiliaria.web.model.Cita;
import com.jerezsurinmobiliaria.web.model.Inmueble;
import com.jerezsurinmobiliaria.web.model.PropiedadAdicional;
import com.jerezsurinmobiliaria.web.model.VendedorInmueble;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;
import java.awt.Color;
import java.io.IOException;
import java.util.List;

@Service
public class InmueblePdfExporter {

    public void exportar(HttpServletResponse response, Inmueble inmueble, 
                         List<VendedorInmueble> vendedores, 
                         List<Cita> citas,
                         List<PropiedadAdicional> propiedades) throws IOException {
        
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
        agregarCelda(tablaInfo, inmueble.getNumHab() + " hab / " + inmueble.getNumBanos() + " baños", normalF, Color.WHITE);
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
            tablaProps.setWidths(new float[] {4.0f, 3.0f, 3.0f}); 

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

        // --- VENDEDORES ---
        if (vendedores != null && !vendedores.isEmpty()) {
            document.add(new Paragraph("Vendedores Asignados", subtituloF));
            
            PdfPTable tablaVendedores = new PdfPTable(3);
            tablaVendedores.setWidthPercentage(100f);
            tablaVendedores.setSpacingBefore(10);
            tablaVendedores.setWidths(new float[] {3.5f, 2.5f, 4.0f});

            agregarCabecera(tablaVendedores, "Nombre", Color.DARK_GRAY);
            agregarCabecera(tablaVendedores, "NIF", Color.DARK_GRAY);
            agregarCabecera(tablaVendedores, "Contacto", Color.DARK_GRAY);

            for (VendedorInmueble vi : vendedores) {
                tablaVendedores.addCell(vi.getVendedor().getNombreCompleto());
                tablaVendedores.addCell(vi.getVendedor().getNif());
                tablaVendedores.addCell(vi.getVendedor().getContacto());
            }
            document.add(tablaVendedores);
            document.add(new Paragraph(" "));
        }

        // --- CITAS ---
        if (citas != null && !citas.isEmpty()) {
            document.add(new Paragraph("Próximas Citas", subtituloF));

            PdfPTable tablaCitas = new PdfPTable(3);
            tablaCitas.setWidthPercentage(100f);
            tablaCitas.setSpacingBefore(10);
            
            agregarCabecera(tablaCitas, "Fecha/Hora", Color.DARK_GRAY);
            agregarCabecera(tablaCitas, "Interesado", Color.DARK_GRAY);
            agregarCabecera(tablaCitas, "Comercial", Color.DARK_GRAY);

            for (Cita c : citas) {
                tablaCitas.addCell(c.getFecha().toString() + " " + c.getHora().toString());
                tablaCitas.addCell(c.getInteresado().getNombreCompleto());
                tablaCitas.addCell(c.getTrabajador().getNombreCompleto());
            }
            document.add(tablaCitas);
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
        PdfPCell celda = new PdfPCell(new Phrase(titulo, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE)));
        celda.setBackgroundColor(backgroundColor);
        celda.setPadding(6);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla.addCell(celda);
    }
}