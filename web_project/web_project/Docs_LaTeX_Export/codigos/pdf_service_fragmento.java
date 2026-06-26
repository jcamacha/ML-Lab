// FRAGMENTO de: java-backend/src/main/java/org/example/services/PdfService.java (líneas 31-72)
// Servicio que genera dinámicamente un documento PDF/A-4 con Apache PDFBox para reportar resultados del experimento.

package org.example.services;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.example.entities.Experiment;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PdfService {
    // ... dependencias y constructor ...

    public byte[] generateExperimentReport(Long experimentId) throws IOException {
        Experiment experiment = experimentRepository.findById(experimentId)
                .orElseThrow(() -> new RuntimeException("Experimento no encontrado con id: " + experimentId));

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDFont fontBold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
            PDFont fontRegular = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Cabecera decorativa
                contentStream.setNonStrokingColor(41, 128, 185); // Color Azul
                contentStream.addRect(0, 780, 595, 42);
                contentStream.fill();

                // Título Cabecera
                contentStream.setNonStrokingColor(255, 255, 255);
                drawText(contentStream, "ML LAB - REPORTE DE EXPERIMENTO", 30, 792, fontBold, 14);

                // Volver a color negro para el texto
                contentStream.setNonStrokingColor(44, 62, 80);
                float y = 730;

                // Título Principal
                drawText(contentStream, "Detalle del Experimento #" + experiment.getExperiment_id(), 30, y, fontBold, 18);
                y -= 30;
                
                // (Omitido por brevedad: dibujo de tablas, métricas y pie de página) ...
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
            return baos.toByteArray();
        }
    }
}
