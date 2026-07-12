package com.project.hrbank.service.basic;


import com.project.hrbank.domain.Department;
import com.project.hrbank.domain.Employee;
import com.project.hrbank.domain.EmployeeStatus;
import com.project.hrbank.domain.FileMeta;
import com.project.hrbank.dto.request.EmployeeCreateRequest;
import com.project.hrbank.dto.response.EmployeeDto;
import com.project.hrbank.exception.BaseException;
import com.project.hrbank.exception.DepartmentNotExistException;
import com.project.hrbank.exception.EmployeeDuplicateException;
import com.project.hrbank.infra.Structure;
import com.project.hrbank.mapper.DtoMapper;
import com.project.hrbank.repository.DepartmentRepository;
import com.project.hrbank.repository.EmployeeRepository;
import com.project.hrbank.repository.FileMetaRepository;
import com.project.hrbank.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.*;

@Service
@RequiredArgsConstructor
public class BasicEmployeeService implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final FileMetaRepository fileMetaRepository;
    private final Structure  structure;
    private final DtoMapper mapper;

    @Override
    public long countEmployees(EmployeeStatus status, LocalDate fromDate, LocalDate toDate) {
        Instant from = fromDate != null ? fromDate.atStartOfDay(ZoneOffset.UTC).toInstant() : null;
        Instant to = toDate != null
                ? toDate.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant()
                : (from != null ? Instant.now() : null);

        return employeeRepository.countByStatusAndHireDateRange(status, from, to);
    }

    @Override
    public EmployeeDto create(EmployeeCreateRequest request, MultipartFile file){
        String name = request.name();
        String email = request.email();
        // 이메일 중복 체크
        checkEmail(email);

        Department department = getDepartmentOrExcept(request.departmentId());
        String employeeNumber = genRandomEmployeeNumber();
        String position = request.position();

        // Todo - 직원 엔티티의 hireDate 저장타입이 Instant  이나, 실제 반환값은 LocalDate 타입임
        // 추후, 엔티티의 타입을 LocalDate 로 변경하면, 그에 맞춰서 변경
        Instant hireDate = request.hireDate().atStartOfDay(ZoneOffset.UTC).toInstant();

        // 상태는 기본 재직중
        EmployeeStatus status = EmployeeStatus.ACTIVE;
        FileMeta fileMeta = file == null ? null : getFileMetaFromMultipart(file);

        // ?? 어디에 저장하는 값
        String memo = request.memo();



        Employee emp = employeeRepository.save(Employee.create(
                name,
                department,
                employeeNumber,
                email,
                position,
                hireDate,
                status,
                fileMeta // 선택적 이미지 저장
        ));
        return mapper.toDto(emp);
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
        String path = saveProfileImage(file);
        return fileMetaRepository.save(new FileMeta(
                path,
                path,
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
