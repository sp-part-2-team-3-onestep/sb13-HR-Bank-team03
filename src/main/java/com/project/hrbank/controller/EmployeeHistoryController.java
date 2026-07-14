package com.project.hrbank.controller;

import com.project.hrbank.dto.response.EmployeeHistoryDetailResponse;
import com.project.hrbank.service.EmployeeHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/change-logs")
@RequiredArgsConstructor
@Slf4j
public class EmployeeHistoryController {

    private final EmployeeHistoryService employeeHistoryService;

    @GetMapping("/{id}")
    public EmployeeHistoryDetailResponse findById(
            @PathVariable Long id
    ) {
        log.info("직원 정보 수정 이력 상세 조회 요청: id={}", id);

        return employeeHistoryService.findById(id);
    }
}