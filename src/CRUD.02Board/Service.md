`Board` 엔티티에서 작성하신 **연관 관계 편의 메서드(`setMember`)**를 실제로 어떻게 써먹는지 확인할 수 있는 단계입니다. 

게시글 서비스는 단순히 저장만 하는 게 아니라, **"어떤 회원이 이 글을 쓰는가?"**를 연결해 주는 것이 핵심입니다.

---

### 1. BoardService 코드

```java
package org.bitcopark.base.domain.board.service;

import lombok.RequiredArgsConstructor;
import org.bitcopark.base.domain.board.entity.Board;
import org.bitcopark.base.domain.board.repository.BoardRepository;
import org.bitcopark.base.domain.member.entity.Member;
import org.bitcopark.base.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    /**
     * 게시글 작성
     */
    @Transactional
    public Long write(Long memberId, String title, String content) {
        // 1. 엔티티 조회 (작성자 확인)
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        // 2. 게시글 생성
        Board board = new Board(title, content);

        // 3. 연관 관계 설정 (유저님이 만든 setMember 메서드 활용!)
        board.setMember(member);

        // 4. 저장
        boardRepository.save(board);
        return board.getId();
    }

    /**
     * 게시글 단건 조회
     */
    public Board findOne(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
    }

    /**
     * 전체 게시글 조회 (최신순)
     */
    public List<Board> findBoards() {
        return boardRepository.findAllByOrderByIdDesc();
    }
}
```

---

### 2. 왜 이렇게 짜야 할까요? (중요 포인트)

* **두 개의 Repository 사용**: `BoardService`라고 해서 `BoardRepository`만 쓰는 게 아닙니다. "누가 썼는지" 알아야 하므로 `MemberRepository`를 함께 주입받아 사용합니다.
* **`board.setMember(member)`의 역할**: 
    * 이 한 줄이 실행되면 `Board` 쪽 외래키(`member_id`)가 세팅될 뿐만 아니라, 유저님이 만드신 코드 덕분에 `member` 객체의 `boards` 리스트에도 이 글이 자동으로 추가됩니다. 
    * 이게 바로 **메모리(객체)와 DB의 상태를 똑같이 맞춰주는** 좋은 습관입니다.
* **`@Transactional`**: 
    * `write` 메서드는 DB에 글을 저장하는 작업이므로 반드시 `@Transactional`이 붙어야 합니다. 만약 저장 중에 에러가 나면 작성자 연결부터 저장까지 싹 다 취소(Rollback)해 줍니다.



---

### 3. "뒤죽박죽"을 막는 서비스 설계 팁

아까 걱정하셨던 "관계가 복잡해지면 난리가 날 것 같다"는 부분에 대한 해답이 여기 있습니다.

1.  **ID로 소통하세요**: 서비스 메서드 파라미터로 `Member` 객체 자체를 넘기지 말고, **`Long memberId`**를 넘기세요. 서비스 안에서 직접 리포지토리로 조회해서 사용하는 것이 훨씬 안전하고 데이터가 꼬이지 않습니다.
2.  **비즈니스 로직의 위치**: "회원은 글을 쓸 수 있다"는 로직은 서비스에 있고, "글과 회원을 연결한다"는 로직은 엔티티(`setMember`)에 있습니다. 이렇게 역할이 딱딱 나뉘어 있어야 나중에 코드가 커져도 관리가 됩니다.

---

### 💡 다음은 무엇을 할까요?

이제 `BoardService`까지 왔으니, 실제 동작을 확인하려면 **`BoardController`**가 필요합니다. 

여기서 한 가지 고민해 볼 점이 있습니다. 게시글을 쓸 때 사용자에게 **`memberId`**를 직접 입력받을 수는 없겠죠? (보통은 로그인한 세션이나 토큰에서 가져옵니다.)

우선은 테스트를 위해 **`memberId`를 포함한 `BoardCreateRequest` DTO**를 만들어서 컨트롤러를 구성해 볼까요? 아니면 서비스 코드 중에 이해가 안 가는 로직이 있으신가요?