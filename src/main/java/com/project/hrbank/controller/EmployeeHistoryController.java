package com.project.hrbank.controller;

import com.project.hrbank.controller.doc.EmployeeHistoryControllerDoc;
import com.project.hrbank.dto.request.EmployeeHistorySearchRequest;
import com.project.hrbank.dto.response.CursorPageResponseChangeLogDto;
import com.project.hrbank.service.EmployeeHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping({"/api/change-logs"})
@RequiredArgsConstructor
@Slf4j
public class EmployeeHistoryController implements EmployeeHistoryControllerDoc {
    private final EmployeeHistoryService employeeHistoryService;

    @GetMapping
    public ResponseEntity<CursorPageResponseChangeLogDto> findByParam(
            @RequestParam(required = false) String employeeNumber,
            @RequestParam(required = false) String type,  // CREATED, UPDATED, DELETED
            @RequestParam(required = false) String memo,
            @RequestParam(required = false) String ipAddress,
            @RequestParam(required = false) Instant atFrom, // datetime
            @RequestParam(required = false) Instant atTo, // datetime
            @RequestParam(required = false) Long idAfter,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(defaultValue = "at") String sortField, // ipAddress (ip), at(시간),
            @RequestParam(defaultValue = "desc") String sortDirection
    ){
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

        return ResponseEntity.ok(employeeHistoryService.findByConditions(request));
    }

}
