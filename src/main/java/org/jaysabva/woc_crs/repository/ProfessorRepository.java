package org.jaysabva.woc_crs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.jaysabva.woc_crs.entity.Professor;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    Professor findByEmail(String email);
    void deleteByEmail(String email);
}
