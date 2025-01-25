package org.jaysabva.woc_crs.service.Implementation;

import jakarta.persistence.EntityNotFoundException;
import org.jaysabva.woc_crs.dto.*;
import org.jaysabva.woc_crs.entity.*;
import org.jaysabva.woc_crs.repository.*;
import org.jaysabva.woc_crs.util.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import org.jaysabva.woc_crs.service.AdminService;

import java.time.LocalDate;
import java.util.*;

@Service
public class AdminServiceImplementation implements AdminService {

    @Autowired
    private StudentRepository studentRepository;
    private ProfessorRepository professorRepository;
    private SemesterRepository semesterRepository;
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private RegistrationRepository registrationRepository;

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
            semesterDto.registrationEndDate(),
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
        semester.setRegistrationEndDate(semesterDto.registrationEndDate());

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
                semester.getEndDate().toString(),
                semester.getRegistrationEndDate().toString()
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

    @Override
    public void sendEmailNotification() {
        Semester semester = semesterRepository.findByRegistrationStatus("Active");
        List<Student> students = studentRepository.findAll();

        semester.setStartDate(semester.getStartDate().minusDays(10).toString());

        for (Student student : students) {
            emailSenderService.sendEmail(student.getEmail(), "Registration for " + semester.getSemesterName() + " semester is now open", emailSenderService.semesterRegistrationEmail(student.getName(), semester.getSemesterName(), semester.getStartDate().toString(), semester.getRegistrationEndDate().toString()));
        }

    }

    @Override
    public void assignCourse() {
        try {
            Semester latestSemester = semesterRepository.findTopByOrderByStartDateDesc();

            if (latestSemester == null) {
                throw new RuntimeException("No Semester found");
            }

            List<Request> requests = requestRepository.findAllByOrderByRequestDateAsc();
            List<Course> allCourses = courseRepository.findAll();

            Map<Long, Course> courseMap = new HashMap<>();

            for (Course course : allCourses) {
                courseMap.put(course.getId(), course);
            }

            for (Request request : requests) {
                int maxCoursesCanEnroll = 2;
                for (Integer courseId : request.getCourseIds()) {
                    if (maxCoursesCanEnroll <= 0)
                        break;

                    Course course = courseMap.get(Long.valueOf(courseId));

                    if (course != null && course.hasAvailableSeats()) {
                        try {
                            System.out.println("Assigning");
                            Registration registration = new Registration();

                            registration.setStudent(request.getStudent());
                            registration.setCourse(course);
                            registration.setSemester(latestSemester);
                            registration.setRegistrationDate(LocalDate.now());

                            System.out.println("Student " + request.getStudent().getId() + " Course " + courseId);
                            registrationRepository.save(registration);

                            course.increaseCurrEnrollment();
                            courseRepository.save(course);

                            maxCoursesCanEnroll--;
                        } catch (DataAccessException e) {
                            throw new RuntimeException("Database error while assigning course.", e);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error During Course Assignment Process");
        }
    }

    @Override
    public List<Registration> getAllRegistration() {
        List<Registration> registrations = registrationRepository.findAll();

        return registrations;
    }
}
