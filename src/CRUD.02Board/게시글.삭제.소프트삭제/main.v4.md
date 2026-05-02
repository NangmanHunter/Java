현업에서는 데이터를 진짜로 지우는 것보다 '삭제되었다'고 표시만 하는 **Soft Delete(논리 삭제)** 방식을 훨씬 선호합니다. 나중에 실수로 지운 데이터를 복구하기도 쉽고, 데이터 간의 관계를 유지하기에도 유리하기 때문이죠.

Spring Data JPA를 사용하신다면, 가장 깔끔하고 "Nangman" 있는 엔티티 구성 방법을 알려드릴게요.

---

### 1. 엔티티(Entity) 구성 (추천 방식)

단순히 `boolean` 필드 하나만 추가하는 것이 아니라, JPA가 제공하는 어노테이션을 활용하면 **조회할 때마다 일일이 "삭제되지 않은 데이터만 가져와"라고 코드를 짤 필요가 없어집니다.**

```java
@Entity
@Getter
@NoArgsConstructor
// 💡 핵심 1: 조회 시 'is_deleted = false'인 데이터만 자동으로 필터링합니다.
@Where(clause = "is_deleted = false")
// 💡 핵심 2: repository.delete()를 호출하면 실제로 지우는 대신 UPDATE 쿼리를 날립니다.
@SQLDelete(sql = "UPDATE board SET is_deleted = true WHERE id = ?")
public class Board {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    
    @Lob
    private String content;

    // 💡 삭제 여부를 저장할 필드
    private boolean isDeleted = false;

    // ... 나머지 필드 (작성자, 시간 등)
}
```

---

### 2. 왜 `@Where`와 `@SQLDelete`를 쓰나요?

이 두 어노테이션을 사용하면 서비스 로직이 매우 단순해집니다.

1.  **조회(`@Where`):** `findAll()`이나 `findById()`를 할 때, **이미 삭제된 게시글은 마치 존재하지 않는 것처럼** 자동으로 제외됩니다. 따로 `where is_deleted = false` 쿼리를 짤 필요가 없습니다.
2.  **삭제(`@SQLDelete`):** 컨트롤러나 서비스에서 `boardRepository.delete(board)`를 호출하면, JPA가 실제 `DELETE` 쿼리 대신 우리가 미리 정의한 `UPDATE` 쿼리를 실행해줍니다.

---

### 3. 조금 더 고도화한다면? (삭제 시간 기록)

단순히 `boolean`만 남기기보다는, **언제 삭제되었는지**를 기록해두면 나중에 데이터 분석이나 복구 시 훨씬 유용합니다.

```java
public class Board {
    // ...
    private LocalDateTime deletedAt; // 삭제 시각 기록
    private boolean isDeleted = false;

    // 삭제 처리용 메서드 (필요 시 직접 호출)
    public void softDelete() {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }
}
```

---

### 🧐 주의할 점 (트레이드 오프)

* **`@Where`의 강제성:** 가끔 관리자 페이지처럼 "삭제된 글까지 포함해서 다 보고 싶다"는 요구사항이 생기면 `@Where`가 오히려 방해가 될 수 있습니다. (이럴 때는 네이티브 쿼리를 쓰거나, `@Where` 없이 서비스에서 필터링해야 합니다.)
* **유니크 제약 조건:** 만약 "제목은 중복될 수 없다" 같은 제약이 있다면, 논리적으로 삭제된 데이터가 DB에 남아있기 때문에 같은 제목의 새 글을 쓸 때 충돌이 날 수 있습니다.

**결론:** 처음 시작하실 때는 위에 적어드린 **`@SQLDelete`와 `@Where` 조합**으로 가시는 게 가장 코드가 간결해집니다. 이렇게 구성하면 서비스 코드는 기존 하드 삭제 방식과 똑같이 `repository.delete(id)` 한 줄로 유지할 수 있으니까요!

이제 DB에 데이터는 남으면서 화면에서는 싹 사라지는 마법을 경험해 보세요. :)