package com.project.hrbank.repository;

import com.project.hrbank.domain.Employee;
import com.project.hrbank.dto.request.EmployeeSearchRequest;

import java.util.List;

public interface EmployeePagingRepository {

  List<Employee> searchByCursor(EmployeeSearchRequest request);
  long countEmployees();
}