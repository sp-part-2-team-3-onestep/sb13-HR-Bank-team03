package com.project.hrbank.dto.response;

import java.time.Instant;

public record DepartmentDto(
        Integer id,
        String name,
        String description,
        String establishedDate,
        Integer employeeCount
){ }
