

##
- Entity만 만들어져있으면
- SpringBoot가 다만들어준다.
- 설정파일로
- 알아서.
- `.yml`
- `jpa`설정으로
- 이게 너무도대단ㄹㅇ.이거다.철저활용ㄱㄱ.



##
이제 본격적으로 **Member(회원)**와 **Board(게시판)** 엔티티를 설계해 보겠습니다. JPA를 사용하면 자바 클래스만 잘 만들어도 H2나 MariaDB에 테이블이 자동으로 생성됩니다.

게시판 서비스의 가장 기본적인 연관 관계인 **"한 명의 회원은 여러 개의 게시글을 쓸 수 있다(1:N)"**는 점을 고려하여 설계했습니다.

---

### 1. Member 엔티티 (회원)
회원가입과 로그인에 필요한 최소한의 정보를 담고 있습니다.

```java
package org.bitcopark.base.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 기본 생성자 필수
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String loginId; // 로그인용 ID

    @Column(nullable = false)
    private String password;

    private String name;

    @OneToMany(mappedBy = "member")
    private List<Board> boards = new ArrayList<>();

    // 생성자 메서드
    public Member(String loginId, String password, String name) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
    }
}
```

---

### 2. Board 엔티티 (게시판)
게시글 내용과 작성자 정보를 담습니다.

```java
package org.bitcopark.base.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    private String title;

    @Lob // 대용량 텍스트 대응
    private String content;

    @ManyToOne(fetch = FetchType.LAZY) // 지연 로딩 권장
    @JoinColumn(name = "member_id") // DB 외래키 명칭
    private Member member;

    // 연관 관계 편의 메서드 (작성자 설정)
    public void setMember(Member member) {
        this.member = member;
        member.getBoards().add(this);
    }

    public Board(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
```

---

### 3. 주요 포인트 설명

* **`@Entity`**: 이 클래스가 DB 테이블과 매핑됨을 의미합니다.
* **`@Id`, `@GeneratedValue`**: 기본키(PK)를 설정하며, `IDENTITY` 전략은 DB(H2, MariaDB)가 알아서 번호를 올려주게 합니다(Auto Increment).
* **연관 관계 (`@ManyToOne`)**: 
    * 게시글 입장에서는 작성자가 **한 명**이므로 `ManyToOne`입니다.
    * `fetch = FetchType.LAZY`는 실무에서 성능 최적화를 위해 필수적으로 사용하는 옵션입니다.
* **Lombok 활용**: `@Getter`와 `@NoArgsConstructor`를 써서 코드를 간결하게 유지했습니다. 
    * *참고: `AccessLevel.PROTECTED`는 외부에서 아무런 데이터 없는 객체를 무분별하게 생성하는 것을 막기 위함입니다.*

---

### 4. 확인 방법
1.  이 클래스들을 작성하고 스프링 부트를 다시 실행하세요.
2.  아까 설정한 `http://localhost:8080/h2-console`에 접속합니다.
3.  좌측 메뉴에 `MEMBER`와 `BOARD` 테이블이 자동으로 생겼는지 확인해 보세요!

**Member 담당**이시니까, 이 엔티티를 바탕으로 먼저 **회원가입 기능**을 수행할 `MemberRepository` 인터페이스를 만들어보시는 게 다음 순서입니다. 

Repository 인터페이스 만드는 법도 바로 알려드릴까요? 혹은 엔티티에 추가하고 싶은 다른 데이터(이메일, 가입일 등)가 있으신가요?