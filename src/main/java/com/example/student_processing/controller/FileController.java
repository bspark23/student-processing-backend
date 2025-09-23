package com.example.student_processing.controller;

import com.example.student_processing.model.Student;
import com.example.student_processing.service.FileProcessingService;
import com.example.student_processing.service.StudentService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "*")
public class FileController {

    private final FileProcessingService fileProcessingService;
    private final StudentService studentService;

    public FileController(FileProcessingService fileProcessingService, StudentService studentService) {
        this.fileProcessingService = fileProcessingService;
        this.studentService = studentService;
    }

    /**
     * Step 1: Generate fake student data as Excel file
     */
    @PostMapping("/generate-excel")
    public ResponseEntity<byte[]> generateExcelFile(@RequestParam(defaultValue = "1000") int numberOfRecords) {
        try {
            byte[] excelData = fileProcessingService.generateExcelFile(numberOfRecords);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "students_" + numberOfRecords + ".xlsx");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelData);
                    
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Step 2: Convert Excel to CSV (adds 10 points to scores)
     */
    @PostMapping("/excel-to-csv")
    public ResponseEntity<byte[]> convertExcelToCsv(@RequestParam("file") MultipartFile excelFile) {
        try {
            if (excelFile.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            byte[] csvData = fileProcessingService.convertExcelToCsv(excelFile);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            headers.setContentDispositionFormData("attachment", "students_converted.csv");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(csvData);
                    
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Step 3: Upload CSV to database (adds 5 more points to scores)
     */
    @PostMapping("/upload-csv")
    public ResponseEntity<Map<String, Object>> uploadCsvToDatabase(@RequestParam("file") MultipartFile csvFile) {
        try {
            if (csvFile.isEmpty()) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "File is empty");
                return ResponseEntity.badRequest().body(error);
            }

            // Parse CSV and get students
            List<Student> students = fileProcessingService.parseCsvFile(csvFile);
            
            // Save to database
            List<Student> savedStudents = studentService.saveAllStudents(students);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Successfully uploaded " + savedStudents.size() + " students to database");
            response.put("studentsUploaded", savedStudents.size());
            response.put("totalStudentsInDB", studentService.getTotalStudentCount());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to upload CSV: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * Complete workflow: Generate Excel → Convert to CSV → Upload to DB
     */
    @PostMapping("/complete-workflow")
    public ResponseEntity<Map<String, Object>> completeWorkflow(@RequestParam(defaultValue = "1000") int numberOfRecords) {
        try {
            Map<String, Object> response = new HashMap<>();
            
            // Step 1: Generate Excel
            byte[] excelData = fileProcessingService.generateExcelFile(numberOfRecords);
            response.put("step1", "Generated Excel with " + numberOfRecords + " records");
            
            // For demonstration, we'll return the workflow status
            // In a real scenario, you might want to store the file temporarily
            response.put("message", "Workflow initiated. Use separate endpoints for each step.");
            response.put("instructions", Map.of(
                "step1", "Use /generate-excel to create Excel file",
                "step2", "Use /excel-to-csv to convert Excel to CSV",
                "step3", "Use /upload-csv to save CSV data to database"
            ));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Workflow failed: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
}