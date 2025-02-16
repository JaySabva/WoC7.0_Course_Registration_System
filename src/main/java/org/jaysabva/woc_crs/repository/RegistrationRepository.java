package org.jaysabva.woc_crs.repository;

import org.jaysabva.woc_crs.entity.Registration;
import org.jaysabva.woc_crs.entity.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    List<Registration> findAllBySemester(Semester semester);

    List<Registration> findByStudent_Id(Long studentId);

    List<Registration> findByCourse_Id(Long courseId);

    List<Registration> findByStudent_IdAndSemester_Id(Long id, Long semesterID);
}
