`Board` 엔티티도 `Member`와 마찬가지로 **`JpaRepository`**를 상속받는 인터페이스만 만들면 기본 CRUD가 완성됩니다. 

다만, 게시판 기능에서는 "특정 회원이 쓴 글만 보기" 혹은 "최신순으로 보기" 같은 조건이 자주 붙기 때문에, 그런 메서드들을 어떻게 추가하는지 중점적으로 보시면 좋습니다.

---

### 1. BoardRepository 코드

```java
package org.bitcopark.base.domain.board.repository;

import org.bitcopark.base.domain.board.entity.Board;
import org.bitcopark.base.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    // 1. 특정 회원이 작성한 모든 게시글 조회
    List<Board> findByMember(Member member);

    // 2. 제목에 특정 단어가 포함된 게시글 검색 (LIKE 검색)
    List<Board> findByTitleContaining(String title);

    // 3. 최신순으로 전체 조회
    List<Board> findAllByOrderByIdDesc();
}
```

---

### 2. 핵심 포인트 설명

* **`extends JpaRepository<Board, Long>`**:
    * 이제 `boardRepository.save()`, `boardRepository.findById()` 등을 기본으로 쓸 수 있습니다.
* **`findByMember(Member member)`**:
    * **연관 관계 조회:** `Board` 엔티티 안에 `member`라는 필드가 있기 때문에, JPA가 알아서 `JOIN` 쿼리나 `WHERE member_id = ?` 쿼리를 생성합니다.
    * 나중에 "마이페이지" 같은 곳에서 내가 쓴 글 목록을 불러올 때 아주 유용합니다.
* **쿼리 메서드의 마법 (`Containing`, `OrderByIdDesc`)**:
    * **`Containing`**: SQL의 `LIKE %...%`와 같습니다. 검색 기능을 만들 때 씁니다.
    * **`OrderByIdDesc`**: ID(PK)를 기준으로 역순(최신순) 정렬하라는 뜻입니다. 게시판은 보통 최신글이 위에 오니까요.

---

### 3. 실무에서 사용하는 모습 (예시)

서비스 계층에서 게시글을 저장할 때, 아까 만든 **연관 관계 편의 메서드**와 리포지토리를 이렇게 조합합니다.

```java
@Transactional
public Long write(Long memberId, String title, String content) {
    // 1. 작성자(Member) 조회
    Member member = memberRepository.findById(memberId).get();
    
    // 2. 게시글 생성
    Board board = new Board(title, content);
    
    // 3. ★중요: 연관 관계 편의 메서드로 양방향 연결
    board.setMember(member); 
    
    // 4. 저장
    boardRepository.save(board);
    
    return board.getId();
}
```



---

### 💡 한 걸음 더: "게시글 조회 성능"

`Board` 엔티티에 `@ManyToOne(fetch = FetchType.LAZY)`라고 적어주셨던 거 기억하시나요? 
* `boardRepository.findById()`로 글을 가져오면 처음에는 `Member` 정보를 가져오지 않습니다.
* 그러다가 `board.getMember().getName()`처럼 실제 회원 정보가 필요한 순간에 JPA가 DB에 한 번 더 물어봅니다.

이게 바로 유저님이 설계하신 **지연 로딩**의 동작 방식입니다. 성능 최적화를 아주 잘 고려하신 설계예요!

이제 `BoardRepository`도 준비되었으니, 게시글 작성과 조회를 담당할 **`BoardService`**를 만들어볼까요? 아니면 게시글 검색 쿼리에 대해 더 궁금한 게 있으신가요?