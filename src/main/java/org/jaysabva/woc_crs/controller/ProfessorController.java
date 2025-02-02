package org.jaysabva.woc_crs.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.jaysabva.woc_crs.service.AdminService;
import org.jaysabva.woc_crs.service.ProfessorService;
import org.jaysabva.woc_crs.service.StudentService;
import org.jaysabva.woc_crs.dto.StudentDto;
import org.jaysabva.woc_crs.dto.ProfessorDto;

import java.util.*;

@RestController
@RequestMapping("/professor")
@Tag(name = "Professor Controller", description = "APIs for Professor")
@Validated
public class ProfessorController {

    private final ProfessorService professorService;

    @Autowired
    public ProfessorController(ProfessorService professorService){
        this.professorService = professorService;
    }

    @PutMapping("/update-professor/{email}")
    public ResponseEntity<String> updateProfessor(@RequestBody @Valid ProfessorDto professorDto, @PathVariable String email){
        try{
            String resultMessage = professorService.updateProfessor(professorDto, email);
            return ResponseEntity.status(HttpStatus.OK).body(resultMessage);
        } catch(EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/get-professor/{email}")
    public ResponseEntity<Map<String, String>> getProfessor(@PathVariable String email){
        try{
            Map<String, String> professorDto= professorService.getProfessor(email);

            return ResponseEntity.status(HttpStatus.OK).body(professorDto);
        } catch(EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new HashMap<>());
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new HashMap<>());
        }
    }
}
