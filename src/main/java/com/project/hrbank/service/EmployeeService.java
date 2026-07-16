package com.project.hrbank.service;

import com.project.hrbank.domain.EmployeeStatus;
import com.project.hrbank.dto.request.EmployeeCreateRequest;
import com.project.hrbank.dto.request.EmployeeSearchRequest;
import com.project.hrbank.dto.response.CursorPageResponse;
import com.project.hrbank.dto.request.EmployeeUpdateRequest;
import com.project.hrbank.dto.response.CursorPageResponse;
import com.project.hrbank.dto.response.EmployeeDistributionDto;
import com.project.hrbank.dto.response.EmployeeDto;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeService {

    long countEmployees(
            EmployeeStatus status,
            LocalDate fromDate,
            LocalDate toDate
    );

    EmployeeDto create(EmployeeCreateRequest request, MultipartFile file, String ip);

    void deleteEmployee(
            Long id,
            String remoteIp
    );

    @Transactional(readOnly = true)
    EmployeeDto findById(Long id);

    CursorPageResponse<EmployeeDto> getEmployeesWithCursor(
            EmployeeSearchRequest request
    );

    EmployeeDto update(
            Long id,
            EmployeeUpdateRequest request,
            MultipartFile file,
            String remoteIp
    );

    List<EmployeeDistributionDto> getEmployeeDistribution(
            String groupBy,
            EmployeeStatus status
    );
}