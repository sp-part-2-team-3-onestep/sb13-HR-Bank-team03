package com.project.hrbank.controller;

import com.project.hrbank.controller.doc.EmployeeHistoryControllerDoc;
import com.project.hrbank.dto.request.EmployeeHistorySearchRequest;
import com.project.hrbank.dto.response.CursorPageResponseChangeLogDto;
import com.project.hrbank.dto.response.EmployeeHistoryDetailResponse;
import com.project.hrbank.service.EmployeeHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/change-logs")
@RequiredArgsConstructor
@Slf4j
public class EmployeeHistoryController implements EmployeeHistoryControllerDoc {

    private final EmployeeHistoryService employeeHistoryService;


    @GetMapping
    public ResponseEntity<CursorPageResponseChangeLogDto> findByParam(
            @RequestParam(required = false) String employeeNumber,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String memo,
            @RequestParam(required = false) String ipAddress,
            @RequestParam(required = false) Instant atFrom,
            @RequestParam(required = false) Instant atTo,
            @RequestParam(required = false) Long idAfter,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(defaultValue = "at") String sortField,
            @RequestParam(defaultValue = "desc") String sortDirection
    ) {
        EmployeeHistorySearchRequest request = EmployeeHistorySearchRequest.builder()
                .employeeNumber(employeeNumber)
                .type(type)
                .memo(memo)
                .ipAddress(ipAddress)
                .atFrom(atFrom)
                .atTo(atTo)
                .idAfter(idAfter)
                .cursor(cursor)
                .size(size)
                .sortField(sortField)
                .sortDirection(sortDirection)
                .build();

        return ResponseEntity.ok(
                employeeHistoryService.findByConditions(request)
        );
    }

    @Override
    @GetMapping("/{id}")
    public EmployeeHistoryDetailResponse findById(
            @PathVariable Long id
    ) {
        log.info("직원 정보 수정 이력 상세 조회 요청: id={}", id);
        return employeeHistoryService.findById(id);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countChangeLogs(
            @RequestParam(required = false) Instant fromDate,
            @RequestParam(required = false) Instant toDate
    ) {
        return ResponseEntity.ok(employeeHistoryService.countChangeLogs(fromDate, toDate));
    }

}