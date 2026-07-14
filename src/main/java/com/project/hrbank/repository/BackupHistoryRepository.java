package com.project.hrbank.repository;

import com.project.hrbank.domain.BackupHistory;
import com.project.hrbank.domain.BackupStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BackupHistoryRepository extends JpaRepository<BackupHistory, Long> {

    BackupHistory findFirstByBackupStatus(BackupStatus status);

}
