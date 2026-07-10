package com.project.hrbank.controller;

import com.project.hrbank.controller.doc.EmployeeControllerDoc;
import com.project.hrbank.domain.EmployeeStatus;
import com.project.hrbank.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping({"/api/employees"})
@RequiredArgsConstructor
@Slf4j
public class EmployeeController implements EmployeeControllerDoc {

    private final EmployeeService employeeService;

    @GetMapping("/count")
    public ResponseEntity<Long> countEmployees(
            @RequestParam(required = false) EmployeeStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {

        return ResponseEntity.ok(employeeService.countEmployees(status, fromDate, toDate));
    }
}
