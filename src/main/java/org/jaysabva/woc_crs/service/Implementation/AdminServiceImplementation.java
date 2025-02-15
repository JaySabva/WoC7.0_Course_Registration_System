package org.jaysabva.woc_crs.service.Implementation;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jaysabva.woc_crs.dto.*;
import org.jaysabva.woc_crs.entity.*;
import org.jaysabva.woc_crs.repository.*;
import org.jaysabva.woc_crs.util.BCryptUtil;
import org.jaysabva.woc_crs.util.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;

import org.jaysabva.woc_crs.service.AdminService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AdminServiceImplementation implements AdminService {

    private final StudentRepository studentRepository;
    private final ProfessorRepository professorRepository;
    private final SemesterRepository semesterRepository;
    private final CourseRepository courseRepository;
    private final EmailSenderService emailSenderService;
    private final RequestRepository requestRepository;
    private final RegistrationRepository registrationRepository;

    @Autowired
    public AdminServiceImplementation(
            StudentRepository studentRepository,
            ProfessorRepository professorRepository,
            SemesterRepository semesterRepository,
            CourseRepository courseRepository,
            EmailSenderService emailSenderService,
            RequestRepository requestRepository,
            RegistrationRepository registrationRepository) {
        this.studentRepository = studentRepository;
        this.professorRepository = professorRepository;
        this.semesterRepository = semesterRepository;
        this.courseRepository = courseRepository;
        this.emailSenderService = emailSenderService;
        this.requestRepository = requestRepository;
        this.registrationRepository = registrationRepository;
    }

    @Override
    public String addStudent(StudentDto studentDto) {

        if(studentRepository.findByEmail(studentDto.email()) != null) {
            throw new EntityExistsException("Student with this email already exists");
        }

        Integer rollNo = studentRepository.countByBatchAndDepartment(studentDto.batch(), Department.valueOf(studentDto.department())) + 1;

        Student student = new Student(
            studentDto.name(),
            studentDto.email(),
            BCryptUtil.hashPassword(studentDto.password()),
            studentDto.batch(),
            studentDto.department(),
            rollNo
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
            throw new EntityExistsException("Professor with this email already exists");
        }

        Professor professor = new Professor(
            professorDto.name(),
            professorDto.email(),
            BCryptUtil.hashPassword(professorDto.password())
        );

        try {
            professorRepository.save(professor);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Failed to add professor due to database constraints");
        }

        return "Professor added successfully";
    }

    @Override
    public String deleteStudent(String email) {

        Student student = studentRepository.findByEmail(email);

        if(student == null) {
            throw new EntityNotFoundException("Student with this email does not exist");
        }

        try {
            studentRepository.delete(student);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to delete student", e);
        }

        return "Student deleted successfully";
    }

    @Override
    public String deleteProfessor(String email) {

        Professor professor = professorRepository.findByEmail(email);

        if(professor == null) {
            throw new EntityNotFoundException("Professor with this email does not exist");
        }

        try {
            professorRepository.delete(professor);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to delete professor", e);
        }

        return "Professor deleted successfully";
    }

    @Override
    public String addSemester(SemesterDto semesterDto) {
        if (semesterRepository.findBySemesterName(semesterDto.semesterName()) != null) {
            throw new EntityExistsException("Semester with this name already exists");
        }

        Semester semester = new Semester(
            semesterDto.semesterName(),
            semesterDto.startDate(),
            semesterDto.endDate(),
            semesterDto.registrationEndDate(),
            semesterDto.registrationStatus()
        );

        try {
            semesterRepository.save(semester);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to add semester due to database constraints");
        }

        return "Semester added successfully";
    }

    @Override
    public String updateSemester(SemesterDto semesterDto, Long id) {
        Semester semester = semesterRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Semester with this id does not exist"));

        if (semesterRepository.findBySemesterName(semesterDto.semesterName()) != null) {
            throw new EntityExistsException("Semester with this name already exists");
        }

        semester.setSemesterName(semesterDto.semesterName() != null ? semesterDto.semesterName() : semester.getSemesterName());
        semester.setStartDate(semesterDto.startDate() != null ? semesterDto.startDate() : semester.getStartDate().toString());
        semester.setEndDate(semesterDto.endDate() != null ? semesterDto.endDate() : semester.getEndDate().toString());
        semester.setRegistrationEndDate(semesterDto.registrationEndDate() != null ? semesterDto.registrationEndDate() : semester.getRegistrationEndDate().toString());
        semester.setRegistrationStatus(semesterDto.registrationStatus() != null ? semesterDto.registrationStatus() : semester.getRegistrationStatus());

        try {
            semesterRepository.save(semester);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to update semester due to database constraints");
        }

        return "Semester updated successfully";
    }

    @Override
    public String deleteSemester(Long id) {
        Semester semester = semesterRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Semester with this id does not exist"));

        try {
            semesterRepository.delete(semester);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to delete semester due to database constraints");
        }

        return "Semester deleted successfully";
    }

    @Override
    public List<Semester> getAllSemesters() {
        List<Semester> semesters = semesterRepository.findAll();

        return semesters;
    }

    @Override
    public String addCourse(CourseDto courseDto) {
        Professor professor = professorRepository.findById(courseDto.professorId()).orElseThrow(() -> new EntityNotFoundException("Professor with this id does not exist"));
        Semester semester = semesterRepository.findById(courseDto.semesterId()).orElseThrow(() -> new EntityNotFoundException("Semester with this name does not exist"));

        Course course = new Course(
            courseDto.courseName(),
            courseDto.courseCode(),
            courseDto.credit(),
            courseDto.max_enrollment() != null ? courseDto.max_enrollment() : Integer.MAX_VALUE,
            courseDto.curr_enrollment() != null ? courseDto.curr_enrollment() : 0,
            professor,
            semester
        );

        try {
            courseRepository.save(course);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to add course due to database constraints");
        }

        return "Course added successfully";
    }

    @Override
    public String updateCourse(CourseDto courseDto, Long id) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Course with this id does not exist"));

        Professor professor = professorRepository.findById(courseDto.professorId() != null ? courseDto.professorId() : course.getProfessor().getId()).orElseThrow(() -> new EntityNotFoundException("Professor with this id does not exist"));
        Semester semester = semesterRepository.findById(courseDto.semesterId() != null ? courseDto.semesterId() : course.getSemester().getId()).orElseThrow(() -> new EntityNotFoundException("Semester with this name does not exist"));

        course.setCourseName(courseDto.courseName() != null ? courseDto.courseName() : course.getCourseName());
        course.setCourseCode(courseDto.courseCode() != null ? courseDto.courseCode() : course.getCourseCode());
        course.setCredits(courseDto.credit() != null ? courseDto.credit() : course.getCredits());
        course.setMax_enrollment(courseDto.max_enrollment() != null ? courseDto.max_enrollment() : course.getMax_enrollment());
        course.setCurr_enrollment(courseDto.curr_enrollment() != null ? courseDto.curr_enrollment() : course.getCurr_enrollment());
        course.setProfessor(professor != null ? professor : course.getProfessor());
        course.setSemester(semester != null ? semester : course.getSemester());

        try {
            courseRepository.save(course);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to update course due to database constraints");
        }

        return "Course updated successfully";
    }

    @Override
    public String deleteCourse(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Course with this id does not exist"));

        try {
            courseRepository.delete(course);
        } catch (Exception e) {
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
        Semester semester = semesterRepository.findByRegistrationStatusOrderByEndDateDesc("Active");
        List<Student> students = studentRepository.findAll();

        semester.setStartDate(semester.getRegistrationEndDate().minusDays(10).toString());

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
                            registration.setRegistrationDate(LocalDateTime.now().toString());

                            System.out.println("Student " + request.getStudent().getId() + " Course " + courseId);
                            registrationRepository.save(registration);

                            course.increaseCurrEnrollment();
                            courseRepository.save(course);

                            maxCoursesCanEnroll--;
                        } catch (Exception e) {
                            throw new RuntimeException("Database error while assigning course.", e);
                        }
                    }
                }

                requestRepository.delete(request);
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

    @Override
    public void sendCourseAssignEmailNotification() {
       Semester latestSemester = semesterRepository.findTopByOrderByStartDateDesc();
       List<Registration> registrations = registrationRepository.findAllBySemester(latestSemester);

       Map<Student, List<Course>> studentRegistrationMap = new HashMap<>();

       for (Registration registration : registrations) {
           if (!studentRegistrationMap.containsKey(registration.getStudent())) {
               studentRegistrationMap.put(registration.getStudent(), new ArrayList<>());
           }

           studentRegistrationMap.get(registration.getStudent()).add(registration.getCourse());
       }

         for (Map.Entry<Student, List<Course>> entry : studentRegistrationMap.entrySet()) {
             emailSenderService.sendEmail(entry.getKey().getEmail(), "Course Registration for " + latestSemester.getSemesterName() + " semester", emailSenderService.courseAssignmentEmail(entry.getKey().getName(), latestSemester.getSemesterName(), entry.getValue()));
         }

         System.out.println("Email sent successfully");
    }

    @Override
    public List<Request> getAllRequests() {
        List<Request> requests = requestRepository.findAll();

        return requests;
    }

    @Override
    public void assignGradesForCourse(MultipartFile file, Long courseId) throws IOException {
        List<Registration> registrations = registrationRepository.findByCourse_Id(courseId);

        InputStream is = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(is);
        Sheet sheet = workbook.getSheetAt(0);

        Map<Long, String> gradeMap = new HashMap<>();
        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue;
            }

            String studentId = row.getCell(0).getStringCellValue();
            String grade = row.getCell(1).getStringCellValue();

            gradeMap.put(Long.valueOf(studentId), grade);
        }

        workbook.close();

        try {
            for (Registration registration : registrations) {
                if (gradeMap.containsKey(registration.getStudent().getId())) {
                    registration.setGrade(gradeMap.get(registration.getStudent().getId()));
                    registrationRepository.save(registration);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to update grades for course", e);
        }
    }
}
