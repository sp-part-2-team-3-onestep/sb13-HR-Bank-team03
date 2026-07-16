package com.project.hrbank.service.basic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.hrbank.domain.Diff;
import com.project.hrbank.domain.Employee;
import com.project.hrbank.domain.EmployeeHistory;
import com.project.hrbank.domain.EmployeeHistoryType;
import com.project.hrbank.dto.request.EmployeeHistorySearchRequest;
import com.project.hrbank.dto.response.ChangeLogDto;
import com.project.hrbank.dto.response.CursorPageResponseChangeLogDto;
import com.project.hrbank.dto.response.DiffDto;
import com.project.hrbank.dto.response.EmployeeHistoryDetailResponse;
import com.project.hrbank.exception.BaseException;
import com.project.hrbank.mapper.DtoMapper;
import com.project.hrbank.repository.EmployeeHistoryRepository;
import com.project.hrbank.repository.EmployeeRepository;
import com.project.hrbank.service.EmployeeHistoryService;
import io.swagger.v3.core.util.Json;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BasicEmployeeHistoryService implements EmployeeHistoryService {

    private final EmployeeHistoryRepository employeeHistoryRepository;
    private final EmployeeRepository employeeRepository;
    private final DtoMapper dtoMapper;

    @Override
    public CursorPageResponseChangeLogDto findByConditions(
            EmployeeHistorySearchRequest request
    ) {
        String field = getPagingField(request.getSortField());
        Slice<EmployeeHistory> result =
                employeeHistoryRepository.findByCondition(
                        request.getEmployeeNumber(),
                        getEmployeeHistoryTypeString(request.getType()),
                        request.getMemo(),
                        request.getIpAddress(),
                        request.getAtFrom(),
                        request.getAtTo(),
                        request.getIdAfter(),
                        request.getCursor(),
                        request.getSize(),
                        field,
                        request.getSortDirection()
                );

        List<EmployeeHistory> content = result.getContent();
        boolean hasNext = result.hasNext();
        long totalElements = employeeHistoryRepository.count();
        String nextCursor = getNextCursor(content, field);
        Long nextIdAfter = getNextId(content);

        List<ChangeLogDto> changeLogs =
                content.stream()
                        .map(dtoMapper::toDto)
                        .toList();

        return new CursorPageResponseChangeLogDto(
                changeLogs,
                nextCursor,
                nextIdAfter,
                (long) content.size(),
                totalElements,
                hasNext
        );
    }

    @Override
    public EmployeeHistoryDetailResponse findById(Long id) {
        ObjectMapper jackson = new ObjectMapper();

        EmployeeHistory employeeHistory =
                employeeHistoryRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "직원 정보 수정 이력을 찾을 수 없습니다. id=" + id
                                )
                        );

        try {
            List<DiffDto> detail = jackson.readValue(
                    employeeHistory.getChangeDetail(),
                    new TypeReference<List<Diff>>(){}
            ).stream().map(dtoMapper::toDto).toList();
            System.out.println(detail);
            return dtoMapper.toEmployeeHistoryDetailResponse(employeeHistory,detail);
        } catch (JsonProcessingException e){
            log.error(e.getMessage(),e);
            throw new BaseException();
        }
    }

    private String getNextCursor(
            List<EmployeeHistory> content,
            String filterType
    ) {
        EmployeeHistory last =
                content.isEmpty()
                        ? null
                        : content.get(content.size() - 1);

        if (last == null) {
            return null;
        }

        if (filterType.equals("ipAddress")) {
            return last.getId().toString();
        }

        return last.getCreateAt().toString();
    }

    private Long getNextId(List<EmployeeHistory> content) {
        return content.isEmpty()
                ? null
                : content.get(content.size() - 1).getId();
    }

    private String getPagingField(String sortField) {
        return switch (sortField) {
            case "ipAddress" -> "ipAddress";
            default -> "createAt";
        };
    }

    private EmployeeHistoryType getEmployeeHistoryTypeString(String type) {
        if (type == null) {
            return null;
        }

        return switch (type) {
            case "CREATED" -> EmployeeHistoryType.CREATED;
            case "UPDATED" -> EmployeeHistoryType.UPDATED;
            case "DELETED" -> EmployeeHistoryType.DELETED;
            default -> null;
        };
    }

    @Override
    public long countChangeLogs(Instant fromDate, Instant toDate) {
        Instant to = toDate != null
                ? toDate
                : Instant.now();

        Instant from = fromDate != null
                ? fromDate
                : to.minus(7, ChronoUnit.DAYS);

        return employeeHistoryRepository.countByCreateAtBetween(from, to);
    }
}