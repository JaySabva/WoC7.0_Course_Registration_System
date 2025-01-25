package org.jaysabva.woc_crs.repository;

import org.jaysabva.woc_crs.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
}
