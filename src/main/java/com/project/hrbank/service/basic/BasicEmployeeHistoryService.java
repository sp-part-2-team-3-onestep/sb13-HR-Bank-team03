package com.project.hrbank.service.basic;


import com.project.hrbank.domain.EmployeeHistory;
import com.project.hrbank.domain.EmployeeHistoryType;
import com.project.hrbank.dto.request.EmployeeHistorySearchRequest;
import com.project.hrbank.dto.response.ChangeLogDto;
import com.project.hrbank.dto.response.CursorPageResponseChangeLogDto;
import com.project.hrbank.mapper.DtoMapper;
import com.project.hrbank.repository.EmployeeHistoryRepository;
import com.project.hrbank.service.EmployeeHistoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class BasicEmployeeHistoryService implements EmployeeHistoryService {
    private final EmployeeHistoryRepository employeeHistoryRepository;
    private final DtoMapper dtoMapper;



    public CursorPageResponseChangeLogDto findByConditions(EmployeeHistorySearchRequest request){
        // 사번, 메모, ip주소 는 부분일치 조건
        // 시간 은 범위조건
        // 유형은 완전일치 조건  // CREATED, UPDATED, DELETED
        // 정렬조건은 하나만

        // 정렬필드가 at -> Instant , ipAddress -> id 반환 (커서)
//        String type = null;
//        if (request.getType() != null) type = getEmployeeHistoryTypeString(request.getType());


        String field = getPagingField(request.getSortField());

        Slice<EmployeeHistory> res = employeeHistoryRepository.findByCondition(
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

        List<EmployeeHistory> content = res.getContent();
        Boolean hasNext = res.hasNext();
        Long totalElements = employeeHistoryRepository.count();
        String nextCursor = getNextCursor(content,field); // tmp
        Long nextIdAfter = getNextId(content);

        List<ChangeLogDto> cld = content.stream().map(dtoMapper::toDto).toList();

        return new CursorPageResponseChangeLogDto(
                cld,
                nextCursor,
                nextIdAfter,
                (long) content.size(),
                totalElements,
                hasNext
        );

    }


//    private Pageable createPageable(Integer size, String sortField, String sortDirection){
//        // 정렬 -> ipAddress = ipAddress, at = createdAt
//        // direction -> asc, desc
//        Sort sort = Sort.by(getSortDirection(sortDirection),getPagingField(sortField));
//        return PageRequest.of(0, size, sort);
//    }


    private String getNextCursor(List<EmployeeHistory> content, String filterType){
        EmployeeHistory last = content.isEmpty() ? null : content.get(content.size() - 1);
        if (last == null) return null;
        else if (filterType.equals("ipAddress")) return last.getId().toString();
        else return last.getCreateAt().toString();
    }

    private Long getNextId(List<EmployeeHistory> content){
        return content.isEmpty() ? null : content.get(content.size() - 1).getId();
    }


    private String getPagingField(String sortField){
        return switch (sortField) {
            case "ipAddress" -> "ipAddress";
            default -> "createAt";
        };
    }

//    private Sort.Direction getSortDirection(String sortDirection){
//        return sortDirection.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
//    }

    private EmployeeHistoryType getEmployeeHistoryTypeString(String type){
        if (type == null) return null;
        return switch (type) {
            case "CREATE" -> EmployeeHistoryType.EMPLOYEE_ADD;
            case "UPDATE" -> EmployeeHistoryType.EMPLOYEE_UPDATED;
            case "DELETE" -> EmployeeHistoryType.EMPLOYEE_DELETED;
            default -> null;
        };
    }


}
