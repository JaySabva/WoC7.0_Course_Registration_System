package org.jaysabva.woc_crs.service;

import org.jaysabva.woc_crs.dto.StudentDto;
import org.jaysabva.woc_crs.dto.ProfessorDto;

public interface AdminService {

    String addStudent(StudentDto studentDto);

    String addProfessor(ProfessorDto professorDto);

    String deleteStudent(String email);

    String deleteProfessor(String email);
}
