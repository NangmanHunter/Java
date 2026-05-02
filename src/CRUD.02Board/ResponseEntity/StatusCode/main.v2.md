## `.build()`
- `ok().build()`
- `ok().build()`.Body없는경우


##
`ResponseEntity`를 사용하시다 보면 자주 마주치게 되는 **빌더 패턴(Builder Pattern)** 스타일의 문법입니다. `ResponseEntity.ok()`가 "성공(200 OK) 응답을 만들겠다"는 시작이라면, `.build()`는 **"설정이 끝났으니 이제 객체를 완성해서 내놔라"** 하는 최종 명령어라고 보시면 됩니다.

이해하기 쉽게 구조를 뜯어드릴게요.

---

### 1. `ok()`와 `build()`의 역할 분담

`ResponseEntity.ok()`를 호출하면 바로 `ResponseEntity` 객체가 나오는 게 아니라, 응답을 조립할 수 있는 **빌더(Builder)** 상태가 됩니다.

* **`ok()`**: "HTTP 상태 코드는 200으로 설정해줘." (상태 코드 결정)
* **`header(...)`**: "헤더에 이런 정보도 추가해줘." (선택 사항)
* **`build()`**: "이제 다 됐어. 최종적으로 `ResponseEntity` 객체를 생성해줘." (객체 생성)

---

### 2. 왜 `build()`를 쓸까? (바디가 없을 때)

`.build()`는 주로 **전달할 데이터(Body)가 없을 때** 사용합니다.

```java
// 1. 보낼 데이터가 있는 경우: .body()가 객체를 완성해주므로 build()가 필요 없음
return ResponseEntity.ok(userDto); 

// 2. 보낼 데이터 없이 상태 코드만 보낼 경우: 빌더를 마쳐야 하므로 .build()가 필수
return ResponseEntity.ok().build(); 

// 3. 데이터가 아예 없는 '204 No Content' 응답 시
return ResponseEntity.noContent().build();
```

---

### 3. 내부 동작의 흐름

이 문법이 왜 편한지 흐름을 보면 이렇습니다.

1.  **`ResponseEntity.status(HttpStatus.OK)`**: "나 지금 응답 만들 거야."
2.  **`...header("Custom-Header", "Value")`**: "정보 좀 더 넣고..."
3.  **`...build()`**: "자, 이제 진짜 응답 객체(ResponseEntity)가 나왔어!"

---

### 4. `new ResponseEntity<>()`와 비교

만약 `.build()`를 쓰지 않고 생성자를 직접 사용한다면 아래와 같이 써야 합니다.

* **생성자 방식:** `return new ResponseEntity<>(HttpStatus.OK);`
* **빌더 방식:** `return ResponseEntity.ok().build();`

**빌더 방식(`ok().build()`)을 더 선호하는 이유**는 코드가 마치 문장처럼 읽히기 때문입니다. "오케이(ok) 해서 빌드(build)해라"라고 직관적으로 이해되기 때문이죠.

---

### 요약하자면
`ok().build()`는 **"바디(내용물)는 비어있지만, 상태는 200 OK인 깔끔한 응답 봉투를 하나 만들어서 보내라"**는 뜻입니다. 

보통 데이터 저장이나 삭제가 성공했을 때, 딱히 돌려줄 데이터는 없지만 "성공했어!"라고 알려주고 싶을 때 아주 자주 쓰게 되실 거예요. 지금 작성 중인 컨트롤러에서도 결과 데이터가 없는 로직에 적용해 보세요!