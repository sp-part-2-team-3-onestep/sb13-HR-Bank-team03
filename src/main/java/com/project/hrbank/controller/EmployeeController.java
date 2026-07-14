package com.project.hrbank.controller;

import com.project.hrbank.controller.doc.EmployeeControllerDoc;
import com.project.hrbank.domain.EmployeeStatus;
import com.project.hrbank.dto.request.EmployeeCreateRequest;
import com.project.hrbank.dto.response.EmployeeDto;
import com.project.hrbank.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EmployeeDto> create(
            @RequestPart(name = "employee") EmployeeCreateRequest request,
            @RequestPart(name = "profile", required = false) MultipartFile file
    ){
        return ResponseEntity.ok(employeeService.create(request,file));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(
            HttpServletRequest request,
            @PathVariable Long id
    ) {
        String ip = request.getRemoteAddr(); // ip
        employeeService.deleteEmployee(id,ip);

        return ResponseEntity.noContent().build();
    }
}