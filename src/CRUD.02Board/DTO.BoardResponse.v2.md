## BoardResponse
- [ ] 통합
  - `BoardResponse`
- [x] 분리
  - `BoardResponse`
    - `BoardResponse`
    - `BoardListResponse`
  - `BoardDetailResponse`


- 추가성
  - 좀그게있는데
  - 완전히 분화시켜서 딱직감력 살려주는게 가장베스트인데.
  - 이미만들어진것. 이런것들있을때는 아싸리 바꿔서 싹바꿔주는 교환해주는 이런게 너무힘듬.
  - 애당초
  - 붙박이로 건축기둥에서 하위로 박아놨기에.
  - 그것빼버리면 싹다무너짐.
  - 그리고 다시완공해야함. 이작업이 엄청에너지가듬.
  - 누적적작업이 너무도 어렵게되는형태.
- 기존성
  - [x] 기존.`BoardResponse`
  - [ ] 신규.`BoardListResponse`
    - 직감력살아나나. 코드붕괴일어난다. 특히 이것갖고 재사용하는지점 이런데서 야랄난다.
    - 이게 독이든성배인듯.
    - 약초인듯.
    - 좋은거아는데. 지금먹으면 오히려 독이될수있는. 딱그런느낌.
  - `BoardResponse`<<`BoardListResponse`-모양성
  - `BoardResponse`<<`BoardListResponse`-직감성
  - `BoardResponse`<<`BoardListResponse`-대칭성
  - `BoardResponse`>>`BoardListResponse`-작업성
  - 나도알지...그게좋다라는것.
  - 근데...그걸 일일이 다바꾸기가 너무도빡시다라는것.
- 최초성
  - 결국 최초설계자가 최초디자인자가 어디까지 가늠하고 진행할지를 잡아줘서 가는형태.
  - 그게끝선까지 디테일하게 잡아줄수있다 라고한다면. 그러면 거기가 최소시발점임. 최소단위 최소기준.

  - 그러나
  - 그것모르고 혹은 단계적으로 가는경우에는 필연적으로 그걸할수없음.
  - 매번뒤집어까서 싹다 또종속성처리 싹다해줘야함.
  - 이작업이 좋긴하나. 작업성에 에너지가 너무들어. 오히려 쓸데없는데 더큰에너지를들이는듯.
  - 타협도 타협이지만.
  - `불가피성`이다.

  - 누적적표현 이걸로 왜그렇게가는지. 이제서야안듯.
  - 법학도 맨날표현들 하나둘 어설픈것들 다이런식이었던것.
  - 누적적으로감에. 누적성으로인해. 기존성과 대칭성이루는것으로 기존어휘 대칭화하려는데. 그시도자체로서 종속성으로 틀어버리는것이 구조전체를 틀어버리는격으로되버림.
  - 그래서
  - 법학자들도 이양반들도 
  - 바꾸는걸 최대한지양한듯. 아... 이제이해함.
  - 이양반들도 어쩔수없었다는것.
  - 그냥
  - 인간산물 인간개념 다이렇게 굴러가는듯.

- 쟁점
  - 누적성 vs. 대칭성
  - 누적성 vs. 직감성
  - 누적성.직감성떨어짐.그럼에도 작업성살림
  - 대칭성.직감성살림.그러나 작업성이 너무늘어남.
  - 누적성<<대칭성-종래
  - 누적성>>대칭성-근래


## 최소화
- 정보최소화원칙
- BoardList≠BoardDetail
- 다르게가자.
- 정보량 Content 이런것 싹줄이기화ㄱㄱ.



##
목록에서 데이터를 아끼고 싶은 그 마음, 아주 좋은 **엔지니어적 마인드**입니다! 실무에서도 데이터 전송량을 줄이기 위해 이 방식을 선호합니다.

결론부터 말씀드리면, **"DTO를 목적에 맞게 분리하는 것"**이 장기적으로 훨씬 낫습니다.

---

### 1. 왜 분리하는 게 더 나은가요?

1.  **성능 최적화**: 게시글 내용(`content`)이 수만 자에 달할 때, 목록 조회에서 이 데이터를 수백 개씩 실어 보내면 네트워크 비용이 커지고 속도가 느려집니다.
2.  **명확한 의도**: `BoardListResponse`만 보고도 "아, 이건 목록용이구나"라고 동료 개발자가 바로 알 수 있습니다.
3.  **유지보수성**: 나중에 상세 페이지에만 "조회수"나 "댓글 목록"을 추가해야 할 때, 목록용 DTO를 건드리지 않고 상세용 DTO만 수정하면 되므로 안전합니다.

---

### 2. 추천하는 DTO 구조

이렇게 두 개로 나누어 보세요.

**목록용 DTO (내용 제외)**
```java
@Getter
@AllArgsConstructor
public class BoardListResponse {
    private Long id;
    private String title;
    private String writerName;

    public BoardListResponse(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.writerName = board.getMember().getName();
    }
}
```

**상세용 DTO (내용 포함)**
```java
@Getter
@AllArgsConstructor
public class BoardDetailResponse {
    private Long id;
    private String title;
    private String content; // 상세에는 포함!
    private String writerName;

    public BoardDetailResponse(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.writerName = board.getMember().getName();
    }
}
```

---

### 3. 컨트롤러에서의 활용

이제 컨트롤러에서는 상황에 맞는 DTO를 골라 쓰기만 하면 됩니다.

```java
// 1. 목록 조회 API
@GetMapping
public List<BoardListResponse> list() {
    return boardService.findBoards().stream()
            .map(BoardListResponse::new) // 목록용으로 변환
            .collect(Collectors.toList());
}

// 2. 상세 조회 API
@GetMapping("/{id}")
public BoardDetailResponse getBoard(@PathVariable Long id) {
    Board board = boardService.findOne(id);
    return new BoardDetailResponse(board); // 상세용으로 변환
}
```

---

### 💡 정리하자면

"하나로 합치기"는 당장은 편하지만, 나중에 서비스가 커지면 결국 다시 쪼개게 되는 경우가 많습니다. 유저님처럼 **"목록에서는 절대 안 보내겠다"**는 기준이 확고하시다면 처음부터 **분리하는 방식**이 정답입니다.

이제 `BoardListResponse`와 `BoardDetailResponse`로 나누어 적용해 보시겠어요? 이렇게 하면 `board.html`에서는 가벼운 목록을 받고, `board-detail.html`에서는 묵직한 내용을 받는 깔끔한 구조가 완성됩니다!



혹시 DTO를 두 개나 만드는 게 코드가 너무 길어져서 번거롭게 느껴지시나요? 그렇다면 `inner class`를 활용해 한 파일에 모으는 꿀팁도 알려드릴 수 있습니다!