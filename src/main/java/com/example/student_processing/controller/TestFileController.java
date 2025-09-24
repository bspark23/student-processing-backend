package com.example.student_processing.controller;

import com.example.student_processing.service.FileProcessingService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test-files")
public class TestFileController {

    private final FileProcessingService fileProcessingService;

    public TestFileController(FileProcessingService fileProcessingService) {
        this.fileProcessingService = fileProcessingService;
    }

    /**
     * GET endpoint for browser testing - Generate Excel file
     */
    @GetMapping("/generate-excel")
    public ResponseEntity<byte[]> generateExcelFileGet(@RequestParam(defaultValue = "10") int numberOfRecords) {
        try {
            byte[] excelData = fileProcessingService.generateExcelFile(numberOfRecords);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "test_students_" + numberOfRecords + ".xlsx");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelData);
                    
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * GET endpoint to check if file processing service is working
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("message", "File processing service is working!");
        status.put("timestamp", System.currentTimeMillis());
        status.put("endpoints", Map.of(
            "generate-excel", "/api/test-files/generate-excel?numberOfRecords=10",
            "main-endpoints", "/api/files/* (POST requests only)"
        ));
        return ResponseEntity.ok(status);
    }
}