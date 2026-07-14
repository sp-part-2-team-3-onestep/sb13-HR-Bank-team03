package com.project.hrbank.repository;

import com.project.hrbank.domain.Department;
import com.project.hrbank.dto.request.DepartmentSearchRequest;

import java.util.List;

public interface PagingRepository {
    List<Department> searchByCursor(DepartmentSearchRequest request);
    long countDepartments(String nameOrDescription);
}
