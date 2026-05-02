

## Login
- [ ] @RequestParam
  - Legacy
  - 둘다읽을줄알음.Url Body-여기서 문제남.
  - 보내줄때 아싸리 Front단에서 Body로 전송해줘야 사고없어짐.
  - Back단에서 @RequestParam이걸쓰는걸로 구분불능이기에.
  - 구분위해 철저히 더앞단에서 Body로 던져줘야함.
  - 자칫 Header로 던져주다가 사고남.
  - 결론.백엔드단에서 구분불능.프론트단에서 제대로 던져줘야함.-그럼에도 설계자체 프론트책임으로 넘기기ㄴㄴ. 아싸리 백으로 더가서 아싸리막아줘버리기ㄱㄱ. 일말의빈틈도 없게끔 만들어줄것ㄱㄱ.
  - 자칫 질질세는물로 전체를무너뜨릴수있음. 집을지을때는 제대로지어야된다.ㄱㄱ. 철저히 더막을것ㄱㄱ. 내쪽아닌 저쪽이어도 철저히 더막아줄것ㄱㄱ.
- [x] @RequeestBody




##
네, 날카로운 지적입니다! **정답입니다.** 제가 처음에 그렇게 알려드린 이유는 두 가지였습니다.

1.  **입문 단계의 복잡도 감소:** JPA와 스프링의 전체 흐름(Controller-Service-Repository)을 먼저 파악하는 게 우선이라, DTO라는 새로운 개념까지 한꺼번에 나오면 너무 뒤죽박죽될까 봐 가장 단순한 형태로 보여드린 것입니다.
2.  **`@RequestParam`의 이중성 테스트:** 유저님이 포스트맨으로 테스트하실 때, 별도의 객체 생성 없이도 바로 값을 넣어 성공 여부를 확인하기에 가장 빠르고 직관적인 방법이기 때문입니다.

---

### 🚨 하지만 실무라면? "절대 이렇게 안 합니다."

지금 작성하신 코드는 두 가지 큰 문제를 안고 있습니다.

1.  **보안 및 관례:** 앞서 대화 나눈 것처럼 비밀번호를 URL에 노출할 위험이 있는 `@RequestParam`보다는, 데이터 본체(Body)에 숨겨서 보내는 `@RequestBody`가 훨씬 안전합니다.
2.  **유지보수:** 지금은 아이디, 비번뿐이지만 나중에 "로그인한 기기 ID", "접속 IP", "2차 인증 번호" 등이 추가되면 메서드의 파라미터가 `@RequestParam`으로 줄줄이 소시지처럼 늘어납니다. (매우 보기 싫고 관리가 안 되죠.)

---

### ✅ 그래서 이제는 '정석'으로 갈아탈 시간입니다!

유저님이 아까 말씀하신 **"뒤죽박죽될 것 같은 느낌"**을 해결하는 가장 좋은 방법이 바로 이 **DTO(Data Transfer Object)**입니다. 

**1. 로그인용 DTO 만들기**
```java
package org.bitcopark.base.domain.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberLoginDto {
    private String loginId;
    private String password;
}
```

**2. 컨트롤러를 '정석'으로 수정하기**
```java
@PostMapping("/login")
public String login(@RequestBody MemberLoginDto loginDto) { // @RequestBody로 변경!
    Member loginMember = memberService.login(loginDto.getLoginId(), loginDto.getPassword());

    if (loginMember == null) {
        return "로그인 실패";
    }
    return loginMember.getName() + "님, 환영합니다!";
}
```

---

### 💡 이렇게 바꾸면 뭐가 좋아지나요?

* **포스트맨 테스트:** 이제 무조건 **Body -> raw -> JSON** 형식을 써야 합니다. (주소창에 `?password=...`를 아무리 쳐도 서버가 거절하므로 훨씬 안전해집니다.)
* **데이터의 구조화:** 로그인에 필요한 데이터가 10개로 늘어나도 컨트롤러 코드는 변하지 않습니다. DTO 클래스 안에 필드만 추가하면 끝입니다.
* **영역의 분리:** DB와 연결된 **Entity(Member)**와 통신에 쓰이는 **DTO**를 분리함으로써, "뒤죽박죽" 꼬이는 현상을 원천 차단합니다.



이제 유저님의 `MemberController`를 이 정석 코드로 한 단계 업그레이드해 보시는 건 어떨까요? 이렇게 하면 아까 400 에러가 났던 그 JSON 형식을 그대로 사용해서 로그인을 성공시키실 수 있습니다!