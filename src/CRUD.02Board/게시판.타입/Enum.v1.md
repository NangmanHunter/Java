게시판 유형(Notice, Community)을 구분할 때는 **`Enum`**을 사용하는 것이 가장 깔끔하고 관리하기 좋습니다. 나중에 `Q&A`, `Event` 같은 게시판이 추가되더라도 대응하기 쉽기 때문이죠.

현재 엔티티에 `BoardType`을 추가하고 구조를 개선하는 방법을 제안해 드립니다.

---

### 1. BoardType Enum 생성
먼저 게시판의 종류를 정의할 Enum 파일을 만듭니다.

```java
package org.bitcopark.base.domain.board.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BoardType {
    NOTICE("공지사항"),
    COMMUNITY("자유게시판");

    private final String description;
}
```

### 2. Board 엔티티 수정
`Board` 엔티티에 위에서 만든 `BoardType` 필드를 추가합니다. 이때 DB에는 문자열 그대로 저장되도록 `@Enumerated(EnumType.STRING)` 설정을 꼭 넣어주세요.

```java
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE board SET is_deleted = true, deleted_at = CURRENT_TIMESTAMP WHERE board_id = ?")
@Where(clause = "is_deleted = false")
public class Board extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    // 추가된 필드: 게시판 유형 (NOTICE, COMMUNITY)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BoardType boardType;

    private String title;

    @Lob
    private String content;

    private long viewCount = 0L;
    private boolean isDeleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 생성자 수정
    public Board(BoardType boardType, String title, String content, Member member) {
        this.boardType = boardType;
        this.title = title;
        this.content = content;
        if (member != null) {
            setMember(member);
        }
    }

    // ... 기존 메서드들
}
```

---



## 공지사항ㆍ게시판
- 같이있는경우 `isPinned` 추가ㄱㄱ.

필드 구성 시 고려할 팁

**공지사항(Notice) 전용 필드 고려**
만약 공지사항은 일반 게시물보다 위에 고정되어야 한다면, `isPinned` 같은 필드를 추가하면 좋습니다.
```java
private boolean isPinned = false; // 상단 고정 여부
```


## 카테고리
#### **카테고리 vs 게시판 유형**
현재는 `Notice`와 `Community`를 별도의 '게시판'으로 보셨지만, 나중에 '자유게시판' 안에서 `질문`, `정보`, `잡담` 같은 세부 카테고리가 필요할 수도 있습니다.
* **BoardType:** 물리적인 큰 게시판 구분 (Notice, Community)
* **Category (필요시 추가):** 게시판 내부의 탭 구분 (Question, Info 등)



### 4. 확장성을 위한 조언
지금처럼 `Board` 엔티티 하나에 `type` 필드를 두는 방식은 **단일 테이블 전략**과 유사합니다. 로직이 단순하고 조인이 필요 없어 성능상 유리합니다. 

만약 나중에 "공지사항에는 파일 첨부가 필수지만, 커뮤니티는 아니다"처럼 **유형별로 가져야 할 데이터(필드)가 완전히 달라진다면** 그때 가서 상속 구조(`@Inheritance`)를 고민해 보셔도 충분합니다. 지금은 **Enum 하나로 구분하는 현재 방식이 가장 효율적**입니다.

이렇게 구성하면 프론트엔드에서 요청을 보낼 때 `boardType: 'NOTICE'`만 넘겨주면 간단하게 구분해서 저장하고 조회할 수 있습니다. 다음 단계로 넘어가 볼까요?