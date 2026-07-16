package com.project.hrbank.dto.response;

public record EmployeeDistributionDto(
        String groupKey,
        Long count,
        Double percentage
) {
}