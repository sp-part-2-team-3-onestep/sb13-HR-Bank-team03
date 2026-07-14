package com.project.hrbank.service;

import com.project.hrbank.dto.response.EmployeeHistoryDetailResponse;

public interface EmployeeHistoryService {

    EmployeeHistoryDetailResponse findById(Long id);
}