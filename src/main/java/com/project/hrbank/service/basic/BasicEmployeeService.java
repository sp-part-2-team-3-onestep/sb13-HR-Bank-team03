package com.project.hrbank.service.basic;


import com.project.hrbank.config.DataCondition;
import com.project.hrbank.domain.*;
import com.project.hrbank.dto.request.EmployeeCreateRequest;
import com.project.hrbank.dto.request.EmployeeSearchRequest;
import com.project.hrbank.dto.response.CursorPageResponse;
import com.project.hrbank.dto.request.EmployeeUpdateRequest;
import com.project.hrbank.dto.response.EmployeeDto;
import com.project.hrbank.exception.BaseException;
import com.project.hrbank.exception.DepartmentNotExistException;
import com.project.hrbank.exception.EmployeeDuplicateException;
import com.project.hrbank.exception.EmployeeNotExistException;
import com.project.hrbank.infra.Structure;
import com.project.hrbank.mapper.DtoMapper;
import com.project.hrbank.repository.DepartmentRepository;
import com.project.hrbank.repository.EmployeeHistoryRepository;
import com.project.hrbank.repository.EmployeeRepository;
import com.project.hrbank.repository.FileMetaRepository;
import com.project.hrbank.service.EmployeeService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.project.hrbank.dto.response.EmployeeDistributionDto;
import java.util.ArrayList;

import java.io.IOException;
import java.io.InputStream;
import java.time.*;

