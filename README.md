# [HR Bank](https://natural-elegance-production-b108.up.railway.app/#/dashboard)
## 프로젝트 소개
> 기업의 핵심 자산인 인적 자원 데이터를 안전하게 저장하고, 대량의 데이터를 주기적으로 백업 및 관리할 수 있는 서비스입니다.
<img width="1471" height="811" alt="image" src="https://github.com/user-attachments/assets/24a85344-b7e2-4208-8705-f51a4d7281a2" />

## ERD
<img width="898" height="608" alt="스크린샷 2026-07-19 오후 10 34 36" src="https://github.com/user-attachments/assets/959652c4-e2e1-4535-a38f-c7272340fc1f" />

## 기술 스택
|구분|기술|
|:-:|:-:|
|**Framework**|![spring](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)|
|**Database&ORM**|![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white) ![H2 DATABASE](https://img.shields.io/badge/H2-09476B?style=for-the-badge&logo=h2database&logoColor=white)|
|**Documentation**|![Swagger](https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white)|
|**배포 및 협업**|![Railway](https://img.shields.io/badge/Railway-13111C?style=for-the-badge&logo=railway&logoColor=white) ![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white)|

## 아키텍처
```
Client (HTML/CSS/JS)
        │  HTTP Request (REST API)
        ▼
┌─────────────────────────────────┐
│         Spring Boot             │
│                                 │
│  Controller                     │
│      │                          │
│  Service                        │
│      │                          │
│  Repository                     │
│   ├─ Spring Data JPA            │
│   └─ QueryDSL (동적 검색/커서 페이징)│
│      │                          │
│  MapStruct (DtoMapper)          │
│   Entity ↔ DTO 변환              │
└─────────────────────────────────┘
        │
        ▼
   Database
 ┌────────────┬──────────────┐
 │ PostgreSQL │  H2 (로컬)    │
 │  (운영)     │  (개발/테스트)  │
 └────────────┴──────────────┘
```

## JPQL
```
@Override
    public List<Employee> searchByCursor(EmployeeSearchRequest request) {

        StringBuilder jpql =
                new StringBuilder("SELECT e FROM Employee e WHERE e.deletedAt IS NULL ");
        String field;

        // 이름 또는 이메일 검색
        if (StringUtils.hasText(request.nameOrEmail())) {
            jpql.append("""
                    AND (
                        LOWER(e.name) LIKE LOWER(:nameOrEmail)
                        OR LOWER(e.email) LIKE LOWER(:nameOrEmail)
                    )
                    """);
        }

        // 상태
        if (request.status() != null) {
            jpql.append("""
                    AND e.status = :status
                    """);
        }

        // 사원번호
        if (StringUtils.hasText(request.employeeNumber())) {
            jpql.append("""
                    AND LOWER(e.employeeNumber)
                    LIKE LOWER(CONCAT('%', :employeeNumber, '%'))
                    """);
        }

        // 부서명
        if (StringUtils.hasText(request.departmentName())) {
            jpql.append("""
                    AND LOWER(e.department.departmentName)
                    LIKE LOWER(CONCAT('%', :departmentName, '%'))
                    """);
        }

        // 직함
        if (StringUtils.hasText(request.position())) {
            jpql.append("""
                    AND LOWER(e.position)
                    LIKE LOWER(CONCAT('%', :position, '%'))
                    """);
        }

        // 입사 시작일
        if (request.hireDateFrom() != null) {
            jpql.append("AND e.hireDate >= :hireDateFrom ");
        }

        // 입사 종료일
        if (request.hireDateTo() != null) {
            jpql.append("AND e.hireDate <= :hireDateTo ");
        }


        // 정렬 기준
        if ("name".equalsIgnoreCase(request.sortField())) {
            field = "e.name";
        } else if ("employeeNumber".equalsIgnoreCase(request.sortField())) {
            field = "e.employeeNumber";
        } else {
            field = "e.hireDate";
        }

        String direction = "asc".equalsIgnoreCase(request.sortDirection()) ? "ASC" : "DESC";


        // Cursor 조건 - idAfter는 있으면 쓰고, 없어도 cursor만으로 동작해야 함
        boolean hasCursor = StringUtils.hasText(request.cursor());
        boolean hasIdAfter = request.idAfter() != null;

        if (hasCursor) {
            String operator = "ASC".equals(direction) ? ">" : "<";

            if (hasIdAfter) {
                jpql.append("AND (")
                        .append(field).append(" ")
                        .append(operator)
                        .append(" :cursor ")
                        .append("OR (")
                        .append(field)
                        .append(" = :cursor ")
                        .append("AND e.id ")
                        .append(operator)
                        .append(" :idAfter)) ");
            } else {
                jpql.append("AND ")
                        .append(field)
                        .append(" ")
                        .append(operator)
                        .append(" :cursor ");
            }
        }

        // 정렬
        jpql.append("ORDER BY ")
                .append(field)
                .append(" ")
                .append(direction)
                .append(", e.id ")
                .append(direction);

        TypedQuery<Employee> query =
                em.createQuery(jpql.toString(), Employee.class);


        if (StringUtils.hasText(request.nameOrEmail())) {
            query.setParameter("nameOrEmail", "%" + request.nameOrEmail() + "%");
        }

        if (request.status() != null) {
            query.setParameter("status", request.status());
        }

        if (StringUtils.hasText(request.employeeNumber())) {
            query.setParameter("employeeNumber", request.employeeNumber());
        }

        if (StringUtils.hasText(request.departmentName())) {
            query.setParameter("departmentName", request.departmentName());
        }

        if (StringUtils.hasText(request.position())) {
            query.setParameter("position", request.position());
        }

        if (request.hireDateFrom() != null) {
            query.setParameter("hireDateFrom", request.hireDateFrom());
        }

        if (request.hireDateTo() != null) {
            query.setParameter("hireDateTo", request.hireDateTo());
        }


        if (hasCursor) {
            if ("hireDate".equalsIgnoreCase(request.sortField())) {
                query.setParameter("cursor", LocalDate.parse(request.cursor()));
            } else {
                query.setParameter("cursor", request.cursor());
            }

            if (hasIdAfter) {
                query.setParameter("idAfter", request.idAfter());
            }
        }

        query.setMaxResults(request.size() + 1);

        return query.getResultList();
    }


    @Override
    public long countEmployees() {
        String sql = "SELECT COUNT(e) FROM Employee e WHERE e.deletedAt IS NULL";
        return em.createQuery(sql, Long.class)
                .getSingleResult();
    }
```

## QueryDSL
```
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
```

### application.yaml
```
datasource:
  url: jdbc:postgresql://localhost:5432/hrbank
  username: onestep
  password: 1234
  driver-class-name: org.postgresql.Driver

datasource:
  url: jdbc:h2:tcp://localhost/~/test
  username: sa
  password:
  driver-class-name: org.h2.Driver
```

## 팀원 소개
|팀장|팀원|팀원|팀원|팀원|팀원|
|:-:|:-:|:-:|:-:|:-:|:-:|
|[오재건](https://github.com/latte798)|[강현구](https://github.com/ssummer96)|[양성식](https://github.com/seongsik-ai)|[유채원](https://github.com/chaewon021734)|[이해빈](https://github.com/haebppy)|[함지원](https://github.com/HamJiWeon)
