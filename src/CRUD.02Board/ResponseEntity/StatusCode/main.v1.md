## ResponseEntity
- [x] 200
- [x] 201
- [ ] 202
- [ ] 203
- [x] 204


##
- 302 Found
- 302.Found
- 302.Found.Redirect

- 브라우저가 서버에 요청을 보냈을 때 서버가 302 응답을 주면, 브라우저는 즉시 응답 헤더에 담긴 Location이라는 주소로 새로운 요청을 자동으로 보냅니다.
- 서버: "302 주소줄게, Location: /login으로 가!"
- 브라우저: "알았어! (사용자 모르게) 바로 /login으로 다시 보낼게."




## 404
- ResponseEntity.status(HttpStatus.NOT_FOUND)


##
백엔드 개발을 하다 보면 누구나 한 번쯤 빠지는 깊은 고민이죠! **"그냥 데이터(DTO)만 보낼 것인가, 아니면 상태 코드까지 담은 봉투(ResponseEntity)에 넣어 보낼 것인가"**의 차이인데요.

결론부터 말씀드리면, **`ResponseEntity`를 사용하는 것이 현대적인 REST API 설계에서는 더 권장되는 방식**입니다. 왜 그런지 핵심 이유를 짚어드릴게요.

---

## 1. ResponseEntity vs DTO (POJO) 비교

| 구분 | DTO 반환 (POJO) | ResponseEntity 반환 |
| :--- | :--- | :--- |
| **제어권** | 스프링에 맡김 (기본 200 OK) | **개발자가 직접 제어** |
| **유연성** | 헤더나 상태 코드를 바꾸기 어려움 | 상황에 따라 201, 204, 400 등 자유로움 |
| **명시성** | 어떤 결과가 나가는지 코드로만 알기 어려움 | HTTP 응답 규격이 코드에 드러남 |
| **복잡도** | 매우 낮음 | 약간의 코드가 추가됨 |

---

## 2. 왜 ResponseEntity를 더 많이 쓸까?

### HTTP 상태 코드의 세밀한 조정
DTO만 던지면 스프링이 알아서 `200 OK`를 내보냅니다. 하지만 실무에서는 데이터가 잘 생성되었을 때 `201 Created`를 주거나, 삭제 후 보낼 데이터가 없을 때 `204 No Content`를 주는 것이 더 정확한 소통 방식입니다.

### 헤더(Header) 조작
파일 다운로드 처리를 하거나, 캐시 제어, 혹은 커스텀 헤더를 실어 보내야 할 때 `ResponseEntity`가 없으면 구현이 매우 까다로워집니다.

### 일관된 응답 구조
`ResponseEntity<T>`를 쓰면 어떤 데이터를 반환하든 응답의 "형식"을 유지하기 좋습니다.

---

## 3. 추천하는 구조: 공통 응답 포맷

사실 가장 세련된 방법은 **`ResponseEntity`와 `DTO`를 조합**하는 것입니다. 모든 응답을 담을 수 있는 `ApiResponse` 같은 공통 DTO를 만들고, 그걸 `ResponseEntity`로 감싸는 거죠.

```java
// 1. 공통 응답 DTO 예시
public class ApiResponse<T> {
    private String message;
    private T data;
}

// 2. 컨트롤러에서의 사용
@GetMapping("/{id}")
public ResponseEntity<ApiResponse<UserDto>> getUser(@PathVariable Long id) {
    UserDto user = userService.findById(id);
    
    return ResponseEntity
            .ok() // 200 OK
            .body(new ApiResponse<>("성공", user));
}
```

---

## 💡 저의 조언: 언제 무엇을 쓸까?

* **학습용/간단한 토이 프로젝트:** 생산성을 위해 그냥 **DTO**나 `@ResponseBody`를 쓰셔도 무방합니다.
* **실무/협업 프로젝트:** 프론트엔드 개발자와의 명확한 소통(상태 코드)을 위해 **`ResponseEntity`** 사용을 강력 추천합니다.

"상태 코드를 내가 직접 결정하겠다"는 의지가 있다면 `ResponseEntity`를, "그냥 데이터만 잘 나가면 장땡이다" 싶으면 DTO를 쓰게 되는데, 결국 서비스가 커지면 전자의 제어권이 반드시 필요해지더라고요.

지금 고민 중인 프로젝트의 성격은 어떤가요? 상황에 맞춰 선택하시는 데 도움을 드릴게요!