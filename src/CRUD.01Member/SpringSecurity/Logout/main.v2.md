스프링 시큐리티(Spring Security)의 기본 설정을 따르고 계시다면, 로그아웃 요청은 **`POST /logout`**으로 보내야 합니다.

구체적인 내용과 주의사항을 정리해 드릴게요.

---

### 1. 로그아웃 요청의 기본 원칙
* **URL:** 별도의 설정을 하지 않았다면 기본값은 `/logout`입니다.
* **HTTP Method:** **`POST`** 방식이 원칙입니다.
    * CSRF(Cross-Site Request Forgery) 보호가 활성화되어 있는 경우(기본값), 반드시 `POST`로 요청을 보내야 안전하게 로그아웃이 처리됩니다.
    * 만약 `GET`으로 보내고 싶다면 별도의 추가 설정(`.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))`)이 필요하지만, 보안상 권장되지 않습니다.

### 2. 프론트엔드 구현 예시
HTML이나 JavaScript에서 요청을 보낼 때 다음과 같이 작성하세요.

**HTML Form 사용 시 (가장 권장)**
```html
<form action="/logout" method="post">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <button type="submit">로그아웃</button>
</form>
```

**JavaScript (Fetch API) 사용 시**
```javascript
fetch('/logout', {
    method: 'POST',
    headers: {
        'X-CSRF-TOKEN': csrfToken // CSRF 토큰 필요
    }
}).then(response => {
    if (response.ok) {
        window.location.href = "/"; // 설정하신 logoutSuccessUrl로 이동
    }
});
```

---

### 3. 작성하신 코드의 의미
현재 설정하신 코드는 로그아웃 프로세스가 **성공적으로 완료된 후**의 동작을 정의하고 있습니다.

* `.logoutSuccessUrl("/")`: 로그아웃이 끝나면 사용자를 메인 페이지(`/`)로 보냅니다.
* `.invalidateHttpSession(true)`: 서버에 저장된 사용자의 세션을 완전히 삭제합니다.

### 💡 팁
만약 로그아웃 경로 자체를 `/logout`이 아닌 다른 경로(예: `/sign-out`)로 바꾸고 싶다면, 코드에 `.logoutUrl("/sign-out")`를 추가하시면 됩니다.

```java
.logout(logout -> logout
    .logoutUrl("/sign-out") // 요청을 받는 주소를 변경
    .logoutSuccessUrl("/")
    .invalidateHttpSession(true)
);
```

요약하자면, 지금 상태에서는 **`POST /logout`**으로 요청을 쏘시면 됩니다! 다른 궁금한 점이 있으신가요?