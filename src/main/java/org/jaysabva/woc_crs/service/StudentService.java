package org.jaysabva.woc_crs.service;

import org.jaysabva.woc_crs.dto.RequestDto;
import org.jaysabva.woc_crs.dto.StudentDto;
import org.jaysabva.woc_crs.entity.Registration;
import org.jaysabva.woc_crs.entity.Request;
import org.jaysabva.woc_crs.entity.Semester;

import java.util.*;

public interface StudentService {
    String updateStudent(StudentDto studentDto, String email);
    Map<String, String> getStudent(String email);

    String requestCourse(RequestDto requestDto);
    Map<String, Map<String, Object>> getRegisteredCourses(Long id);

    Map<String, Object> getRegisteredCourses(Long id, Long semesterID);

    byte[] generateTranscript(Long studentID, Long semesterID);
}
