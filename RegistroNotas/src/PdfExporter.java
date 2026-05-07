import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;

public class PdfExporter {

    public static void exportGradesToPdf(List<GradeRecord> records, File outputFile) throws Exception {

        if (outputFile == null) {
            throw new IllegalArgumentException("outputFile no puede ser null");
        }

        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(outputFile));
            document.open();

            document.add(new Paragraph("Historial de Notas"));
            document.add(new Paragraph(" "));

            if (records == null || records.isEmpty()) {
                document.add(new Paragraph("(Sin registros)"));
            } else {
                for (GradeRecord r : records) {
                    if (r == null) {
                        document.add(new Paragraph("(Registro nulo)"));
                        continue;
                    }

                    document.add(new Paragraph(
                            "ID: " + safe(r.getId()) +
                            " | Estudiante: " + safe(r.getStudentName()) +
                            " | Curso: " + safe(r.getSubject()) +
                            " | Nota: " + safe(r.getGrade())
                    ));
                }
            }

        } finally {
            if (document.isOpen()) {
                document.close();
            }
        }
    }

    private static String safe(Object value) {
        return value == null ? "" : String.valueOf(value);
    }
}