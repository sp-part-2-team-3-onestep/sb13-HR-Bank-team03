package com.project.hrbank.service;

import com.project.hrbank.dto.response.BackupDto;

public interface BackupHistoryService {
    BackupDto create(String workerIp);
    BackupDto getLatestBackup(String status);
}
