package com.project.hrbank.repository.querydsl;

import com.project.hrbank.domain.EmployeeHistory;
import com.project.hrbank.domain.EmployeeHistoryType;
import com.project.hrbank.domain.QEmployee;

import com.project.hrbank.domain.QEmployeeHistory;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.time.Instant;
import java.util.List;


@RequiredArgsConstructor
public class EmployeeHistoryRepositoryImpl implements EmployeeHistoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private static final QEmployeeHistory eh = QEmployeeHistory.employeeHistory;
    private static final QEmployee e = QEmployee.employee;

    @Override
    public Slice<EmployeeHistory> findByCondition(
            String employeeNumber,
            EmployeeHistoryType type,
            String memo,
            String ipAddress,
            Instant atFrom,
            Instant atTo,
            Long idAfter,
            String cursor,
            Long size,
            String sortField,
            String sortDirection
    ) {
        /*
        sortDirection가 asc가 아니라면 내림차순으로 간주(null이나 오타가 들어와도 기본값을 내림차순으로 처리)
        default value: desc
        */
        boolean desc = !"asc".equals(sortDirection);

        BooleanBuilder where = new BooleanBuilder();

//        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + type); // 이상 없음

        where.and(employeeNumberContains(employeeNumber));
        where.and(typeEq(type));
        where.and(memoContains(memo));
        where.and(ipAddressContains(ipAddress));
        where.and(atBetween(atFrom, atTo));
        where.and(cursorCondition(sortField, cursor, idAfter, desc));

        List<EmployeeHistory> results = queryFactory
                .selectFrom(eh) // EmployeeHistory를 조회 대상으로 잡는다.
                .join(eh.employee, e).fetchJoin() // Employee를 지연 로딩 없이 한 번에 같이 가져온다.
                .where(where) // BooleanBuilder에서 만든 where 조건들을 적용한다.
                .orderBy(orderSpecifiers(sortField, desc)) // orderSpecifiers가 만들어주는 정렬 조건
                .limit(size + 1L) // size보다 1개 더 가져온다.
                .fetch(); //

        boolean hasNext = results.size() > size;
        if (hasNext) {
            results.remove(results.size() - 1);
        }

        return new SliceImpl<>(results, PageRequest.of(0, Math.toIntExact(size)), hasNext);
    }



    /**
     * 필터 조건들을 하나씩 and로 붙인다.<br>
     * 각 메서드 값이 null => null을 return하는데
     * BooleanBuilder.and(null)은 아무것도 안 하고 무시한다.
     * 그래서 사용자가 값을 안 넘긴 필터는 자동으로 쿼리에서 빠진다.<br>
     * WHERE 1=1(TRUE) AND (조건1) AND (조건2)...<br>
     * .containsIgnoreCase() - 대소문자 구분 없이 이 문자열을 포함하는가?
     */
    private BooleanExpression employeeNumberContains(String employeeNumber) {
        return employeeNumber == null ?
                null : e.employeeNumber.containsIgnoreCase(employeeNumber);
    }



    private BooleanExpression typeEq(EmployeeHistoryType type) {
        return type == null ? null : eh.type.eq(type);
    }

    private BooleanExpression memoContains(String memo) {
        return memo == null ? null : eh.memo.containsIgnoreCase(memo);
    }

    private BooleanExpression ipAddressContains(String ipAddress) {
        return ipAddress == null ? null : eh.ipAddress.containsIgnoreCase(ipAddress);
    }

    private BooleanExpression atBetween(Instant from, Instant to) {
        if (from == null && to == null) return null;
        if (from == null) return eh.createAt.loe(to);
        if (to == null) return eh.createAt.goe(from);
        return eh.createAt.between(from, to);
    }

    private BooleanExpression cursorCondition(String sortField, String cursor, Long idAfter, boolean desc) {
        if (cursor == null || idAfter == null) {
            return null;
        }
        if ("ipAddress".equals(sortField)) {
            return desc
                    ? eh.ipAddress.lt(cursor).or(eh.ipAddress.eq(cursor).and(eh.id.lt(idAfter)))
                    : eh.ipAddress.gt(cursor).or(eh.ipAddress.eq(cursor).and(eh.id.gt(idAfter)));
        }
        Instant cursorAt = Instant.parse(cursor);
        return desc
                ? eh.createAt.lt(cursorAt).or(eh.createAt.eq(cursorAt).and(eh.id.lt(idAfter)))
                : eh.createAt.gt(cursorAt).or(eh.createAt.eq(cursorAt).and(eh.id.gt(idAfter)));
    }

    private OrderSpecifier<?>[] orderSpecifiers(String sortField, boolean desc) {
        if ("ipAddress".equals(sortField)) {
            return new OrderSpecifier[]{ desc ? eh.ipAddress.desc() : eh.ipAddress.asc(), desc ? eh.id.desc() : eh.id.asc() };
        }
        return new OrderSpecifier[]{ desc ? eh.createAt.desc() : eh.createAt.asc(), desc ? eh.id.desc() : eh.id.asc() };
    }
}
