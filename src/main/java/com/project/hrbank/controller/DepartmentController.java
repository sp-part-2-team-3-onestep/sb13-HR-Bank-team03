package com.project.hrbank.controller;

import com.project.hrbank.dto.response.DepartmentDto;
import com.project.hrbank.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDto> findById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(departmentService.findById(id));
    }
}
