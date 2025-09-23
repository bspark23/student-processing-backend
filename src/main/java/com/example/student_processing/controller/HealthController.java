package com.example.student_processing.controller;

import com.example.student_processing.service.StudentService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
@CrossOrigin(origins = "*")
public class HealthController {

    private final StudentService studentService;

    public HealthController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public Map<String, Object> healthCheck() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", LocalDateTime.now());
        health.put("service", "Student Processing System");
        health.put("version", "1.0.0");
        
        try {
            long studentCount = studentService.getTotalStudentCount();
            health.put("database", "Connected");
            health.put("totalStudents", studentCount);
        } catch (Exception e) {
            health.put("database", "Error: " + e.getMessage());
        }
        
        return health;
    }

    @GetMapping("/endpoints")
    public Map<String, Object> getAvailableEndpoints() {
        Map<String, Object> endpoints = new HashMap<>();
        
        // Student endpoints
        Map<String, String> studentEndpoints = new HashMap<>();
        studentEndpoints.put("GET /api/students", "Get all students with pagination");
        studentEndpoints.put("GET /api/students/{id}", "Get student by database ID");
        studentEndpoints.put("GET /api/students/student/{studentId}", "Get student by student ID");
        studentEndpoints.put("GET /api/students/classes", "Get all class names");
        studentEndpoints.put("GET /api/students/stats", "Get statistics");
        studentEndpoints.put("POST /api/students", "Create new student");
        studentEndpoints.put("PUT /api/students/{id}", "Update student");
        studentEndpoints.put("DELETE /api/students/{id}", "Delete student");
        
        // File processing endpoints
        Map<String, String> fileEndpoints = new HashMap<>();
        fileEndpoints.put("POST /api/files/generate-excel", "Generate fake student Excel file");
        fileEndpoints.put("POST /api/files/excel-to-csv", "Convert Excel to CSV (+10 points)");
        fileEndpoints.put("POST /api/files/upload-csv", "Upload CSV to database (+5 points)");
        fileEndpoints.put("POST /api/files/complete-workflow", "Complete workflow guide");
        
        // Export endpoints
        Map<String, String> exportEndpoints = new HashMap<>();
        exportEndpoints.put("GET /api/export/excel", "Export students to Excel");
        exportEndpoints.put("GET /api/export/csv", "Export students to CSV");
        exportEndpoints.put("GET /api/export/pdf", "Export students to PDF");
        
        endpoints.put("students", studentEndpoints);
        endpoints.put("files", fileEndpoints);
        endpoints.put("export", exportEndpoints);
        
        return endpoints;
    }
}