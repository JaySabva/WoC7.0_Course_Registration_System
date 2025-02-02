package org.jaysabva.woc_crs.service;

import org.jaysabva.woc_crs.dto.RequestDto;
import org.jaysabva.woc_crs.dto.StudentDto;
import org.jaysabva.woc_crs.entity.Registration;
import org.jaysabva.woc_crs.entity.Request;

import java.util.*;

public interface StudentService {
    String updateStudent(StudentDto studentDto, String email);
    Map<String, String> getStudent(String email);

    String requestCourse(RequestDto requestDto);
    List<Request> getAllRequests();
    List<Registration> getRegisteredCourses(Long id);
}
