package org.jaysabva.woc_crs.controller;

import org.jaysabva.woc_crs.dto.CourseDto;
import org.jaysabva.woc_crs.entity.Course;
import org.jaysabva.woc_crs.util.EmailSenderService;
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

    @Autowired
    private EmailSenderService emailSenderService;

    @PostMapping("/add-student")
    public ResponseEntity<String> addStudent(@RequestBody StudentDto studentDto) {
        try {
            String resultMessage = adminService.addStudent(studentDto);

            emailSenderService.sendEmail(studentDto.email(), "Welcome to Course Registration System", emailSenderService.registrationEmail(studentDto.name(), studentDto.email(), studentDto.password(), "Student", "github.com/JaySabva"));

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

            emailSenderService.sendEmail(professorDto.email(), "Welcome to Course Registration System", emailSenderService.registrationEmail(professorDto.name(), professorDto.email(), professorDto.password(), "Professor", "github.com/JaySabva"));

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

    @PutMapping("/update-semester/{id}")
    public ResponseEntity<String> updateSemester(@RequestBody SemesterDto semesterDto, @PathVariable Long id) {
        try {
            String resultMessage = adminService.updateSemester(semesterDto, id);

            return ResponseEntity.status(HttpStatus.OK).body(resultMessage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unexpected error occurred: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete-semester/{id}")
    public ResponseEntity<String> deleteSemester(@PathVariable Long id) {
        try {
            String resultMessage = adminService.deleteSemester(id);

            return ResponseEntity.status(HttpStatus.OK).body(resultMessage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
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

    @PostMapping("/add-course")
    public ResponseEntity<String> addCourse(@RequestBody CourseDto courseDto) {
        try {
            String resultMessage = adminService.addCourse(courseDto);

            return ResponseEntity.status(HttpStatus.CREATED).body(resultMessage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unexpected error occurred: " + e.getMessage());
        }
    }

    @PutMapping("/update-course/{id}")
    public ResponseEntity<String> updateCourse(@RequestBody CourseDto courseDto, @PathVariable Long id) {
        try {
            String resultMessage = adminService.updateCourse(courseDto, id);

            return ResponseEntity.status(HttpStatus.OK).body(resultMessage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unexpected error occurred: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete-course/{id}")
    public ResponseEntity<String> deleteCourse(@PathVariable Long id) {
        try {
            String resultMessage = adminService.deleteCourse(id);

            return ResponseEntity.status(HttpStatus.OK).body(resultMessage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/get-all-courses")
    public ResponseEntity<List<Course>> getAllCourses() {
        try {
            List<Course> courses = adminService.getAllCourses();

            return ResponseEntity.status(HttpStatus.OK).body(courses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ArrayList<>());
        }
    }
}
