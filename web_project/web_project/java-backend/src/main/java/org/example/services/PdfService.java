package org.example.services;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.example.entities.Dataset;
import org.example.entities.Experiment;
import org.example.repositories.DatasetRepository;
import org.example.repositories.ExperimentRepository;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

@Service
public class PdfService {

    private final ExperimentRepository experimentRepository;
    private final DatasetRepository datasetRepository;

    public PdfService(ExperimentRepository experimentRepository, DatasetRepository datasetRepository) {
        this.experimentRepository = experimentRepository;
        this.datasetRepository = datasetRepository;
    }

    // GENERAR REPORTE DE UN EXPERIMENTO
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
                contentStream.setNonStrokingColor(41, 128, 185); // Azul
                contentStream.addRect(0, 780, 595, 42);
                contentStream.fill();

                // Titulo Cabecera
                contentStream.setNonStrokingColor(255, 255, 255);
                drawText(contentStream, "ML LAB - REPORTE DE EXPERIMENTO", 30, 792, fontBold, 14);

                // Volver a color negro para texto
                contentStream.setNonStrokingColor(44, 62, 80);

                float y = 730;

                // Titulo Principal
                drawText(contentStream, "Detalle del Experimento #" + experiment.getExperiment_id(), 30, y, fontBold, 18);
                y -= 30;

                // Linea separadora
                contentStream.setStrokingColor(220, 220, 220);
                contentStream.moveTo(30, y);
                contentStream.lineTo(565, y);
                contentStream.stroke();
                y -= 25;

                // Informacion General
                drawText(contentStream, "Informacion General", 30, y, fontBold, 12);
                y -= 20;

                drawText(contentStream, "Modelo utilizado: " + cleanString(experiment.getModel_name()), 45, y, fontRegular, 10);
                y -= 15;
                drawText(contentStream, "Dataset asociado: " + cleanString(experiment.getDataset_name()), 45, y, fontRegular, 10);
                y -= 15;
                
                String dateStr = experiment.getCreated_at() != null 
                        ? experiment.getCreated_at().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                        : "N/A";
                drawText(contentStream, "Fecha de creacion: " + dateStr, 45, y, fontRegular, 10);
                y -= 30;

                // Seccion de Metricas
                drawText(contentStream, "Metricas del Modelo", 30, y, fontBold, 12);
                y -= 20;

                drawText(contentStream, "Exactitud (Accuracy): " + (experiment.getAccuracy() != null ? experiment.getAccuracy() : "N/A"), 45, y, fontRegular, 10);
                y -= 15;
                drawText(contentStream, "Error Cuadratico Medio (MSE): " + (experiment.getMse() != null ? experiment.getMse() : "N/A"), 45, y, fontRegular, 10);
                y -= 40;

                // Linea separadora
                contentStream.moveTo(30, y);
                contentStream.lineTo(565, y);
                contentStream.stroke();
                y -= 20;

                // Pie de pagina
                drawText(contentStream, "Reporte generado automaticamente por el sistema ML Lab.", 30, y, fontRegular, 8);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
            return baos.toByteArray();
        }
    }

    // GENERAR FICHA TECNICA DE DATASET
    public byte[] generateDatasetReport(Long datasetId) throws IOException {
        Dataset dataset = datasetRepository.findById(datasetId)
                .orElseThrow(() -> new RuntimeException("Dataset no encontrado con id: " + datasetId));

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDFont fontBold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
            PDFont fontRegular = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Cabecera decorativa
                contentStream.setNonStrokingColor(39, 174, 96); // Verde
                contentStream.addRect(0, 780, 595, 42);
                contentStream.fill();

                // Titulo Cabecera
                contentStream.setNonStrokingColor(255, 255, 255);
                drawText(contentStream, "ML LAB - FICHA TECNICA DE DATASET", 30, 792, fontBold, 14);

                // Volver a color negro para texto
                contentStream.setNonStrokingColor(44, 62, 80);

                float y = 730;

                // Titulo Principal
                drawText(contentStream, cleanString(dataset.getName()), 30, y, fontBold, 18);
                y -= 30;

                // Linea separadora
                contentStream.setStrokingColor(220, 220, 220);
                contentStream.moveTo(30, y);
                contentStream.lineTo(565, y);
                contentStream.stroke();
                y -= 25;

                // Descripcion
                drawText(contentStream, "Descripcion:", 30, y, fontBold, 12);
                y -= 20;

                String description = dataset.getDescription() != null ? dataset.getDescription() : "Sin descripcion.";
                // Simple corte de descripcion por longitud
                if (description.length() > 80) {
                    drawText(contentStream, cleanString(description.substring(0, 80)), 45, y, fontRegular, 10);
                    y -= 15;
                    drawText(contentStream, cleanString(description.substring(80, Math.min(description.length(), 160))), 45, y, fontRegular, 10);
                } else {
                    drawText(contentStream, cleanString(description), 45, y, fontRegular, 10);
                }
                y -= 30;

                // Especificaciones Tecnicas
                drawText(contentStream, "Especificaciones Tecnicas", 30, y, fontBold, 12);
                y -= 20;

                drawText(contentStream, "Tipo de Dataset: " + cleanString(dataset.getDatasetType()), 45, y, fontRegular, 10);
                y -= 15;
                drawText(contentStream, "Numero de muestras: " + (dataset.getNumSamples() != null ? dataset.getNumSamples() : "N/A"), 45, y, fontRegular, 10);
                y -= 15;
                drawText(contentStream, "Numero de caracteristicas: " + (dataset.getNumFeatures() != null ? dataset.getNumFeatures() : "N/A"), 45, y, fontRegular, 10);
                y -= 15;
                drawText(contentStream, "Variable objetivo: " + cleanString(dataset.getTargetVariable()), 45, y, fontRegular, 10);
                y -= 15;
                
                String dateStr = dataset.getCreatedAt() != null 
                        ? dataset.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                        : "N/A";
                drawText(contentStream, "Fecha de registro: " + dateStr, 45, y, fontRegular, 10);
                y -= 40;

                // Linea separadora
                contentStream.moveTo(30, y);
                contentStream.lineTo(565, y);
                contentStream.stroke();
                y -= 20;

                // Pie de pagina
                drawText(contentStream, "Ficha técnica generada automáticamente por el sistema ML Lab.", 30, y, fontRegular, 8);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
            return baos.toByteArray();
        }
    }

    // METODO AUXILIAR PARA DIBUJAR TEXTO
    private void drawText(PDPageContentStream stream, String text, float x, float y, PDFont font, float fontSize) throws IOException {
        stream.beginText();
        stream.setFont(font, fontSize);
        stream.newLineAtOffset(x, y);
        stream.showText(text != null ? text : "N/A");
        stream.endText();
    }

    // METODO AUXILIAR PARA ELIMINAR ACENTOS Y CARACTERES NO SOPORTADOS POR HELVETICA
    private String cleanString(String text) {
        if (text == null) return "N/A";
        return text.replace("á", "a")
                .replace("é", "e")
                .replace("í", "i")
                .replace("ó", "o")
                .replace("ú", "u")
                .replace("ñ", "n")
                .replace("Á", "A")
                .replace("É", "E")
                .replace("Í", "I")
                .replace("Ó", "O")
                .replace("Ú", "U")
                .replace("Ñ", "N");
    }
}
