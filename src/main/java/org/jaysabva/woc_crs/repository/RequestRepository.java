package org.jaysabva.woc_crs.repository;

import org.jaysabva.woc_crs.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.jaysabva.woc_crs.entity.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    Request findByStudent(Student student);
    List<Request> findAllByOrderByRequestDateAsc();
}
