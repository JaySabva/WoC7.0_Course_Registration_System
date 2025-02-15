package org.jaysabva.woc_crs.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.jaysabva.woc_crs.dto.CourseDto;
import org.jaysabva.woc_crs.entity.Course;
import org.jaysabva.woc_crs.entity.Registration;
import org.jaysabva.woc_crs.entity.Request;
import org.jaysabva.woc_crs.entity.Semester;
import org.jaysabva.woc_crs.util.EmailSenderService;
import org.jaysabva.woc_crs.util.Role;
import org.jaysabva.woc_crs.util.RoleRequired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.jaysabva.woc_crs.service.AdminService;
import org.jaysabva.woc_crs.dto.StudentDto;
import org.jaysabva.woc_crs.dto.ProfessorDto;
import org.jaysabva.woc_crs.dto.SemesterDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin Controller", description = "APIs for Admin")
@Validated

@RoleRequired(Role.ADMIN)
public class AdminController {

    private final AdminService adminService;
    private final EmailSenderService emailSenderService;

    @Autowired
    public AdminController(AdminService adminService, EmailSenderService emailSenderService) {
        this.adminService = adminService;
        this.emailSenderService = emailSenderService;
    }

    @PostMapping("/add-student")
    public ResponseEntity<String> addStudent(@RequestBody @Valid StudentDto studentDto) {
        try {
            String resultMessage = adminService.addStudent(studentDto);

            emailSenderService.sendEmail(studentDto.email(), "Welcome to Course Registration System", emailSenderService.registrationEmail(studentDto.name(), studentDto.email(), studentDto.password(), "Student", "github.com/JaySabva"));

            return ResponseEntity.status(HttpStatus.CREATED).body(resultMessage);
        } catch (EntityExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unexpected error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/add-professor")
    public ResponseEntity<String> addProfessor(@RequestBody @Valid ProfessorDto professorDto) {
        try {
            String resultMessage = adminService.addProfessor(professorDto);

            emailSenderService.sendEmail(professorDto.email(), "Welcome to Course Registration System", emailSenderService.registrationEmail(professorDto.name(), professorDto.email(), professorDto.password(), "Professor", "github.com/JaySabva"));

            return ResponseEntity.status(HttpStatus.CREATED).body(resultMessage);
        } catch (EntityExistsException e) {
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
        } catch (EntityNotFoundException e) {
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
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unexpected error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/add-semester")
    public ResponseEntity<String> addSemester(@RequestBody @Valid SemesterDto semesterDto) {
        try {
            String resultMessage = adminService.addSemester(semesterDto);

            return ResponseEntity.status(HttpStatus.CREATED).body(resultMessage);
        } catch (EntityExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unexpected error occurred: " + e.getMessage());
        }
    }

    @PutMapping("/update-semester/{id}")
    public ResponseEntity<String> updateSemester(@RequestBody @Valid SemesterDto semesterDto, @PathVariable Long id) {
        try {
            String resultMessage = adminService.updateSemester(semesterDto, id);

            return ResponseEntity.status(HttpStatus.OK).body(resultMessage);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (EntityExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unexpected error occurred: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete-semester/{id}")
    public ResponseEntity<String> deleteSemester(@PathVariable Long id) {
        try {
            String resultMessage = adminService.deleteSemester(id);

            return ResponseEntity.status(HttpStatus.OK).body(resultMessage);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/get-all-semesters")
    public ResponseEntity<List<Semester>> getAllSemesters() {
        try {
            List<Semester> semesters = adminService.getAllSemesters();

            return ResponseEntity.status(HttpStatus.OK).body(semesters);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ArrayList<>());
        }
    }

    @PostMapping("/add-course")
    public ResponseEntity<String> addCourse(@RequestBody @Valid CourseDto courseDto) {
        try {
            String resultMessage = adminService.addCourse(courseDto);

            return ResponseEntity.status(HttpStatus.CREATED).body(resultMessage);
        } catch (EntityExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unexpected error occurred: " + e.getMessage());
        }
    }

    @PutMapping("/update-course/{id}")
    public ResponseEntity<String> updateCourse(@RequestBody @Valid CourseDto courseDto, @PathVariable Long id) {
        try {
            String resultMessage = adminService.updateCourse(courseDto, id);

            return ResponseEntity.status(HttpStatus.OK).body(resultMessage);
        } catch (EntityNotFoundException e) {
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
        } catch (EntityNotFoundException e) {
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

    @GetMapping("/send-email-notification")
    public ResponseEntity<String> sendEmailNotification() {
        try {
            adminService.sendEmailNotification();

            return ResponseEntity.status(HttpStatus.OK).body("Email notification sent successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/assign-course")
    public ResponseEntity<String> assignCourses() {
        try {
            adminService.assignCourse();

            return ResponseEntity.status(HttpStatus.OK).body("Courses are Assigned");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/get-all-registration")
    public ResponseEntity<List<Registration>> getAllRegistration() {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.getAllRegistration());
    }

    @GetMapping("/send-course-assign-email-notification")
    public ResponseEntity<String> sendCourseAssignEmailNotification() {
        try {
            adminService.sendCourseAssignEmailNotification();

            return ResponseEntity.status(HttpStatus.OK).body("Course Assign Email notification sent successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/get-all-requests")
    public ResponseEntity<List<Request>> getAllRequest() {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.getAllRequests());
    }

    @PostMapping("/assign-grades-for-course/{courseID}")
    public ResponseEntity<String> assignGradesForCourse(@RequestParam("file") MultipartFile file, @PathVariable Long courseID) {
        try {
            adminService.assignGradesForCourse(file, courseID);

            return ResponseEntity.status(HttpStatus.OK).body("Grades assigned successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unexpected error occurred: " + e.getMessage());
        }
    }
}
