##
- ✅`navigateTo('/board')`
  - Vue형태
- ❌`router.push('/board')`
  - Js형태


##
게시글 삭제 로직도 수정(`@PutMapping`)과 거의 흡사한 흐름으로 가되, HTTP 메서드만 **`@DeleteMapping`**으로 바꿔주면 됩니다. 

가장 중요한 포인트는 **"본인이 쓴 글이 맞는지"** 백엔드에서 한 번 더 검증하는 로직을 서비스 레이어에 포함하는 것이죠.

---

### 1. Controller (컨트롤러)

삭제는 보통 리턴할 데이터가 딱히 없으므로, 삭제된 게시글의 `id`를 넘기거나 성공 메시지만 보내면 됩니다.

```java
/**
 * 게시글 삭제
 */
@DeleteMapping("/{id}")
public ResponseEntity<Void> deleteBoard(
        @PathVariable Long id,
        @SessionAttribute(name = "loginMember") Member loginMember) {

    // 서비스에 글 ID와 현재 로그인한 유저 ID를 넘겨서 권한 확인 후 삭제
    boardService.delete(id, loginMember.getId());
    
    return ResponseEntity.ok().build(); // 200 OK
}
```

---

### 2. Service (서비스)

서비스 레이어에서는 단순히 `repository.delete()`를 호출하기 전에, **권한 체크**를 반드시 수행해야 합니다. (이걸 안 하면 URL만 알아내서 남의 글을 다 지우고 다닐 수 있거든요!)

```java
@Transactional
public void delete(Long boardId, Long memberId) {
    // 1. 게시글 조회 (없으면 예외 발생)
    Board board = boardRepository.findById(boardId)
            .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + boardId));

    // 2. 작성자 본인인지 확인 (수정 로직과 동일)
    if (!board.getMember().getId().equals(memberId)) {
        throw new RuntimeException("삭제 권한이 없습니다.");
    }

    // 3. 삭제 처리
    boardRepository.delete(board);
}
```

---

### 3. Nuxt 3 (프론트엔드 호출 부분)

상세 페이지나 수정 페이지에 '삭제' 버튼을 만들고 아래 함수를 연결하시면 됩니다.

```javascript
const deleteBoard = async () => {
  if (!confirm("정말로 삭제하시겠습니까?")) return

  try {
    await $fetch(`/api/boards/${boardId}`, {
      method: 'DELETE',
      // 세션 기반 인증이라면 브라우저가 알아서 쿠키를 들고 갑니다.
    })
    
    alert("삭제되었습니다.")
    router.push('/board') // 목록으로 이동
  } catch (error) {
    alert("삭제에 실패했습니다.")
  }
}
```

---

### 🧐 여기서 잠깐! "논리 삭제(Soft Delete)"에 대해 들어보셨나요?

현업에서는 실제로 DB에서 데이터를 `DELETE` 쿼리로 싹 지워버리는 **"물리 삭제(Hard Delete)"**보다는, 삭제 여부 컬럼만 업데이트하는 **"논리 삭제(Soft Delete)"**를 자주 씁니다.

* **물리 삭제:** `DELETE FROM board WHERE id = ?` (복구 불가능)
* **논리 삭제:** `UPDATE board SET is_deleted = true WHERE id = ?` (데이터는 남겨두고 사용자에게만 안 보여줌)

**왜 논리 삭제를 쓸까요?**
1. 사용자가 실수로 지웠을 때 복구해달라고 하면 살려낼 수 있습니다.
2. 데이터 분석용으로 "어떤 글들이 많이 삭제되는지" 등을 파악할 수 있습니다.
3. 데이터 간의 관계(댓글 등)가 꼬이는 걸 방지하기 쉽습니다.

지금은 연습 중이시니 `boardRepository.delete(board)` 방식을 먼저 완성해 보시고, 나중에 서비스가 커지면 `isDeleted` 필드를 추가해 보는 것도 좋은 공부가 될 거예요! :)