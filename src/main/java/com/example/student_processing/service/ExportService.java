package com.example.student_processing.service;

import com.example.student_processing.model.Student;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.opencsv.CSVWriter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExportService {

    /**
     * Export students to Excel format
     */
    public byte[] exportToExcel(List<Student> students) {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Students Report");
            
            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Student ID", "First Name", "Last Name", "Full Name", "Class", "Date of Birth", "Score"};
            
            for (int i = 0; i < headers.length; i++) {
                org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                
                // Style header
                CellStyle headerStyle = workbook.createCellStyle();
                Font font = workbook.createFont();
                font.setBold(true);
                headerStyle.setFont(font);
                cell.setCellStyle(headerStyle);
            }

            // Add data rows
            int rowNum = 1;
            for (Student student : students) {
                Row row = sheet.createRow(rowNum++);
                
                row.createCell(0).setCellValue(student.getId());
                row.createCell(1).setCellValue(student.getStudentId());
                row.createCell(2).setCellValue(student.getFirstName());
                row.createCell(3).setCellValue(student.getLastName());
                row.createCell(4).setCellValue(student.getFullName());
                row.createCell(5).setCellValue(student.getClassName());
                row.createCell(6).setCellValue(student.getDob().toString());
                row.createCell(7).setCellValue(student.getScore());
            }

            // Set column widths manually (autoSizeColumn doesn't work in headless environment)
            sheet.setColumnWidth(0, 2000);  // ID
            sheet.setColumnWidth(1, 3000);  // Student ID
            sheet.setColumnWidth(2, 4000);  // First Name
            sheet.setColumnWidth(3, 4000);  // Last Name
            sheet.setColumnWidth(4, 5000);  // Full Name
            sheet.setColumnWidth(5, 3000);  // Class
            sheet.setColumnWidth(6, 3500);  // Date of Birth
            sheet.setColumnWidth(7, 2500);  // Score

            workbook.write(outputStream);
            return outputStream.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Error exporting to Excel", e);
        }
    }

    /**
     * Export students to CSV format
     */
    public byte[] exportToCsv(List<Student> students) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             OutputStreamWriter writer = new OutputStreamWriter(outputStream);
             CSVWriter csvWriter = new CSVWriter(writer)) {

            // Write header
            String[] headers = {"ID", "Student ID", "First Name", "Last Name", "Full Name", "Class", "Date of Birth", "Score"};
            csvWriter.writeNext(headers);

            // Write data
            for (Student student : students) {
                String[] row = {
                    student.getId().toString(),
                    student.getStudentId().toString(),
                    student.getFirstName(),
                    student.getLastName(),
                    student.getFullName(),
                    student.getClassName(),
                    student.getDob().toString(),
                    student.getScore().toString()
                };
                csvWriter.writeNext(row);
            }

            csvWriter.flush();
            return outputStream.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Error exporting to CSV", e);
        }
    }

    /**
     * Generate proper PDF document using iText
     */
    public byte[] exportToPdf(List<Student> students) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Add title
            Paragraph title = new Paragraph("STUDENT REPORT")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(20)
                    .setBold();
            document.add(title);

            // Add generation date
            Paragraph date = new Paragraph("Generated on: " + 
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(12);
            document.add(date);

            // Add total count
            Paragraph totalCount = new Paragraph("Total Students: " + students.size())
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(14)
                    .setBold();
            document.add(totalCount);

            // Add some space
            document.add(new Paragraph("\n"));

            // Create table
            Table table = new Table(UnitValue.createPercentArray(new float[]{1, 2, 2, 2, 2, 2, 1}));
            table.setWidth(UnitValue.createPercentValue(100));

            // Add headers
            String[] headers = {"ID", "Student ID", "First Name", "Last Name", "Class", "Date of Birth", "Score"};
            for (String header : headers) {
                Cell headerCell = new Cell().add(new Paragraph(header).setBold());
                headerCell.setTextAlignment(TextAlignment.CENTER);
                table.addHeaderCell(headerCell);
            }

            // Add data rows
            for (Student student : students) {
                table.addCell(new Cell().add(new Paragraph(student.getId().toString())));
                table.addCell(new Cell().add(new Paragraph(student.getStudentId().toString())));
                table.addCell(new Cell().add(new Paragraph(student.getFirstName())));
                table.addCell(new Cell().add(new Paragraph(student.getLastName())));
                table.addCell(new Cell().add(new Paragraph(student.getClassName())));
                table.addCell(new Cell().add(new Paragraph(student.getDob().toString())));
                
                // Color code scores
                Cell scoreCell = new Cell().add(new Paragraph(student.getScore().toString()));
                if (student.getScore() >= 90) {
                    scoreCell.setBackgroundColor(com.itextpdf.kernel.colors.ColorConstants.LIGHT_GRAY);
                }
                table.addCell(scoreCell);
            }

            document.add(table);

            // Add footer
            document.add(new Paragraph("\n"));
            Paragraph footer = new Paragraph("End of Report")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(10);
            document.add(footer);

            document.close();
            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }
}