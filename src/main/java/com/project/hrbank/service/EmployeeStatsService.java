package com.project.hrbank.service;

import com.project.hrbank.domain.EmployeeTrendUnit;
import com.project.hrbank.dto.response.EmployeeTrendResponse;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeStatsService {

  List<EmployeeTrendResponse> getEmployeeTrend(
      LocalDate from,
      LocalDate to,
      EmployeeTrendUnit unit
  );
}