package com.example.student_processing.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
public class SimpleUploadController {

    @PostMapping("/upload")
    public Map<String, Object> uploadFile(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        
        if (file.isEmpty()) {
            response.put("error", "No file uploaded");
            response.put("status", "ERROR");
            return response;
        }
        
        response.put("message", "File uploaded successfully!");
        response.put("filename", file.getOriginalFilename());
        response.put("size", file.getSize());
        response.put("contentType", file.getContentType());
        response.put("status", "SUCCESS");
        
        return response;
    }

    @GetMapping("/test-interface")
    public String testInterface() {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <title>Student Processing System - Test Interface</title>
                <style>
                    body { font-family: Arial, sans-serif; margin: 20px; }
                    .section { margin: 20px 0; padding: 15px; border: 1px solid #ccc; }
                    .button { padding: 10px 15px; margin: 5px; background: #007bff; color: white; border: none; cursor: pointer; }
                    .button:hover { background: #0056b3; }
                    input[type="file"] { margin: 10px 0; }
                    input[type="number"] { width: 100px; }
                    .result { margin: 10px 0; padding: 10px; background: #f8f9fa; border: 1px solid #dee2e6; }
                </style>
            </head>
            <body>
                <h1>🎓 Student Processing System - Test Interface</h1>
                
                <div class="section">
                    <h2>📊 1. Generate Excel File</h2>
                    <form action="/api/files/generate-excel" method="post">
                        <label>Number of records: </label>
                        <input type="number" name="numberOfRecords" value="10" min="1" max="1000">
                        <button type="submit" class="button">Generate Excel</button>
                    </form>
                </div>
                
                <div class="section">
                    <h2>🔄 2. Convert Excel to CSV</h2>
                    <form action="/api/files/excel-to-csv" method="post" enctype="multipart/form-data">
                        <label>Select Excel file: </label>
                        <input type="file" name="file" accept=".xlsx,.xls" required>
                        <button type="submit" class="button">Convert to CSV</button>
                    </form>
                </div>
                
                <div class="section">
                    <h2>💾 3. Upload CSV to Database</h2>
                    <form action="/api/files/upload-csv" method="post" enctype="multipart/form-data">
                        <label>Select CSV file: </label>
                        <input type="file" name="file" accept=".csv" required>
                        <button type="submit" class="button">Upload to Database</button>
                    </form>
                </div>
                
                <div class="section">
                    <h2>👥 4. Student Management</h2>
                    <a href="/api/students" target="_blank"><button class="button">View All Students</button></a>
                    <a href="/api/health" target="_blank"><button class="button">Health Check</button></a>
                    <a href="/api/health/endpoints" target="_blank"><button class="button">All Endpoints</button></a>
                </div>
                
                <div class="section">
                    <h2>📈 5. Export Data</h2>
                    <a href="/api/export/excel"><button class="button">Export to Excel</button></a>
                    <a href="/api/export/csv"><button class="button">Export to CSV</button></a>
                    <a href="/api/export/pdf"><button class="button">Export to PDF</button></a>
                </div>
                
                <div class="section">
                    <h2>➕ 6. Add Student Manually</h2>
                    <form id="addStudentForm">
                        <input type="text" id="firstName" placeholder="First Name" required>
                        <input type="text" id="lastName" placeholder="Last Name" required>
                        <input type="text" id="className" placeholder="Class Name" required>
                        <input type="date" id="dob" required>
                        <input type="number" id="score" placeholder="Score" min="0" max="100" required>
                        <input type="number" id="studentId" placeholder="Student ID" required>
                        <button type="button" class="button" onclick="addStudent()">Add Student</button>
                    </form>
                    <div id="addResult" class="result" style="display:none;"></div>
                </div>
                
                <script>
                    async function addStudent() {
                        const student = {
                            firstName: document.getElementById('firstName').value,
                            lastName: document.getElementById('lastName').value,
                            className: document.getElementById('className').value,
                            dob: document.getElementById('dob').value,
                            score: parseInt(document.getElementById('score').value),
                            studentId: parseInt(document.getElementById('studentId').value)
                        };
                        
                        try {
                            const response = await fetch('/api/students', {
                                method: 'POST',
                                headers: { 'Content-Type': 'application/json' },
                                body: JSON.stringify(student)
                            });
                            
                            const result = await response.json();
                            const resultDiv = document.getElementById('addResult');
                            resultDiv.style.display = 'block';
                            
                            if (response.ok) {
                                resultDiv.innerHTML = '<strong>✅ Success!</strong><br>' + JSON.stringify(result, null, 2);
                                document.getElementById('addStudentForm').reset();
                            } else {
                                resultDiv.innerHTML = '<strong>❌ Error!</strong><br>' + JSON.stringify(result, null, 2);
                            }
                        } catch (error) {
                            document.getElementById('addResult').innerHTML = '<strong>❌ Error!</strong><br>' + error.message;
                        }
                    }
                </script>
            </body>
            </html>
            """;
    }
}