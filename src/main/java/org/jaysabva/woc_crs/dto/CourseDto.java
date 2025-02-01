package org.jaysabva.woc_crs.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.jaysabva.woc_crs.entity.Professor;
import org.jaysabva.woc_crs.entity.Semester;

public record CourseDto(
        @NotBlank String courseName,
        @NotBlank String courseCode,
        @NotNull Long credit,
        @NotNull Long max_enrollment,
        Long curr_enrollment,
        @NotNull Long professorId,
        @NotNull Long semesterId) {
}
