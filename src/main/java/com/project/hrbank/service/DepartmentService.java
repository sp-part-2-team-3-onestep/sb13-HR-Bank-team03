package com.project.hrbank.service;

import com.project.hrbank.dto.request.DepartmentCreateRequest;
import com.project.hrbank.dto.request.DepartmentSearchRequest;
import com.project.hrbank.dto.request.DepartmentUpdateRequest;
import com.project.hrbank.dto.response.CursorPageResponse;
import com.project.hrbank.dto.response.DepartmentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DepartmentService {

    DepartmentDto create(DepartmentCreateRequest request);
    DepartmentDto update(Long id, DepartmentUpdateRequest request);
    DepartmentDto findById(Long id);
    CursorPageResponse<DepartmentDto> getDepartmentsWithCursor(DepartmentSearchRequest request);
}