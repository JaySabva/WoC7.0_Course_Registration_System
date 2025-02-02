package org.jaysabva.woc_crs.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String courseName;

    @NotBlank
    private String courseCode;

    @NotBlank
    private Long credits;

    @NotBlank
    private Long max_enrollment;

    @NotBlank
    private Long curr_enrollment;

    @ManyToOne
    @JoinColumn(name = "professor_id", referencedColumnName = "id")
    private Professor professor;

    @ManyToOne
    @JoinColumn(name = "semester_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Semester semester;

    public Course() {

    }

    public Course(String courseName, String courseCode, Long credits, Long max_enrollment, Long curr_enrollment, Professor professor, Semester semester) {
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.credits = credits;
        this.max_enrollment = max_enrollment;
        this.curr_enrollment = curr_enrollment;
        this.professor = professor;
        this.semester = semester;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public Long getCredits() {
        return credits;
    }

    public void setCredits(Long credits) {
        this.credits = credits;
    }

    public Long getMax_enrollment() {
        return max_enrollment;
    }

    public void setMax_enrollment(Long max_enrollment) {
        this.max_enrollment = max_enrollment;
    }

    public Long getCurr_enrollment() {
        return curr_enrollment;
    }

    public void setCurr_enrollment(Long curr_enrollment) {
        this.curr_enrollment = curr_enrollment;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public boolean hasAvailableSeats() {
        return max_enrollment - curr_enrollment > 0;
    }

    public void increaseCurrEnrollment() {
        curr_enrollment++;
    }
}
