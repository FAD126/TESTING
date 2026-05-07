package com.example;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfExporter {

    public static void exportGradesToPdf(List<GradeRecord> records, File outputFile) throws Exception {
        if (outputFile == null) {
            throw new IllegalArgumentException("outputFile no puede ser null");
        }

        Document document = new Document();
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            PdfWriter.getInstance(document, fos);
            document.open();

            document.add(new Paragraph("Historial de Notas"));
            document.add(new Paragraph(" "));

            if (records == null || records.isEmpty()) {
                document.add(new Paragraph("(Sin registros)"));
                return;
            }

            for (GradeRecord r : records) {
                if (r == null) {
                    document.add(new Paragraph("(Registro nulo)"));
                    continue;
                }

                document.add(new Paragraph(
                        "ID: " + safe(r.getId())
                        + " | Estudiante: " + safe(r.getStudentName())
                        + " | Curso: " + safe(r.getSubject())
                        + " | Nota: " + safe(r.getGrade())
                ));
            }
        } finally {
            try {
                if (document.isOpen()) {
                    document.close();
                }
            } catch (Exception ignore) {
            }
        }
    }

    private static String safe(Object value) {
        return value == null ? "" : String.valueOf(value);
    }
}
