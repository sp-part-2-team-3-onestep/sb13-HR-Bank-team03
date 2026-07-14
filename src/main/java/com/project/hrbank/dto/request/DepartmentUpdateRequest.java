package com.project.hrbank.dto.request;

public record DepartmentUpdateRequest(
        String name,
        String description,
        String establishedDate
) {
}
