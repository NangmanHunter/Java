##
- Enum.대문자.여기서 삑사리.
- Url소문자.Enum대문자.삑사리
- Url소문자≠Enum대문자.삑사리
- `Url소문자≠Enum대문자`.삑사리


##
컨트롤러 코드를 보니 400 에러(Bad Request)가 발생할 수 있는 **결정적인 후보**가 몇 가지 보입니다. Spring Boot가 `@RequestBody`나 `@PathVariable`에서 데이터를 바인딩할 때 하나라도 어긋나면 바로 400을 던지거든요.

가장 의심되는 부분들을 순서대로 체크해보세요.

---

### 1. `BoardType` 열거형(Enum) 바인딩 오류 (가장 유력)
현재 URL이 `/api/boards/notice`로 들어오고 있는데, 백엔드의 `BoardType`이라는 **Enum**에 `notice`라는 값이 정확히 있는지 확인해야 합니다.

* **대소문자 문제:** 만약 Enum 정의가 `NOTICE` (대문자)로 되어 있다면, 경로에 소문자 `notice`가 들어왔을 때 Spring은 이를 매칭하지 못하고 400 에러를 낼 수 있습니다.
* **해결책:** * Enum 값을 `notice` (소문자)로 수정하거나,
    * 컨트롤러 파라미터 앞에 `@PathVariable("boardType") String type`으로 받은 뒤 내부에서 직접 변환해 보세요.

### 2. `BoardCreateRequest` 필드명 불일치
프런트엔드 Nuxt 코드에서는 `title`, `content`라는 키로 보냈습니다.
```javascript
body: { title: title.value, content: content.value }
```
백엔드의 `BoardCreateRequest` 클래스 안에 필드명이 **정확히** `title`과 `content`인가요? 
* 필드명이 다르면(예: `boardTitle`, `boardContent`) `@RequestBody` 바인딩에 실패하여 400 에러가 발생합니다.
* 클래스에 **Getter**와 **기본 생성자**가 반드시 있어야 Jackson 라이브러리가 데이터를 넣어줄 수 있습니다.

### 3. `@AuthenticationPrincipal` (로그인 정보) 누락
만약 로그인이 되어있지 않은 상태이거나 세션이 만료되었다면, `MemberAdapter`가 `null`로 들어올 수 있습니다. 
* Spring Security 설정에 따라 인증되지 않은 요청이 이 컨트롤러에 도달하면 403(Forbidden)이 나기도 하지만, 객체 변환 과정에서 문제가 생기면 400으로 보일 때도 있습니다.
* **체크 방법:** 로그인이 정상적으로 되어 있는 상태에서 요청을 보냈는지 확인하세요. (`credentials: 'include'` 옵션은 잘 넣으셨네요!)

---

### 🔍 확실하게 확인하는 방법

브라우저의 **개발자 도구(F12) -> Network** 탭에서 빨간색으로 뜬 `notice` 요청을 클릭해 보세요.

1.  **Payload:** `{ title: "...", content: "..." }` 데이터가 잘 가고 있는지 확인.
2.  **Preview 또는 Response:** Spring Boot가 친절하게 에러 원인을 적어줬을 확률이 높습니다. 
    * `"JSON parse error: Cannot deserialize value of type ..."` -> 2번 문제
    * `"Method argument type mismatch"` -> 1번 문제

### 💡 추천하는 수정 팁
Enum 바인딩 문제를 피하기 위해 컨트롤러를 아래처럼 살짝 수정해서 테스트해보세요.

```java
@PostMapping("/{boardType}")
public ResponseEntity<?> write(
    @PathVariable String boardType, // 일단 String으로 받아보기
    @RequestBody BoardCreateRequest request,
    @AuthenticationPrincipal MemberAdapter loginMemberAdapter) {
    
    // 여기서 로그를 찍어보면 어디서 막히는지 바로 알 수 있습니다.
    System.out.println("Received Type: " + boardType);
    System.out.println("Request Data: " + request.getTitle());
    
    // ... 이하 동일
}
```

**질문:** 혹시 백엔드 `BoardCreateRequest` 클래스 소스도 볼 수 있을까요? 필드명과 어노테이션을 보면 더 정확히 알 수 있을 것 같아요!