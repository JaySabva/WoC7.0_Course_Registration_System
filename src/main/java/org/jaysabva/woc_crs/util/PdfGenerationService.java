package org.jaysabva.woc_crs.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.jaysabva.woc_crs.dto.Transcript;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class PdfGenerationService {

    public byte[] generateTranscript(Transcript transcript) throws DocumentException, NoSuchFieldException {
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, outputStream);

        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph title = new Paragraph("J. D. Sabva Institute of Engineering", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph("\n"));

        PdfPTable header = new PdfPTable(1);
        header.setWidthPercentage(100);
        header.addCell(createCell("TRANSCRIPT", true));
        document.add(header);

        PdfPTable info = new PdfPTable(2);
        info.setWidthPercentage(100);

        float[] columnWidths = { 0.3f, 0.7f };
        info.setWidths(columnWidths);

        info.addCell(createCell("STUDENT ID", true));
        info.addCell(createCell(transcript.getStudentID().toString(), false));
        info.addCell(createCell("NAME", true));
        info.addCell(createCell(transcript.getStudentName(), false));
        info.addCell(createCell("BATCH", true));
        info.addCell(createCell(transcript.getBatch().toString(), false));
        info.addCell(createCell("PROGRAM", true));
        info.addCell(createCell(transcript.getDepartment(), false));
        info.addCell(createCell("SEMESTER", true));
        info.addCell(createCell(transcript.getSemesterName(), false));
        info.addCell(createCell("START DATE", true));
        info.addCell(createCell(transcript.getStartDate().toString(), false));
        document.add(info);

        document.add(new Paragraph("\n"));
        PdfPTable courses = new PdfPTable(6);
        courses.setWidthPercentage(100);
        courses.addCell(createCell("COURSE TITLE", true));
        courses.addCell(createCell("COURSE CODE", true));
        courses.addCell(createCell("CREDIT", true));
        courses.addCell(createCell("GRADE", true));
        courses.addCell(createCell("GRADE POINT", true));
        courses.addCell(createCell("PROFESSOR", true));

        for (Object courseObj : transcript.getCourses()) {
            Map<String, Object> courseMap = (Map<String, Object>) courseObj;

            courses.addCell(createCell(courseMap.get("courseName").toString(), false));
            courses.addCell(createCell(courseMap.get("courseCode").toString(), false));
            courses.addCell(createCell(courseMap.get("credits").toString(), false));
            courses.addCell(createCell(courseMap.get("grade").toString(), false));
            courses.addCell(createCell(courseMap.get("gradePoint").toString(), false));
            courses.addCell(createCell(courseMap.get("professor").toString(), false));
        }

        document.add(courses);

        document.add(new Paragraph("\n"));

        document.add(new PdfPTable(1) {{
            setWidthPercentage(100);
            addCell(createCell("CURRENT SEMESTER PERFORMANCE", true));
        }});

        PdfPTable performance = new PdfPTable(4);
        performance.setWidthPercentage(100);
        performance.addCell(createCell("CREDITS REGISTERED", true));
        performance.addCell(createCell("CREDITS EARNED", true));
        performance.addCell(createCell("GRADE POINTS EARNED", true));
        performance.addCell(createCell("GPA", true));

        Map<String, Object> grades = transcript.getGrades();
        performance.addCell(createCell(grades.get("creditRegistered").toString(), false));
        performance.addCell(createCell(grades.get("creditEarned").toString(), false));
        performance.addCell(createCell(grades.get("gradePoints").toString(), false));
        performance.addCell(createCell(grades.get("gpa").toString(), false));

        document.add(performance);

        document.add(new Paragraph("\n"));
        String generatedOn = "Generated on: " + LocalDateTime.now().toString();
        Paragraph footer = new Paragraph(generatedOn, FontFactory.getFont(FontFactory.HELVETICA, 8));
        footer.setAlignment(Element.ALIGN_RIGHT);
        document.add(footer);

        document.close();
        return outputStream.toByteArray();
    }
    private PdfPCell createCell(String content, boolean isHeader) {
        Font cellFont = isHeader ? FontFactory.getFont(FontFactory.HELVETICA_BOLD) : FontFactory.getFont(FontFactory.HELVETICA);
        PdfPCell cell = new PdfPCell(new Phrase(content, cellFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);

        if (isHeader) {
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        }
        return cell;
    }
}
