package org.jaysabva.woc_crs.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "semesters")
public class Semester {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String semesterName;

    private LocalDate startDate;

    private LocalDate endDate;

    public Semester() {

    }

    public Semester(Long id, String semesterName, String startDate, String endDate) {
        this.id = id;
        this.semesterName = semesterName;
        this.startDate = LocalDate.parse(startDate);
        this.endDate = LocalDate.parse(endDate);

        System.out.println(this.startDate);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSemesterName() {
        return semesterName;
    }

    public void setSemesterName(String semesterName) {
        this.semesterName = semesterName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = LocalDate.parse(startDate);
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = LocalDate.parse(endDate);
    }
}