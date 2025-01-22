package org.jaysabva.woc_crs.service.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import org.jaysabva.woc_crs.service.AdminService;
import org.jaysabva.woc_crs.service.ProfessorService;


import org.jaysabva.woc_crs.entity.Student;
import org.jaysabva.woc_crs.entity.Professor;
import org.jaysabva.woc_crs.dto.StudentDto;
import org.jaysabva.woc_crs.repository.StudentRepository;
import org.jaysabva.woc_crs.dto.ProfessorDto;
import org.jaysabva.woc_crs.repository.ProfessorRepository;
import org.jaysabva.woc_crs.service.StudentService;

@Service
public class ProfessorServiceImplementation implements ProfessorService {

    @Autowired
    private ProfessorRepository professorRepository;

    public ProfessorServiceImplementation(ProfessorRepository professorRepository){
        this.professorRepository = professorRepository;
    }

    @Override
    public String updateProfessor(ProfessorDto professorDto, String email){

        Professor professor = professorRepository.findByEmail(email);
        if(professor == null){
            throw new IllegalArgumentException("Professor with this email does not exist");
        }

        if(professorRepository.findByEmail(professorDto.email()) != null) {
            throw new IllegalArgumentException("Professor with this email already exists");
        }

        professor.setName(professorDto.name());
        professor.setEmail(professorDto.email());
        professor.setPassword(professorDto.password());

        try {
            professorRepository.save(professor);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Failed to update professor due to database constraints");
        }

        return "Professor updated successfully";
    }

    @Override
    public java.util.Map<String, String> getProfessor(String email){
        Professor professor = professorRepository.findByEmail(email);
        if(professor == null){
            throw new IllegalArgumentException("Professor with this email does not exist");
        }
        return new java.util.HashMap<String, String>(){{
            put("name", professor.getName());
            put("email", professor.getEmail());
        }};
    }
}
