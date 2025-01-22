package org.jaysabva.woc_crs.service;

import org.jaysabva.woc_crs.dto.StudentDto;
import org.jaysabva.woc_crs.dto.ProfessorDto;

import java.util.*;

public interface StudentService {
    String updateStudent(StudentDto studentDto, String email);
    Map<String, String> getStudent(String email);
}
