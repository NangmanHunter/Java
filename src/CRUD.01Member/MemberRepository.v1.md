드디어 JPA의 진짜 마법이 시작되는 구간입니다. 

JPA는 **`JpaRepository`**라는 인터페이스를 상속받기만 하면, 우리가 흔히 쓰는 CRUD(생성, 조회, 수정, 삭제) SQL을 알아서 다 만들어 줍니다. 직접 쿼리를 짤 필요가 없어요.

---

### 1. MemberRepository 코드

이 인터페이스 하나면 회원가입, 회원 조회, 삭제가 모두 가능해집니다.

```java
package org.bitcopark.base.domain.member.repository;

import org.bitcopark.base.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 로그인 ID로 회원 찾기 (로그인 시 필요)
    Optional<Member> findByLoginId(String loginId);

    // 이름으로 회원 찾기 (중복 확인 등 필요 시)
    Optional<Member> findByName(String name);
}
```

---

### 2. 핵심 포인트 설명

* **`extends JpaRepository<Member, Long>`**:
    * **`Member`**: 이 리포지토리가 관리할 엔티티 클래스입니다.
    * **`Long`**: `Member` 엔티티의 `@Id`(PK) 타입입니다.
    * 이 상속 하나로 `save()`, `findById()`, `findAll()`, `delete()` 같은 메서드를 바로 쓸 수 있게 됩니다.
* **쿼리 메서드 (Query Methods)**:
    * `findByLoginId`라고 이름을 지으면, JPA가 이 이름을 분석해서 `SELECT * FROM member WHERE login_id = ?`라는 SQL을 자동으로 생성합니다. **(이게 진짜 편한 기능입니다!)**
* **`Optional<Member>`**:
    * 회원을 찾았는데 없을 수도 있죠? 이때 `null`을 반환하는 대신 `Optional`로 감싸서 안전하게 처리할 수 있도록 돕는 자바의 보관함 같은 것입니다.

---

### 3. 어떻게 사용하나요? (예시)

나중에 서비스 계층에서 이렇게 쓰게 됩니다.

```java
// 회원 저장 (INSERT)
memberRepository.save(new Member("user1", "1234", "홍길동"));

// 아이디로 조회 (SELECT)
Member member = memberRepository.findByLoginId("user1")
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));
```

---

### 4. 뒤죽박죽이 안 되는 이유

아까 걱정하셨던 "설계가 꼬일 것 같은 느낌"을 이 리포지토리가 해결해 줍니다. 
* **복잡한 연관 관계**는 엔티티끼리 알아서 처리하게 두고, 
* **우리는 딱 필요한 데이터**만 리포지토리에서 꺼내오면 됩니다.



### 💡 다음 단계 제안
리포지토리까지 만들었으니, 이제 이 리포지토리를 사용해서 실제 **비즈니스 로직(회원가입, 로그인)**을 구현할 **`MemberService`**를 만들어볼 차례입니다. 

서비스에서는 **"중복 아이디가 있는지 확인하고, 없으면 저장한다"** 같은 실제 규칙들을 코딩하게 됩니다. 서비스 코드도 바로 확인해 보시겠어요? 아니면 리포지토리 메서드 이름 짓는 법이 더 궁금하신가요?