package com.project.hrbank.dto.request;

import com.project.hrbank.domain.EmployeeStatus;

import java.time.Instant;

public record EmployeeUpdateRequest(
        String name,
        String email,
        Long departmentId,
        String position,
        Instant hireDate,
        EmployeeStatus status,
        String memo
) {
}
