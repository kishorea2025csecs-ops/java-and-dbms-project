package com.parkease;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ParkEase - Smart Parking Management System
 * Full-Stack OOP & DBMS Academic Project
 * 
 * Demonstrates:
 * - Spring Boot 3 MVC & Spring Data JPA
 * - Object-Oriented Programming (Polymorphism, Inheritance, Encapsulation, Abstraction)
 * - MySQL Relational Database Management System Design
 */
@SpringBootApplication
public class ParkEaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParkEaseApplication.class, args);
        System.out.println("=========================================================");
        System.out.println("  ParkEase Smart Parking System is RUNNING!");
        System.out.println("  Access Dashboard: http://localhost:8080");
        System.out.println("  Default Admin Login: admin / admin123");
        System.out.println("=========================================================");
    }
}

