package com.example.student_processing.controller;

import com.example.student_processing.service.StudentService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*")
public class TestController {

    private final StudentService studentService;

    public TestController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/hello")
    public Map<String, Object> hello() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Hello! Backend is working!");
        response.put("timestamp", LocalDateTime.now());
        response.put("status", "SUCCESS");
        return response;
    }

    @GetMapping("/simple")
    public String simple() {
        return "Simple test endpoint working!";
    }

    @GetMapping("/database")
    public Map<String, Object> testDatabase() {
        Map<String, Object> response = new HashMap<>();
        try {
            long count = studentService.getTotalStudentCount();
            response.put("message", "Database connection successful!");
            response.put("studentCount", count);
            response.put("status", "SUCCESS");
        } catch (Exception e) {
            response.put("message", "Database connection failed!");
            response.put("error", e.getMessage());
            response.put("status", "ERROR");
        }
        return response;
    }

    @PostMapping("/upload-test")
    public Map<String, Object> testFileUpload(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (file.isEmpty()) {
                response.put("error", "File is empty");
                return response;
            }
            
            response.put("message", "File upload successful!");
            response.put("filename", file.getOriginalFilename());
            response.put("size", file.getSize());
            response.put("contentType", file.getContentType());
            response.put("status", "SUCCESS");
        } catch (Exception e) {
            response.put("error", e.getMessage());
            response.put("status", "ERROR");
        }
        return response;
    }
}