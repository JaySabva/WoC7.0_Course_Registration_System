package org.jaysabva.woc_crs.dto;

import jakarta.validation.constraints.NotBlank;

public record SemesterDto (
        String semesterName,
        String startDate,
        String endDate,
        String registrationEndDate,
        String registrationStatus) {
}
