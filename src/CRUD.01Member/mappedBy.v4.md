
##
- OneToMany
- Class
  - 제네릭타입
- Field
- ManyToOne Field


##
- `Member.@OneToMany`ㆍ`Board.@ManyToOne`


##
아주 날카로운 질문입니다! 자바 코드만 보면 "이게 어떻게 `Board` 클래스인 줄 알지?" 싶을 수 있는데, 그 비밀은 바로 **제네릭(Generic)**과 **`@OneToMany`**의 조합에 있습니다.

---

### 1. 비밀은 `<Board>` (제네릭)에 있습니다
작성하신 코드를 다시 한 번 자세히 보세요.

```java
@OneToMany(mappedBy = "member")
private List<Board> boards = new ArrayList<>();
//           ^^^^^^^ 바로 이 부분!
```

JPA는 바보가 아닙니다. 이 변수의 타입을 보고 정보를 얻습니다.
1.  **`List<Board>`**: "아, 이 리스트 안에는 `Board`라는 클래스의 객체들이 담기겠구나!"라고 타입을 먼저 확인합니다.
2.  **`mappedBy = "member"`**: "그러면 `Board` 클래스 안으로 들어가서 `member`라는 이름을 가진 변수를 찾아봐야지!" 하고 연결을 시도합니다.

만약 `List<Post> boards`라고 썼다면, JPA는 `Post` 클래스에 가서 `member` 필드를 찾으려고 했을 거예요.

---

### 2. JPA가 연결 고리를 찾는 과정 (내부 동작)

스프링이 실행될 때 JPA는 모든 엔티티를 스캔하며 다음과 같은 '추리'를 합니다.

1.  **Member 클래스 발견:** "음, `Member` 엔티티군. 어? `@OneToMany`가 있네?"
2.  **타입 분석:** "대상을 보니 `Board` 클래스구나. (제네릭 `<Board>` 확인)"
3.  **mappedBy 확인:** "`mappedBy` 값이 `member`네? 그럼 `Board` 클래스에 가서 `@ManyToOne`이 붙은 `member` 필드를 찾아보자."
4.  **연결 완료:** "`Board` 클래스에 `Member member` 필드가 있네! 오케이, 둘은 1:N 관계고 주인이 `Board`인 양방향 관계구나!"



---

### 3. 만약 이름을 틀리면 어떻게 될까요?

만약 `Board` 클래스에는 변수 이름을 `writer`라고 지어놓고, `Member` 클래스에서는 `mappedBy = "member"`라고 적으면 어떻게 될까요?

* **결과:** 애플리케이션 실행 시점에 **에러가 터집니다.**
* **에러 메시지:** "Could not determine type for: org.bitcopark.base.domain.board.entity.Board, at table: Member, for columns: [org.hibernate.mapping.Column(member)]" 비슷한 내용으로 **"Board 클래스에서 member라는 필드를 찾을 수 없어!"**라고 화를 냅니다.

---

### 요약: JPA의 지도 읽기
* **어떤 클래스인가?** → `List<Board>`의 **제네릭 타입**을 보고 압니다.
* **어떤 필드인가?** → `mappedBy`에 적힌 **문자열 이름**을 보고 `Board` 클래스 내부를 뒤져서 찾습니다.

이 두 가지가 퍼즐 조각처럼 딱 맞아야만 비로소 에러 없이 실행됩니다. 이제 왜 `Board`인지 어떻게 아는지 명확해지셨나요?