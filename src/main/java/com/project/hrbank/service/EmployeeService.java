package com.project.hrbank.service;

import com.project.hrbank.domain.EmployeeStatus;
import com.project.hrbank.dto.request.EmployeeCreateRequest;
import com.project.hrbank.dto.response.EmployeeDto;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public interface EmployeeService {

    long countEmployees(EmployeeStatus status, LocalDate fromDate, LocalDate toDate);

    EmployeeDto create(EmployeeCreateRequest request, MultipartFile file);

    void deleteEmployee(Long id, String remoteIp);
}