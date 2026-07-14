package com.project.hrbank.controller;

import com.project.hrbank.controller.doc.EmployeeHistoryControllerDoc;
import com.project.hrbank.dto.response.EmployeeHistoryDetailResponse;
import com.project.hrbank.service.EmployeeHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/change-logs")
@RequiredArgsConstructor
@Slf4j
public class EmployeeHistoryController implements EmployeeHistoryControllerDoc {

    private final EmployeeHistoryService employeeHistoryService;

    @Override
    @GetMapping("/{id}")
    public EmployeeHistoryDetailResponse findById(
            @PathVariable Long id
    ) {
        log.info("직원 정보 수정 이력 상세 조회 요청: id={}", id);

        return employeeHistoryService.findById(id);
    }
}