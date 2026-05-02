##
- Enum넘기기ㄱㄱ.
- 어차피 알아서 Json 문자열 이런걸로바꿔준다ㄱㄱ.


##
DTO인 **`BoardResponse`**에서 `BoardType`을 어떻게 다룰지는 프론트엔드가 이 데이터를 어떻게 사용할지에 달려 있습니다. 결론부터 말씀드리면, **`BoardType` (Enum) 그대로 넘겨주는 것을 추천**합니다.

하지만 화면에 "공지사항" 같은 한글 이름을 보여줘야 하므로, 두 가지 데이터를 모두 제공하는 방식이 가장 친절합니다.

---

### 1. 추천하는 구성: Enum 그대로 + 한글 명칭 추가
프론트엔드에서는 `NOTICE`라는 코드로 로직(예: 아이콘 변경, 색상 변경)을 짜고, `공지사항`이라는 텍스트로 화면에 뿌려주는 경우가 많기 때문입니다.

```java
public record BoardResponse(
        Long id,
        BoardType boardType,      // 로직용 (NOTICE, COMMUNITY)
        String boardTypeName,   // 화면 표시용 (공지사항, 자유게시판)
        String title,
        String writerName,
        long viewCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public BoardResponse(Board board) {
        this(
                board.getId(),
                board.getBoardType(), // Enum 객체 그대로 전달
                board.getBoardType().getDescription(), // "공지사항" 문자열 전달
                board.getTitle(),
                board.getMember().getName(),
                board.getViewCount(),
                board.getCreatedAt(),
                board.getUpdatedAt()
        );
    }
}
```

---

### 2. 왜 `String`이 아니라 `BoardType`인가요?

* **JSON 변환:** 스프링부트는 기본적으로 Enum을 JSON으로 바꿀 때 이름을 문자열(`"NOTICE"`)로 변환해서 내보냅니다. 즉, 받는 쪽(프론트)에서는 어차피 문자열로 받게 됩니다.
* **타입 안전성:** 나중에 이 DTO를 다른 자바 레이어에서 재사용할 때, `String`이면 무슨 값이 들어있는지 모르지만 `BoardType`이면 명확하게 게시판 유형임을 알 수 있습니다.
* **프론트엔드 대응:** 프론트엔드(Vue/Nuxt)에서도 `if (data.boardType === 'NOTICE')` 처럼 코드를 짜는 게 `data.boardType === '공지사항'` 보다 훨씬 안전합니다. (기획이 바뀌어 한글 이름이 "전체공지"로 바뀌어도 프론트 로직을 수정할 필요가 없으니까요.)

---

### 3. 만약 하나만 선택해야 한다면?

만약 필드를 늘리기 싫고 딱 하나만 보내야 한다면, 역시 **`BoardType`**입니다.

```java
public record BoardResponse(
        Long id,
        BoardType boardType, // JSON 출력 시 "NOTICE"
        String title,
        // ... 생략
)
```
이렇게만 보내도 프론트엔드에서 `NOTICE`를 받아서 자기들이 가진 매핑 테이블(Dictionary)을 통해 "공지사항"으로 바꿔 출력할 수 있습니다. 

하지만 백엔드에서 `description`("공지사항")까지 같이 내려주면 프론트엔드 개발자가 할 일이 줄어들어서 아주 좋아할 거예요.

---

### 💡 팁: Jackson 설정
만약 JSON 결과값에 Enum의 이름(`NOTICE`) 대신 한글 설명(`공지사항`)이 바로 나가게 하고 싶다면, Enum 클래스의 `getDescription` 메서드 위에 `@JsonValue`를 붙이면 됩니다. 하지만 보통은 로직용(ID)과 표시용(Name)을 분리해서 위 1번 방식처럼 내려주는 게 가장 정석입니다.

이 구조로 `BoardResponse`를 수정해 보시겠어요? DTO까지 수정하면 이제 목록 조회 기능이 완벽하게 마무리될 것 같네요!