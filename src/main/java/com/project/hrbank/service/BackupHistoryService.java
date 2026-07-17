package com.project.hrbank.service;

import com.project.hrbank.dto.request.BackupHistorySearchRequest;
import com.project.hrbank.dto.response.BackupDto;
import com.project.hrbank.dto.response.CursorPageResponse;

public interface BackupHistoryService {
    BackupDto create(String workerIp);
    BackupDto getLatestBackup(String status);
    CursorPageResponse<BackupDto> getBackupList(
            BackupHistorySearchRequest request,
            int pageSize,
            String sort,
            String direction
    );
}
