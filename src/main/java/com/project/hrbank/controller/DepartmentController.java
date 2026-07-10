package com.project.hrbank.controller;

import com.project.hrbank.controller.doc.DepartmentControllerDoc;
import com.project.hrbank.dto.request.DepartmentCreateRequest;
import com.project.hrbank.dto.response.DepartmentDto;
import com.project.hrbank.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping({"/api/departments"})
@RequiredArgsConstructor
@Slf4j
public class DepartmentController implements DepartmentControllerDoc {

    private final DepartmentService departmentService;

    @PostMapping("")
    public ResponseEntity<DepartmentDto> create(
            @RequestBody DepartmentCreateRequest request
            )
    {
        return ResponseEntity.ok(departmentService.create(request));
    }

}
