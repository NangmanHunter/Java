

##
아하, 상황이 파악되었습니다! 처음에는 **게시글 목록**용으로 `BoardResponse`를 만들다 보니 `content` 필드를 빼놓으셨군요. 그런데 상세 페이지에서는 `content`가 반드시 필요하니 필드에 빨간 줄(에러)이 뜨는 상황입니다.

이럴 때 실무에서 가장 많이 쓰는 **두 가지 전략** 중 선택하시면 됩니다. 유저님의 스타일에 맞는 걸 골라보세요!

---

### 방법 1: `BoardResponse` 하나로 통합하기 (가장 간편)
가장 초경량으로 가고 싶다면, 기존 DTO에 `content` 필드만 추가하는 방식입니다. 목록 조회 시에도 `content`가 포함되어 데이터 양이 조금 늘어나지만, 코드가 단순해집니다.

```java
@Getter
@AllArgsConstructor
public class BoardResponse {
    private Long id;
    private String title;
    private String content; // ← 필드 추가!
    private String writerName;

    public BoardResponse(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent(); // 이제 에러가 안 납니다.
        this.writerName = board.getMember().getName();
    }
}
```

### 방법 2: 목록용과 상세용 DTO 분리하기 (권장)
실무에서는 데이터 전송 최적화를 위해 **목록용(List)**과 **상세용(Detail)** DTO를 따로 관리하는 경우가 많습니다. 목록에는 내용이 필요 없으니까요.

* **`BoardListResponse`**: `id`, `title`, `writerName` (목록 뿌리기용)
* **`BoardDetailResponse`**: `id`, `title`, `content`, `writerName` (상세 페이지용)

---

### 💡 유저님께 드리는 추천: "그냥 필드 하나 추가하세요!"

지금은 프로젝트 규모가 크지 않고, 공부하는 단계이므로 **방법 1(필드 추가)**을 추천합니다. 

`BoardResponse`에 `private String content;` 한 줄만 적어주면 목록과 상세 페이지 양쪽에서 모두 행복하게 사용할 수 있습니다.

**[최종 수정된 BoardResponse]**
```java
@Getter
@AllArgsConstructor
@NoArgsConstructor // JSON 변환을 위해 기본 생성자도 있는 게 좋습니다.
public class BoardResponse {
    private Long id;
    private String title;
    private String content; // 필드 추가
    private String writerName;

    public BoardResponse(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent(); 
        this.writerName = board.getMember().getName();
    }
}
```

### 🚨 주의사항: 컨트롤러의 목록 조회 메서드
만약 `BoardResponse`에 필드를 추가하면, 기존에 목록을 가져오던 컨트롤러의 `list()` 메서드에서도 자동으로 `content`를 포함해서 보내주게 됩니다. 

```java
@GetMapping
public List<BoardResponse> list() {
    return boardService.findBoards().stream()
            .map(BoardResponse::new) // 여기서도 이제 content가 포함됨
            .collect(Collectors.toList());
}
```

이렇게 하면 `board-detail.html`의 빨간 줄도 사라지고, 목록 페이지에서도 아무런 문제 없이 작동할 거예요! `content` 필드 추가하고 다시 한번 빌드해 보시겠어요?

혹시 "목록에서는 절대 content를 보내고 싶지 않아!"라는 확고한 철학이 있으시다면, 그때는 DTO를 두 개로 쪼개는 코드를 짜 드릴게요! 어떤 쪽이 더 끌리시나요?