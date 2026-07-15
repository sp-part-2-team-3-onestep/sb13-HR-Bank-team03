package com.project.hrbank.dto.request;

public record DepartmentSearchRequest(
    String nameOrDescription,
    String cursor,
    Long idAfter,
    Integer size,
    String sortField,
    String sortDirection
) {
    // size에 기본값을 부여하고 싶다면 컴팩트 생성자를 활용할 수 있습니다.
    public DepartmentSearchRequest {
        if (size == null) size = 10;
        if (sortField == null || sortField.isBlank()) sortField = "establishedDate";
        if (sortDirection == null || sortDirection.isBlank()) sortDirection = "desc";
    }
}