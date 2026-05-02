실무에서 **Soft Delete(논리 삭제)**를 사용하는 이유는 데이터가 "실수로 삭제"되는 것을 방지하고, 나중에 통계 분석이나 복구 목적으로 남겨두기 위해서입니다.

JPA에서 이를 구현할 때 매번 수정 쿼리를 직접 짜는 것은 번거롭기 때문에, 보통 **어노테이션 기반의 자동화** 방식을 많이 사용합니다.

---

### 1. Entity 설정 (@SQLDelete, @Where)

가장 편한 방법은 Hibernate가 제공하는 기능을 활용하는 것입니다. 엔티티에 직접 삭제 로직을 심어버리는 거죠.

```java
@Entity
@Getter
@SQLDelete(sql = "UPDATE board SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?") // (1)
@Where(clause = "deleted_at IS NULL") // (2)
public class Board extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;

    // Soft Delete 여부를 판단하는 필드
    private LocalDateTime deletedAt;
}
```

* **(1) `@SQLDelete`**: `repository.delete(board)`를 호출할 때, 실제 `DELETE` 쿼리 대신 우리가 정의한 `UPDATE` 쿼리가 실행됩니다.
* **(2) `@Where`**: 데이터를 조회(`findAll`, `findById` 등)할 때, 기본적으로 `deleted_at IS NULL` 조건이 붙은 데이터만 가져옵니다. 삭제된 데이터는 아예 안 보이게 처리하는 거죠.

---

### 2. Service 로직 구현

위 설정을 해두면 서비스 로직은 평소와 똑같습니다. DB에서 진짜 지워지는지 걱정할 필요가 없죠.

```java
@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public void deletePost(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        
        // 실제로는 DB에서 지워지지 않고 @SQLDelete에 의해 UPDATE 쿼리가 날아갑니다.
        boardRepository.delete(board);
    }
}
```

---

### 3. 직접 수동으로 처리하고 싶을 때

만약 어노테이션 방식이 너무 "마법" 같아서 명시적으로 코드를 짜고 싶다면, 서비스 단에서 필드만 바꿔주면 됩니다.

```java
public void softDelete(Long id) {
    Board board = boardRepository.findById(id).get();
    // 1. 엔티티 내부 메소드로 삭제 시간 기록
    board.markAsDeleted(); 
}

// Board 엔티티 내부
public void markAsDeleted() {
    this.deletedAt = LocalDateTime.now();
}
```
*이 방식은 조회를 할 때마다 `deletedAt이 null인 것만 찾아라`라는 조건을 Repository 쿼리에 직접 다 붙여줘야 하는 번거로움이 있습니다.*

---

### 💡 실무 운영 팁

1.  **복구 요청 시**: `deletedAt` 필드만 다시 `NULL`로 바꾸면 감쪽같이 복구됩니다.
2.  **데이터 무결성**: 유니크(Unique) 제약 조건이 걸린 필드가 있다면 주의해야 합니다. 삭제된 데이터가 여전히 DB에 남아있으므로, 같은 값으로 다시 가입/생성할 때 충돌이 날 수 있습니다.
3.  **성능 관리**: 삭제된 데이터가 너무 많아지면 인덱스 효율이 떨어질 수 있습니다. 일정 기간(예: 1년)이 지난 `deletedAt` 데이터는 별도의 백업 테이블로 옮기거나 진짜로 지우는 배치(Batch) 작업을 돌리기도 합니다.

`bitcopark-base` 프로젝트에 적용하신다면 **1번 방식(@SQLDelete)**을 가장 추천드립니다. 코드가 훨씬 간결해지니까요! 

삭제된 게시글도 관리자 페이지에서는 보이게 하고 싶은가요? 아니면 완전히 사용자 화면에서만 안 보이게 하면 될까요?