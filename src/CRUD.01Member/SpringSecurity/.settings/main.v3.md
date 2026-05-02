가장 신기하면서도 처음에는 헷갈리는 지점이죠! 결론부터 말씀드리면, **컨트롤러에 `/member/login-proc`을 직접 만들 필요가 전혀 없습니다.**

그 이유는 **Spring Security가 필터(Filter) 레벨에서 이 요청을 가로채서 직접 처리하기 때문**입니다.

---

### 1. 동작 원리 (왜 안 만들어도 되나?)

Spring Security는 애플리케이션의 앞단에서 **필터 체인(Filter Chain)**이라는 일종의 '검문소'를 운영합니다.

* **사용자의 동작:** 로그인 페이지(`form`)에서 `action="/member/login-proc"`으로 POST 요청을 보냅니다.
* **Security의 동작:** `UsernamePasswordAuthenticationFilter`라는 녀석이 대기하고 있다가, 설정된 `.loginProcessingUrl`과 일치하는 요청이 들어오면 **"아, 이건 내가 처리할 로그인 요청이구나!"** 하고 가로챕니다.
* **내부 처리:** 가로챈 요청에서 `username`과 `password`를 추출해, 우리가 만든 `UserDetailsService`를 호출하여 DB의 정보와 비교합니다.



---

### 2. HTML 폼(Form)은 이렇게만 만드세요

컨트롤러는 필요 없지만, 로그인 페이지(`html`)의 폼 태그는 설정과 일치시켜야 합니다.

```html
<form action="/member/login-proc" method="post">
    <input type="text" name="username" placeholder="이메일을 입력하세요">
    <input type="password" name="password" placeholder="비밀번호">
    
    <button type="submit">로그인</button>
</form>
```

---

### 3. 만약 로그인이 성공/실패한다면?

컨트롤러가 없는데 어디로 이동할지는 `SecurityConfig`에서 우리가 설정한 대로 움직입니다.

* **성공 시:** `.defaultSuccessUrl("/")`에 적힌 대로 메인 페이지로 이동합니다.
* **실패 시:** 기본적으로 `.loginPage("/member/login")`으로 다시 돌아가며, 뒤에 에러 파라미터(`?error`)를 붙여줍니다.

---

### 4. 주의할 점 (이건 꼭 확인하세요!)

1.  **Method는 반드시 POST:** 로그인 처리는 보안상 POST 방식이어야 하며, Security 필터도 POST 요청만 가로챕니다.
2.  **ID/PW 필드 이름:** Spring Security는 기본적으로 `username`과 `password`라는 이름을 기대합니다. 만약 이메일을 아이디로 쓰더라도 HTML의 `name` 속성은 `username`으로 적거나, 설정에서 `.usernameParameter("email")`처럼 바꿔줘야 합니다.
3.  **CSRF 필터:** 만약 `.csrf(csrf -> csrf.disable())` 설정을 하지 않았다면, 폼 태그 안에 CSRF 토큰을 넣어야 요청이 거부되지 않습니다. (처음 연습하실 때는 `disable` 해두고 기능부터 확인하시는 게 편해요.)

**요약하자면:**
개발자인 우리는 **"로그인 폼 화면"**과 **"DB에서 유저를 찾는 서비스(UserDetailsService)"**만 만들면 되고, 그 사이의 복잡한 인증 과정(비밀번호 대조, 세션 생성 등)은 Security가 `/member/login-proc` 주소에서 알아서 다 해주는 구조입니다.

이제 `/member/login-proc` 컨트롤러는 지우거나 만들지 마시고, 바로 테스트해보셔도 됩니다! 혹시 DB에 저장된 비밀번호가 **암호화(`BCrypt`)** 되어 있는지는 꼭 확인해 보세요.