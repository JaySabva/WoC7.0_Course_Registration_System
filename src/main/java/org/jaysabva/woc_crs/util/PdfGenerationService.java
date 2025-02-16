package org.jaysabva.woc_crs.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.jaysabva.woc_crs.dto.Transcript;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Map;

@Service
public class PdfGenerationService {

    public byte[] generateTranscript(Transcript transcript) throws DocumentException, NoSuchFieldException {
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, outputStream);

        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph title = new Paragraph("Transcript", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph("\n"));
        PdfPTable info = new PdfPTable(6);
        info.setWidthPercentage(100);
        info.addCell(createCell("Name", true));
        info.addCell(createCell(transcript.getStudentName(), false));
        info.addCell(createCell("Program", true));
        info.addCell(createCell(transcript.getDepartment(), false));
        info.addCell(createCell("Semester", true));
        info.addCell(createCell(transcript.getSemesterName(), false));
        info.addCell(createCell("Student ID", true));
        info.addCell(createCell(transcript.getStudentID().toString(), false));
        info.addCell(createCell("Batch", true));
        info.addCell(createCell(transcript.getBatch().toString(), false));
        info.addCell(createCell("Start Date", true));
        info.addCell(createCell(transcript.getStartDate().toString(), false));
        document.add(info);

        document.add(new Paragraph("\n"));
        PdfPTable courses = new PdfPTable(6);
        courses.setWidthPercentage(100);
        courses.addCell(createCell("Course Title", true));
        courses.addCell(createCell("Course Code", true));
        courses.addCell(createCell("Credit", true));
        courses.addCell(createCell("Grade", true));
        courses.addCell(createCell("Grade Point", true));
        courses.addCell(createCell("Professor", true));

        for (Object courseObj : transcript.getCourses()) {
            Map<String, Object> courseMap = (Map<String, Object>) courseObj;

            // Directly access values from the map
            courses.addCell(createCell(courseMap.get("courseName").toString(), false));
            courses.addCell(createCell(courseMap.get("courseCode").toString(), false));
            courses.addCell(createCell(courseMap.get("credits").toString(), false));
            courses.addCell(createCell(courseMap.get("grade").toString(), false));
            courses.addCell(createCell(courseMap.get("gradePoint").toString(), false));
            courses.addCell(createCell(courseMap.get("professor").toString(), false));
        }

        document.add(courses);

        document.close();
        return outputStream.toByteArray();
    }
    private PdfPCell createCell(String content, boolean isHeader) {
        PdfPCell cell = new PdfPCell(new Phrase(content));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        if (isHeader) {
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setPadding(5);
        } else {
            cell.setPadding(5);
        }
        return cell;
    }
}
