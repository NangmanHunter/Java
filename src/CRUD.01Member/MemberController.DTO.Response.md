## Response
- [x] Controller
  ```java
  Member loginMember = memberService.login(request.getLoginId(), request.getPassword());
  ...
  return new MemberResponse(
          loginMember.getId(), 
          loginMember.getLoginId(), 
          loginMember.getName()
      );
  ```

- [ ] Service




##
좋은 질문입니다! 사실 회원가입이나 로그인 후에 **"회원가입 완료!"** 같은 문자열만 달랑 보내는 것보다, 실무에서는 **필요한 정보만 골라 담은 DTO**를 내려주는 게 정석입니다.

특히 **`Member` 엔티티를 그대로 반환하면 절대 안 되는 이유**와 함께 어디서 세팅하는지 알려드릴게요.

---

### 1. Response DTO, 왜 만드나요? (보안 & 무한루프 방지)

만약 `Member` 엔티티를 그대로 반환(`return member`)하면 어떤 일이 벌어질까요?
1.  **비밀번호 유출**: 사용자의 패스워드 필드까지 JSON에 포함되어 브라우저로 날아갑니다. (매우 위험!)
2.  **무한 루프**: 아까 만든 `List<Board>`가 문제예요. `Member`를 그리려다 `Board`를 부르고, `Board` 안에 있는 `Member`를 또 그리려다 서버가 뻗어버릴 수 있습니다.
3.  **불필요한 데이터**: 화면에는 '이름'만 보여주면 되는데, 가입일, 수정일, 권한 등 모든 정보가 나가면 리소스 낭비입니다.

---

### 2. 어디서 세팅하나요? (Service vs Controller)

결론부터 말씀드리면 **Service**에서 엔티티를 찾은 뒤, **Controller**에서 DTO로 변환하거나 **Service**에서 아예 DTO로 반환하는 방식 중 선택합니다. 요즘은 **Controller**에서 변환하는 방식을 조금 더 선호합니다.

#### [Step 1] Response 객체 만들기
```java
@Getter
@AllArgsConstructor
public class MemberResponse {
    private Long id;
    private String loginId;
    private String name;
}
```

#### [Step 2] Controller에서 세팅하기
```java
@PostMapping("/login")
public MemberResponse login(@RequestBody MemberLoginRequest request) {
    Member loginMember = memberService.login(request.getLoginId(), request.getPassword());

    if (loginMember == null) {
        throw new RuntimeException("로그인 실패");
    }

    // 엔티티를 Response DTO로 변환해서 반환!
    return new MemberResponse(
        loginMember.getId(), 
        loginMember.getLoginId(), 
        loginMember.getName()
    );
}
```

---

### 3. 세팅하는 위치의 장단점

| 위치 | 방법 | 특징 |
| :--- | :--- | :--- |
| **Service에서 변환** | `public MemberResponse login(...)` | 컨트롤러가 아주 깔끔해집니다. 하지만 서비스가 '화면 규격(DTO)'을 알아야 한다는 단점이 있습니다. |
| **Controller에서 변환** | `new MemberResponse(entity)` | 서비스는 순수하게 데이터(엔티티)만 주고, 컨트롤러가 "이 화면엔 이 정보가 필요해"라고 결정합니다. **(권장)** |



---

### 💡 정리하자면

* **Request DTO**: 들어오는 문 (입구)
* **Entity**: 우리 집 내부 (DB와 연결된 소중한 자산)
* **Response DTO**: 나가는 문 (출구, 손님에게 보여줄 것만 챙긴 가방)

이렇게 입구와 출구를 DTO로 꽉 막아두면, 유저님이 걱정하셨던 **"내부가 뒤죽박죽 꼬이는 현상"**을 완벽하게 방지할 수 있습니다. 

이제 로그인이 성공하면 유저의 이름과 ID가 담긴 JSON이 예쁘게 포스트맨에 찍히겠네요! 한 번 적용해 보시겠어요?