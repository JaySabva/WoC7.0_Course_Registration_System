package org.jaysabva.woc_crs.service;

import org.jaysabva.woc_crs.dto.StudentDto;
import org.jaysabva.woc_crs.dto.ProfessorDto;
import org.jaysabva.woc_crs.dto.SemesterDto;
import java.util.*;

public interface AdminService {

    String addStudent(StudentDto studentDto);

    String addProfessor(ProfessorDto professorDto);

    String deleteStudent(String email);

    String deleteProfessor(String email);

    String addSemester(SemesterDto semesterDto);

    List<SemesterDto> getAllSemesters();
}
