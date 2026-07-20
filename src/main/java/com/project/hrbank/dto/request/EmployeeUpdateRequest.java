package com.project.hrbank.dto.request;

import com.project.hrbank.domain.EmployeeStatus;

import java.time.LocalDate;

public record EmployeeUpdateRequest(
        String name,
        String email,
        Long departmentId,
        String position,
        LocalDate hireDate,
        EmployeeStatus status,
        String memo
) {
}
