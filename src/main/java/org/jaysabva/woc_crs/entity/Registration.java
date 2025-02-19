package org.jaysabva.woc_crs.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;


@Setter
@Getter

@Entity
@Table(name = "Registration")
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "semester_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Semester semester;

    private LocalDateTime registrationDate;

    private Grade grade;

    public Registration() {
    }

    public Registration(Student student, Course course, Semester semester, String registrationDate) {
        this.student = student;
        this.course = course;
        this.semester = semester;
        this.registrationDate = LocalDateTime.parse(registrationDate);
        this.grade = Grade.I;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = LocalDateTime.parse(registrationDate);
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = Grade.valueOf(grade);
    }
}
