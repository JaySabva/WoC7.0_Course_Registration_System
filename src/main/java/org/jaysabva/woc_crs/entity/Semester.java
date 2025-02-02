package org.jaysabva.woc_crs.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.time.LocalDateTime;

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

    @NotBlank
    @Column(unique = true)
    private String semesterName;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private LocalDateTime registrationEndDate;

    private String registrationStatus;

    public Semester() {

    }

    public Semester(String semesterName, String startDate, String endDate, String registrationEndDate, String registrationStatus) {
        this.semesterName = semesterName;
        this.startDate = LocalDateTime.parse(startDate);
        this.endDate = LocalDateTime.parse(endDate);
        this.registrationEndDate = LocalDateTime.parse(registrationEndDate);
        this.registrationStatus = registrationStatus;
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

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = LocalDateTime.parse(startDate);
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = LocalDateTime.parse(endDate);
    }

    public LocalDateTime getRegistrationEndDate() {
        return registrationEndDate;
    }

    public void setRegistrationEndDate(String registrationEndDate) {
        this.registrationEndDate = LocalDateTime.parse(registrationEndDate);
    }

    public String getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(String registrationStatus) {
        this.registrationStatus = registrationStatus;
    }
}
