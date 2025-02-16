package org.jaysabva.woc_crs.service.Implementation;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.jaysabva.woc_crs.entity.Course;
import org.jaysabva.woc_crs.entity.Semester;
import org.jaysabva.woc_crs.repository.CourseRepository;
import org.jaysabva.woc_crs.util.BCryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.jaysabva.woc_crs.service.ProfessorService;


import org.jaysabva.woc_crs.entity.Professor;
import org.jaysabva.woc_crs.dto.ProfessorDto;
import org.jaysabva.woc_crs.repository.ProfessorRepository;

import java.util.*;

@Service
public class ProfessorServiceImplementation implements ProfessorService {

    private final ProfessorRepository professorRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public ProfessorServiceImplementation(ProfessorRepository professorRepository, CourseRepository courseRepository){
        this.professorRepository = professorRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public String updateProfessor(ProfessorDto professorDto, String email){

        Professor professor = professorRepository.findByEmail(email);
        if(professor == null){
            throw new EntityNotFoundException("Professor with this email does not exist");
        }

        if(professorRepository.findByEmail(professorDto.email()) != null) {
            throw new EntityExistsException("Professor with this email already exists");
        }

        professor.setName(professorDto.name());
        professor.setEmail(professorDto.email());
        professor.setPassword(BCryptUtil.hashPassword(professorDto.password()));

        try {
            professorRepository.save(professor);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to update professor due to database constraints");
        }

        return "Professor updated successfully";
    }

    @Override
    public Map<String, String> getProfessor(String email){
        Professor professor = professorRepository.findByEmail(email);
        if(professor == null){
            throw new EntityNotFoundException("Professor with this email does not exist");
        }
        return new HashMap<> (){{
            put("name", professor.getName());
            put("email", professor.getEmail());
        }};
    }

    @Override
    public Map<String, Map<String, Object>> getAllCourses(Long professorId){
        List<Course> courses = courseRepository.findByProfessor_Id(professorId);

        Map<String, Map<String, Object>> courseMap = new LinkedHashMap<>();
        for(Course course: courses) {
            Semester semester = course.getSemester();

            Map<String, Object> courseDetails = new LinkedHashMap<>();
            courseDetails.put("id", course.getId());
            courseDetails.put("name", course.getCourseName());
            courseDetails.put("course code", course.getCourseCode());
            courseDetails.put("credit", course.getCredits());
            courseDetails.put("max enrollment", course.getMax_enrollment());
            courseDetails.put("current enrollment", course.getCurr_enrollment());

            if (courseMap.containsKey(semester.getSemesterName())) {
                ((List<Map<String, Object>>) courseMap.get(semester.getSemesterName()).get("Courses")).add(courseDetails);
            } else {
                Map<String, Object> mainMap = new LinkedHashMap<>();
                mainMap.put("semester-name", semester.getSemesterName());
                mainMap.put("semester-id", semester.getId());
                mainMap.put("start-date", semester.getStartDate());
                mainMap.put("end-date", semester.getEndDate());
                mainMap.put("registration-end-date", semester.getRegistrationEndDate());

                List<Map<String, Object>> courseList = new ArrayList<>();
                courseList.add(courseDetails);
                mainMap.put("Courses", courseList);

                courseMap.put(semester.getSemesterName(), mainMap);
            }
        }

        return courseMap;
    }
}
