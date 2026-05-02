


## Login.Url
- [ ] `/login`
- [ ] `/api/login`
- [x] `/api/auth/login`



<!--  -->
- 요즘느끼는게 Url디자인이 너무도 중요하다.
- 알면알수록 더중요한듯. 더명확히 더섬세히 더선명하게ㄱㄱ.
- 명확성이라는것을 더확실히 제안위해. 더알아야하고. 더명확해야된다. 그래야지만이 명확성개념 명확한개념 명확한기준 던질수있음. 이것계속연마ㄱㄱ.





## 💡 추천 조합 (React 등 SPA와 연동 시)

작성하신 코드의 흐름을 봤을 때, 다음과 같이 구성하는 것이 가장 깔끔하고 관리하기 편합니다.


```java
.formLogin(form -> form
    .loginPage("/login")             // 1. 프론트의 로그인 페이지 URL (리다이렉트용)
    .loginProcessingUrl("/api/login") // 2. 실제 로그인을 수행할 API 주소
    .usernameParameter("loginId")
    .successHandler(...)              // 3. 성공 시 JSON 응답 (200 OK)
    .failureHandler(...)              // 4. 실패 시 JSON 응답 (401 Unauthorized)
)
```

### 왜 `/api/login`을 추천하나요?
1.  **CORS 관리**: 나중에 필터 설정에서 `/api/**` 경로에 대해서만 특정 설정을 몰아넣기 편합니다.
2.  **구분**: 일반적인 페이지 이동(GET)과 데이터 처리(POST)를 URL 구조만 봐도 쉽게 알 수 있습니다.
3.  **전문성**: 보통 협업 시에 `proc` 같은 약어보다는 전체 단어를 사용하거나 경로 구조(`auth`, `api` 등)로 의미를 전달하는 것이 더 세련된 느낌을 줍니다.


---

### ⚠️ 주의할 점
URL을 변경하시면 **두 군데**를 같이 수정해야 합니다:
1.  **SecurityConfig**: `.loginProcessingUrl("/새로운-URL")`
2.  **프론트엔드 코드**: `fetch("http://localhost:8080/새로운-URL", { method: "POST", ... })`

현재 코드에서는 `/member/login-proc`를 사용 중이신데, 이 이름도 틀린 것은 아닙니다! 다만 조금 더 요즘 트렌드에 맞춘다면 **`/api/login`**이나 **`/auth/login`** 정도로 바꿔보시는 건 어떨까요?

지금 프론트엔드에서는 백엔드 어느 주소로 로그인을 요청하고 계신가요? 거기에 맞춰서 하나로 통일하는 게 가장 중요합니다!