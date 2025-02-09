package org.jaysabva.woc_crs.service.Implementation;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.jaysabva.woc_crs.util.BCryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.jaysabva.woc_crs.service.ProfessorService;


import org.jaysabva.woc_crs.entity.Professor;
import org.jaysabva.woc_crs.dto.ProfessorDto;
import org.jaysabva.woc_crs.repository.ProfessorRepository;

import java.util.HashMap;
import java.util.Map;

@Service
public class ProfessorServiceImplementation implements ProfessorService {

    private final ProfessorRepository professorRepository;

    @Autowired
    public ProfessorServiceImplementation(ProfessorRepository professorRepository){
        this.professorRepository = professorRepository;
    }

    @Override
    public String updateProfessor(ProfessorDto professorDto, String email){

        Professor professor = professorRepository.findByEmail(email);
        if(professor == null){
            throw new EntityNotFoundException("Professor with this email does not exist");
        }

        if(professorRepository.findByEmail(professorDto.email()) != null) {
            throw new EntityExistsException("Professor with this email already exists");
        }

        professor.setName(professorDto.name());
        professor.setEmail(professorDto.email());
        professor.setPassword(BCryptUtil.hashPassword(professorDto.password()));

        try {
            professorRepository.save(professor);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to update professor due to database constraints");
        }

        return "Professor updated successfully";
    }

    @Override
    public Map<String, String> getProfessor(String email){
        Professor professor = professorRepository.findByEmail(email);
        if(professor == null){
            throw new EntityNotFoundException("Professor with this email does not exist");
        }
        return new HashMap<> (){{
            put("name", professor.getName());
            put("email", professor.getEmail());
        }};
    }
}
