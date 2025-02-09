package org.jaysabva.woc_crs.service;

import org.jaysabva.woc_crs.dto.*;
import org.jaysabva.woc_crs.entity.Course;
import org.jaysabva.woc_crs.entity.Registration;
import org.jaysabva.woc_crs.entity.Request;
import org.jaysabva.woc_crs.entity.Semester;

import java.util.*;

public interface AdminService {

    String addStudent(StudentDto studentDto);

    String addProfessor(ProfessorDto professorDto);

    String deleteStudent(String email);

    String deleteProfessor(String email);

    String addSemester(SemesterDto semesterDto);

    String updateSemester(SemesterDto semesterDto, Long id);

    String deleteSemester(Long id);

    List<Semester> getAllSemesters();

    String addCourse(CourseDto courseDto);

    String updateCourse(CourseDto courseDto, Long id);

    String deleteCourse(Long id);

    List<Course> getAllCourses();

    void sendEmailNotification();

    void assignCourse();

    List<Registration> getAllRegistration();

    void sendCourseAssignEmailNotification();

    List<Request> getAllRequests();
}
