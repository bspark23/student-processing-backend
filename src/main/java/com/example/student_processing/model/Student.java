package com.example.student_processing.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String className;

    @Column(nullable = false)
    private LocalDate dob;

    @Column(nullable = false)
    private Integer score;

    @Column(unique = true)
    private Long studentId;

    // Default constructor
    public Student() {}

    // Constructor for CSV/Excel processing
    public Student(String firstName, String lastName, String className, LocalDate dob, Integer score, Long studentId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.className = className;
        this.dob = dob;
        this.score = score;
        this.studentId = studentId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    // Helper method to get full name
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
