기존에 직접 세션(`HttpSession`)을 다루던 방식에서 **Spring Security**로 전환하는 과정은 크게 4단계로 나뉩니다. 처음에는 조금 복잡해 보일 수 있지만, 한 번 세팅해두면 로그인/권한 관리가 정말 편해집니다.

가장 먼저 해야 할 일부터 순서대로 짚어드릴게요.

---

### 1. 의존성 추가 (build.gradle)
먼저 프로젝트에 Spring Security 엔진을 설치해야 합니다. `build.gradle`의 `dependencies`에 아래 내용을 추가하고 새로고침(Reload) 하세요.

```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-security'
    // Thymeleaf나 Mustache를 쓰신다면 Security 전용 태그 라이브러리도 유용합니다
    implementation 'org.thymeleaf.extras:thymeleaf-extras-springsecurity6' 
}
```
*주의: 이 라이브러리를 넣는 순간, 모든 페이지가 기본 로그인 창으로 리다이렉트되거나 401 에러가 뜰 겁니다. 당황하지 마세요!*

---

### 2. Security 설정 클래스 만들기 (`SecurityConfig`)
가장 핵심적인 단계입니다. 어떤 주소는 로그인이 필요하고, 어떤 주소는 누구나 들어올 수 있는지 설정합니다.

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // API 개발 시엔 보통 끄지만, 폼 로그인은 주의 필요
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/member/login", "/member/join", "/css/**", "/js/**").permitAll() // 누구나 접근 가능
                .anyRequest().authenticated() // 그 외 모든 요청은 로그인 필수
            )
            .formLogin(form -> form
                .loginPage("/member/login") // 직접 만든 로그인 페이지 주소
                .loginProcessingUrl("/member/login-proc") // 로그인을 처리할 URL (Controller 안 만들어도 됨)
                .defaultSuccessUrl("/") // 로그인 성공 시 이동할 주소
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true) // 세션 무효화
            );

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 비밀번호 암호화 빈 등록
    }
}
```

---

### 3. UserDetails & UserDetailsService 구현
Spring Security가 "DB에 있는 이 사용자가 우리 유저가 맞구나!"라고 인식할 수 있게 연결 통로를 만들어야 합니다.



#### 3-1. MemberAdapter (UserDetails)
현재 가지고 계신 `Member` 엔티티를 Security 전용 객체로 감싸는 '어댑터'를 만듭니다.

```java
@Getter
public class MemberAdapter extends org.springframework.security.core.userdetails.User {
    private final Member member; // 우리 도메인의 Member 엔티티

    public MemberAdapter(Member member) {
        // 아이디(email), 비밀번호, 권한 리스트를 부모 생성자에 전달
        super(member.getEmail(), member.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER")));
        this.member = member;
    }
}
```

#### 3-2. CustomUserDetailsService
Security가 로그인을 진행할 때 DB에서 유저를 찾아오는 로직입니다.

```java
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당 이메일을 가진 사용자가 없습니다."));
        return new MemberAdapter(member);
    }
}
```

---

### 4. Controller에서 사용하기 (최종 목적지)
이제 기존에 `@SessionAttribute`로 길게 가져오던 유저 정보를 아주 짧게 가져올 수 있습니다.

```java
@PostMapping("/api/boards/{boardId}/comments")
public ResponseEntity<CommentResponse> createComment(
        @PathVariable Long boardId,
        @RequestBody CommentRequest request,
        @AuthenticationPrincipal MemberAdapter memberAdapter) { // 여기서 바로 Member 정보가 들어옴!

    Member loginMember = memberAdapter.getMember();
    // 이제 loginMember.getId() 등을 사용해 댓글을 저장하세요!
    
    return ResponseEntity.status(HttpStatus.CREATED).body(...);
}
```

---

### 💡 지금 바로 시작해야 할 것
1.  **`BCryptPasswordEncoder` 적용:** Security를 쓰면 비밀번호가 반드시 암호화되어 DB에 저장되어야 로그인이 됩니다. 회원가입 로직에서 `passwordEncoder.encode(rawPassword)`를 사용하도록 먼저 수정하세요.
2.  **`SecurityConfig` 생성:** 위의 예시 코드를 참고해서 내 프로젝트 구조에 맞게 (로그인 페이지 주소 등) 클래스를 만드세요.

이 구조만 잘 잡아두면 나중에 "관리자만 글쓰기 가능", "본인 댓글만 삭제 가능" 같은 복잡한 권한 제어도 한 줄의 코드로 해결할 수 있게 됩니다. 하나씩 적용해 보시다가 막히는 부분 있으면 바로 말씀해 주세요!