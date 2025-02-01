package org.jaysabva.woc_crs.service.Implementation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.SynchronizationType;
import org.jaysabva.woc_crs.dto.RequestDto;
import org.jaysabva.woc_crs.entity.*;
import org.jaysabva.woc_crs.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import org.jaysabva.woc_crs.service.AdminService;

import org.jaysabva.woc_crs.dto.StudentDto;
import org.jaysabva.woc_crs.dto.ProfessorDto;
import org.jaysabva.woc_crs.service.StudentService;

import java.time.LocalDate;
import java.util.*;

@Service
public class StudentServiceImplementation implements StudentService{

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private SemesterRepository semesterRepository;

    public StudentServiceImplementation(StudentRepository studentRepository){
        this.studentRepository = studentRepository;
    }

    @Override
    public String updateStudent(StudentDto studentDto, String email){

        Student student = studentRepository.findByEmail(email);
        if(student == null){
            throw new IllegalArgumentException("Student with this email does not exist");
        }

        if(studentRepository.findByEmail(studentDto.email()) != null) {
            throw new IllegalArgumentException("Student with this email already exists");
        }

        student.setName(studentDto.name());
        student.setEmail(studentDto.email());
        student.setPassword(studentDto.password());

        try {
            studentRepository.save(student);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Failed to update student due to database constraints");
        }

        return "Student updated successfully";
    }

    @Override
    public Map<String, String> getStudent(String email){
        Student student = studentRepository.findByEmail(email);
        if(student == null){
            throw new IllegalArgumentException("Student with this email does not exist");
        }
        return new HashMap<String, String>(){{
            put("name", student.getName());
            put("email", student.getEmail());
        }};
    }

    @Override
    public String requestCourse(RequestDto requestDto){
        Student student = studentRepository.findById(requestDto.studentId()).orElseThrow(() -> new EntityNotFoundException("Student with this id does not exist"));
        Course course = courseRepository.findById(requestDto.courseIds().get(0).longValue()).orElseThrow(() -> new EntityNotFoundException("Course with this id does not exist"));
        Semester semester = semesterRepository.findById(course.getSemester().getId()).orElseThrow(() -> new EntityNotFoundException("Semester with this id does not exist"));

        if (semester.getRegistrationStatus().equals("Closed")) {
            throw new IllegalArgumentException("Registration is closed for this semester");
        }

        int semesterXor = 0;
        for (Integer courseId : requestDto.courseIds()) {
            course = courseRepository.findById(Long.valueOf(courseId)).orElseThrow(() -> new EntityNotFoundException("Course with this id does not exist"));

            semesterXor ^= course.getSemester().getId();
            if (semesterXor != course.getSemester().getId() && semesterXor != 0) {
                throw new IllegalArgumentException("Courses belong to different semesters");
            }
        }

        Request request = new Request(
                student,
                requestDto.courseIds(),
                requestDto.requestDate(),
                requestDto.status()
        );

        try {
            if (requestRepository.findByStudent(student) != null) {

                Request existingRequest = requestRepository.findByStudent(student);
                existingRequest.setCourseIds(requestDto.courseIds());
                existingRequest.setRequestDate(LocalDate.parse(requestDto.requestDate()));
                existingRequest.setStatus(requestDto.status());

                requestRepository.save(existingRequest);

                return "Course request updated successfully";
            }

            requestRepository.save(request);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Failed to request course due to database constraints");
        }

        return "Course requested successfully";
    }

    @Override
    public List<Request> getAllRequests() {
        List<Request> requests = requestRepository.findAll();

        return requests;
    }
}
