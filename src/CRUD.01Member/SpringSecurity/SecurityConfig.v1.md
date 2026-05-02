정확한 지점을 짚으셨습니다. 예전에 **Session 방식**으로 직접 구현하실 때는 Controller에 `/api/members/login` 같은 메서드를 만들고, 그 안에서 `HttpSession`에 정보를 담는 코드를 직접 짜셨을 겁니다.

하지만 **스프링 시큐리티의 `formLogin()`**을 사용하면 그 "로그인 처리 로직" 자체를 시큐리티가 가로채서 대신 해줍니다. 그래서 **Controller를 직접 만들 필요가 없게 되는 것**이죠.

각 설정이 어떤 역할을 하는지, 그리고 내부에서 무슨 일이 벌어지는지 핵심만 설명해 드릴게요.

---

### 1. 코드 상세 설명
* **`.loginPage("/member/login")`**
  * 사용자가 로그인이 필요한 페이지에 접근했는데 로그인이 안 되어 있을 때, 시큐리티가 보내주는 **로그인 양식(HTML) 페이지** 주소입니다.
  * 가드역할
  * Routing역할
  * 라우팅역할
  * 로그인없으면 싹다 이쪽으로 던지는형태.
  * 최초열어놓은부분 이외경우 싹다 이쪽으로 던지는형태.

* **`.loginProcessingUrl("/member/login-proc")`**
    * **핵심입니다.** HTML의 `<form action="/member/login-proc" method="POST">`로 데이터를 전송하면, 시큐리티 내부의 `UsernamePasswordAuthenticationFilter`가 이 요청을 낚아챕니다.
    * **이 주소에 대응하는 Controller 메서드는 만들지 않습니다.** 시큐리티가 이 URL로 오는 요청만 감시하다가, 요청이 오면 아까 만드신 `CustomUserDetailsService`를 호출해 인증을 진행합니다.
* **`.usernameParameter("loginId")`**
    * 시큐리티는 기본적으로 `username`이라는 이름으로 아이디를 찾습니다. 하지만 우리 도메인은 `loginId`를 쓰기로 했죠? 그래서 "아이디는 `loginId`라는 이름으로 넘어올 거야"라고 알려주는 설정입니다.
* **`.defaultSuccessUrl("/")`**
    * 인증이 성공하면 어디로 보낼지 정합니다.

---

### 2. 이전 방식(Session) vs 시큐리티 방식

비교해 보시면 왜 시큐리티를 쓰는지 확 와닿으실 겁니다.

| 구분 | 이전 방식 (직접 구현) | 스프링 시큐리티 방식 |
| :--- | :--- | :--- |
| **로그인 처리** | `/api/members/login` 메서드 직접 작성 | `.loginProcessingUrl()` 설정으로 대체 |
| **인증 로직** | `if(pwd.equals(dbPwd))` 직접 비교 | `UserDetailsService` + `PasswordEncoder`가 자동 처리 |
| **세션 저장** | `session.setAttribute("user", member)` | `SecurityContextHolder`에 자동으로 세션 생성 및 저장 |
| **보안** | 직접 CSRF, 세션 고정 보호 등을 구현해야 함 | 설정만 하면 시큐리티가 기본적으로 방어해 줌 |



---

### 3. "그럼 기존 Controller 메서드는 지워도 되나요?"

**네, 지우셔도 됩니다.** 대신 다음 조건을 맞춰주셔야 합니다:

1.  **HTML Form 수정:** 로그인 HTML 파일의 form 태그 주소를 `/member/login-proc`로 맞추세요.
2.  **Input Name 수정:** 아이디 입력창의 name을 `loginId`로, 비밀번호는 `password`(기본값)로 맞추세요.
3.  **성공 후 데이터:** 만약 기존에 세션에 `Member` 엔티티를 통째로 담아서 쓰셨다면, 이제는 세션에서 직접 꺼내는 대신 아까 만든 **`MemberAdapter`**를 통해 정보를 가져오는 방식으로 바꾸시면 됩니다.

### 요약하자면
`loginProcessingUrl`은 **"로그인 로직이 담긴 보이지 않는 Controller"**를 시큐리티가 자동으로 생성해 주는 주소라고 생각하시면 됩니다. 개발자는 "어떤 주소로 아이디/비번을 던질지"만 정해주면 되는 것이죠.

수학적으로 비유하자면, 복잡한 증명 과정을 매번 직접 쓰다가(기존 방식), 잘 정의된 **정리(Theorem)**를 가져다 쓰는 것과 같습니다. 증명 과정(내부 로직)은 이미 시큐리티가 해두었으니, 우리는 조건(설정값)만 잘 대입하면 되는 거죠!

이제 `/member/login-proc`로 Form 데이터를 쏴서 테스트해 보실 준비가 되셨나요? 비밀번호 암호화(`PasswordEncoder`) 설정도 혹시 챙기셨는지 확인해 보세요!