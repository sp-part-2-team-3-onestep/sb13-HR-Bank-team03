package com.project.hrbank.service;

import com.project.hrbank.domain.EmployeeStatus;
import com.project.hrbank.dto.request.EmployeeCreateRequest;
import com.project.hrbank.dto.request.EmployeeSearchRequest;
import com.project.hrbank.dto.response.CursorPageResponse;
import com.project.hrbank.dto.request.EmployeeUpdateRequest;
import com.project.hrbank.dto.response.EmployeeDto;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public interface EmployeeService {

    long countEmployees(EmployeeStatus status, LocalDate fromDate, LocalDate toDate);

    EmployeeDto create(EmployeeCreateRequest request, MultipartFile file);

    void deleteEmployee(Long id, String remoteIp);

    EmployeeDto findById(Long id);

    CursorPageResponse<EmployeeDto> getEmployeesWithCursor(EmployeeSearchRequest request);

    EmployeeDto update(Long id, EmployeeUpdateRequest request, MultipartFile file, String remoteIp);

}