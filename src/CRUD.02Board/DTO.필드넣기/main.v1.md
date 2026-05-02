##
- Entity.✅상속>>❌개별
- 엔티티.✅상속>>❌개별
- DTO.❌상속>>✅개별
- 디티오.❌상속>>✅개별


##
엔티티에서 `BaseTimeEntity`를 상속받아 공통 필드를 관리하는 방식은 아주 깔끔하고 정석적인 접근입니다. 하지만 **DTO에서도 상속을 사용할 것인가**에 대해서는 실무적으로 고려해야 할 포인트가 몇 가지 있습니다.

결론부터 말씀드리면, DTO는 **상속보다는 필드를 직접 넣거나 필요한 정보만 포함하는 방식**을 더 권장합니다. 그 이유와 효율적인 구성 방안을 정리해 드릴게요.

---

## 1. DTO에서 상속을 지양하는 이유

엔티티는 데이터베이스 설계(공통 컬럼) 관점에서 상속이 유리하지만, DTO는 **화면에 보여줄 데이터**에 집중해야 합니다.

* **유연성 부족:** 게시판 목록에서는 `createdAt`이 필요하지만, 게시글 수정 폼(Request DTO)에서는 `createdAt`이 필요 없을 수 있습니다. 상속을 쓰면 불필요한 필드까지 따라오게 됩니다.
* **복잡도 증가:** 프로젝트가 커지면 `BoardResponse`, `MemberResponse` 등 수많은 DTO가 생기는데, 이를 모두 상속 구조로 묶으면 코드를 파악할 때 부모 클래스까지 계속 왔다 갔다 하며 확인해야 하는 번거로움이 생깁니다.
* **API 스펙의 고정:** DTO는 API의 얼굴입니다. 상속 구조를 바꾸면 의도치 않게 여러 API의 응답 규격이 한꺼번에 바뀌는 위험이 있습니다.

---

## 2. 권장하는 구성 방식

`BoardResponse`에는 필요한 필드만 명시적으로 넣는 것이 가장 명확합니다. 날짜 데이터가 공통적으로 자주 쓰인다면 아래와 같이 구성해 보세요.

### 방법 A: 필요한 필드만 직접 추가 (가장 추천)
가장 단순하고 가독성이 좋습니다.

```java
@Getter
@AllArgsConstructor
public class BoardResponse {
    private Long id;
    private String title;
    private String writerName;
    private long viewCount;
    
    // 필요한 날짜 정보만 명시적으로 포함
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

### 방법 B: 응답용 공통 상속 클래스 (제한적 사용)
모든 Response DTO가 반드시 생성/수정일자를 포함해야 한다는 규칙이 있다면, `BaseResponse` 같은 이름을 만들어 상속할 수도 있습니다. 하지만 이 역시 나중에 "특정 API에서는 날짜를 빼주세요"라는 요구사항이 나오면 난처해질 수 있습니다.

---

## 3. 실무적인 팁: 정적 팩토리 메서드 사용

DTO 내부에서 엔티티를 DTO로 변환하는 로직을 관리하면 서비스 레이어가 훨씬 깔끔해집니다.

```java
@Getter
@Builder
public class BoardResponse {
    private Long id;
    private String title;
    private String writerName;
    private LocalDateTime createdAt;

    // Entity -> DTO 변환 로직을 DTO 안에 위치시킴
    public static BoardResponse from(Board board) {
        return BoardResponse.builder()
            .id(board.getId())
            .title(board.getTitle())
            .writerName(board.getWriter().getName()) // 연관관계 처리
            .createdAt(board.getCreatedAt()) // BaseTimeEntity 필드 사용
            .build();
    }
}
```

## 요약하자면

1.  **엔티티**는 중복 제거와 DB 관리를 위해 `BaseTimeEntity` 상속이 매우 좋습니다.
2.  **DTO**는 API마다 요구사항이 다르므로 **상속보다는 필드를 직접 선언**하여 독립성을 유지하세요.
3.  날짜 필드는 화면(UI) 요구사항에 따라 `createdAt` 정도만 포함시키는 경우가 가장 많습니다.

지금 작성하신 `BoardResponse`에 `private LocalDateTime createdAt;` 하나만 딱 추가해서 사용하시는 걸 추천드려요! `deletedAt` 같은 정보는 보통 API 응답에는 포함하지 않으니까요.