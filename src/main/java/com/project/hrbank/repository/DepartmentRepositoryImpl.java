package com.project.hrbank.repository;

import com.project.hrbank.domain.Department;
import com.project.hrbank.dto.request.DepartmentSearchRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DepartmentRepositoryImpl implements PagingRepository {

    private final EntityManager em;

    @Override
    public List<Department> searchByCursor(DepartmentSearchRequest request) {

        StringBuilder jpql = new StringBuilder("SELECT d FROM Department d WHERE d.deletedAt IS NULL ");

        // 1. 검색어 조건
        boolean hasSearch = StringUtils.hasText(request.nameOrDescription());
        if (hasSearch) {
            jpql.append("AND (LOWER(d.departmentName) LIKE LOWER(:search) OR LOWER(d.description) LIKE LOWER(:search)) ");
        }

        String field = "name".equalsIgnoreCase(request.sortField()) ? "d.departmentName" : "d.establishedDate";
        String direction = "asc".equalsIgnoreCase(request.sortDirection()) ? "ASC" : "DESC";

        // 2. 커서 기반 페이징 조건 (lastId -> idAfter 로 수정)
        boolean hasCursor = StringUtils.hasText(request.cursor()) && request.idAfter() != null;
        if (hasCursor) {
            String op = "asc".equalsIgnoreCase(request.sortDirection()) ? ">" : "<";

            jpql.append("AND (").append(field).append(" ").append(op).append(" :cursor ")
                // 쿼리 파라미터 이름도 :idAfter 로 매핑
                .append("OR (").append(field).append(" = :cursor AND d.id ").append(op).append(" :idAfter)) ");
        }

        // 3. 정렬 조건 설정
        jpql.append("ORDER BY ").append(field).append(" ").append(direction).append(", d.id ").append(direction);

        TypedQuery<Department> query = em.createQuery(jpql.toString(), Department.class);

        // 4. 파라미터 바인딩
        if (hasSearch) {
            query.setParameter("search", "%" + request.nameOrDescription() + "%");
        }

        if (hasCursor) {
            if ("name".equalsIgnoreCase(request.sortField())) {
                query.setParameter("cursor", request.cursor());
            } else {
                query.setParameter("cursor", LocalDate.parse(request.cursor()));
            }
            query.setParameter("idAfter", request.idAfter()); // 바인딩도 idAfter 로 변경
        }

        query.setMaxResults(request.size() + 1); // 다음 페이지 여부 판별을 위해 +1개 조회
        return query.getResultList();
    }

    @Override
    public long countDepartments(String nameOrDescription) {
        StringBuilder jpql = new StringBuilder("SELECT COUNT(d) FROM Department d WHERE d.deletedAt IS NULL ");

        boolean hasSearch = StringUtils.hasText(nameOrDescription);
        if (hasSearch) {
            jpql.append("AND (LOWER(d.departmentName) LIKE LOWER(:search) OR LOWER(d.description) LIKE LOWER(:search)) ");
        }

        TypedQuery<Long> query = em.createQuery(jpql.toString(), Long.class);
        if (hasSearch) {
            query.setParameter("search", "%" + nameOrDescription + "%");
        }
        return query.getSingleResult();
    }
}