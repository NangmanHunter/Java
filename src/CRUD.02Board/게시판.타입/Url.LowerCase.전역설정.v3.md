
##
- `@PathVariable`ㆍ`@RequestParam` 여기에적용



##
네, 정확하게 거기에 적용되는 것이 맞습니다!

지금 추가하신 코드는 스프링이 **"URL 경로(`@PathVariable`)나 쿼리 파라미터(`@RequestParam`)로 들어오는 문자열을 어떻게 내가 원하는 객체(Enum 등)로 바꿀 것인가?"**에 대한 규칙을 정의한 것입니다.

이 코드가 작동하는 과정을 아주 쉽게 풀어드릴게요.

---

### 1. 작동 순서 (Flow)

1.  **프론트엔드 요청:** `POST /api/boards/notice` (소문자 `notice`로 요청을 보냄)
2.  **스프링의 탐색:** 컨트롤러의 `@PathVariable BoardType boardType`을 보고, "어? 문자열 `notice`를 `BoardType`이라는 Enum으로 바꿔야 하네?"라고 인지합니다.
3.  **컨버터 작동:** 이때 아까 등록하신 `WebConfig`의 **Converter**가 출동합니다.
    * `source`: `"notice"` (소문자 문자열)
    * `source.toUpperCase()`: `"NOTICE"` (대문자로 변환)
    * `BoardType.valueOf("NOTICE")`: Enum 상수를 찾아냄
4.  **컨트롤러 전달:** 성공적으로 변환된 `BoardType.NOTICE` 객체가 컨트롤러의 인자값으로 쏙 들어갑니다.



---

### 2. 왜 이 방식이 좋은가요?

* **타입 안전성:** 컨트롤러 내부에서는 `String`이 아니라 이미 검증된 `BoardType` Enum 객체를 바로 사용하기 때문에 오타 걱정이 없습니다.
* **중복 제거:** 만약 이 설정이 없다면, 모든 컨트롤러 메서드마다 `String type`으로 받은 뒤 수동으로 `type.toUpperCase()`를 해줘야 했을 겁니다. 이제는 그럴 필요가 없죠.
* **유연성:** 프론트엔드에서 `notice`, `NOTICE`, 심지어 `nOtIcE`라고 보내도 백엔드는 찰떡같이 대문자로 바꿔서 인식합니다.

---

### 3. @RequestBody와는 다른가요?

중요한 차이점이 하나 있습니다.
* **지금 추가한 컨버터:** URL 경로(`@PathVariable`)나 쿼리 스트링에 적용됩니다.
* **@RequestBody:** 이건 JSON 데이터를 객체로 바꾸는 과정이라 **Jackson**이라는 라이브러리가 담당합니다. 만약 JSON 본문에 담긴 Enum도 소문자로 받고 싶다면 아까 말씀드린 `@JsonCreator`를 Enum 클래스 안에 넣으셔야 합니다.

**하지만 지금처럼 경로 변수(`/api/boards/notice`)로 쓰실 때는 이 `addConverter` 설정이 정답입니다!**

이제 프론트엔드에서 소문자로 API를 호출해 보세요. 아까 났던 400 에러 대신 **201 Created**가 뜨면서 DB에 데이터가 예쁘게 들어갈 겁니다. :) 또 막히는 부분 있으신가요?