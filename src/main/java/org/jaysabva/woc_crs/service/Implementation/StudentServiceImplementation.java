package org.jaysabva.woc_crs.service.Implementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import org.jaysabva.woc_crs.service.AdminService;

import org.jaysabva.woc_crs.entity.Student;
import org.jaysabva.woc_crs.entity.Professor;
import org.jaysabva.woc_crs.dto.StudentDto;
import org.jaysabva.woc_crs.repository.StudentRepository;
import org.jaysabva.woc_crs.dto.ProfessorDto;
import org.jaysabva.woc_crs.repository.ProfessorRepository;
import org.jaysabva.woc_crs.service.StudentService;

import java.util.*;

@Service
public class StudentServiceImplementation implements StudentService{

    @Autowired
    private StudentRepository studentRepository;

    public StudentServiceImplementation(StudentRepository studentRepository){
        this.studentRepository = studentRepository;
    }

    @Override
    public String updateStudent(StudentDto studentDto, String email){

        Student student = studentRepository.findByEmail(email);
        if(student == null){
            throw new IllegalArgumentException("Student with this email does not exist");
        }

        if(studentRepository.findByEmail(studentDto.email()) != null) {
            throw new IllegalArgumentException("Student with this email already exists");
        }

        student.setName(studentDto.name());
        student.setEmail(studentDto.email());
        student.setPassword(studentDto.password());

        try {
            studentRepository.save(student);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Failed to update student due to database constraints");
        }

        return "Student updated successfully";
    }

    @Override
    public Map<String, String> getStudent(String email){
        Student student = studentRepository.findByEmail(email);
        if(student == null){
            throw new IllegalArgumentException("Student with this email does not exist");
        }
        return new HashMap<String, String>(){{
            put("name", student.getName());
            put("email", student.getEmail());
        }};
    }

}
