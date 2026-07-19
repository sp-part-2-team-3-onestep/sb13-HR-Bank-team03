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

## 팀원 소개
|팀장|팀원|팀원|팀원|팀원|팀원|
|:-:|:-:|:-:|:-:|:-:|:-:|
|[오재건](https://github.com/latte798)|[강현구](https://github.com/ssummer96)|[양성식](https://github.com/seongsik-ai)|[유채원](https://github.com/chaewon021734)|[이해빈](https://github.com/haebppy)|[함지원](https://github.com/HamJiWeon)
