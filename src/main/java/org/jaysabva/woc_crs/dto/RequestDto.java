package org.jaysabva.woc_crs.dto;

import java.util.List;

public record RequestDto(Long id, List<Integer> courseIds, String requestDate, String status, Long studentId) {}
