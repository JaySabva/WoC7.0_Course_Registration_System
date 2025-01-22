package org.jaysabva.woc_crs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.jaysabva.woc_crs.entity.Semester;
import org.springframework.stereotype.Repository;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, Long> {
    Semester findBySemesterName(String semesterName);
}
