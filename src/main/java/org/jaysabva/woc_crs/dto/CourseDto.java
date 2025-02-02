package org.jaysabva.woc_crs.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CourseDto(
        String courseName,
        String courseCode,
        Long credit,
        Long max_enrollment,
        Long curr_enrollment,
        Long professorId,
        Long semesterId) {
}
