이제 대망의 마지막 단계인 **`MemberController`**입니다. 컨트롤러는 외부(브라우저, 포스트맨 등)에서 들어오는 HTTP 요청을 받아, 우리가 만든 **`MemberService`**에 일을 시키고 결과를 다시 돌려주는 **'안내 데스크'** 역할을 합니다.

---

### 1. MemberController 코드

가장 표준적인 REST API 형태의 구성입니다.

```java
package org.bitcopark.base.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.bitcopark.base.domain.member.entity.Member;
import org.bitcopark.base.domain.member.service.MemberService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // JSON 데이터를 주고받는 컨트롤러임을 선언
@RequestMapping("/api/members") // 이 컨트롤러의 모든 주소는 /api/members로 시작
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원가입 API
     * POST http://localhost:8080/api/members/signup
     */
    @PostMapping("/signup")
    public String signUp(@RequestBody Member member) {
        memberService.join(member);
        return "회원가입 완료! ID: " + member.getLoginId();
    }

    /**
     * 로그인 API
     * POST http://localhost:8080/api/members/login
     */
    @PostMapping("/login")
    public String login(@RequestParam String loginId, @RequestParam String password) {
        Member loginMember = memberService.login(loginId, password);
        
        if (loginMember == null) {
            return "로그인 실패: 아이디 또는 비밀번호를 확인하세요.";
        }
        return loginMember.getName() + "님, 환영합니다!";
    }

    /**
     * 전체 회원 목록 조회 (테스트용)
     * GET http://localhost:8080/api/members
     */
    @GetMapping
    public List<Member> list() {
        return memberService.findMembers();
    }
}
```

---

### 2. 핵심 어노테이션 설명

* **`@RestController`**: 
    * 예전에는 HTML 뷰를 그리는 `@Controller`를 썼지만, 요즘은 데이터를 JSON 형태로 주고받는 `@RestController`를 주로 사용합니다.
* **`@RequestMapping("/api/members")`**: 
    * 공통 주소를 미리 뽑아두는 겁니다. 이렇게 하면 메서드마다 `/api/members`를 반복해서 적지 않아도 됩니다.
* **`@PostMapping` / `@GetMapping`**: 
    * **POST**: 데이터를 새로 만들거나(회원가입), 보안이 필요한 정보(로그인)를 보낼 때 씁니다.
    * **GET**: 데이터를 단순 조회할 때 씁니다.
* **`@RequestBody`**: 
    * HTTP 요청 몸체(Body)에 담긴 JSON 데이터를 자바 객체(`Member`)로 자동 변환해 줍니다. 
* **`@RequestParam`**: 
    * URL 뒤에 붙는 파라미터(`?loginId=test`)를 읽어올 때 씁니다.

---

### 3. 전체 흐름 요약 (Member 파트의 한 사이클)

이제 유저님이 만든 모든 조각이 하나로 연결되었습니다.

1.  **브라우저**: "회원가입 정보(JSON) 보낼게!" (`POST /api/members/signup`)
2.  **Controller**: "오케이, 접수! **Service**님, 이 정보로 가입시켜 주세요."
3.  **Service**: "잠깐, 중복 검사 좀 하고... 문제없네! **Repository**님, 이거 저장하세요."
4.  **Repository**: "JPA 마법 발동! **DB**에 `INSERT` 쿼리 날립니다."
5.  **DB**: "저장 완료!" (H2 데이터베이스에 쏙 들어감)



---

### 💡 실무를 위한 아주 중요한 팁

지금 코드는 **`Member` 엔티티**를 컨트롤러에서 직접 주고받고 있습니다. 하지만 실제 프로젝트에서는 **DTO(Data Transfer Object)**라는 별도의 객체를 만들어 사용하는 것이 철칙입니다.

* **이유**: 엔티티에는 비밀번호나 내부 정보가 들어있는데, 이걸 그대로 화면에 던지면 보안상 위험하고 아까 말씀드린 '무한 루프'가 터질 수 있습니다.

---

### ✅ 축하합니다!
이제 **Member(회원)** 기능의 가장 기본적인 뼈대가 모두 완성되었습니다. 

1.  `Entity`: DB 설계
2.  `Repository`: DB 통로
3.  `Service`: 비즈니스 로직
4.  `Controller`: 외부 인터페이스

이제 애플리케이션을 실행하고 **포스트맨(Postman)** 같은 도구로 실제로 회원가입이 되는지 테스트해 보실 수 있습니다. 혹시 **테스트 방법**이 궁금하신가요? 아니면 방금 말씀드린 **DTO**를 적용해서 코드를 더 안전하게 고쳐볼까요?