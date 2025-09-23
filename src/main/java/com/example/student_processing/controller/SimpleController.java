package com.example.student_processing.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello World! Spring Boot is working!";
    }

    @GetMapping("/test")
    public String test() {
        return "Test endpoint working!";
    }
}