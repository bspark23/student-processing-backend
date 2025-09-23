package com.example.student_processing.service;

import com.example.student_processing.model.Student;
import com.example.student_processing.repository.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // GET all students with pagination
    public Page<Student> getAllStudents(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }

    // GET all students (no pagination)
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // GET one student by database ID
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ Student not found with id " + id));
    }

    // GET one student by student ID
    public Optional<Student> getStudentByStudentId(Long studentId) {
        return studentRepository.findByStudentId(studentId);
    }

    // Search students by name
    public Page<Student> searchStudentsByName(String name, Pageable pageable) {
        return studentRepository.findByNameContaining(name, pageable);
    }

    // Filter students by class
    public Page<Student> getStudentsByClass(String className, Pageable pageable) {
        return studentRepository.findByClassName(className, pageable);
    }

    // Get all distinct class names
    public List<String> getAllClassNames() {
        return studentRepository.findDistinctClassNames();
    }

    // CREATE new student
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    // BULK CREATE students (for CSV upload)
    public List<Student> saveAllStudents(List<Student> students) {
        return studentRepository.saveAll(students);
    }

    // UPDATE student
    public Student updateStudent(Long id, Student studentDetails) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ Student not found with id " + id));

        student.setFirstName(studentDetails.getFirstName());
        student.setLastName(studentDetails.getLastName());
        student.setClassName(studentDetails.getClassName());
        student.setDob(studentDetails.getDob());
        student.setScore(studentDetails.getScore());
        student.setStudentId(studentDetails.getStudentId());

        return studentRepository.save(student);
    }

    // DELETE student
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ Student not found with id " + id));

        studentRepository.delete(student);
    }

    // Get total count
    public long getTotalStudentCount() {
        return studentRepository.count();
    }

    // Get count by class
    public long getStudentCountByClass(String className) {
        return studentRepository.countByClassName(className);
    }
}
