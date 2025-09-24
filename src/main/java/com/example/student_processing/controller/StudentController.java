package com.example.student_processing.controller;

import com.example.student_processing.model.Student;
import com.example.student_processing.service.FileProcessingService;
import com.example.student_processing.service.StudentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;
    private final FileProcessingService fileProcessingService;

    public StudentController(StudentService studentService, FileProcessingService fileProcessingService) {
        this.studentService = studentService;
        this.fileProcessingService = fileProcessingService;
    }

    // ✅ GET all students with pagination and search
    @GetMapping
    public Map<String, Object> getAllStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String className) {
        
        try {
            List<Student> allStudents = studentService.getAllStudents();
            
            // Simple filtering (you can enhance this with database queries)
            List<Student> filteredStudents = allStudents.stream()
                .filter(student -> {
                    boolean matchesSearch = search == null || search.trim().isEmpty() ||
                        student.getFirstName().toLowerCase().contains(search.toLowerCase()) ||
                        student.getLastName().toLowerCase().contains(search.toLowerCase());
                    
                    boolean matchesClass = className == null || className.trim().isEmpty() ||
                        student.getClassName().equals(className);
                    
                    return matchesSearch && matchesClass;
                })
                .toList();
            
            // Simple pagination
            int totalItems = filteredStudents.size();
            int totalPages = (int) Math.ceil((double) totalItems / size);
            int startIndex = page * size;
            int endIndex = Math.min(startIndex + size, totalItems);
            
            List<Student> pageStudents = filteredStudents.subList(startIndex, endIndex);
            
            Map<String, Object> response = new HashMap<>();
            response.put("students", pageStudents);
            response.put("currentPage", page);
            response.put("totalItems", totalItems);
            response.put("totalPages", totalPages);
            response.put("hasNext", page < totalPages - 1);
            response.put("hasPrevious", page > 0);
            
            return response;
        } catch (Exception e) {
            throw new RuntimeException("Error getting students: " + e.getMessage());
        }
    }

    // ✅ GET one student by database ID
    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    // ✅ GET one student by student ID
    @GetMapping("/student/{studentId}")
    public ResponseEntity<Student> getStudentByStudentId(@PathVariable Long studentId) {
        Optional<Student> student = studentService.getStudentByStudentId(studentId);
        return student.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    // ✅ GET all class names
    @GetMapping("/classes")
    public List<String> getAllClassNames() {
        return studentService.getAllClassNames();
    }

    // ✅ GET statistics
    @GetMapping("/stats")
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        List<Student> allStudents = studentService.getAllStudents();
        
        stats.put("totalStudents", allStudents.size());
        stats.put("totalClasses", studentService.getAllClassNames().size());
        
        if (!allStudents.isEmpty()) {
            // Calculate score statistics
            double averageScore = allStudents.stream()
                .mapToInt(Student::getScore)
                .average()
                .orElse(0.0);
            
            int highestScore = allStudents.stream()
                .mapToInt(Student::getScore)
                .max()
                .orElse(0);
            
            int lowestScore = allStudents.stream()
                .mapToInt(Student::getScore)
                .min()
                .orElse(0);
            
            stats.put("averageScore", Math.round(averageScore * 100.0) / 100.0);
            stats.put("highestScore", highestScore);
            stats.put("lowestScore", lowestScore);
            
            // Grade distribution
            long excellentCount = allStudents.stream().filter(s -> s.getScore() >= 90).count();
            long goodCount = allStudents.stream().filter(s -> s.getScore() >= 70 && s.getScore() < 90).count();
            long averageCount = allStudents.stream().filter(s -> s.getScore() >= 50 && s.getScore() < 70).count();
            long belowAverageCount = allStudents.stream().filter(s -> s.getScore() < 50).count();
            
            stats.put("gradeDistribution", Map.of(
                "excellent", excellentCount,
                "good", goodCount,
                "average", averageCount,
                "belowAverage", belowAverageCount
            ));
        } else {
            stats.put("averageScore", 0);
            stats.put("highestScore", 0);
            stats.put("lowestScore", 0);
            stats.put("gradeDistribution", Map.of(
                "excellent", 0,
                "good", 0,
                "average", 0,
                "belowAverage", 0
            ));
        }
        
        return stats;
    }

    // ✅ CREATE new student
    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.saveStudent(student);
    }

    // ✅ UPDATE student by ID
    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable Long id, @RequestBody Student studentDetails) {
        return studentService.updateStudent(id, studentDetails);
    }

    // ✅ DELETE student by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Student with ID " + id + " deleted successfully!");
        return ResponseEntity.ok(response);
    }
}
