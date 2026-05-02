## Json.BooleanKey
- Jackson.Class.`isWriter`ㆍ`writer`
- Jackson.Class.`writer`
- Jackson.Class.`isWriter→writer`
- Jackson.Class.`isWriter → writer`
- Jackson.Class.✅`isWriter → writer`
- Jackson.Class.`isWriter`(컨트롤러)ㆍ`writer`(프론트)
- Jackson.Record.`isWriter`
- Jackson.Record.❌`isWriter → writer`
- Jackson.Record.`isWriter`ㆍ`isWriter`
- Jackson.Record.`isWriter`(컨트롤러)ㆍ`isWriter`(프론트)


##
그 질문, 정말 많은 개발자가 겪는 **Jackson(Spring의 JSON 변환 라이브러리)의 유서 깊은 'Boolean 네이밍 룰'** 때문입니다.

전에는 `writer`로 나갔는데 지금은 `isWriter`로 나가는 이유, 그리고 왜 달라졌는지 명확하게 짚어드릴게요. 결론부터 말씀드리면 **"일반 클래스이냐, Record이냐"**의 차이 때문입니다.

---

### 1. 일반 클래스(Class)일 때 (과거 방식)
일반 클래스에 `private boolean isWriter`라고 만들고 `@Getter`를 붙이면, 롬복(Lombok)은 `getIsWriter()`가 아니라 **`isWriter()`**라는 이름의 메서드를 만듭니다.

Jackson 라이브러리는 JSON을 만들 때 `get`이나 `is`를 떼고 이름을 정하는 규칙이 있습니다.
* `isWriter()` 메서드에서 `is`를 뗌 → **`writer`**라는 키로 JSON 생성.
* 그래서 전에는 프론트에서 `board.writer`로 받을 수 있었던 것입니다.

### 2. 레코드(Record)일 때 (현재 방식)
하지만 지금은 **`record`**를 사용하고 계시죠? Record는 설계 철학 자체가 다릅니다.

* **메서드 이름**: Record는 `is`나 `get`을 붙이지 않고, **필드 이름 그대로** 메서드를 만듭니다.
* 즉, `isWriter` 필드가 있으면 접근 메서드 이름도 그냥 **`isWriter()`**입니다.
* **Jackson의 동작**: Jackson이 Record를 변환할 때는 메서드 이름을 분석하는 게 아니라 **필드 이름 그 자체**를 가져다 씁니다.
* **결과**: 필드명이 `isWriter`니까 JSON 키값도 그대로 **`isWriter`**가 됩니다.

---

### 3. 해결 방법 (3가지 중 선택)

#### 방법 1: 프론트엔드 코드 수정 (가장 추천)
가장 깔끔한 방법입니다. 백엔드의 실제 데이터 구조에 맞게 프론트를 수정합니다.
```html
<UButton v-if="board.isWriter" ... />
```

#### 방법 2: 백엔드 필드명 변경
만약 꼭 `writer`라는 이름으로 보내고 싶다면, Record의 필드명에서 `is`를 뺍니다.
```java
public record BoardDetailResponse(
    Long id,
    // ...
    boolean writer // 이렇게 하면 JSON도 "writer": true 로 나갑니다.
)
```

#### 방법 3: `@JsonProperty` 사용
필드명은 유지하고 내보내는 이름만 바꾸고 싶을 때 사용합니다.
```java
public record BoardDetailResponse(
    Long id,
    @JsonProperty("writer") // JSON으로 나갈 때만 "writer"로 강제 지정
    boolean isWriter
)
```

---

### 💡 정리하자면
* **일반 클래스**: Getter 규칙(`is` 제거) 때문에 `isWriter` → `writer`가 됨.
* **Record**: 필드명 중심이기 때문에 `isWriter` → `**isWriter**`가 됨.

"전에는 됐는데 왜 지금은 안 되지?"의 범인은 바로 **Record 도입으로 인한 메서드 명명 규칙의 변화**였습니다. 자바 버전이 올라가고 최신 문법을 쓰면서 발생하는 아주 자연스러운 현상이니, 당황하지 마시고 프론트엔드 변수명만 살짝 맞춰주시면 됩니다!