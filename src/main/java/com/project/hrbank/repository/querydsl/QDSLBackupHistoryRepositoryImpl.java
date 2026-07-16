package com.project.hrbank.repository.querydsl;

import com.project.hrbank.domain.BackupHistory;
import com.project.hrbank.domain.BackupStatus;
import com.project.hrbank.domain.QBackupHistory;
import com.project.hrbank.domain.QFileMeta;
import com.project.hrbank.dto.request.BackupHistorySearchRequest;
import com.project.hrbank.exception.BackupHistoryStatusException;
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
public class QDSLBackupHistoryRepositoryImpl implements QDSLBackupHistoryRepository {
    private final JPAQueryFactory queryFactory;

    private final QBackupHistory backupHistory = QBackupHistory.backupHistory;
    private final QFileMeta fileMeta = QFileMeta.fileMeta;

    public Slice<BackupHistory> backupHistory(
            BackupHistorySearchRequest req,
            int pageSize,
            String sort,
            String direction

    ) {

        boolean desc = direction.equalsIgnoreCase("desc");

        BooleanBuilder where = new BooleanBuilder();

        where.and(workerExpression(req.worker()));
        where.and(timeExpression(req.startedAtFrom(),req.startedAtTo()));
        where.and(typeExpression(getBackupHistoryStatus(req.status())));
        // 커서 조건 형성
        where.and(cursorExpression(desc,req.cursor(),sort));

        List<BackupHistory> data = queryFactory.selectFrom(backupHistory)
                .leftJoin(backupHistory.fileMeta, fileMeta).fetchJoin()
                .where(where)
                .orderBy(order(desc,sort))
                .limit(pageSize + 1L)
                .fetch();

        boolean hasNext = data.size() > pageSize;
        if (hasNext) data.remove(data.size()-1);

        System.out.println(data);

        return new SliceImpl<>(data, PageRequest.of(0,pageSize),hasNext);

    }


    private BooleanExpression workerExpression(String worker){
        return worker == null ? null : backupHistory.ip.containsIgnoreCase(worker);
    }

    // 시간 범위 ->  시작보다 크거나 같고. 끝보다 작거나 같음
    private BooleanExpression timeExpression(Instant start, Instant end){
        if (start == null) return end == null ? null : backupHistory.startTime.loe(end);
        return end == null ? backupHistory.startTime.goe(start) : backupHistory.startTime.between(start, end);
    }

    private BooleanExpression typeExpression(BackupStatus status){
        return status == null ? null : backupHistory.backupStatus.eq(status);
    }

    private BooleanExpression cursorExpression(boolean desc, String cursor, String field) {
        if (cursor == null) return null;
        Instant time = Instant.parse(cursor);
        // startedAt and endedAt
        if (field.equals("endedAt")) return desc ? backupHistory.endTime.lt(time) : backupHistory.endTime.gt(time);
        return desc ? backupHistory.startTime.lt(time) : backupHistory.startTime.gt(time);
    }

    private OrderSpecifier<?> order(boolean desc,String field) {
        if (field.equals("endedAt")) return desc ? backupHistory.endTime.desc() : backupHistory.endTime.asc();
        return desc ? backupHistory.startTime.desc() : backupHistory.startTime.asc();
    }


    private BackupStatus getBackupHistoryStatus(String status){
        if (status == null) return null;
        switch (status){
            case "COMPLETED" -> { return BackupStatus.DONE; }
            case "FAILED" -> { return BackupStatus.FAIL; }
            case "IN_PROGRESS" -> { return BackupStatus.RUNNING; }
            default -> throw new BackupHistoryStatusException("잘못된 요청입니다","not current request");
        }
    }



}
