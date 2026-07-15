package com.project.hrbank.repository;

import com.project.hrbank.domain.BackupHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BackupHistoryRepository extends JpaRepository<BackupHistory, Long> {
}