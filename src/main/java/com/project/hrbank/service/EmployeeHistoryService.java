package com.project.hrbank.service;

import com.project.hrbank.dto.request.EmployeeHistorySearchRequest;
import com.project.hrbank.dto.response.CursorPageResponseChangeLogDto;
import com.project.hrbank.dto.response.EmployeeHistoryDetailResponse;

import java.time.Instant;

public interface EmployeeHistoryService {

    CursorPageResponseChangeLogDto findByConditions(
            EmployeeHistorySearchRequest request
    );

    EmployeeHistoryDetailResponse findById(Long id);

    long countChangeLogs(Instant fromDate, Instant toDate);
}