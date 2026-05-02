##
- [x] 경로변수
  - NuxtUI 여기에서 파일기반임. 디렉토리기반이라 경로기반으로 지원해줌.
  - 경로기반으로 싹진행ㄱㄱ.
- [ ] 쿼리파라미터
  - 예전방식이 이쪽이 더많은듯.
  - 검색에는 용이해도. 프레임워크 여기와의 호환은 또다른문제인듯.-현행최적에 계속맞춰갈것ㄱㄱ.


##
- 기존것그대로 vs. 쪼개기
- 기존것그대로쪼개기추가화 vs. 쪼개기
- 기존것그대로쪼개기추가화<<쪼개기
- 기존것그대로<<쪼개기
- 기존것그대로<<쪼개기-더독립적
- 기존것그대로<<쪼개기-더모듈적
- 기존것그대로<<쪼개기-더분화적
- 기존것그대로<<쪼개기-더단위적
- 기존것그대로<<쪼개기-정책이 달라지는쪽이 더강할것임. 쪼개는게 더맞다 ㄹㅇ.ㄱㄱ.


##
게시판이 `Notice`와 `Community`로 나뉘면, 프론트엔드 입장에서는 **"공지사항 페이지"**와 **"자유게시판 페이지"**가 각각 존재할 확률이 99%입니다.

따라서 컨트롤러도 사용자의 의도(어떤 게시판을 보러 왔는가)를 명확히 반영할 수 있도록 설계해야 합니다. 크게 두 가지 전략이 있는데, 상황에 맞춰 추천해 드릴게요.

---

### 전략 1. 쿼리 파라미터 방식 (추천 ⭐)
하나의 엔드포인트(`/api/boards`)를 유지하되, 어떤 타입인지 파라미터로 받는 방식입니다.

* **URL 구조:** `/api/boards?type=NOTICE`
* **장점:** 컨트롤러 코드가 간결하고, 나중에 게시판 타입이 10개로 늘어나도 메서드를 추가할 필요가 없습니다.

```java
@GetMapping
public List<BoardResponse> list(@RequestParam(name = "type", required = false) BoardType type) {
    List<Board> boards;
    
    if (type != null) {
        // 특정 타입만 조회 (예: /api/boards?type=NOTICE)
        boards = boardService.findBoardsByType(type);
    } else {
        // 전체 조회 (기존 로직 유지)
        boards = boardService.findBoards();
    }

    return boards.stream()
            .map(BoardResponse::new)
            .collect(Collectors.toList());
}
```

### 전략 2. 경로 변수(Path Variable) 방식
URL 경로에 게시판 타입을 명시하는 방식입니다.

* **URL 구조:** `/api/boards/NOTICE` 또는 `/api/boards/COMMUNITY`
* **장점:** URL 자체가 매우 명확하고, 게시판별로 캐싱을 하거나 보안 설정을 다르게 하기 좋습니다.

```java
@GetMapping("/{type}")
public List<BoardResponse> listByType(@PathVariable BoardType type) {
    return boardService.findBoardsByType(type).stream()
            .map(BoardResponse::new)
            .collect(Collectors.toList());
}
```

---

### 💡 저의 제안: "전략 1(쿼리 파라미터)"을 기본으로 가져가세요.

왜냐하면 **검색이나 필터링** 때문입니다. 나중에 게시판 목록에서 "제목 검색", "작성자 검색" 같은 기능이 추가될 텐데, 이때는 어차피 쿼리 파라미터를 쓰게 됩니다.

* `/api/boards?type=NOTICE&search=휴강` (공지사항 중 '휴강' 검색)
* `/api/boards?type=COMMUNITY&page=1` (자유게시판 1페이지)

이런 식으로 확장하기가 훨씬 유연합니다.

---

### 🛠️ 구현을 위해 준비할 것들

1.  **Repository 수정:** `type`으로 조회하는 메서드가 필요합니다.
    ```java
    public interface BoardRepository extends JpaRepository<Board, Long> {
        List<Board> findByType(BoardType type);
    }
    ```
2.  **Service 수정:** 컨트롤러가 넘겨준 `type`을 받아 Repository를 호출합니다.



### 결론
기존의 전체 조회 메서드를 **파라미터를 받는 형태로 확장**하는 쪽으로 가닥을 잡으시는 게 어떨까요? 

만약 "나는 공지사항 메인 페이지랑 자유게시판 메인 페이지를 완전히 다른 기능으로 분리하고 싶다"면 메서드를 새로 파는 게 맞지만, **데이터 구조(Board 엔티티)가 같다면** 하나의 메서드에서 `type`으로 분기 처리하는 것이 중복 코드를 줄이는 지름길입니다.

어떤 방식이 지금 만드시는 `bitcopark-base`의 느낌과 더 잘 맞으시나요?