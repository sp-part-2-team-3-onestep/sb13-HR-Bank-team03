package com.project.hrbank.dto.request;

import com.project.hrbank.domain.EmployeeStatus;
import java.time.LocalDate;

public record EmployeeSearchRequest(
    String keyword,
    EmployeeStatus status,
    String employeeNumber,
    String departmentName,
    String position,
    LocalDate hireDateFrom,
    LocalDate hireDateTo,

    String cursor,
    Long idAfter,
    Integer size,
    String sortField,
    String sortDirection
) {

  public EmployeeSearchRequest {

    if (size == null) {size = 10;}
    if (sortField == null || sortField.isBlank()) {sortField = "hireDate";}
    if (sortDirection == null || sortDirection.isBlank()) {sortDirection = "desc";}
  }
}