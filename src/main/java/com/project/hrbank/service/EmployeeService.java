package com.project.hrbank.service;

import com.project.hrbank.domain.EmployeeStatus;

import java.time.LocalDate;

public interface EmployeeService {

    long countEmployees(EmployeeStatus status, LocalDate fromDate, LocalDate toDate);
}
