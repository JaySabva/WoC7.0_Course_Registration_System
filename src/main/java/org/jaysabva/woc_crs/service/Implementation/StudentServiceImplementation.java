package org.jaysabva.woc_crs.service.Implementation;
import jakarta.persistence.EntityNotFoundException;
import org.jaysabva.woc_crs.dto.RequestDto;
import org.jaysabva.woc_crs.entity.Course;
import org.jaysabva.woc_crs.entity.Request;
import org.jaysabva.woc_crs.repository.CourseRepository;
import org.jaysabva.woc_crs.repository.RequestRepository;
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

        int semesterXor = 0;
        for (Integer courseId : requestDto.courseIds()) {
            Course course = courseRepository.findById(Long.valueOf(courseId)).orElseThrow(() -> new EntityNotFoundException("Course with this id does not exist"));

            semesterXor ^= course.getSemester().getId();
            if (semesterXor != course.getSemester().getId() && semesterXor != 0) {
                throw new IllegalArgumentException("Courses belong to different semesters");
            }
        }

        Request request = new Request(
                requestDto.id(),
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
