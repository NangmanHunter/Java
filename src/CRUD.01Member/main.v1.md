##
- Singup
- Login
- Logout
- 내정보조회


##
Spring Boot와 JPA, MariaDB를 활용한 **Member(회원) 도메인** 개발은 프로젝트의 뼈대를 잡는 가장 중요한 작업입니다. 보안과 확장성을 고려해야 하므로, 아래와 같은 단계별 로직으로 접근하는 것을 추천합니다.

---

## 1. 요구사항 및 도메인 모델링 (Entity 설계)
먼저 회원이 가져야 할 최소한의 필드를 정의하고 JPA 엔티티를 작성합니다.

* **필수 필드:** 아이디(Login ID), 비밀번호, 이름, 이메일, 가입일, 권한(Role) 등.
* **엔티티 설계 시 주의사항:**
    * 비밀번호는 반드시 암호화하여 저장해야 합니다. (BCrypt 등 사용)
    * `CreatedAt`, `UpdatedAt`은 `BaseEntity`를 만들어 공통 처리하는 것이 깔끔합니다.



## 2. 계층별 개발 순서 (Layered Architecture)
Spring의 전형적인 3계층 구조에 맞춰 아래 순서로 진행해 보세요.

### Step 1: Repository 계층 (JPA)
`JpaRepository`를 상속받아 인터페이스를 만듭니다.
* `findByLoginId(String loginId)`: 로그인 시 아이디로 회원을 찾는 기능.
* `existsByLoginId(String loginId)`: 회원가입 시 중복 아이디 체크 기능.

### Step 2: Service 계층 (Business Logic)
실제 핵심 로직이 들어가는 부분입니다.
* **회원가입:** 아이디 중복 체크 -> 비밀번호 암호화 -> `save()`.
* **로그인:** 아이디로 사용자 조회 -> 암호화된 비밀번호 매칭 확인.
* **회원 정보 수정/탈퇴:** 영속성 컨텍스트의 **더티 체킹(Dirty Checking)**을 활용해 업데이트.

### Step 3: DTO(Data Transfer Object) 정의
엔티티를 직접 컨트롤러에서 노출하지 않도록 합니다.
* `MemberJoinRequest`: 가입 시 입력받는 데이터.
* `MemberResponse`: 조회 시 반환할 데이터 (비밀번호 제외).

### Step 4: Controller 계층 (API)
RESTful하게 API를 설계합니다.
* `POST /api/members`: 회원가입
* `POST /api/login`: 로그인 (Session 또는 JWT 방식 선택 필요)
* `GET /api/members/me`: 내 정보 조회

---

## 3. MariaDB 연결 설정 (`application.yml`)
프로젝트의 `src/main/resources/application.yml` 파일에 MariaDB 연결 정보를 설정해야 합니다.

```yaml
spring:
  datasource:
    url: jdbc:mariadb://localhost:3306/your_db_name
    username: your_username
    password: your_password
    driver-class-name: org.mariadb.jdbc.Driver

  jpa:
    hibernate:
      ddl-auto: update # 개발 단계에서는 update, 운영은 none/validate
    show-sql: true
    properties:
      hibernate:
        format_sql: true
```

---

## 4. 추가로 고려하면 좋은 포인트
* **Validation:** `spring-boot-starter-validation`을 사용하여 이메일 형식이나 비밀번호 길이를 컨트롤러 단에서 1차 검증하세요.
* **Exception Handling:** `@RestControllerAdvice`를 만들어 아이디 중복 등 비즈니스 예외 발생 시 일관된 에러 응답을 내려주면 프론트엔드 작업이 수월해집니다.
* **Security:** 처음부터 Spring Security를 넣으면 복잡할 수 있으니, 우선 로직을 구현한 뒤에 Security를 덧입히는 방식을 추천합니다.

어떤 부분부터 구체적인 코드가 필요하신가요? 엔티티 설계나 로그인 구현 방식(Session vs JWT) 중 고민되는 지점을 말씀해 주시면 더 자세히 도와드릴게요.