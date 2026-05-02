

##
`백엔드`.✅`.logoutSuccessUrl("/")`
`풀스택`.✅`.logoutSuccessUrl("/")`
`프론트+백엔드`.❌`.logoutSuccessUrl("/")`
`프론트+백엔드`.❌`302`
`프론트+백엔드`.✅`200`>>❌`302`


##
프론트엔드 코드를 보니 Nuxt 3(또는 Vue 3)를 사용 중이시군요! 작성하신 `handleLogout` 함수가 스프링 시큐리티 설정과 맞물려 잘 작동하게 하려면 **백엔드 설정**과 **프론트엔드의 토큰 전달** 이 두 가지만 잘 맞추면 됩니다.

현재 코드에서 수정하거나 확인해야 할 핵심 포인트를 짚어드릴게요.

---

### 1. 스프링 시큐리티 설정 수정 (URL 매칭)
현재 프론트에서 `$fetch('/api/auth/logout', ...)`으로 요청을 보내고 있습니다. 하지만 스프링 시큐리티의 기본 로그아웃 경로는 `/logout`입니다. 따라서 백엔드 설정을 프론트 경로에 맞춰줘야 합니다.

```java
// Spring Security 설정 클래스
.logout(logout -> logout
    .logoutUrl("/api/auth/logout") // 프론트엔드의 요청 경로와 일치시킴
    .logoutSuccessHandler((request, response, authentication) -> {
        response.setStatus(HttpServletResponse.SC_OK); // 성공 시 200 OK 응답 (리다이렉트 방지)
    })
    .invalidateHttpSession(true)
    .deleteCookies("JSESSIONID") // 쿠키 삭제 명시
);
```
> **주의:** API 방식(SPA/CSRF)에서는 `.logoutSuccessUrl("/")`를 사용하면 서버가 302 리다이렉트 응답을 보냅니다. 프론트엔드 비동기 요청($fetch)에서는 리다이렉트보다 **200 OK** 상태 코드를 받는 것이 처리가 깔끔합니다.

---

### 2. CSRF 토큰 처리
코드에 `csrfToken` 변수가 있는데, 이 값을 어디서 가져오는지 확인이 필요합니다. 스프링 시큐리티는 기본적으로 CSRF 보호를 위해 매 요청마다 유효한 토큰을 요구합니다.

* **Cookie 방식:** 스프링에서 CSRF 토큰을 쿠키(`XSRF-TOKEN`)에 담아 보내도록 설정했다면, 프론트엔드에서 쿠키를 읽어 `headers`에 담아야 합니다.
* **간단한 테스트용:** 만약 개발 단계에서 번거롭다면 백엔드에서 `.csrf(csrf -> csrf.disable())`를 사용하여 잠시 끄고 테스트해 볼 수 있습니다. (운영 환경에서는 권장하지 않음)

---

### 3. 프론트엔드 코드 보완 (Nuxt 3)
`credentials: 'include'` 설정은 아주 좋습니다. 세션 쿠키를 서버에 보내야 하니까요. 코드를 조금 더 안정적으로 다듬어 보겠습니다.



```javascript
<script setup>
const { isLoggedIn } = useAuth()
const config = useRuntimeConfig()
// 만약 쿠키에서 CSRF 토큰을 가져와야 한다면 useCookie 활용
const csrfToken = useCookie('XSRF-TOKEN').value 

async function handleLogout() {
  try {
    await $fetch('/api/auth/logout', {
      baseURL: config.public.apiBase,
      method: 'POST',
      credentials: 'include', // 세션 쿠키 전송 필수
      headers: {
        // 백엔드 CSRF 설정에 따라 이름이 다를 수 있음 (기본은 X-CSRF-TOKEN 또는 X-XSRF-TOKEN)
        'X-XSRF-TOKEN': csrfToken 
      }
    })

    // 서버 요청 성공 후 클라이언트 상태 초기화
    isLoggedIn.value = false 
    alert("로그아웃 되었습니다.")
    navigateTo('/') 
  } catch (e) {
    // 이미 세션이 만료된 경우 403이나 401이 뜰 수 있음
    // 이 경우에도 클라이언트 상태는 로그아웃으로 만드는 것이 사용자 경험에 좋음
    isLoggedIn.value = false
    navigateTo('/')
    console.error("로그아웃 프로세스 중 알림:", e)
  }
}
</script>
```

---

### 체크리스트
1.  **백엔드:** `logoutUrl`이 `/api/auth/logout`으로 되어 있는가?
2.  **백엔드:** 로그아웃 성공 시 302 리다이렉트가 아닌 **200 OK**를 반환하는가? (`logoutSuccessHandler` 사용)
3.  **프론트:** `config.public.apiBase`가 백엔드 서버 주소와 정확히 일치하는가?
4.  **CORS:** 백엔드 CORS 설정에서 `allowCredentials(true)`와 프론트엔드 도메인이 허용되어 있는가?

위 설정만 맞추면 버튼 클릭 시 세션이 무효화되고 상태가 정상적으로 변경될 거예요! 현재 백엔드에서 CSRF 설정을 어떻게 하고 계신가요?