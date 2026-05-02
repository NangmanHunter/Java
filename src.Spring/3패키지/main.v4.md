최상위 패키지(예: `com.shop.api`) 아래에 붙는 하위 패키지들은 실제 **"가구 배치"**를 하는 단계라고 보시면 됩니다. 개발자들 사이에서 가장 많이 쓰이는 방식은 크게 두 가지인데, 프로젝트의 성격에 따라 선택합니다.

---

## 1. 계층 기반 구조 (Package by Layer)
가장 전통적이고 표준적인 방식입니다. 코드의 **역할(Role)**에 따라 폴더를 나눕니다. 규모가 아주 크지 않은 프로젝트나 처음 시작할 때 가장 추천하는 방식입니다.

* **특징:** 서비스 전체의 구조가 한눈에 들어오고, 스프링의 전형적인 흐름(`Controller -> Service -> Repository`)을 따르기 편합니다.

```text
com.shop.api
├── controller  # (또는 web) API 엔드포인트 모음
│   ├── MemberController.java
│   └── OrderController.java
├── service     # 비즈니스 로직 (계산, 비즈니스 규칙) 모음
│   ├── MemberService.java
│   └── OrderService.java
├── repository  # DB 접근 로직 (JPA 등) 모음
│   ├── MemberRepository.java
│   └── OrderRepository.java
├── domain      # (또는 entity) DB 테이블과 매핑되는 객체
│   ├── Member.java
│   └── Order.java
└── dto         # 데이터를 주고받기 위한 단순 객체 (Data Transfer Object)
    ├── MemberJoinRequest.java
    └── OrderResponse.java
```



---

## 2. 도메인 기반 구조 (Package by Feature/Domain)
프로젝트 규모가 커질 때 사용하는 방식입니다. '회원', '주문', '상품' 등 **기능(Feature)** 단위로 폴더를 나눕니다.

* **특징:** '회원' 관련 코드를 수정할 때 `member` 폴더 안에서 모든 걸 해결할 수 있습니다. 마이크로서비스(MSA)로 전환할 때 매우 유리합니다.

```text
com.shop.api
├── member              # 회원과 관련된 모든 것
│   ├── MemberController.java
│   ├── MemberService.java
│   ├── MemberRepository.java
│   └── Member.java
├── order               # 주문과 관련된 모든 것
│   ├── OrderController.java
│   ├── OrderService.java
│   ├── OrderRepository.java
│   └── Order.java
└── common              # 모든 기능에서 공통으로 쓰는 것
    └── utils
```

---

## 3. 그 외 자주 쓰는 공통 패키지
계층형이든 도메인형이든 상관없이 관습적으로 만드는 패키지들입니다.

| 패키지명 | 용도 |
| :--- | :--- |
| **`config`** | 스프링 설정 파일들 (SecurityConfig, DatabaseConfig 등) |
| **`exception`** | 에러 처리를 위한 커스텀 예외 클래스들 |
| **`common`** | 프로젝트 전반에서 공용으로 쓰는 유틸리티 클래스나 상수 |
| **`security`** | 인증/인가(로그인 관련) 로직 |

---

### 어떤 걸 선택해야 할까요?
처음 연습하시거나 소규모 웹 애플리케이션(예: 통계 계산기, 간단한 게시판 등)을 만드신다면 **1번(계층 기반 구조)**를 강력히 추천합니다. 

* **이유 1:** 거의 모든 스프링 강의나 예제 코드가 이 구조를 따릅니다.
* **이유 2:** 스프링의 핵심 원리인 '관심사의 분리'를 배우기에 가장 직관적입니다.

지금 만들고 계신 프로젝트는 주로 어떤 기능을 담당하나요? 그 기능에 맞춰서 폴더 이름을 같이 짜볼 수도 있습니다.