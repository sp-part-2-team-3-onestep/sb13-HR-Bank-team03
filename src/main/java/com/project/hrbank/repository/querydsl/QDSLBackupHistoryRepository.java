package com.project.hrbank.repository.querydsl;

import com.project.hrbank.domain.BackupHistory;
import com.project.hrbank.dto.request.BackupHistorySearchRequest;
import org.springframework.data.domain.Slice;

public interface QDSLBackupHistoryRepository {

    Slice<BackupHistory> backupHistory(
            BackupHistorySearchRequest req,
            int pageSize,
            String sort,
            String direction

    );
}
