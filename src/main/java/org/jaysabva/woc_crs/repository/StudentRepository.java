package org.jaysabva.woc_crs.repository;

import org.jaysabva.woc_crs.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.jaysabva.woc_crs.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByEmail(String email);
    void deleteByEmail(String email);

    Integer countByBatchAndDepartment(Integer batch, Department departmentAbvName);
}
