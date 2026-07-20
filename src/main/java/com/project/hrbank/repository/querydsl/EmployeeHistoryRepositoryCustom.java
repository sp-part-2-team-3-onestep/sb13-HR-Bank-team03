package com.project.hrbank.repository.querydsl;

import com.project.hrbank.domain.EmployeeHistory;
import com.project.hrbank.domain.EmployeeHistoryType;
import org.springframework.data.domain.Slice;

import java.time.Instant;

public interface EmployeeHistoryRepositoryCustom {

    Slice<EmployeeHistory> findByCondition(
            String employeeNumber,
            EmployeeHistoryType type,
            String memo,
            String ipAddress,
            Instant atFrom,
            Instant atTo,
            Long idAfter,
            String cursor,
            Long size,
            String sortField,
            String sortDirection
    );
}