@Service
@RequiredArgsConstructor
@Transactional
public class BasicEmployeeService implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeHistoryRepository employeeHistoryRepository;
    private final DepartmentRepository departmentRepository;
    private final FileMetaRepository fileMetaRepository;
    private final Structure structure;
    private final DtoMapper mapper;
    private final DataCondition dataCondition;

    @Transactional(readOnly = true)
    @Override
    public long countEmployees(EmployeeStatus status, LocalDate fromDate, LocalDate toDate) {
        Instant from = fromDate != null ? fromDate.atStartOfDay(ZoneOffset.UTC).toInstant() : null;
        Instant to = toDate != null
            ? toDate.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant()
            : (from != null ? Instant.now() : null);

        return employeeRepository.countByStatusAndHireDateRange(status, from, to);
    }

    @Override
    public EmployeeDto create(EmployeeCreateRequest request, MultipartFile file, String ip){
        String name = request.name();
        String email = request.email();
        // 이메일 중복 체크
        checkEmail(email);

        Department department = getDepartmentOrExcept(request.departmentId());
        String employeeNumber = genRandomEmployeeNumber();
        String position = request.position();

        Instant hireDate = request.hireDate().atStartOfDay(ZoneOffset.UTC).toInstant();

        EmployeeStatus status = EmployeeStatus.ACTIVE;
        FileMeta fileMeta = file == null ? null : getFileMetaFromMultipart(file);

        String memo = request.memo();

        Employee emp = employeeRepository.save(Employee.create(
            name,
            department,
            employeeNumber,
            email,
            position,
            hireDate,
            status,
            fileMeta
        ));

        dataCondition.flagSetChanged();
        // 히스토리 추가

        employeeHistoryRepository.save(new EmployeeHistory(
                emp,
                emp.getDepartment(),
                EmployeeHistoryType.EMPLOYEE_ADD,
                "직원 생성",
                memo,
                ip
        ));

        return mapper.toDto(emp);
    }

    @Transactional(readOnly = true)
    @Override
    public EmployeeDto findById(Long id) {
        Employee employee = getEmployeeOrExcept(id);

        return mapper.toDto(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public CursorPageResponse<EmployeeDto> getEmployeesWithCursor(
        EmployeeSearchRequest request
    ) {

        List<Employee> employees =
            employeeRepository.searchByCursor(request);


        boolean hasNext =
            employees.size() > request.size();


        if (hasNext) {
            employees.remove(request.size().intValue());
        }


        List<EmployeeDto> content =
            employees.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());


        String nextCursor = null;
        Long nextIdAfter = null;


        if (!content.isEmpty()) {

            EmployeeDto last =
                content.get(content.size() - 1);


            if ("name".equalsIgnoreCase(request.sortField())) {

                nextCursor = last.name();

            } else if ("employeeNumber"
                .equalsIgnoreCase(request.sortField())) {

                nextCursor = last.employeeNumber();

            } else {

                nextCursor = last.hireDate().toString();
            }
            nextIdAfter = last.id();
        }


        long totalElements =
            employeeRepository.countEmployees();


        return new CursorPageResponse<>(
            content,
            nextCursor,
            nextIdAfter,
            request.size(),
            totalElements,
            hasNext
        );
    }

    @Override
    public void deleteEmployee(Long id, String remoteIp) {
        Employee employee = getEmployeeOrExcept(id);

        FileMeta profileImage = employee.getProfileImaged();

        employee.update(
            employee.getName(),
            employee.getDepartment(),
            employee.getEmail(),
            employee.getPosition(),
            employee.getHireDate(),
            EmployeeStatus.RESIGNED,
            null,
            Instant.now()
        );

        Employee emp = employeeRepository.save(employee);

        // EmployeeHistory 로그 추가
        employeeHistoryRepository.save(new EmployeeHistory(
            emp,
            emp.getDepartment(),
            EmployeeHistoryType.EMPLOYEE_DELETED,
            "직원 삭제",
            null,
            remoteIp
        ));


        if (profileImage != null) {
            structure.delete(profileImage.getFileName());
            fileMetaRepository.delete(profileImage);
        }

        dataCondition.flagSetChanged();
    }

    @Override
    public EmployeeDto update(Long id, EmployeeUpdateRequest request, MultipartFile file, String remoteIp) {
        Employee employee = getEmployeeOrExcept(id);

        String newEmail = request.email() != null ? request.email() : employee.getEmail();
        if (!newEmail.equals(employee.getEmail())) {
            checkEmail(newEmail);
        }

        Department newDepartment = request.departmentId() != null
                ? getDepartmentOrExcept(request.departmentId())
                : employee.getDepartment();

        String newName = request.name() != null ? request.name() : employee.getName();
        String newPosition = request.position() != null ? request.position() : employee.getPosition();
        Instant newHireDate = request.hireDate() != null ? request.hireDate() : employee.getHireDate();
        EmployeeStatus newStatus = request.status() != null ? request.status() : employee.getStatus();

        FileMeta oldProfileImage = employee.getProfileImaged();
        FileMeta newProfileImage = oldProfileImage;
        if (file != null && !file.isEmpty()) {
            newProfileImage = getFileMetaFromMultipart(file);
        }

        employee.update(
                newName,
                newDepartment,
                newEmail,
                newPosition,
                newHireDate,
                newStatus,
                newProfileImage,
                employee.getDeletedAt()
        );

        Employee saved = employeeRepository.save(employee);

        employeeHistoryRepository.save(new EmployeeHistory(
                saved,
                saved.getDepartment(),
                EmployeeHistoryType.EMPLOYEE_UPDATED,
                "직원 수정",
                request.memo(),
                remoteIp
        ));

        if (file != null && !file.isEmpty() && oldProfileImage != null) {
            structure.delete(oldProfileImage.getFileName());
            fileMetaRepository.delete(oldProfileImage);
        }

        dataCondition.flagSetChanged();

        return mapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDistributionDto> getEmployeeDistribution(
            String groupBy,
            EmployeeStatus status
    ) {
        List<Object[]> rows;

        if ("department".equalsIgnoreCase(groupBy)) {
            rows = employeeRepository.countGroupByDepartment(status);
        } else if ("position".equalsIgnoreCase(groupBy)) {
            rows = employeeRepository.countGroupByPosition(status);
        } else {
            throw new IllegalArgumentException("지원하지 않는 groupBy입니다. : " + groupBy);
        }

        long total = rows.stream()
                .mapToLong(r -> (Long) r[1])
                .sum();

        List<EmployeeDistributionDto> result = new ArrayList<>();

        for (Object[] row : rows) {
            String groupKey = (String) row[0];
            Long count = (Long) row[1];

            double percentage =
                    total == 0
                            ? 0.0
                            : Math.round(count * 1000.0 / total) / 10.0;

            result.add(
                    new EmployeeDistributionDto(
                            groupKey,
                            count,
                            percentage
                    )
            );
        }

        return result;
    }

    private Employee getEmployeeOrExcept(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotExistException("직원이 존재하지 않습니다. - " + id, "Employee not exists"));
    }

    private void checkEmail(String email){
        if (employeeRepository.existsByEmail(email)){
            throw new EmployeeDuplicateException(
                "email was duplicated : " + email,
                "이메일 중복"
            );
        }
    }

    private Department getDepartmentOrExcept(Long departmentId){
        Department department = departmentRepository.findById(departmentId).orElse(null);
        if (department == null) {
            throw new DepartmentNotExistException("부서가 존재하지 않습니다. - " + departmentId,"Department not exists");
        }
        return department;
    }

    private String genRandomEmployeeNumber(){
        String tag = "EMP";
        Instant now = Instant.now();
        int year =  now.atZone(ZoneOffset.UTC).toLocalDate().getYear();
        return tag + "-" + year + "-" + now.toEpochMilli();
    }

    private FileMeta getFileMetaFromMultipart(MultipartFile file){
        String originalFileName = saveProfileImage(file);
        return fileMetaRepository.save(new FileMeta(
            originalFileName,
            file.getContentType(),
            file.getSize()
        ));
    }

    private String saveProfileImage(MultipartFile file){
        try(InputStream in = file.getInputStream()){
            return structure.put(file.getOriginalFilename(),in.readAllBytes());
        } catch (IOException e){
            throw new BaseException("프로필 저장 에러");
        }
    }
}