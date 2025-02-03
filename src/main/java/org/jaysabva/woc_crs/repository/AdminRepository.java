package org.jaysabva.woc_crs.repository;

import org.jaysabva.woc_crs.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Object findByEmail(String email);
}
