##
- 회원가입
  - 중복조회

- 정보조회

- 로그인
- 로그아웃


##
- CRUD
- 생성
- 읽기
- 수정
- 삭제


- 로그인
  - 로그인
    - 세션
    - JWT
  - 로그아웃

- 삭제
  - 삭제.수정 형태로가는거니까.
  - 실제서비스에맞게끔.
  - 이거를 좀특정하는게 어려움.
  - Delete 실제서비스 


## 게시판
- 게시판
  - 생성
  - 수정
  - 삭제
  - 조회
    - 어떻게 타임리프.SSR.
      - CSR.나중엔가야되니까.-어떤식으로 구성할지


- 댓글
  - ...
  - 





##
컨트롤러는 클라이언트의 요청을 받아서 서비스로 넘겨주는 **'문지기'** 역할을 합니다. 

Member 컨트롤러에서는 크게 **회원가입, 로그인, 로그아웃, 정보 조회**로 나누어 설계하면 됩니다. Rest API 관습에 맞춰서 구성해 볼게요.

---

### 1. MemberController 구조 설계

가장 깔끔한 구성은 요청의 성격(Read/Write)에 따라 HTTP Method를 적절히 사용하는 것입니다.

| 기능 | HTTP Method | URL Path | 설명 |
| :--- | :--- | :--- | :--- |
| **회원가입** | `POST` | `/api/members/signup` | 새로운 회원 데이터 생성 |
| **로그인** | `POST` | `/api/members/login` | 인증 정보 확인 및 세션 생성 |
| **로그아웃** | `POST` | `/api/members/logout` | 세션/토큰 무효화 |
| **내 정보 조회** | `GET` | `/api/members/me` | 현재 로그인한 사용자 정보 반환 |
| **중복 확인** | `GET` | `/api/members/check-id` | 아이디 중복 여부 쿼리 |

---

### 2. 코드 레벨에서의 구성 (예시)

Spring Boot에서 컨트롤러를 짤 때, 서비스 계층을 의존성 주입(DI)받아 호출하는 전형적인 형태입니다.

```java
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 1. 회원가입
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@Valid @RequestBody MemberJoinRequest request) {
        memberService.join(request);
        return ResponseEntity.ok("회원가입 성공");
    }

    // 2. 로그인
    @PostMapping("/login")
    public ResponseEntity<MemberResponse> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        MemberResponse response = memberService.login(request);
        
        // 세션 방식일 경우 여기서 세션 생성 로직 처리
        HttpSession session = httpRequest.getSession();
        session.setAttribute("loginMember", response);
        
        return ResponseEntity.ok(response);
    }

    // 3. 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // 세션 무효화
        }
        return ResponseEntity.ok().build();
    }

    // 4. 정보 조회 (내 정보)
    @GetMapping("/me")
    public ResponseEntity<MemberResponse> getMyInfo(@SessionAttribute(name = "loginMember", required = false) MemberResponse loginMember) {
        if (loginMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(loginMember);
    }
}
```

---

### 3. 설계 시 포인트

1.  **Request/Response DTO 분리:** * `Member` 엔티티를 컨트롤러에서 직접 주고받지 마세요. 
    * `MemberJoinRequest`(입력용), `MemberResponse`(출력용)처럼 별도의 객체를 만들어야 보안상 안전하고 API가 유연해집니다.
    
    - MemberJoinRequest
    - MemberResponse

    - Member


2.  **Validation 적용:**
    * `@Valid` 어노테이션을 써서 아이디나 비밀번호 형식이 맞지 않으면 서비스 로직을 타기도 전에 컨트롤러에서 컷(`400 Bad Request`)해주는 게 좋습니다.

3.  **로그인 유지 방식의 위치:**
    * 세션 방식을 쓴다면 위 예시처럼 `HttpServletRequest`를 받아 세션을 건드리는 로직이 들어가고, JWT라면 토큰을 생성해서 헤더에 담아주는 로직이 들어갑니다.



---

### 4. 다음 작업 제안

이제 큰 그림은 나왔으니, **`MemberJoinRequest` DTO**를 먼저 만들어보시는 건 어떨까요? 
회원가입할 때 어떤 정보(아이디, 비번, 닉네임, 이메일 등)를 받을지 결정하면 엔티티 설계까지 쭉 연결됩니다. 

필요하시다면 제가 기본적인 DTO와 엔티티 구성 코드를 짜드릴 수도 있어요. 어떤 정보부터 받아볼까요?