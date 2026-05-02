백엔드 개발, 특히 Java Spring Boot를 학습하시다 보면 가장 먼저 마주하게 되는 핵심 개념들입니다. 간단히 요약하자면 **DAO는 '데이터에 접근하는 로직'**을, **DTO는 '데이터를 실어 나르는 바구니'**를 의미합니다.

---

### 1. DAO (Data Access Object)
**"데이터베이스 전담 비서"**
데이터베이스(DB)의 data에 접근하기 위한 객체입니다. 실제로 DB에 접속해서 데이터를 삽입, 삭제, 조회하는 등의 CRUD 작업을 수행합니다.

* **역할:** DB 로직과 비즈니스 로직을 분리하기 위해 사용합니다.
* **특징:** * 직접 SQL을 실행하거나 JPA의 `Repository`처럼 DB 인터페이스 역할을 합니다.
    * 비즈니스 로직(Service 계층)에서 DB에 어떻게 접근하는지 몰라도 되게끔 캡슐화합니다.
    * Spring Data JPA에서는 보통 `Repository`가 이 역할을 대신합니다.

### 2. DTO (Data Transfer Object)
**"데이터 배달 가방"**
계층 간(Controller ↔ Service ↔ Repository) 데이터 교환을 위해 사용하는 객체입니다.

* **역할:** 데이터를 한꺼번에 묶어서 전달하며, 로직을 갖지 않는 순수한 데이터 객체입니다.
* **특징:**
    * **보안:** DB 테이블 구조(Entity)를 그대로 클라이언트에 노출하지 않고, 필요한 데이터만 골라서 보낼 수 있습니다.
    * **유연성:** API 스펙이 변해도 DB 구조를 바꿀 필요 없이 DTO만 수정하면 됩니다.
    * 주로 `getter`, `setter` 메소드(또는 Java 14+의 `record`)만 포함합니다.

---

### 💡 한눈에 비교하기

| 구분 | DAO (Data Access Object) | DTO (Data Transfer Object) |
| :--- | :--- | :--- |
| **목적** | DB 접근 및 조작 (CRUD) | 계층 간 데이터 전송 |
| **주요 로직** | SQL 실행, DB 연결 등 | Getter / Setter (데이터 담기) |
| **위치** | Persistence(데이터 접근) 계층 | 모든 계층 간 이동 |
| **현대적 대체** | Spring Data JPA의 Repository | Java Record, VO(경우에 따라) |

### 🔄 실제 흐름 예시
1.  **사용자**가 게시글 조회를 요청합니다.
2.  **Controller**가 요청을 받고 **Service**를 호출합니다.
3.  **Service**는 **DAO(Repository)**에게 데이터를 요청합니다.
4.  **DAO**는 DB에서 데이터를 꺼내와서 **Entity**에 담습니다.
5.  **Service**는 보안이나 편의를 위해 **Entity**를 **DTO**로 변환합니다.
6.  최종적으로 **DTO**가 **Controller**를 거쳐 **사용자**에게 전달됩니다.

현재 진행 중인 **Spring Boot 프로젝트나 URL Unfurling 프로젝트**에서도 외부 API나 DB에서 가져온 원본 데이터(Entity)를 그대로 쓰기보다는, 클라이언트에 보여줄 필드만 정의한 DTO를 따로 만들어 관리하시는 것이 유지보수 면에서 훨씬 유리합니다.