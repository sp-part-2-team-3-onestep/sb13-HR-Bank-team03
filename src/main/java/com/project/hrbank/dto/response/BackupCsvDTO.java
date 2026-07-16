package com.project.hrbank.dto.response;

import java.time.LocalDate;

public record BackupCsvDTO(
        Long id,
        String employeeNumber,
        String name,
        String email,
        String department,
        String position,
        LocalDate hireDate,
        String status
) {
}
