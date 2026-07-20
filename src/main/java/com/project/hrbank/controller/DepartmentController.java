package com.project.hrbank.controller;

import com.project.hrbank.controller.doc.DepartmentControllerDoc;
import com.project.hrbank.dto.request.DepartmentCreateRequest;
import com.project.hrbank.dto.request.DepartmentSearchRequest;
import com.project.hrbank.dto.request.DepartmentUpdateRequest;
import com.project.hrbank.dto.response.CursorPageResponse;
import com.project.hrbank.dto.response.DepartmentDto;
import com.project.hrbank.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/{id}")
    public ResponseEntity<DepartmentDto> update(
        @PathVariable Long id,
        @RequestBody DepartmentUpdateRequest request
    ){
        return ResponseEntity.ok(departmentService.update(id,request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDto> findById(
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(departmentService.findById(id));
    }

    /**
     * 부서 목록 조회 (무한 스크롤 커서 기반 페이징 - RequestDTO 적용)
     */
    @GetMapping("")
    public ResponseEntity<CursorPageResponse<DepartmentDto>> getDepartments(
        DepartmentSearchRequest searchRequest
    ) {
        return ResponseEntity.ok(departmentService.getDepartmentsWithCursor(searchRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        departmentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}