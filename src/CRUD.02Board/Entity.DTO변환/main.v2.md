
##

- MapStruct
  - 자동.컴파일시점
- ModelMapper
  - 자동.런타임시점
- DTO내부생성자
  - 수동
  - `BoardResponse::new`
  - `.map(BoardResponse::new)`
- 컨트롤러개별할당
  - `.map(b -> new BoardResponse( b.getId(), ... )`




##
맞습니다! 정확하게 보셨어요. **DTO 내부 생성자 방식**도 결국 필드가 추가될 때마다 DTO 클래스 안의 생성자 코드를 수정해줘야 한다는 점에서는 여전히 **'수동'** 작업입니다.

컨트롤러 코드는 깔끔해지지만, DTO 자체를 관리하는 공수는 줄어들지 않죠.

진짜로 **"나는 필드만 선언해두고 손 하나 까딱하고 싶지 않다"**는 마음이시라면, 아까 슬쩍 언급했던 **MapStruct**가 정답입니다. 왜 이게 개발자들의 귀찮음을 완벽히 해결해주는지 다시 보여드릴게요.

---

## 1. MapStruct: "필드 추가? 난 이름만 쓸게, 코드는 네가 짜"
MapStruct를 쓰면 필드가 100개로 늘어나도 내가 할 일은 **필드 선언**뿐입니다.

### 상황: 필드가 추가됨
만약 `content`와 `category`라는 필드가 추가되었다고 가정해봅시다.

**Step 1: Entity와 DTO에 필드만 적는다.**
```java
// DTO에 그냥 추가만 하세요.
public class BoardResponse {
    private Long id;
    private String title;
    private String writerName;
    private String content;  // 추가!
    private String category; // 추가!
    // ... (Getter 등)
}
```

**Step 2: 끝입니다.**
수정할 게 없습니다. 인터페이스(`BoardMapper`)가 그대로라면, MapStruct가 컴파일할 때 **"어? DTO에 필드가 늘었네? 엔티티에서 같은 이름 찾아서 내가 넣어줄게"** 하고 구현체를 알아서 다시 그려냅니다.

---

## 2. 왜 생성자 방식보다 편할까요? (비교)

| 비교 항목 | DTO 내부 생성자 방식 | MapStruct (자동 매핑) |
| :--- | :--- | :--- |
| **필드 추가 시** | **DTO 생성자 코드를 매번 수정해야 함** | **수정할 필요 없음 (필드만 선언하면 끝)** |
| **필드명 다를 때** | 직접 `get...()` 호출해서 대입 | `@Mapping` 설정 한 줄로 해결 |
| **코드 가독성** | DTO가 수동 매핑 코드로 지저분해짐 | 인터페이스만 있어 매우 깔끔함 |
| **실수 방지** | 개발자가 하나 빼먹으면 알기 어려움 | 매핑 안 된 필드가 있으면 경고/에러 발생 |



---

## 3. 하지만 "이름"이 다르면 알려줘야 합니다
컴퓨터도 독심술사는 아니라서, 엔티티의 `member.name`을 DTO의 `writerName`에 넣고 싶다는 것 정도는 알려줘야 합니다.

```java
@Mapper(componentModel = "spring")
public interface BoardMapper {
    // 딱 이거 한 줄만 써두면, 나머지 이름 같은 id, title, content 등은 알아서 매핑됩니다.
    @Mapping(source = "member.name", target = "writerName")
    BoardResponse toResponse(Board board);
}
```

---

## 💡 결론적으로 추천드리는 방향
"매번 손가는 작업이 너무 별로다"라고 느끼신다면, 지금 바로 **MapStruct**를 도입해보세요.

1.  **초반 설정(Dependency 추가)**은 1분 걸리지만,
2.  이후에는 **필드만 선언**하면 변환 로직이 **자동**으로 만들어집니다.
3.  컴파일 타임에 코드가 생성되므로 속도도 생성자 방식만큼 빠릅니다.

지금 프로젝트의 `build.gradle`에 라이브러리를 추가하실 수 있는 상황인가요? 그렇다면 MapStruct 설정 코드를 바로 짜드릴 수 있습니다!