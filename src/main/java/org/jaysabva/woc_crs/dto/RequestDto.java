package org.jaysabva.woc_crs.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RequestDto(
        @NotNull List<Integer> courseIds,
        @NotBlank String requestDate,
        @NotNull Long studentId) {
}
