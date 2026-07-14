package com.project.hrbank.dto.response;

import com.project.hrbank.domain.EmployeeHistoryType;

import java.time.Instant;

public record EmployeeHistoryDetailResponse(
        Long id,
        Long employeeId,
        String employeeName,
        Long departmentId,
        String departmentName,
        EmployeeHistoryType type,
        String changeDetail,
        String memo,
        String ipAddress,
        Instant createdAt
) {
}