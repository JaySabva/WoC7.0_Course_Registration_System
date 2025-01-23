package org.jaysabva.woc_crs.service.Implementation;

import jakarta.persistence.EntityNotFoundException;
import org.jaysabva.woc_crs.dto.CourseDto;
import org.jaysabva.woc_crs.entity.Course;
import org.jaysabva.woc_crs.repository.CourseRepository;
import org.jaysabva.woc_crs.repository.SemesterRepository;
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
import org.jaysabva.woc_crs.dto.SemesterDto;
import org.jaysabva.woc_crs.entity.Semester;

import java.util.*;

@Service
public class AdminServiceImplementation implements AdminService {

    @Autowired
    private StudentRepository studentRepository;
    private ProfessorRepository professorRepository;
    private SemesterRepository semesterRepository;
    @Autowired
    private CourseRepository courseRepository;

    public AdminServiceImplementation(StudentRepository studentRepository, ProfessorRepository professorRepository, SemesterRepository semesterRepository) {
        this.studentRepository = studentRepository;
        this.professorRepository = professorRepository;
        this.semesterRepository = semesterRepository;
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

    @Override
    public String addSemester(SemesterDto semesterDto) {
        if (semesterRepository.findBySemesterName(semesterDto.semesterName()) != null) {
            throw new IllegalArgumentException("Semester with this name already exists");
        }

        Semester semester = new Semester(
            semesterDto.id(),
            semesterDto.semesterName(),
            semesterDto.startDate(),
            semesterDto.endDate(),
            "Active"
        );

        try {
            semesterRepository.save(semester);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Failed to add semester due to database constraints");
        }

        return "Semester added successfully";
    }

    @Override
    public String updateSemester(SemesterDto semesterDto, Long id) {
        Semester semester = semesterRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Semester with this id does not exist"));

        if (semesterRepository.findBySemesterName(semesterDto.semesterName()) != null) {
            throw new IllegalArgumentException("Semester with this name already exists");
        }

        semester.setSemesterName(semesterDto.semesterName());
        semester.setStartDate(semesterDto.startDate());
        semester.setEndDate(semesterDto.endDate());

        try {
            semesterRepository.save(semester);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Failed to update semester due to database constraints");
        }

        return "Semester updated successfully";
    }

    @Override
    public String deleteSemester(Long id) {
        Semester semester = semesterRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Semester with this id does not exist"));

        try {
            semesterRepository.delete(semester);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Failed to delete semester due to database constraints");
        }

        return "Semester deleted successfully";
    }
    @Override
    public List<SemesterDto> getAllSemesters() {
        List<Semester> semesters = semesterRepository.findAll();
        List<SemesterDto> semesterDtos = new ArrayList<>();

        for (Semester semester : semesters) {
            SemesterDto semesterDto = new SemesterDto(
                semester.getId(),
                semester.getSemesterName(),
                semester.getStartDate().toString(),
                semester.getEndDate().toString()
            );
            semesterDtos.add(semesterDto);
        }

        return semesterDtos;
    }

    @Override
    public String addCourse(CourseDto courseDto) {
        Professor professor = professorRepository.findById(courseDto.professorId()).orElseThrow(() -> new EntityNotFoundException("Professor with this id does not exist"));
        Semester semester = semesterRepository.findById(courseDto.semesterId()).orElseThrow(() -> new EntityNotFoundException("Semester with this name does not exist"));

        Course course = new Course(
            courseDto.id(),
            courseDto.courseName(),
            courseDto.courseCode(),
            courseDto.credit(),
            courseDto.max_enrollment(),
            courseDto.curr_enrollment(),
            professor,
            semester
        );

        try {
            courseRepository.save(course);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Failed to add course due to database constraints");
        }

        return "Course added successfully";
    }

    @Override
    public String updateCourse(CourseDto courseDto, Long id) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Course with this id does not exist"));

        Professor professor = professorRepository.findById(courseDto.professorId()).orElseThrow(() -> new EntityNotFoundException("Professor with this id does not exist"));
        Semester semester = semesterRepository.findById(courseDto.semesterId()).orElseThrow(() -> new EntityNotFoundException("Semester with this name does not exist"));

        course.setCourseName(courseDto.courseName());
        course.setCourseCode(courseDto.courseCode());
        course.setCredits(courseDto.credit());
        course.setMax_enrollment(courseDto.max_enrollment());
        course.setCurr_enrollment(courseDto.curr_enrollment());
        course.setProfessor(professor);
        course.setSemester(semester);

        try {
            courseRepository.save(course);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Failed to update course due to database constraints");
        }

        return "Course updated successfully";
    }

    @Override
    public String deleteCourse(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Course with this id does not exist"));

        try {
            courseRepository.delete(course);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Failed to delete course due to database constraints");
        }

        return "Course deleted successfully";
    }

    @Override
    public List<Course> getAllCourses() {
        List<Course> courses = courseRepository.findAll();

        return courses;
    }
}
