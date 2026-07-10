package com.project.hrbank.service.basic;

import com.project.hrbank.domain.Department;
import com.project.hrbank.dto.response.DepartmentDto;
import com.project.hrbank.exception.DepartmentNotExistException;
import com.project.hrbank.repository.DepartmentRepository;
import com.project.hrbank.repository.EmployeeRepository;
import com.project.hrbank.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicDepartmentService implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public DepartmentDto findById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotExistException(
                                "부서가 존재하지 않습니다. - " + id, "Department not exists"));

        long employeeCount = employeeRepository.countByDepartment_Id(id);

        return new DepartmentDto(
                department.getId(),
                department.getDepartmentName(),
                department.getDescription(),
                department.getEstablishedDate(),
                employeeCount
                );
    }


}
