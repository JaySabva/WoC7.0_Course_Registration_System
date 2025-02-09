package org.jaysabva.woc_crs.service.Implementation;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.ResourceClosedException;
import org.jaysabva.woc_crs.dto.RequestDto;
import org.jaysabva.woc_crs.entity.*;
import org.jaysabva.woc_crs.repository.*;
import org.jaysabva.woc_crs.util.BCryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.jaysabva.woc_crs.dto.StudentDto;
import org.jaysabva.woc_crs.service.StudentService;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class StudentServiceImplementation implements StudentService{

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final RequestRepository requestRepository;
    private final SemesterRepository semesterRepository;
    private final RegistrationRepository registrationRepository;

    @Autowired
    public StudentServiceImplementation(
            StudentRepository studentRepository,
            CourseRepository courseRepository,
            RequestRepository requestRepository,
            SemesterRepository semesterRepository, RegistrationRepository registrationRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.requestRepository = requestRepository;
        this.semesterRepository = semesterRepository;
        this.registrationRepository = registrationRepository;
    }

    @Override
    public String updateStudent(StudentDto studentDto, String email){

        Student student = studentRepository.findByEmail(email);
        if(student == null){
            throw new EntityNotFoundException("Student with this email does not exist");
        }

        if(!Objects.equals(student.getEmail(), studentDto.email()) && studentRepository.findByEmail(studentDto.email()) != null) {
            throw new EntityExistsException("Student with this email already exists");
        }

        student.setName(studentDto.name());
        student.setEmail(studentDto.email());
        student.setPassword(BCryptUtil.hashPassword(studentDto.password()));

        try {
            studentRepository.save(student);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to update student due to database constraints");
        }

        return "Student updated successfully";
    }

    @Override
    public Map<String, String> getStudent(String email){
        Student student = studentRepository.findByEmail(email);
        if(student == null){
            throw new EntityNotFoundException("Student with this email does not exist");
        }
        return new HashMap<>(){{
            put("name", student.getName());
            put("email", student.getEmail());
        }};
    }

    @Override
    public String requestCourse(RequestDto requestDto){
        Student student = studentRepository.findById(requestDto.studentId()).orElseThrow(() -> new EntityNotFoundException("Student with this id does not exist"));
        Course course = courseRepository.findById(requestDto.courseIds().getFirst().longValue()).orElseThrow(() -> new EntityNotFoundException("Course with this id does not exist"));
        Semester semester = semesterRepository.findById(course.getSemester().getId()).orElseThrow(() -> new EntityNotFoundException("Semester with this id does not exist"));

        if (!semester.getRegistrationStatus().equals("Active")) {
            throw new ResourceClosedException("Registration is closed for this semester");
        }

        if (!semester.getRegistrationEndDate().isAfter(LocalDateTime.now())) {
            semester.setRegistrationStatus("Closed");
            semesterRepository.save(semester);

            throw new ResourceClosedException("Registration is closed for this semester");
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
                LocalDateTime.now().toString()
        );

        try {
            if (requestRepository.findByStudent(student) != null) {

                Request existingRequest = requestRepository.findByStudent(student);
                existingRequest.setCourseIds(requestDto.courseIds());
                existingRequest.setRequestDate(LocalDateTime.now().toString());

                requestRepository.save(existingRequest);

                return "Course request updated successfully";
            }

            requestRepository.save(request);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to request course due to database constraints");
        }

        return "Course requested successfully";
    }

    @Override
    public Map<String, Map<String, Object>> getRegisteredCourses(Long id) {
        List<Registration> registrations = registrationRepository.findByStudent_Id(id);
        Map<String, Map<String, Object>> registrationsMap = new HashMap<>();

        for (Registration registration : registrations) {
            Semester semester = registration.getSemester();

            Map<String, Object> courseMap = new HashMap<>();
            courseMap.put("id", registration.getCourse().getId());
            courseMap.put("name", registration.getCourse().getCourseName());
            courseMap.put("course code", registration.getCourse().getCourseCode());
            courseMap.put("credits", registration.getCourse().getCredits());
            courseMap.put("professor", registration.getCourse().getProfessor().getName());
            courseMap.put("grade", registration.getGrade());

            if (registrationsMap.containsKey(semester.getSemesterName())) {
                ((List<Map<String, Object>>) registrationsMap.get(semester.getSemesterName()).get("Courses")).add(courseMap);
            } else {
                Map<String, Object> mainMap = new HashMap<>();
                mainMap.put("semester-name", semester.getSemesterName());
                mainMap.put("semester-id", semester.getId());
                mainMap.put("start-date", semester.getStartDate());
                mainMap.put("end-date", semester.getEndDate());
                mainMap.put("registration-end-date", semester.getRegistrationEndDate());

                List<Map<String, Object>> courseList = new ArrayList<>();
                courseList.add(courseMap);
                mainMap.put("Courses", courseList);

                registrationsMap.put(semester.getSemesterName(), mainMap);
            }
        }

        return registrationsMap;
    }


}
