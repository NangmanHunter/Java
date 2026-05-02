## Service
- @Transactional(readOnly = true)
  - Class
- @Transactional
  - Method


##
이제 리포지토리(DB 통로)를 만들었으니, 이를 활용해 실제 **비즈니스 로직**을 수행하는 **`MemberService`**를 만들 차례입니다.

서비스 계층은 **"사용자가 회원가입 버튼을 눌렀을 때, 어떤 검사를 하고 어떤 순서로 DB에 저장할지"**를 결정하는 핵심 두뇌입니다.

---

### 1. MemberService 코드

가장 기본적인 회원가입과 로그인 로직을 담은 구성입니다.

```java
package org.bitcopark.base.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.bitcopark.base.domain.member.entity.Member;
import org.bitcopark.base.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true) // 기본적으로 조회 성능 최적화
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원가입
     */
    @Transactional // 쓰기 작업이므로 readOnly = false (기본값)
    public Long join(Member member) {
        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    /**
     * 중복 아이디 검증 로직
     */
    private void validateDuplicateMember(Member member) {
        memberRepository.findByLoginId(member.getLoginId())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 아이디입니다.");
                });
    }

    /**
     * 로그인 체크
     */
    public Member login(String loginId, String password) {
        return memberRepository.findByLoginId(loginId)
                .filter(m -> m.getPassword().equals(password)) // 비밀번호 일치 확인
                .orElse(null); // 실패 시 null 반환 (실무에선 예외 처리가 좋음)
    }

    /**
     * 전체 회원 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }
}
```

---

### 2. 핵심 포인트 설명

* **`@Service`**: 스프링에게 "이 클래스는 비즈니스 로직을 담당하는 서비스 객체야"라고 알려주는 어노테이션입니다.
* **`@Transactional`**: **JPA에서 가장 중요한 어노테이션 중 하나입니다.**
    * 메서드 안에서 일어나는 모든 DB 작업이 '전부 성공'하거나 '전부 실패(Rollback)'하도록 묶어줍니다.
    * 특히 `readOnly = true`를 클래스 레벨에 두면, 조회만 하는 메서드들에서 성능 이점을 얻을 수 있습니다.
* **`@RequiredArgsConstructor`**: `final`이 붙은 리포지토리를 스프링이 알아서 주입(Dependency Injection)해 주도록 만드는 롬복 어노테이션입니다.
* **비즈니스 규칙 적용**: 리포지토리의 `save`를 그냥 쓰는 게 아니라, `validateDuplicateMember` 같은 메서드를 만들어 **"우리 서비스는 중복 아이디를 허용하지 않는다"**는 규칙을 강제합니다.



---

### 3. 왜 리포지토리를 직접 안 쓰고 서비스를 거치나요?

유저님이 아까 "뒤죽박죽될 것 같다"고 걱정하셨던 부분이 여기서 해결됩니다.

1.  **역할 분담**: 리포지토리는 "DB에서 데이터를 꺼내온다"에만 집중하고, 서비스는 "꺼내온 데이터를 어떻게 요리할지"에만 집중합니다.
2.  **트랜잭션 관리**: 회원가입을 하다가 갑자기 서버가 꺼졌을 때, DB에 데이터가 반만 저장되는 끔찍한 상황을 서비스의 `@Transactional`이 막아줍니다.
3.  **가독성**: 컨트롤러에서 복잡한 로직을 다 짜면 코드가 난잡해지지만, 서비스에 모아두면 다른 곳에서도 재사용하기 좋습니다.

---

### 💡 현재 진행 상황

1.  **Entity**: 테이블 설계 완료
2.  **Repository**: DB 통로 확보 (JPA 마법)
3.  **Service**: 비즈니스 로직 작성 완료

이제 마지막으로 이 서비스의 기능을 외부(브라우저나 앱)에서 호출할 수 있게 문을 열어주는 **`Controller`**만 만들면 **Member 파트의 한 사이클**이 끝납니다! 

컨트롤러까지 달려볼까요? 아니면 서비스 코드 중에 이해가 안 가는 부분이 있으신가요? 특히 `@Transactional` 개념은 꽤 생소하실 수 있어요.