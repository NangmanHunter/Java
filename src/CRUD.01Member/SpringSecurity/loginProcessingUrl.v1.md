

## 기본값.세션
- Security기본값.Session
- `.formLogin()`.이것자체도 이미 세션방식



* **방식은 무엇인가?**: 현재 설정상으로는 **세션 방식**입니다. (JSESSIONID 쿠키를 통해 브라우저와 대화하게 됩니다.)


세션 방식인지 토큰 방식인지 어떻게 알까?
작성하신 코드는 **100% 세션 방식**입니다. 왜 그렇게 확신할 수 있느냐 하면, 스프링 시큐리티의 **기본값**이 세션이기 때문입니다.

* **따로 설정하지 않으면**: 시큐리티는 인증에 성공하면 내부적으로 `HttpSession`을 생성하고, 거기에 인증 객체(`SecurityContext`)를 집어넣습니다.
* **증거**: 
    * `.formLogin()`을 사용했다는 것 자체가 세션 기반의 전통적인 로그인 방식을 쓰겠다는 의지표명과 같습니다.
    * 만약 JWT 같은 토큰 방식을 쓰고 싶었다면, `.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))` 같은 설정을 추가해서 세션을 명시적으로 꺼야 합니다.




인증이 성공한 뒤 "어떻게" 유지되는가? (세션의 마법)
질문하신 `loginProcessingUrl`에서 인증이 성공하면 다음과 같은 연쇄 반응이 일어납니다:

1.  **세션 생성**: 서버 메모리에 세션 저장소를 만들고 사용자의 정보를 저장합니다.
2.  **쿠키 발급**: 응답 헤더에 `JSESSIONID`라는 쿠키를 담아 브라우저로 보냅니다.
3.  **이후 요청**: 브라우저는 모든 요청마다 이 쿠키를 보냅니다.
4.  **세션 확인**: 시큐리티의 `SecurityContextPersistenceFilter`가 쿠키를 보고 세션을 찾아 "아, 아까 로그인한 그 사람이네!" 하고 인증 상태를 유지시켜 줍니다.






##
그 의문은 아주 합리적인 질문입니다! "코드 몇 줄 썼는데 대체 내부에서 무슨 일이 벌어지는 거지?"라는 생각이 드는 게 당연하거든요.

결론부터 말씀드리면, 스프링 시큐리티는 **"약속된 이름의 필터"**를 미리 준비해두고, 설정값(`loginProcessingUrl` 등)이 들어오면 그 필터를 활성화해서 대신 일해주는 구조입니다.

---

### 1. 왜 Controller를 안 만들어도 될까?
스프링 시큐리티 내부에는 **`UsernamePasswordAuthenticationFilter`**라는 녀석이 미리 대기하고 있습니다.

* **기본 동작**: 평소에는 가만히 있다가, 설정하신 `.loginProcessingUrl("/member/login-proc")`로 `POST` 요청이 들어오면 "어? 내 담당이네?" 하고 낚아챕니다.
* **낚아챈 뒤 하는 일**: 
    1.  `request.getParameter("loginId")`와 `password`를 꺼냅니다. (이때 `usernameParameter` 설정을 참고하죠.)
    2.  이 정보로 **Authentication(인증객체)**를 만듭니다.
    3.  **AuthenticationManager**에게 넘겨서 "이 사람 맞는 정보인지 확인해줘!"라고 시킵니다.

즉, 여러분이 작성해야 할 **로그인 로직(파라미터 추출 → DB 대조 → 세션 생성)을 필터가 이미 다 구현해 놓았기 때문에** URL만 지정해주면 끝나는 것입니다.




### 4. 정리하자면
* **어디에 해당되는가?**: `UsernamePasswordAuthenticationFilter`라는 내장 필터의 동작을 설정하신 겁니다.
* **왜 URL 하나로 끝나는가?**: 시큐리티가 그 URL로 오는 POST 요청을 가로채서 처리하는 로직을 이미 다 짜놨기 때문입니다.


---

### 💡 한 가지 체크할 점 (중요)
지금 프론트엔드가 `localhost:3000` (리액트나 뷰)이고 백엔드가 스프링인 상황이죠? 
세션 방식은 **쿠키**를 주고받아야 하기 때문에, 설정하신 `corsConfigurationSource`에서 `.setAllowCredentials(true)`를 하신 건 아주 잘하신 선택입니다! 이게 없으면 프론트에서 쿠키를 못 받아가거든요.

혹시 지금 프론트에서 로그인을 시도했을 때, 브라우저 개발자 도구(F12)의 **Application -> Cookies** 탭에 `JSESSIONID`가 들어오는지 확인해 보셨나요?