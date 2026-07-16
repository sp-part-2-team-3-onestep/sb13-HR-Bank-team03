package com.project.hrbank.dto.response;

import com.project.hrbank.domain.EmployeeHistoryType;

import java.time.Instant;
import java.util.List;

public record EmployeeHistoryDetailResponse(
        Long id,
        EmployeeHistoryType type,
        String employeeNumber,
        String memo,
        String ipAddress,
        Instant at,
        String employeeName,
        Long profileImageId,
        List<DiffDto> diffs
) {
}