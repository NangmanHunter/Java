## Package.Structure



스프링 부트 프로젝트에서 가장 대중적으로 사용하는 패키지 구조 예시를 보여드릴게요. 보통 **도메인 계층(Layered Architecture)** 방식이라고 부르는 구조입니다.

가상의 쇼핑몰 서비스(`com.shop.api`)를 만든다고 가정했을 때의 구조입니다.

### 📂 표준 패키지 구조 예시
```text
src/main/java
└── com.shop.api
    ├── ShopApplication.java (메인 실행 클래스)
    │
    ├── common          # 공통 유틸리티, 예외 처리, 상수 등
    │   ├── exception   # 커스텀 예외 클래스
    │   └── utils       # 날짜 계산, 암호화 등 공용 도구
    │
    ├── config          # 보안(Security), DB 설정, Swagger 등 환경 설정
    │
    ├── controller      # 외부 요청을 받는 지점 (API 엔드포인트)
    │   ├── MemberController.java
    │   └── ItemController.java
    │
    ├── service         # 핵심 비즈니스 로직 (회원가입 로직, 주문 계산 등)
    │   ├── MemberService.java
    │   └── ItemService.java
    │
    ├── repository      # 데이터베이스 접근 (JPA 인터페이스 등)
    │   ├── MemberRepository.java
    │   └── ItemRepository.java
    │
    └── domain / entity # DB 테이블과 매핑되는 객체들
        ├── Member.java
        └── Item.java
```

---

### 💡 각 폴더의 핵심 역할
1.  **`controller` (외모/창구):** * 사용자가 "로그인하고 싶어요"라고 요청을 보내면 가장 먼저 만나는 곳입니다. 
    * 요청을 받아서 적절한 서비스에 전달하고 결과만 돌려줍니다.
2.  **`service` (두뇌):** * 진짜 복잡한 계산이나 로직이 일어나는 곳입니다. 
    * "이 사용자가 이미 가입했나?", "비밀번호가 맞나?" 같은 판단을 여기서 합니다.
3.  **`repository` (손/발):** * 데이터베이스에 직접 가서 데이터를 가져오거나 저장하는 일만 전문적으로 합니다.
4.  **`domain` 또는 `entity` (데이터):** * 우리 시스템에서 다루는 "회원", "상품", "주문" 같은 핵심 개념을 코드로 정의한 것입니다.

### 🌟 팁: 왜 이렇게 나누나요?
이렇게 나눠두면 나중에 **"회원 가입 로직이 좀 이상한데?"** 싶을 때 고민할 필요 없이 바로 `MemberService` 파일을 열면 됩니다. 코드가 수만 줄이 되어도 길을 잃지 않기 위한 **지도**를 만드는 셈이죠.

지금 개발하시려는 기능 중에 구체적으로 어떤 부분을 패키지로 나누는 게 고민되시나요? (예: 로그인 기능, 게시판 기능 등)