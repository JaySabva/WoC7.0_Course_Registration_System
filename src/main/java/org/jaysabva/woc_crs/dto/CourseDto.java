package org.jaysabva.woc_crs.dto;

import org.jaysabva.woc_crs.entity.Professor;
import org.jaysabva.woc_crs.entity.Semester;

public record CourseDto(Long id, String courseName, String courseCode, Long credit, Long max_enrollment, Long curr_enrollment, Long professorId, Long semesterId) {}