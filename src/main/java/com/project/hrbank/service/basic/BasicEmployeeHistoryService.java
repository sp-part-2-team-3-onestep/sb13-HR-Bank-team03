package com.project.hrbank.service.basic;

import com.project.hrbank.domain.EmployeeHistory;
import com.project.hrbank.dto.response.EmployeeHistoryDetailResponse;
import com.project.hrbank.mapper.DtoMapper;
import com.project.hrbank.repository.EmployeeHistoryRepository;
import com.project.hrbank.service.EmployeeHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicEmployeeHistoryService implements EmployeeHistoryService {

    private final EmployeeHistoryRepository employeeHistoryRepository;
    private final DtoMapper dtoMapper;

    @Override
    public EmployeeHistoryDetailResponse findById(Long id) {
        EmployeeHistory employeeHistory =
                employeeHistoryRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "직원 정보 수정 이력을 찾을 수 없습니다. id=" + id
                                )
                        );

        return dtoMapper.toEmployeeHistoryDetailResponse(employeeHistory);
    }


}