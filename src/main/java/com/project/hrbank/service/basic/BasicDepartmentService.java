package com.project.hrbank.service.basic;


import com.project.hrbank.domain.Department;
import com.project.hrbank.dto.request.DepartmentCreateRequest;
import com.project.hrbank.dto.response.DepartmentDto;
import com.project.hrbank.exception.DepartmentExisted;
import com.project.hrbank.mapper.DtoMapper;
import com.project.hrbank.repository.DepartmentRepository;
import com.project.hrbank.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicDepartmentService implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final DtoMapper mapper;

    public DepartmentDto create(DepartmentCreateRequest request){
        String newName = request.name();
        String newDescription = request.description();
        String newEstablishedDate = request.establishedDate();

        // "YYYY-MM-DD"(ISO-8610 표준 포맷) -> Localdate
        LocalDate newDate = LocalDate.parse(newEstablishedDate);

        // 제약 조건 - 이름은 중복 될 수 없음
        if (departmentRepository.existsByDepartmentName(newName)) throw new DepartmentExisted("이름이 이미 존재합니다. - " + newName,"Department already exists");

        return mapper.toDto(
                departmentRepository.save(new Department(newName,newDescription,newDate))
        );
    }

}
