package com.project.hrbank.dto.request;
import java.time.Instant;

public record BackupHistorySearchRequest(
        String worker,
        String status,
        Instant startedAtFrom,
        Instant startedAtTo,
        Long idAfter,
        String cursor
) {
}
