package org.jaysabva.woc_crs.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.jaysabva.woc_crs.service.AdminService;
import org.jaysabva.woc_crs.dto.StudentDto;
import org.jaysabva.woc_crs.dto.ProfessorDto;
import org.jaysabva.woc_crs.dto.SemesterDto;

import java.util.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/add-student")
    public ResponseEntity<String> addStudent(@RequestBody StudentDto studentDto) {
        try {
            String resultMessage = adminService.addStudent(studentDto);

            return ResponseEntity.status(HttpStatus.CREATED).body(resultMessage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unexpected error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/add-professor")
    public ResponseEntity<String> addProfessor(@RequestBody ProfessorDto professorDto) {
        try {
            String resultMessage = adminService.addProfessor(professorDto);

            return ResponseEntity.status(HttpStatus.CREATED).body(resultMessage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unexpected error occurred: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete-student/{email}")
    public ResponseEntity<String> deleteStudent(@PathVariable String email) {
        try {
            String resultMessage = adminService.deleteStudent(email);

            return ResponseEntity.status(HttpStatus.OK).body(resultMessage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unexpected error occurred: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete-professor/{email}")
    public ResponseEntity<String> deleteProfessor(@PathVariable String email) {
        try {
            String resultMessage = adminService.deleteProfessor(email);

            return ResponseEntity.status(HttpStatus.OK).body(resultMessage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unexpected error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/add-semester")
    public ResponseEntity<String> addSemester(@RequestBody SemesterDto semesterDto) {
        try {
            String resultMessage = adminService.addSemester(semesterDto);

            return ResponseEntity.status(HttpStatus.CREATED).body(resultMessage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/get-all-semesters")
    public ResponseEntity<List<SemesterDto>> getAllSemesters() {
        try {
            List<SemesterDto> semesters = adminService.getAllSemesters();

            return ResponseEntity.status(HttpStatus.OK).body(semesters);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ArrayList<>());
        }
    }
}
