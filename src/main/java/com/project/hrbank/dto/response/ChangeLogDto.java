package com.project.hrbank.dto.response;

import java.time.Instant;

public record ChangeLogDto(
        Long id,
        String type,
        String employeeNumber,
        String memo,
        String ipAddress,
        Instant at
) {
}
