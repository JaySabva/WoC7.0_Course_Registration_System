package org.jaysabva.woc_crs.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Setter
@Getter

@Entity
@Table(name = "students")
public class Student {

    @Id
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    @Column(unique = true)
    private String email;

    @NotBlank
    private String password;

    private Integer batch;

    @Enumerated
    private Department department;

    private Integer rollNo;

    public Student() {

    }

    public Student(String name, String email, String password, Integer batch, String department, Integer rollNo) {
        this.id = generateStudentID(batch, department, rollNo);
        this.name = name;
        this.email = email;
        this.password = password;
        this.batch = batch;
        this.department = Department.valueOf(department);
        this.rollNo = rollNo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getBatch() {
        return batch;
    }

    public void setBatch(Integer batch) {
        this.batch = batch;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = Department.valueOf(department);
    }

    public Integer getRollNo() {
        return rollNo;
    }

    public void setRollNo(Integer rollNo) {
        this.rollNo = rollNo;
    }

    private Long generateStudentID(Integer batch, String department, Integer rollNo) {
        return Long.parseLong(batch + Department.valueOf(department).getCode() + String.format("%03d", rollNo));
    }
}
