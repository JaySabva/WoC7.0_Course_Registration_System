package org.jaysabva.woc_crs.repository;

import org.jaysabva.woc_crs.entity.Registration;
import org.jaysabva.woc_crs.entity.Semester;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    List<Registration> findAllBySemester(Semester semester);
}
