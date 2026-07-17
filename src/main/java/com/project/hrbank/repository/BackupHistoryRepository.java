package com.project.hrbank.repository;

import com.project.hrbank.domain.BackupHistory;
import com.project.hrbank.domain.BackupStatus;
import com.project.hrbank.repository.querydsl.QDSLBackupHistoryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BackupHistoryRepository extends JpaRepository<BackupHistory, Long>, QDSLBackupHistoryRepository {

    BackupHistory findFirstByBackupStatus(BackupStatus status);

}
