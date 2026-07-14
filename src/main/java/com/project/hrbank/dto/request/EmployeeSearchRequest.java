package com.project.hrbank.dto.request;

public record EmployeeSearchRequest(
    String cursor,
    Long idAfter,
    Integer size,
    String sortField,
    String sortDirection
) {

  public EmployeeSearchRequest {
    if (size == null) {
      size = 10;
    }

    // 기본 정렬: 입사일 최신순
    if (sortField == null || sortField.isBlank()) {
      sortField = "hireDate";
    }

    if (sortDirection == null || sortDirection.isBlank()) {
      sortDirection = "desc";
    }
  }
}