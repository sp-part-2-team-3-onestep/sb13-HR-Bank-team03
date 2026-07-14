package com.project.hrbank.repository;

import com.project.hrbank.domain.EmployeeHistory;
import com.project.hrbank.repository.querydsl.EmployeeHistoryRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeHistoryRepository extends JpaRepository<EmployeeHistory, Long>, EmployeeHistoryRepositoryCustom {

//    @Query(
//            """
//            SELECT eh
//            FROM EmployeeHistory eh
//                JOIN FETCH eh.employee e
//            WHERE (:start_at IS NULL OR eh.createAt >= :start_at)
//                AND (:end_at IS NULL OR eh.createAt <= :end_at)
//            """
//    )
//    Slice<EmployeeHistory> findByCondition(
//            Pageable pageable,
////            @Param(value = "employeeNumber") String employeeNumber,
////            @Param(value = "memo") String memo,
////            @Param(value = "ip") String ipAddress,
////            @Param(value = "type") String type,
//            @Param(value = "start_at")Instant start,
//            @Param(value = "end_at") Instant end
//            );

//    Slice<EmployeeHistory> findByConditionWithTime();

// ===
//    @Query(
//            """
//            SELECT eh
//            FROM EmployeeHistory eh
//                JOIN FETCH eh.employee e
//            WHERE (:employeeNumber IS NULL OR e.employeeNumber LIKE CONCAT('%',:employeeNumber,'%'))
//                AND (:memo IS NULL OR eh.memo LIKE CONCAT('%',:memo,'%'))
//                AND (:ip IS NULL OR eh.ipAddress LIKE CONCAT('%',:ip,'%'))
//                AND (:type IS NULL OR eh.type = :type)
//                AND (:start_at IS NULL OR eh.createAt >= :start_at)
//                AND (:end_at IS NULL OR eh.createAt <= :end_at)
//
//            """
//    )
//    Slice<EmployeeHistory> findByConditionDesc(
//            Pageable pageable,
//            @Param(value = "employeeNumber") String employeeNumber,
//            @Param(value = "memo") String memo,
//            @Param(value = "ip") String ipAddress,
//            @Param(value = "type") String type,
//            @Param(value = "start_at")Instant start,
//            @Param(value = "end_at") Instant end,
//            @Param(value = "cursor") String cursorIp
//    );
}