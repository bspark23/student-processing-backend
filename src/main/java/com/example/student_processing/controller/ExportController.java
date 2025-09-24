package com.example.student_processing.controller;

import com.example.student_processing.model.Student;
import com.example.student_processing.service.ExportService;
import com.example.student_processing.service.StudentService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/export")
public class ExportController {

    private final ExportService exportService;
    private final StudentService studentService;

    public ExportController(ExportService exportService, StudentService studentService) {
        this.exportService = exportService;
        this.studentService = studentService;
    }

    /**
     * Export all students to Excel
     */
    @GetMapping("/excel")
    public ResponseEntity<byte[]> exportToExcel(
            @RequestParam(required = false) String className,
            @RequestParam(required = false) String search) {
        
        List<Student> students = getFilteredStudents(className, search);
        byte[] excelData = exportService.exportToExcel(students);
        
        String filename = "students_report_" + getCurrentTimestamp() + ".xlsx";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", filename);
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(excelData);
    }

    /**
     * Export all students to CSV
     */
    @GetMapping("/csv")
    public ResponseEntity<byte[]> exportToCsv(
            @RequestParam(required = false) String className,
            @RequestParam(required = false) String search) {
        
        List<Student> students = getFilteredStudents(className, search);
        byte[] csvData = exportService.exportToCsv(students);
        
        String filename = "students_report_" + getCurrentTimestamp() + ".csv";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentDispositionFormData("attachment", filename);
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(csvData);
    }

    /**
     * Export all students to PDF
     */
    @GetMapping("/pdf")
    public ResponseEntity<byte[]> exportToPdf(
            @RequestParam(required = false) String className,
            @RequestParam(required = false) String search) {
        
        List<Student> students = getFilteredStudents(className, search);
        byte[] pdfData = exportService.exportToPdf(students);
        
        String filename = "students_report_" + getCurrentTimestamp() + ".pdf";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", filename);
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfData);
    }

    private List<Student> getFilteredStudents(String className, String search) {
        if (search != null && !search.trim().isEmpty()) {
            // For export, we want all matching results, so use a large page size
            Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by("id"));
            return studentService.searchStudentsByName(search, pageable).getContent();
        } else if (className != null && !className.trim().isEmpty()) {
            Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by("id"));
            return studentService.getStudentsByClass(className, pageable).getContent();
        } else {
            return studentService.getAllStudents();
        }
    }

    private String getCurrentTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
    }
}