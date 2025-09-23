package com.example.student_processing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.student_processing")
public class StudentProcessingApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentProcessingApplication.class, args);
    }
}
