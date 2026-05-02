##
- Service.반환.DTO>>Id
- Service.반환.DTO vs. Entity


##
보통 이 지점에서 많은 개발자가 고민을 하는데요, 결론부터 말씀드리면 **Service의 반환형을 `Long`(ID값)에서 `CommentResponse`(DTO)로 변경하는 것**을 추천합니다.

그 이유와 함께 코드를 어떻게 맞추면 좋을지 정리해 드릴게요.

---

## 1. 왜 Service에서 DTO를 반환하는 게 좋을까요?

* **컨트롤러의 부담 완화:** 현재 컨트롤러는 `CommentResponse`를 기다리고 있는데 서비스가 `Long`만 주면, 컨트롤러가 다시 Repository를 뒤져서 엔티티를 찾고 DTO로 변환해야 합니다. 서비스가 한 번에 다 해서 주면 컨트롤러가 훨씬 깔끔해집니다.
* **프론트엔드 요구사항:** 댓글을 작성하자마자 화면에 '작성자', '작성시간', '내용'이 바로 떠야 합니다. ID만 넘겨주면 프론트엔드에서 정보를 표시하기 위해 서버에 한 번 더 조회 요청을 보내야 하는 낭비가 발생합니다.

---

## 2. 코드 수정 가이드

### Service 수정 (Long → CommentResponse)
엔티티를 저장(`save`)한 후, 그 엔티티를 가지고 DTO를 생성해서 반환하도록 수정합니다.

```java
@Transactional
public CommentResponse save(String loginId, Long boardId, CommentRequest request) {
    Board board = boardRepository.findById(boardId)
            .orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다."));

    Member member = memberRepository.findByLoginId(loginId)
            .orElseThrow(() -> new IllegalArgumentException("회원이 없습니다."));

    Comment comment = request.toEntity(board, member);
    Comment savedComment = commentRepository.save(comment);

    // 저장된 엔티티를 DTO로 변환해서 반환 (정적 팩토리 메서드 활용)
    return CommentResponse.from(savedComment);
}
```

### CommentResponse 수정 (변환 로직 추가)
엔티티(`Comment`)를 넣으면 DTO(`CommentResponse`)로 바꿔주는 메서드를 추가합니다.

```java
@Getter
@AllArgsConstructor
@Builder
public class CommentResponse {
    private Long id;
    private String content;
    private String writerName;
    private LocalDateTime createdAt;

    // Entity를 DTO로 변환해주는 메서드
    public static CommentResponse from(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .writerName(comment.getMember().getName()) // 작성자 이름
                .createdAt(comment.getCreatedAt())         // BaseTimeEntity 필드
                .build();
    }
}
```

---

## 3. 구조 요약



1.  **Controller**: 로그인 세션 정보와 DTO를 서비스에 넘깁니다.
2.  **Service**: 필요한 엔티티(Board, Member)를 찾아와서 `Comment`를 저장하고, 결과물을 **`CommentResponse`**로 예쁘게 포장해서 컨트롤러에 돌려줍니다.
3.  **Controller**: 받은 응답(Body)을 `201 Created` 상태 코드와 함께 클라이언트에 보냅니다.

---

### 💡 팁
만약 "나는 서비스에서 DTO를 의존하게 하고 싶지 않다(순수하게 유지하고 싶다)"는 철학을 가지신 분들은 서비스에서 `Comment` 엔티티 자체를 반환하기도 합니다. 하지만 실무(특히 API 개발)에서는 **Service에서 DTO를 반환**하는 방식이 코드량도 적고 훨씬 직관적이라서 많이 사용됩니다.

이제 서비스의 반환형을 `CommentResponse`로 바꾸고, 저장된 객체를 `from()` 메서드로 변환해서 return 해보세요. 빨간 줄이 싹 사라질 겁니다!