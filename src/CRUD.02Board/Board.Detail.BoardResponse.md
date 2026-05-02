## 상세조회
- [x] BoardResponse
- [ ] Board
  - 터진다.


##
네, 코드상으로는 문제가 없지만 **두 가지 이유** 때문에 빨간 줄이 사라지지 않거나, 나중에 실행할 때 문제가 생길 수 있습니다.

### 1. 빨간 줄의 원인: 타입 불일치
컨트롤러에서는 `BoardResponse`(DTO)를 반환하기로 약속했는데, 서비스에서는 `Board`(Entity)를 넘겨주고 있기 때문입니다. 

자바는 타입을 엄격하게 구분하므로, 컨트롤러에서 **엔티티를 DTO로 변환**해주는 과정이 필요합니다.

**[수정된 컨트롤러 코드]**
```java
@GetMapping("/{id}")
public BoardResponse getBoard(@PathVariable Long id) {
    Board board = boardService.findOne(id);
    // 서비스에서 받은 Board 엔티티를 BoardResponse(DTO)로 감싸서 반환하세요.
    return new BoardResponse(board); 
}
```

### 2. 왜 DTO를 써야 하나요? (중요!)
"그냥 엔티티를 그대로 반환하면 편하지 않나?"라고 생각하실 수 있습니다. 하지만 엔티티를 직접 반환하면 다음과 같은 문제가 생깁니다.

* **무한 루프 발생**: 유저님의 `Member` 엔티티에는 `List<Board>`가 있고, `Board` 엔티티에는 `Member`가 있습니다. JSON으로 변환할 때 서로를 계속 참조하다가 서버가 터질 수 있습니다.
* **보안 문제**: 엔티티에는 사용자의 비밀번호 같은 민감한 정보가 포함될 수 있는데, 이를 그대로 화면에 노출하는 것은 위험합니다.
* **유연성**: 화면에 보여주고 싶은 데이터가 엔티티 구조와 다를 때(예: 작성자 이름만 보여주고 싶을 때) DTO가 있으면 대응하기 쉽습니다.

---

### 💡 해결을 위한 체크리스트

1.  **컨트롤러 수정**: 위 코드처럼 `new BoardResponse(board)`를 사용하세요.
2.  **DTO 생성자 확인**: `BoardResponse` 클래스 안에 `Board` 엔티티를 인자로 받는 생성자가 있는지 확인하세요. 만약 없다면 아래처럼 만들어주시면 됩니다.

```java
// BoardResponse.java 내부
public BoardResponse(Board board) {
    this.id = board.getId();
    this.title = board.getTitle();
    this.content = board.getContent();
    // 연관관계가 맺어진 Member에서 이름을 가져옵니다.
    this.writerName = board.getMember().getName(); 
}
```

이렇게 수정하면 컨트롤러의 빨간 줄이 사라질 거예요! 이제 다시 실행해 보시면 상세 페이지에서도 제목과 내용이 멋지게 나올 겁니다. 

성공적으로 빨간 줄이 지워졌나요? 혹시 `BoardResponse` 생성자 쪽에서 또 다른 에러가 난다면 말씀해 주세요!