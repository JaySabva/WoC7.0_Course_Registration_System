package org.jaysabva.woc_crs.service.Implementation;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.ResourceClosedException;
import org.jaysabva.woc_crs.dto.RequestDto;
import org.jaysabva.woc_crs.dto.Transcript;
import org.jaysabva.woc_crs.entity.*;
import org.jaysabva.woc_crs.repository.*;
import org.jaysabva.woc_crs.util.BCryptUtil;
import org.jaysabva.woc_crs.util.PdfGenerationService;
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
    private final PdfGenerationService pdfGenerationService;

    @Autowired
    public StudentServiceImplementation(
            StudentRepository studentRepository,
            CourseRepository courseRepository,
            RequestRepository requestRepository,
            SemesterRepository semesterRepository, RegistrationRepository registrationRepository, PdfGenerationService pdfGenerationService) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.requestRepository = requestRepository;
        this.semesterRepository = semesterRepository;
        this.registrationRepository = registrationRepository;
        this.pdfGenerationService = pdfGenerationService;
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

        student.setName(studentDto.name() != null ? studentDto.name() : student.getName());
        student.setEmail(studentDto.email() != null ? studentDto.email() : student.getEmail());
        student.setPassword(studentDto.password() != null ? BCryptUtil.hashPassword(studentDto.password()) : student.getPassword());
        student.setBatch(studentDto.batch() != null ? studentDto.batch() : student.getBatch());
        student.setDepartment(studentDto.department() != null ? studentDto.department() : student.getDepartment().toString());

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
        return new LinkedHashMap<>(){{
            put("id", student.getId().toString());
            put("name", student.getName());
            put("email", student.getEmail());
            put("batch", student.getBatch().toString());
            put("department", student.getDepartment().getName());
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
        Map<String, Map<String, Object>> registrationsMap = new LinkedHashMap<>();

        for (Registration registration : registrations) {
            Semester semester = registration.getSemester();

            Map<String, Object> courseMap = new LinkedHashMap<>();
            courseMap.put("id", registration.getCourse().getId());
            courseMap.put("name", registration.getCourse().getCourseName());
            courseMap.put("course code", registration.getCourse().getCourseCode());
            courseMap.put("credits", registration.getCourse().getCredits());
            courseMap.put("professor", registration.getCourse().getProfessor().getName());
            courseMap.put("grade", registration.getGrade());

            if (registrationsMap.containsKey(semester.getSemesterName())) {
                ((List<Map<String, Object>>) registrationsMap.get(semester.getSemesterName()).get("Courses")).add(courseMap);
            } else {
                Map<String, Object> mainMap = new LinkedHashMap<>();
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

    @Override
    public Map<String, Object> getRegisteredCourses(Long id, Long semesterID) {
        List<Registration> registrations = registrationRepository.findByStudent_IdAndSemester_Id(id, semesterID);
        Map<String, Object> registrationsMap = new LinkedHashMap<>();

        registrationsMap.put("semester-name", registrations.getFirst().getSemester().getSemesterName());
        registrationsMap.put("semester-id", registrations.getFirst().getSemester().getId());
        registrationsMap.put("start-date", registrations.getFirst().getSemester().getStartDate());
        registrationsMap.put("end-date", registrations.getFirst().getSemester().getEndDate());
        registrationsMap.put("registration-end-date", registrations.getFirst().getSemester().getRegistrationEndDate());

        List<Map<String, Object>> courseList = new ArrayList<>();
        double totalCredits = 0.0;
        double totalCreditEarned = 0.0;
        double gradePoints = 0.0;
        double gpa = 0.0;
        for (Registration registration : registrations) {
            Map<String, Object> courseMap = new LinkedHashMap<>();
            courseMap.put("id", registration.getCourse().getId());
            courseMap.put("name", registration.getCourse().getCourseName());
            courseMap.put("course code", registration.getCourse().getCourseCode());
            courseMap.put("credits", registration.getCourse().getCredits());
            courseMap.put("professor", registration.getCourse().getProfessor().getName());
            courseMap.put("grade", registration.getGrade());
            courseMap.put("grade point", registration.getGrade().getGradePoint() * registration.getCourse().getCredits());

            courseList.add(courseMap);

            totalCredits += registration.getCourse().getCredits();
            totalCreditEarned += registration.getGrade().isPassing() ? registration.getCourse().getCredits() : 0;
            gradePoints += (registration.getCourse().getCredits() * registration.getGrade().getGradePoint());
        }

        registrationsMap.put("Courses", courseList);
        registrationsMap.put("Credits Registered", totalCredits);
        registrationsMap.put("Credits Earned", totalCreditEarned);
        registrationsMap.put("Grade Points Earned", gradePoints);
        registrationsMap.put("GPA", totalCreditEarned == 0 ? "N/A" : gradePoints / totalCreditEarned);

        return registrationsMap;
    }

    @Override
    public byte[] generateTranscript(Long id, Long semesterID) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Student with this id does not exist"));
        Semester semester = semesterRepository.findById(semesterID).orElseThrow(() -> new EntityNotFoundException("Semester with this id does not exist"));
        List<Registration> registrations = registrationRepository.findByStudent_IdAndSemester_Id(id, semesterID);

        Transcript transcript = new Transcript();
        transcript.setStudentID(student.getId());
        transcript.setStudentName(student.getName());
        transcript.setBatch(Long.valueOf(student.getBatch()));
        transcript.setDepartment(student.getDepartment().getName());
        transcript.setSemesterName(semester.getSemesterName());
        transcript.setStartDate(semester.getStartDate());

        List<Object> courses = new ArrayList<>();
        for (Registration registration : registrations) {
            Map<String, Object> courseMap = new LinkedHashMap<>();
            courseMap.put("courseName", registration.getCourse().getCourseName());
            courseMap.put("courseCode", registration.getCourse().getCourseCode());
            courseMap.put("credits", registration.getCourse().getCredits());
            courseMap.put("professor", registration.getCourse().getProfessor().getName());
            courseMap.put("grade", registration.getGrade().toString());
            courseMap.put("gradePoint", registration.getGrade().getGradePoint() * registration.getCourse().getCredits());

            courses.add(courseMap);
        }
        transcript.setCourses(courses);

        try {
            return pdfGenerationService.generateTranscript(transcript);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to generate transcript");
        }
    }
}
