##
```
access-control-allow-credentials    true
access-control-allow-origin         http://localhost:3000
```


##
Nuxt의 catch(error) 부분에서 console.log(error.response.status)를 찍어보세요.
- 403 Forbidden: CORS 문제이거나 CSRF 필터가 작동 중인 경우입니다.
- 401 Unauthorized: 아이디/비번이 틀렸거나, DB 비밀번호가 암호화되지 않아 시큐리티가 "이건 가짜야!"라고 판단한 경우입니다. (우리가 만든 failureHandler가 동작한 상태)
- 0 또는 Network Error: 백엔드 서버가 꺼져있거나 CORS 정책에 의해 브라우저가 응답 읽기를 거부한 경우입니다.

