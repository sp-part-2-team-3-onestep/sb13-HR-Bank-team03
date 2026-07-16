package com.project.hrbank.repository;

import com.project.hrbank.domain.EmployeeHistory;
import com.project.hrbank.repository.querydsl.EmployeeHistoryRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;

public interface EmployeeHistoryRepository extends JpaRepository<EmployeeHistory, Long>, EmployeeHistoryRepositoryCustom {

    long countByCreateAtBetween(Instant fromDate, Instant toDate);

}