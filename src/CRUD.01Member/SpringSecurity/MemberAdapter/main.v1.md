이 부분은 스프링 시큐리티를 처음 접할 때 가장 헷갈리는 지점 중 하나입니다. 결론부터 말씀드리면, **"스프링 시큐리티라는 거대한 기계가 사용자 정보를 가져올 때 사용할 구멍(규격)을 맞춰주는 작업"**이라고 이해하시면 됩니다.

구조적으로 왜 이렇게 작성했는지 핵심 이유 3가지를 짚어드릴게요.

---

### 1. 시큐리티와 우리 DB 사이의 '통역사' 역할
스프링 시큐리티는 여러분의 DB에 `Member` 테이블이 있는지, 컬럼명이 `login_id`인지 `email`인지 전혀 모릅니다. 시큐리티는 오직 `UserDetailsService` 인터페이스에 정의된 `loadUserByUsername`이라는 메서드를 통해서만 "사용자 정보를 달라"고 요청합니다.

* **시큐리티의 요청:** "여기 ID(loginId) 줄게. 이 사람 비밀번호랑 권한 정보 좀 찾아서 **내가 읽을 수 있는 타입(UserDetails)**으로 포장해서 가져와."
* **CustomUserDetailsService의 역할:** 1.  `MemberRepository`를 통해 우리 DB에서 사용자를 찾습니다.
    2.  찾은 `Member` 엔티티를 시큐리티가 이해할 수 있는 `MemberAdapter`(UserDetails의 구현체)로 변환합니다.

### 2. 왜 `MemberAdapter`를 쓰나요? (어댑터 패턴)
스프링 시큐리티는 `UserDetails`라는 인터페이스를 통해서만 사용자 정보를 읽습니다. 그런데 우리가 만든 `Member` 엔티티에 직접 시큐리티 코드를 넣으면 도메인이 오염되죠.

그래서 **중간 연결 다리**를 만든 겁니다.
* `MemberAdapter`는 시큐리티의 `User` 클래스를 상속받았으므로 시큐리티 입장에서는 **"오, 내가 아는 유저네!"**라고 인식합니다.
* 동시에 내부에 우리 실체인 `private final Member member;`를 품고 있어서, 나중에 컨트롤러에서 로그인한 사용자의 이름이나 PK 값을 아주 쉽게 꺼내 쓸 수 있게 됩니다.

### 3. 인증 프로세스에서의 흐름
사용자가 로그인을 시도하면 내부적으로 다음과 같은 일이 벌어집니다.

1.  사용자가 아이디/비번 제출.
2.  시큐리티가 `CustomUserDetailsService.loadUserByUsername("입력한ID")`를 호출.
3.  DB에서 `Member`를 찾아서 `MemberAdapter`에 담아 반환.
4.  **시큐리티 기계 내부**에서 사용자가 입력한 비번과 `MemberAdapter`가 가진 비번(DB 값)을 비교.
5.  일치하면 인증 성공!

---

### 컨트롤러에서 이렇게 쓸 수 있게 됩니다
이 설정을 해두면, 나중에 컨트롤러에서 아주 **"낭만"** 있게 사용자 정보를 가져올 수 있습니다.

```java
@GetMapping("/profile")
public String getProfile(@AuthenticationPrincipal MemberAdapter memberAdapter) {
    // MemberAdapter 덕분에 캐스팅 없이 바로 우리 Member 객체에 접근 가능!
    Member member = memberAdapter.getMember();
    System.out.println("로그인한 사람 이름: " + member.getName());
    return "profile";
}
```

**요약하자면:**
`CustomUserDetailsService`는 시큐리티가 "DB 가서 회원 정보 좀 캐와!"라고 시킬 때 사용하는 **심부름꾼 매뉴얼**을 정의한 것이라고 보시면 됩니다. 이 매뉴얼이 없으면 시큐리티는 어디서 데이터를 가져와야 할지 몰라 헤매게 됩니다.

이제 이 구조가 왜 필요한지 조금 감이 오시나요? 혹시 `@AuthenticationPrincipal` 부분에서 더 궁금한 게 생기셨나요?