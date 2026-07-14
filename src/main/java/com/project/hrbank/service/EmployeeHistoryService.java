package com.project.hrbank.service;

import com.project.hrbank.dto.request.EmployeeHistorySearchRequest;
import com.project.hrbank.dto.response.CursorPageResponseChangeLogDto;

public interface EmployeeHistoryService {

    CursorPageResponseChangeLogDto findByConditions(EmployeeHistorySearchRequest request);

}
