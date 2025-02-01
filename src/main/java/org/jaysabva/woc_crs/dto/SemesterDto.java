package org.jaysabva.woc_crs.dto;

import jakarta.validation.constraints.NotBlank;

public record SemesterDto (
        @NotBlank String semesterName,
        @NotBlank String startDate,
        @NotBlank String endDate,
        @NotBlank String registrationEndDate) {
}
