package org.jaysabva.woc_crs.repository;

import org.jaysabva.woc_crs.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByProfessor_Id(Long professorId);
}
