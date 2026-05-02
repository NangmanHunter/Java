아하, 원인을 바로 찾았습니다! **400 Bad Request**나 오류가 난 이유는 컨트롤러의 **데이터 받는 방식**과 포스트맨에서 **데이터를 보내는 방식**이 서로 맞지 않기 때문입니다.

현재 작성하신 `MemberController`의 `login` 메서드는 다음과 같이 되어 있습니다.

```java
@PostMapping("/login")
public String login(@RequestParam String loginId, @RequestParam String password) { ... }
```

### 1. 왜 오류가 났을까요?
* **컨트롤러 상황:** `@RequestParam`을 사용하셨습니다. 이것은 URL 뒤에 붙는 파라미터(`?loginId=testuser&password=...`)나 HTML Form 형식을 기다린다는 뜻입니다.
* **포스트맨 상황:** 유저님은 지금 **JSON Body**(`{ "loginId": "..." }`)로 데이터를 보내고 계십니다.

서버는 "주소창이나 폼 데이터로 올 줄 알았는데, 왜 갑자기 JSON 덩어리가 들어오지?" 하면서 거절한 것입니다.

---

### 2. 해결 방법 (둘 중 하나를 선택하세요)

#### 방법 A: 포스트맨 설정을 변경 (코드 수정 없이 테스트)
가장 빠르게 테스트해보는 방법입니다. JSON으로 보내지 말고 **Params**로 보냅니다.
1.  포스트맨의 **Body** 탭에서 `none`을 선택 (작성하신 JSON은 무시됩니다).
2.  URL 주소 옆의 **Params** 탭을 클릭합니다.
3.  **KEY**: `loginId` / **VALUE**: `testuser`
4.  **KEY**: `password` / **VALUE**: `password123` 입력
    * 그러면 주소가 `http://localhost:8080/api/members/login?loginId=testuser&password=password123`로 바뀔 거예요.
5.  그대로 **Send**를 누릅니다.

#### 방법 B: 컨트롤러 코드를 수정 (JSON으로 로그인하고 싶을 때)
회원가입처럼 JSON 데이터를 주고받는 형식을 유지하고 싶다면, 별도의 **로그인용 객체(DTO)**를 만들어야 합니다.

**1단계: 로그인용 클래스 생성**
```java
public class LoginRequest {
    public String loginId;
    public String password;
}
```

**2단계: 컨트롤러 수정**
```java
@PostMapping("/login")
public String login(@RequestBody LoginRequest request) { // @RequestParam 대신 @RequestBody
    Member loginMember = memberService.login(request.loginId, request.password);
    // ... 이하 동일
}
```

---

### 💡 추천하는 방법
일단은 **방법 A**로 해서 현재 코드가 잘 돌아가는지 먼저 확인해 보세요! `@RequestParam`은 웹 브라우저나 간단한 통신에서 매우 자주 쓰이는 방식이라, 파라미터 방식으로 먼저 성공해 보는 것이 좋습니다.

지금 바로 포스트맨에서 **Params** 탭에 값을 넣고 다시 쏴보시겠어요? 결과가 어떻게 나오는지 궁금하네요!