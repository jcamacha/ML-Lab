package org.example.controllers;

import org.example.services.PdfService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pdf")
@CrossOrigin(origins = "*")
public class PdfController {

    private final PdfService service;

    public PdfController(PdfService service) {
        this.service = service;
    }

    // OBTENER REPORTE PDF DE EXPERIMENTO
    @GetMapping("/experiment/{id}")
    public ResponseEntity<byte[]> getExperimentReport(@PathVariable Long id) {
        try {
            byte[] pdfBytes = service.generateExperimentReport(id);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.builder("inline")
                    .filename("reporte_experimento_" + id + ".pdf").build());
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // OBTENER FICHA TECNICA PDF DE DATASET
    @GetMapping("/dataset/{id}")
    public ResponseEntity<byte[]> getDatasetReport(@PathVariable Long id) {
        try {
            byte[] pdfBytes = service.generateDatasetReport(id);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.builder("inline")
                    .filename("ficha_dataset_" + id + ".pdf").build());
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
