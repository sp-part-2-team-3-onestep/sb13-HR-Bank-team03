package com.project.hrbank.service;

import com.project.hrbank.dto.response.DepartmentDto;

public interface DepartmentService {

    DepartmentDto findById(Long id);
}
