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

@Service
public class AdminServiceImplementation implements AdminService {

    @Autowired
    private StudentRepository studentRepository;
    private ProfessorRepository professorRepository;

    public AdminServiceImplementation(StudentRepository studentRepository, ProfessorRepository professorRepository) {
        this.studentRepository = studentRepository;
        this.professorRepository = professorRepository;
    }

    @Override
    public String addStudent(StudentDto studentDto) {

        if(studentRepository.findByEmail(studentDto.email()) != null) {
            throw new IllegalArgumentException("Student with this email already exists");
        }

        Student student = new Student(
            studentDto.id(),
            studentDto.name(),
            studentDto.email(),
            studentDto.password()
        );

        try {
            studentRepository.save(student);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Failed to add student due to database constraints");
        }

        return "Student added successfully";
    }

    @Override
    public String addProfessor(ProfessorDto professorDto) {

        if(professorRepository.findByEmail(professorDto.email()) != null) {
            throw new IllegalArgumentException("Professor with this email already exists");
        }

        Professor professor = new Professor(
            professorDto.id(),
            professorDto.name(),
            professorDto.email(),
            professorDto.password()
        );

        try {
            professorRepository.save(professor);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Failed to add professor due to database constraints");
        }

        return "Professor added successfully";
    }

    @Override
    @Transactional
    public String deleteStudent(String email) {

        Student student = studentRepository.findByEmail(email);

        if(student == null) {
            throw new IllegalArgumentException("Student with this email does not exist");
        }

        studentRepository.deleteByEmail(email);

        return "Student deleted successfully";
    }

    @Override
    @Transactional
    public String deleteProfessor(String email) {

        Professor professor = professorRepository.findByEmail(email);

        if(professor == null) {
            throw new IllegalArgumentException("Professor with this email does not exist");
        }

        professorRepository.deleteByEmail(email);

        return "Professor deleted successfully";
    }
}
