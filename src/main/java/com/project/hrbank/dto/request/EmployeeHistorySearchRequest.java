package com.project.hrbank.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Builder
@Getter
public class EmployeeHistorySearchRequest {
    private String employeeNumber;
    private String type;
    private String memo;
    private String ipAddress;
    private Instant atFrom;
    private Instant atTo;
    private Long idAfter;
    private String cursor;
    private Long size;
    private String sortField;
    private String sortDirection;
}
