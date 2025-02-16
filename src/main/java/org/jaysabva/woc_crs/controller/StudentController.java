package org.jaysabva.woc_crs.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.hibernate.ResourceClosedException;
import org.jaysabva.woc_crs.config.JwtContext;
import org.jaysabva.woc_crs.dto.RequestDto;
import org.jaysabva.woc_crs.entity.Registration;
import org.jaysabva.woc_crs.entity.Request;
import org.jaysabva.woc_crs.entity.Semester;
import org.jaysabva.woc_crs.util.Role;
import org.jaysabva.woc_crs.util.RoleRequired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.jaysabva.woc_crs.service.StudentService;
import org.jaysabva.woc_crs.dto.StudentDto;

import java.util.*;

@RestController
@RequestMapping("/student")
@Tag(name = "Student Controller", description = "APIs for Student")
@Validated

@RoleRequired(Role.STUDENT)
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService){
        this.studentService = studentService;
    }

    @PutMapping("/update-student")
    public ResponseEntity<String> updateStudent(@RequestBody @Valid StudentDto studentDto){
        try{
            String email = JwtContext.getEmail();

            String resultMessage = studentService.updateStudent(studentDto, email);
            return ResponseEntity.status(HttpStatus.OK).body(resultMessage);
        } catch(EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (EntityExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/get-student")
    public ResponseEntity<Map<String, String>> getStudent(){
        try{
            String email = JwtContext.getEmail();

            Map<String, String> studentDto= studentService.getStudent(email);

            return ResponseEntity.status(HttpStatus.OK).body(studentDto);
        } catch(EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new HashMap<>());
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new HashMap<>());
        }
    }

    @PostMapping("/submit-course-form")
    public ResponseEntity<String> requestCourse(@RequestBody @Valid RequestDto requestDto){
        try{
            Long studentId = JwtContext.getId();
            requestDto = requestDto.withStudentId(studentId);

            String resultMessage = studentService.requestCourse(requestDto);
            return ResponseEntity.status(HttpStatus.OK).body(resultMessage);
        } catch(EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ResourceClosedException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/get-registered-courses/")
    public ResponseEntity<Map<String, Map<String, Object>>> getRegisteredCourses() {
        try {
            Long id = JwtContext.getId();

            return ResponseEntity.status(HttpStatus.OK).body(studentService.getRegisteredCourses(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new HashMap<>());
        }
    }

    @GetMapping("/get-registered-courses/{semesterID}")
    public ResponseEntity<Map<String, Object>> getRegisteredCourses(@PathVariable Long semesterID) {
        try {
            Long id = JwtContext.getId();
            return ResponseEntity.status(HttpStatus.OK).body(studentService.getRegisteredCourses(id, semesterID));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new HashMap<>());
        }
    }
}
