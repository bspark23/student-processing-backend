package com.example.student_processing.config;

import com.example.student_processing.model.Student;
import com.example.student_processing.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public void run(String... args) throws Exception {
        // Only add sample data if database is empty
        if (studentRepository.count() == 0) {
            // Add some sample students
            studentRepository.save(new Student("John", "Doe", "Class A", LocalDate.of(2005, 3, 15), 85, 1001L));
            studentRepository.save(new Student("Jane", "Smith", "Class A", LocalDate.of(2005, 7, 22), 92, 1002L));
            studentRepository.save(new Student("Mike", "Johnson", "Class B", LocalDate.of(2005, 1, 10), 78, 1003L));
            studentRepository.save(new Student("Sarah", "Williams", "Class B", LocalDate.of(2005, 9, 5), 88, 1004L));
            studentRepository.save(new Student("David", "Brown", "Class C", LocalDate.of(2005, 12, 18), 95, 1005L));
            
            System.out.println("Sample data initialized: " + studentRepository.count() + " students added");
        }
    }
}