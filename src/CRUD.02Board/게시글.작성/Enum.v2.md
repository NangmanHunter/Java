##
- fetch대문자보내기 vs. fetch소문자보내기



##
원인을 정확히 찾으셨네요! Spring Boot의 기본 설정에서 **Enum은 대문자와 소문자를 엄격하게 구분**합니다.

클라이언트(Nuxt)에서 `/notice`(소문자)로 보냈는데, 서버의 Enum이 `NOTICE`(대문자)라면 데이터 바인딩에 실패하여 **400 Bad Request**가 발생합니다.

이 문제를 해결하는 가장 깔끔한 방법 3가지를 알려드릴게요. 상황에 맞는 것을 선택하세요.

---

### 방법 1: `@PathVariable`에서 직접 대문자로 변환 (가장 간단)
컨트롤러로 들어오는 값을 일단 `String`으로 받은 뒤, 내부에서 Enum으로 변환하는 방법입니다.

```java
@PostMapping("/{boardType}")
public ResponseEntity<?> write(
        @PathVariable String boardType, // String으로 받기
        @RequestBody BoardCreateRequest request,
        @AuthenticationPrincipal MemberAdapter loginMemberAdapter) {

    // 소문자로 들어온 값을 대문자로 바꿔서 Enum으로 변환
    BoardType type = BoardType.valueOf(boardType.toUpperCase());

    long boardId = boardService.write(
            loginMemberAdapter.getMember().getId(),
            type,
            request.getTitle(),
            request.getContent());
    
    // ... 이하 동일
}
```

### 방법 2: Enum에 `@JsonCreator` 설정 (추천)
Enum 클래스 안에 어떤 문자열이 들어와도 처리할 수 있는 로직을 넣는 방식입니다. 이 방식을 쓰면 컨트롤러 코드를 수정할 필요가 없어 깔끔합니다.

```java
public enum BoardType {
    NOTICE, FREE, QNA;

    @JsonCreator
    public static BoardType from(String value) {
        for (BoardType type : BoardType.values()) {
            if (type.name().equalsIgnoreCase(value)) { // 대소문자 무시 비교
                return type;
            }
        }
        return null;
    }
}
```

### 방법 3: 프런트엔드(Nuxt)에서 대문자로 보내기
가장 원시적이지만 확실한 방법입니다. 호출하는 주소 자체를 대문자로 바꿉니다.

```javascript
// Nuxt 3 코드 수정
const data = await $fetch('/api/boards/NOTICE', { // 소문자 notice 대신 대문자
  baseURL: config.public.apiBase,
  method: 'POST',
  // ...
})
```

---

### 💡 추천하는 방식
**방법 2**를 가장 추천합니다. 왜냐하면 나중에 다른 곳에서 `BoardType`을 사용할 때도 대소문자 문제로부터 자유로워지기 때문입니다.

만약 **방법 2**를 적용하기 어렵다면, **방법 1**처럼 컨트롤러에서 `String`으로 받아서 `.toUpperCase()` 처리를 해주는 것이 가장 빠르게 문제를 해결하는 길입니다!

**추가 팁:** 400 에러가 사라진 뒤에 혹시나 데이터가 제대로 안 들어온다면, `BoardCreateRequest` 클래스에 **Getter**와 **기본 생성자**가 있는지 꼭 다시 한번 확인해 보세요! 가끔 `Lombok`의 `@Getter`가 누락되어 에러가 나는 경우도 많습니다.