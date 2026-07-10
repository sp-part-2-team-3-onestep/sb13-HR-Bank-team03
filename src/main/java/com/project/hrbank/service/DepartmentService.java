package com.project.hrbank.service;

import com.project.hrbank.dto.request.DepartmentCreateRequest;
import com.project.hrbank.dto.response.DepartmentDto;

public interface DepartmentService {
    DepartmentDto create(DepartmentCreateRequest request);
}
