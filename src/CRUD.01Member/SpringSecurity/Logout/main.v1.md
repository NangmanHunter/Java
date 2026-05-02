
##
- [x] `/logout`
  - ` `
  - 없어도됨
  - `.logoutUrl("/logout")`
  - 기본Url.ㄱㄱ.
- [ ] `/member/logout`
  - `.logoutUrl("/member/logout")`


##
로그아웃도 로그인과 마찬가지로 **"스프링 시큐리티가 미리 만들어둔 기능"**을 사용하는 방식입니다. 결론부터 말씀드리면, 별도의 Controller를 만들 필요 없이 **특정 URL로 요청만 날리면** 시큐리티가 알아서 세션을 파괴하고 쿠키를 무효화합니다.

구체적으로 어떻게 동작하는지 핵심을 짚어드릴게요.

---

### 1. 로그아웃의 기본 원리
스프링 시큐리티 설정에서 `.logout()`을 선언하면 내부적으로 `LogoutFilter`가 활성화됩니다.

* **기본 URL**: 따로 설정하지 않았다면 기본값은 **`POST /logout`**입니다.
* **작동 방식**: 사용자가 이 URL로 요청을 보내면 `LogoutFilter`가 가로채서 다음 작업들을 수행합니다.
    1.  **세션 무효화**: 서버 메모리에 있는 세션 정보를 지웁니다 (`.invalidateHttpSession(true)` 설정 때문).
    2.  **보안 컨텍스트 초기화**: `SecurityContextHolder`에 남아있는 인증 정보를 삭제합니다.
    3.  **쿠키 삭제**: 브라우저에게 "네가 가진 `JSESSIONID` 이제 무효야"라고 알려줍니다.
    4.  **리다이렉트**: 모든 작업이 끝나면 설정하신 `.logoutSuccessUrl("/")`로 보냅니다.

---

### 2. 프론트엔드(React 등)에서 호출하는 방법
현재 프론트가 `localhost:3000`에 떠 있다면, 로그아웃 버튼을 눌렀을 때 백엔드로 요청을 보내야 합니다.

**중요한 점:** 스프링 시큐리티의 기본 로그아웃 요청 방식은 **`POST`**입니다. (CSRF 공격 방지 등을 위해 기본값이 POST로 설정되어 있습니다.)

```javascript
// 프론트엔드 예시 코드
const handleLogout = () => {
    fetch('http://localhost:8080/logout', { // 백엔드 로그아웃 주소
        method: 'POST',
        credentials: 'include', // 세션 쿠키를 같이 보내야 하므로 필수!
    })
    .then(response => {
        if (response.ok) {
            window.location.href = "/"; // 성공 시 메인으로 이동
        }
    });
};
```

---

### 3. 설정 커스터마이징 (필요한 경우)
만약 `/logout`이 아니라 다른 주소를 쓰고 싶거나, GET 방식으로 간단하게 처리하고 싶다면 설정을 살짝 수정할 수 있습니다.

```java
.logout(logout -> logout
        .logoutUrl("/member/logout") // 로그아웃을 처리할 임의의 URL 설정
        .logoutSuccessUrl("/")
        .invalidateHttpSession(true)
        .deleteCookies("JSESSIONID") // 쿠키 이름을 명시해서 삭제 가능
)
```

---

### 4. 궁금해하실 핵심 요약

> **Q: "page처럼 뭐 날려야되는 공간(URL)이 있어야 되는 건가요?"**

**A: 네, 있습니다.** 하지만 Controller에 `@PostMapping("/logout")`을 직접 만드실 필요는 없습니다. 
1.  스프링 시큐리티가 내부적으로 **`/logout`** (혹은 설정한 주소)을 이미 기다리고 있습니다.
2.  프론트엔드에서 해당 주소로 **POST 요청**만 날려주시면 됩니다.
3.  만약 로그아웃 성공 시 리다이렉트가 아니라 **JSON 응답(예: "성공했습니다")**을 주고 싶다면, 로그인처럼 `.logoutSuccessHandler()`를 사용하시면 됩니다.



지금 구조에서는 로그인을 성공했을 때 `successHandler`를 통해 JSON 메시지를 보내도록 만드셨으니, 로그아웃도 성공 시 메인페이지로 튕기기보다는 **200 OK 응답**을 주는 `Handler`를 다는 게 프론트엔드(React) 입장에서는 처리하기 더 편할 수도 있습니다! 이 부분도 추가해 드릴까요?