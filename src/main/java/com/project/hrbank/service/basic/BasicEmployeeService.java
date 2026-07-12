package com.project.hrbank.service.basic;


import com.project.hrbank.domain.EmployeeStatus;
import com.project.hrbank.repository.EmployeeRepository;
import com.project.hrbank.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class BasicEmployeeService implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public long countEmployees(EmployeeStatus status, LocalDate fromDate, LocalDate toDate) {
        Instant from = fromDate != null ? fromDate.atStartOfDay(ZoneOffset.UTC).toInstant() : null;
        Instant to = toDate != null
                ? toDate.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant()
                : (from != null ? Instant.now() : null);

        return employeeRepository.countByStatusAndHireDateRange(status, from, to);
    }
}
