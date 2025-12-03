package com.jerezsurinmobiliaria.web.service;

import com.jerezsurinmobiliaria.web.model.Cita;
import com.jerezsurinmobiliaria.web.model.Inmueble;
import com.jerezsurinmobiliaria.web.model.PropiedadAdicional; // Importar esto
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

    // 1. Actualizamos la firma del método para aceptar "List<PropiedadAdicional> propiedades"
    public void exportar(HttpServletResponse response, Inmueble inmueble, 
                         List<VendedorInmueble> vendedores, 
                         List<Cita> citas,
                         List<PropiedadAdicional> propiedades) throws IOException {
        
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        // Fuentes
        Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontTitulo.setSize(18);
        fontTitulo.setColor(Color.BLUE);

        Font fontSubtitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontSubtitulo.setSize(14);
        fontSubtitulo.setColor(Color.DARK_GRAY);

        // --- TÍTULO Y DATOS GENERALES (Igual que antes) ---
        Paragraph titulo = new Paragraph("Ficha del Inmueble #" + inmueble.getId(), fontTitulo);
        titulo.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(titulo);
        document.add(new Paragraph(" "));

        PdfPTable tablaInfo = new PdfPTable(2);
        tablaInfo.setWidthPercentage(100f);
        tablaInfo.setSpacingBefore(10);
        
        agregarCelda(tablaInfo, "Dirección:", true);
        agregarCelda(tablaInfo, inmueble.getDireccion(), false);
        agregarCelda(tablaInfo, "Tipo:", true);
        agregarCelda(tablaInfo, inmueble.getTipoVivienda(), false);
        agregarCelda(tablaInfo, "Precio:", true);
        agregarCelda(tablaInfo, inmueble.getPrecio() + " €", false);
        agregarCelda(tablaInfo, "Hab / Baños:", true);
        agregarCelda(tablaInfo, inmueble.getNumHab() + " hab / " + inmueble.getNumBanos() + " baños", false);
        agregarCelda(tablaInfo, "Metros:", true);
        agregarCelda(tablaInfo, inmueble.getMetrosCuadrados() + " m²", false);

        document.add(tablaInfo);
        document.add(new Paragraph(" "));

        // --- NUEVA SECCIÓN: PROPIEDADES ADICIONALES ---
        if (propiedades != null && !propiedades.isEmpty()) {
            Paragraph pProps = new Paragraph("Propiedades Adicionales (Garajes, Trasteros...)", fontSubtitulo);
            document.add(pProps);

            PdfPTable tablaProps = new PdfPTable(3); // 3 Columnas
            tablaProps.setWidthPercentage(100f);
            tablaProps.setSpacingBefore(10);
            // Anchos relativos: Tipo ancho, Derrama medio, IBI medio
            tablaProps.setWidths(new float[] {4.0f, 3.0f, 3.0f}); 

            // Cabeceras
            agregarCabecera(tablaProps, "Tipo");
            agregarCabecera(tablaProps, "Derrama");
            agregarCabecera(tablaProps, "IBI");

            // Datos
            for (PropiedadAdicional pa : propiedades) {
                tablaProps.addCell(pa.getTipo());
                
                // Formatear dinero si es posible, si no, string simple
                String derramaStr = (pa.getDerrama() != null) ? pa.getDerrama() + " €" : "-";
                tablaProps.addCell(derramaStr);
                
                tablaProps.addCell(pa.getIbi() != null ? pa.getIbi() : "-");
            }
            document.add(tablaProps);
            document.add(new Paragraph(" "));
        }

        // --- VENDEDORES (Igual que antes) ---
        if (vendedores != null && !vendedores.isEmpty()) {
            Paragraph pVendedores = new Paragraph("Vendedores Asignados", fontSubtitulo);
            document.add(pVendedores);
            
            PdfPTable tablaVendedores = new PdfPTable(3);
            tablaVendedores.setWidthPercentage(100f);
            tablaVendedores.setSpacingBefore(10);
            tablaVendedores.setWidths(new float[] {3.5f, 2.5f, 4.0f});

            agregarCabecera(tablaVendedores, "Nombre");
            agregarCabecera(tablaVendedores, "NIF");
            agregarCabecera(tablaVendedores, "Contacto");

            for (VendedorInmueble vi : vendedores) {
                tablaVendedores.addCell(vi.getVendedor().getNombreCompleto());
                tablaVendedores.addCell(vi.getVendedor().getNif());
                tablaVendedores.addCell(vi.getVendedor().getContacto());
            }
            document.add(tablaVendedores);
            document.add(new Paragraph(" "));
        }

        // --- CITAS (Igual que antes) ---
        if (citas != null && !citas.isEmpty()) {
            Paragraph pCitas = new Paragraph("Próximas Citas", fontSubtitulo);
            document.add(pCitas);

            PdfPTable tablaCitas = new PdfPTable(3);
            tablaCitas.setWidthPercentage(100f);
            tablaCitas.setSpacingBefore(10);
            
            agregarCabecera(tablaCitas, "Fecha/Hora");
            agregarCabecera(tablaCitas, "Interesado");
            agregarCabecera(tablaCitas, "Comercial");

            for (Cita c : citas) {
                tablaCitas.addCell(c.getFecha().toString() + " " + c.getHora().toString());
                tablaCitas.addCell(c.getInteresado().getNombreCompleto());
                tablaCitas.addCell(c.getTrabajador().getNombreCompleto());
            }
            document.add(tablaCitas);
        }

        document.close();
    }

    private void agregarCelda(PdfPTable tabla, String texto, boolean esNegrita) {
        Font font = esNegrita ? FontFactory.getFont(FontFactory.HELVETICA_BOLD) : FontFactory.getFont(FontFactory.HELVETICA);
        PdfPCell celda = new PdfPCell(new Phrase(texto != null ? texto : "", font));
        celda.setPadding(5);
        if (esNegrita) celda.setBackgroundColor(Color.LIGHT_GRAY);
        tabla.addCell(celda);
    }

    private void agregarCabecera(PdfPTable tabla, String titulo) {
        PdfPCell celda = new PdfPCell(new Phrase(titulo, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE)));
        celda.setBackgroundColor(Color.DARK_GRAY);
        celda.setPadding(5);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla.addCell(celda);
    }
}