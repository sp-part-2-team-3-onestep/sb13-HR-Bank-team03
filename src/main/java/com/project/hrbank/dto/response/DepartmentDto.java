package com.project.hrbank.dto.response;

import java.time.LocalDate;

public record DepartmentDto(
    Integer id,
    String name,
    String description,
    String establishedDate,
    Integer employeeCount
){ }