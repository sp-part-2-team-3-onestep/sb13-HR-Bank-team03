package com.project.hrbank.service;

import com.project.hrbank.dto.request.DepartmentCreateRequest;
import com.project.hrbank.dto.request.DepartmentUpdateRequest;
import com.project.hrbank.dto.response.DepartmentDto;

public interface DepartmentService {

    DepartmentDto create(DepartmentCreateRequest request);
    DepartmentDto update(Long id, DepartmentUpdateRequest request);
    DepartmentDto findById(Long id);
  
}
