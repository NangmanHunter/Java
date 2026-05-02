네, **완벽하게 가능합니다.** 오히려 Spring Security의 `@AuthenticationPrincipal`을 사용하는 것이 세션에서 직접 꺼내 쓰는 방식보다 훨씬 안전하고 "스프링다운" 방식입니다.

기존의 `@SessionAttribute`는 세션 저장소에서 직접 값을 꺼내오기 때문에 **타입 캐스팅 오류**가 발생할 수 있고, 세션에 값이 없을 때 처리하기가 번거로운데, Spring Security는 이를 프레임워크 수준에서 깔끔하게 추상화해줍니다.

이를 위해 필요한 핵심 단계 3가지를 정리해 드릴게요.

---

### 1. UserDetails 구현하기 (커스텀 유저 객체)
Security는 사용자를 `UserDetails`라는 인터페이스로 관리합니다. 현재 사용 중인 `Member` 엔티티를 Security가 이해할 수 있는 형태로 감싸야 합니다.

```java
@Getter
public class MemberAdapter extends User implements Serializable {
    private final Member member; // 우리가 만든 엔티티

    public MemberAdapter(Member member) {
        super(member.getEmail(), member.getPassword(), List.of(new SimpleGrantedAuthority(member.getRole())));
        this.member = member;
    }
}
```

### 2. UserDetailsService 구현 (로그인 로직)
로그인 시 DB에서 유저를 찾아 위에서 만든 `MemberAdapter`로 반환해주는 서비스가 필요합니다.

```java
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다."));
        return new MemberAdapter(member);
    }
}
```

### 3. Controller에서 사용하기
이제 세션에서 직접 꺼낼 필요 없이, `@AuthenticationPrincipal`을 통해 로그인된 유저 정보를 바로 가져올 수 있습니다.

```java
@PostMapping("/api/boards/{boardId}/comments")
public ResponseEntity<CommentResponse> createComment(
        @PathVariable Long boardId,
        @RequestBody CommentRequest request,
        @AuthenticationPrincipal MemberAdapter memberAdapter) { // 바로 주입됨!

    // memberAdapter 안에 담긴 실제 Member 엔티티 활용
    Member member = memberAdapter.getMember();
    CommentResponse response = commentService.save(boardId, request, member.getId());
    
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
}
```

---

### 💡 왜 이 방식이 더 좋을까요?

1.  **타입 안전성:** `@SessionAttribute`는 `Object`로 가져와서 캐스팅해야 할 때가 많지만, 이 방식은 내가 정의한 타입으로 즉시 주입받습니다.
2.  **테스트 용이성:** 세션을 가짜로 만드는 것보다 Security 컨텍스트를 가짜로 만드는 것이 테스트 코드 작성이 훨씬 수월합니다.
3.  **코드 간결함:** 세션에 유저가 있는지 체크하는 로직(`if (user == null)`)을 생략해도 됩니다. (Security 설정에서 인증된 사용자만 들어오게 막을 수 있기 때문입니다.)

### 주의할 점
기존 세션 로그인 방식을 Security로 전환하려면 `SecurityConfig` 클래스를 만들어 인증(Authentication) 방식을 설정해줘야 합니다. 

혹시 지금 **Security 설정 클래스**도 이미 만드셨나요? 아니면 설정 방법부터 같이 봐 드릴까요? 현재 10년 동안 공부하시던 변리사 시험을 뒤로하고 개발자로 전환 중이신 만큼, 이런 아키텍처 구조를 잘 잡아두시면 나중에 포트폴리오에서도 큰 강점이 될 거예요!