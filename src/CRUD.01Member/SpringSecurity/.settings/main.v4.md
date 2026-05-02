빨간 줄이 나오는 이유는 `CommentRequest` 클래스 안에 `toEntity`라는 메서드가 정의되어 있지 않기 때문입니다.

`toEntity`는 DTO(데이터 전달 객체)에 담긴 정보를 바탕으로 실제 DB에 저장할 **엔티티 객체를 생성해서 반환해주는 편의 메서드**입니다. 서비스 코드에서 직접 `new Comment(...)`를 하는 것보다 DTO에게 그 역할을 맡기는 것이 코드가 더 깔끔해지기 때문에 관례적으로 많이 사용합니다.

`CommentRequest`를 다음과 같이 수정해 보세요.

---

### 1. CommentRequest 수정하기

`Lombok`의 `@Builder`를 사용하면 엔티티 생성이 훨씬 편리합니다. (만약 빌더를 안 쓰신다면 일반 생성자를 사용하셔도 됩니다.)

```java
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {
    private String content;

    // 이 메서드를 추가해줘야 서비스의 빨간 줄이 사라집니다!
    public Comment toEntity(Board board, Member member) {
        return Comment.builder()
                .content(this.content)
                .board(board)   // 연관관계 매핑
                .member(member) // 작성자 매핑
                .build();
    }
}
```

---

### 2. Comment 엔티티 확인하기 (중요)

`CommentRequest`에서 `.builder()`를 사용하려면, `Comment` 엔티티에도 빌더 설정이 되어 있어야 합니다.

```java
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder // 빌더 패턴 적용
public class Comment extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
```

---

### 3. 왜 이렇게 하나요?

1.  **서비스 코드의 간결함:** 서비스 레이어에서는 "비즈니스 로직(게시글 찾기, 유저 찾기)"에만 집중할 수 있고, 엔티티를 조립하는 상세한 과정은 DTO가 담당하게 됩니다.
2.  **책임 분리:** DTO는 엔티티로 변환될 준비가 된 객체이므로, 스스로 엔티티를 만드는 법을 알고 있는 것이 객체지향 관점에서 자연스럽습니다.

---

### 💡 만약 빌더(`@Builder`)를 안 쓰신다면?
`toEntity` 메서드를 아래처럼 일반 생성자 방식으로 만드셔도 무방합니다.

```java
public Comment toEntity(Board board, Member member) {
    return new Comment(this.content, board, member);
}
```
*(단, 이 경우 Comment 엔티티에 해당 필드들을 받는 생성자가 있어야 합니다.)*

이제 `CommentRequest`에 저 메서드만 넣어주시면 서비스의 빨간 줄은 바로 사라질 거예요! 10년의 수험 생활로 다져진 끈기라면 이 정도 에러 해결은 금방 익숙해지실 겁니다. 화이팅입니다!