package com.project.hrbank.repository;

import com.project.hrbank.domain.EmployeeHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeHistoryRepository extends JpaRepository<EmployeeHistory, Long> {


}
