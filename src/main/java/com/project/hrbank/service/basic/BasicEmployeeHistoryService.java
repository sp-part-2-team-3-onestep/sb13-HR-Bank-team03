package com.project.hrbank.service.basic;

import com.project.hrbank.domain.EmployeeHistory;
import com.project.hrbank.domain.EmployeeHistoryType;
import com.project.hrbank.dto.request.EmployeeHistorySearchRequest;
import com.project.hrbank.dto.response.ChangeLogDto;
import com.project.hrbank.dto.response.CursorPageResponseChangeLogDto;
import com.project.hrbank.dto.response.EmployeeHistoryDetailResponse;
import com.project.hrbank.mapper.DtoMapper;
import com.project.hrbank.repository.EmployeeHistoryRepository;
import com.project.hrbank.service.EmployeeHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicEmployeeHistoryService implements EmployeeHistoryService {

    private final EmployeeHistoryRepository employeeHistoryRepository;
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
        EmployeeHistory employeeHistory =
                employeeHistoryRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "직원 정보 수정 이력을 찾을 수 없습니다. id=" + id
                                )
                        );

        return dtoMapper.toEmployeeHistoryDetailResponse(employeeHistory);
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
            case "CREATE" -> EmployeeHistoryType.EMPLOYEE_ADD;
            case "UPDATE" -> EmployeeHistoryType.EMPLOYEE_UPDATED;
            case "DELETE" -> EmployeeHistoryType.EMPLOYEE_DELETED;
            default -> null;
        };
    }
}