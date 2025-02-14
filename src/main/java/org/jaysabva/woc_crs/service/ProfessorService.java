package org.jaysabva.woc_crs.service;

import org.jaysabva.woc_crs.dto.ProfessorDto;

import java.util.*;

public interface ProfessorService {
    String updateProfessor(ProfessorDto professorDto, String email);
    Map<String, String> getProfessor(String email);
    Map<String, Map<String, Object>> getAllCourses(Long professorId);
}
