package org.jaysabva.woc_crs.controller;

import org.jaysabva.woc_crs.dto.RequestDto;
import org.jaysabva.woc_crs.entity.Request;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.jaysabva.woc_crs.service.AdminService;
import org.jaysabva.woc_crs.service.StudentService;
import org.jaysabva.woc_crs.dto.StudentDto;
import org.jaysabva.woc_crs.dto.ProfessorDto;

import java.util.*;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService){
        this.studentService = studentService;
    }

    @PutMapping("/update-student/{email}")
    public ResponseEntity<String> updateStudent(@RequestBody StudentDto studentDto, @PathVariable String email){
        try{
            String resultMessage = studentService.updateStudent(studentDto, email);
            return ResponseEntity.status(HttpStatus.OK).body(resultMessage);
        } catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/get-student/{email}")
    public ResponseEntity<Map<String, String>> getStudent(@PathVariable String email){
        try{
            Map<String, String> studentDto= studentService.getStudent(email);

            return ResponseEntity.status(HttpStatus.OK).body(studentDto);
        } catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new HashMap());
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new HashMap());
        }
    }

    @PostMapping("/submit-course-form")
    public ResponseEntity<String> requestCourse(@RequestBody RequestDto requestDto){
        try{
            String resultMessage = studentService.requestCourse(requestDto);
            return ResponseEntity.status(HttpStatus.OK).body(resultMessage);
        } catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/get-all-requests")
    public ResponseEntity<List<Request>> getAllRequest() {
        return ResponseEntity.status(HttpStatus.OK).body(studentService.getAllRequests());
    }
}
