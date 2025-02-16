package org.jaysabva.woc_crs.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter

public class Transcript {
    private Long studentID;
    private String studentName;
    private Long batch;
    private String department;

    private String semesterName;
    private LocalDateTime startDate;

    private List<Object> courses;

    private Map<String, Object> grades;

    public Long getStudentID() {
        return studentID;
    }

    public void setStudentID(Long studentID) {
        this.studentID = studentID;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Long getBatch() {
        return batch;
    }

    public void setBatch(Long batch) {
        this.batch = batch;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSemesterName() {
        return semesterName;
    }

    public void setSemesterName(String semesterName) {
        this.semesterName = semesterName;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public List<Object> getCourses() {
        return courses;
    }

    public void setCourses(List<Object> courses) {
        this.courses = courses;
    }

    public Map<String, Object> getGrades() {
        return grades;
    }

    public void setGrades(Map<String, Object> grades) {
        this.grades = grades;
    }
}
