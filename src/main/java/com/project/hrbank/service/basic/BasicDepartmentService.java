package com.project.hrbank.service.basic;

import com.project.hrbank.domain.Department;
import com.project.hrbank.dto.request.DepartmentCreateRequest;
import com.project.hrbank.dto.request.DepartmentSearchRequest;
import com.project.hrbank.dto.request.DepartmentUpdateRequest;
import com.project.hrbank.dto.response.CursorPageResponse;
import com.project.hrbank.dto.response.DepartmentDto;
import com.project.hrbank.exception.DepartmentNameDuplicateException;
import com.project.hrbank.exception.DepartmentNotExistException;
import com.project.hrbank.mapper.DtoMapper;
import com.project.hrbank.repository.DepartmentRepository;
import com.project.hrbank.repository.EmployeeRepository;
import com.project.hrbank.repository.PagingRepository;
import com.project.hrbank.service.DepartmentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BasicDepartmentService implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final DtoMapper mapper;

    public DepartmentDto create(DepartmentCreateRequest request){
        String newName = request.name();
        String newDescription = request.description();
        String newEstablishedDate = request.establishedDate();

        // "YYYY-MM-DD"(ISO-8610 표준 포맷) -> Localdate
        LocalDate newDate = LocalDate.parse(newEstablishedDate);

        // 제약 조건 - 이름은 중복 될 수 없음
        nameCheck(newName);

        Department dpt = departmentRepository.save(new Department(newName,newDescription,newDate));
        System.out.println(dpt.getId());
        return mapper.toDto(
                dpt
                ,0
        );
    }

    // Todo - employee 카운터 조회 매서드 Employee 레포지토리에 추가. dto 변환 매서드에 파라매터로 추가
    // Todo - localdatetime 변환시 값이 없어도 에러가 나지 않도록 수정.
    public DepartmentDto update(Long id, DepartmentUpdateRequest request){
        String newName = request.name();
        String newDescription = request.description();
        String newEstablishedDate = request.establishedDate();
        LocalDate newDate = LocalDate.parse(newEstablishedDate);

        // 제약 조건 - 이름은 중복 될 수 없음
        nameCheck(newName);
        // 제약 - id 에 해당하는 부서 없음
        Department department = getEntityOrExcept(id);

        if (newName != null) department.setDepartmentName(newName);
        if (newDescription != null) department.setDescription(newDescription);
        if (newDate != null) department.setEstablishedDate(newDate);


        return mapper.toDto(departmentRepository.save(department),0);
    }
  
    @Override
    public DepartmentDto findById(Long id) {
        Department department = getEntityOrExcept(id);
      
        int employeeCount = employeeRepository.countByDepartmentId(id);

        return mapper.toDto(department, employeeCount);
    }


    // 이름 중복을 체크하는 함수 입니다. 중복시 에러(이름 중복) 반환
    private void nameCheck(String name){
        if (departmentRepository.existsByDepartmentName(name)) {
            throw new DepartmentNameDuplicateException("이름이 이미 존재합니다. - " + name,"Department already exists");
        }
    }

    // 부서가 존재하지 않을 경우의 함수
    private Department getEntityOrExcept(Long id){
        Department department = departmentRepository.findById(id).orElse(null);
        if (department == null) {
            throw new DepartmentNotExistException("부서가 존재하지 않습니다. - " + id,"Department not exists");
        }
        return department;
    }

    @Override
    public CursorPageResponse<DepartmentDto> getDepartmentsWithCursor(DepartmentSearchRequest request) {

        List<Department> departments = departmentRepository.searchByCursor(request);

        boolean hasNext = departments.size() > request.size();
        if (hasNext) {
            departments.remove(request.size().intValue());
        }

        // Entity -> DTO 변환 시 실제 직원수를 조회하여 매핑
        List<DepartmentDto> content = departments.stream()
                .map(dept -> {
                    // DB에서 해당 부서의 실제 직원수를 조회
                    int employeeCount = employeeRepository.countByDepartmentId(dept.getId());

                    return mapper.toDto(dept, employeeCount);
                })
                .collect(Collectors.toList());

        String nextCursor = null;
        Long nextIdAfter = null;

        if (!content.isEmpty()) {
            DepartmentDto lastElement = content.get(content.size() - 1);

            if ("name".equalsIgnoreCase(request.sortField())) {
                nextCursor = lastElement.name();
            } else {
                nextCursor = lastElement.establishedDate();
            }

            nextIdAfter = lastElement.id().longValue();
        }

        long totalElements = departmentRepository.countDepartments(request.nameOrDescription());

        return new CursorPageResponse<>(
                content, nextCursor, nextIdAfter, request.size(), totalElements, hasNext
        );
    }
}
