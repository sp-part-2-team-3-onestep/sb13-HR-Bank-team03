package com.project.hrbank.dto.request;

public record DepartmentCreateRequest (
    String name,
    String description,
    String establishedDate
){}