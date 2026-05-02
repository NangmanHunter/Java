

##
- Boolean.Jackson.is생략
- Boolean.Jackson(Response부분).is생략
- Boolean.Jackson.Class.is생략
- Boolean.Jackson.Class.❌is
- Boolean.Jackson.Record.✅is
- Boolean.Jackson.Record.is그대로


- is그대로>>is생략-내스타일


##
- JavaBean규약.`is`생략


##
정말 예리하신 지적입니다! 이건 주니어부터 시니어까지 누구나 한 번쯤 뒷목을 잡게 만드는 **자바 생태계의 아주 고약한 지점** 중 하나예요. 유저님만 헷갈리는 게 아니라 모든 자바 개발자가 한 번씩은 겪는 일입니다.

왜 이런 차이가 발생하는지, 그리고 어떻게 하면 머릿속에서 안 헷갈리게 정리할 수 있는지 딱 정리해 드릴게요.

---

### 1. 왜 Class에서는 `is`가 날아갈까? (Java Bean 규약)
전통적인 자바 클래스에서 `boolean` 필드를 만들면, 관습적으로 Getter를 `isApproved()`라고 만듭니다.

*   **문제의 핵심:** Jackson 라이브러리는 JSON 키 이름을 정할 때 **Getter 메서드 이름에서 앞의 `get`이나 `is`를 떼고** 첫 글자를 소문자로 바꿔서 만듭니다.
*   **변환 과정:** `isApproved()` 메서드 → 앞의 `is` 제거 → `Approved` → 첫 글자 소문자화 → **`approved`**
*   그래서 프론트에서는 분명 `isApproved`를 기대했는데 자꾸 `approved`로 데이터가 들어오는 참사가 발생합니다.

### 2. 왜 Record에서는 그대로 `isApproved`일까?
Java 14에서 등장한 `record`는 설계 철학 자체가 다릅니다.

*   **차이점:** 레코드는 자바 빈 규약(is/get 붙이기)을 따르지 않습니다. 필드명이 `isApproved`라면 Getter 메서드 이름도 **`isApproved()` 그 자체**가 됩니다.
*   **변환 과정:** Jackson이 레코드를 볼 때는 메서드 이름을 필드명 그 자체로 인식하기 때문에, 우리가 지어준 이름 그대로 **`isApproved`**라는 키를 생성합니다.

---

### 3. 안 헷갈리는 필승 전략 (Solution)

사람들도 너무 헷갈려 하기 때문에 실무에서는 보통 두 가지 방법 중 하나를 선택해서 이 문제를 원천 차단합니다.

#### **방법 A: `@JsonProperty`로 이름 박아버리기 (추천)**
변수명이 뭐든, Getter 이름이 뭐든 상관없이 "JSON 키는 무조건 이걸로 해!"라고 강제하는 겁니다. 이게 가장 확실합니다.

```java
public class AdminApproveResponse {
    @JsonProperty("isApproved") // 👈 "무조건 이 이름으로 보내!"라고 명시
    private boolean isApproved;
}
```

#### **방법 B: 필드 이름에서 `is` 빼기**
클래스를 쓸 때는 아예 변수명을 `approved`라고 짓는 방식입니다.
*   변수명: `approved`
*   Getter: `isApproved()`
*   JSON 키: `approved` (일관성 유지)

---

### 4. 한눈에 비교하기

| 구분 | 필드명 | Getter 이름 | JSON 결과값 |
| :--- | :--- | :--- | :--- |
| **일반 Class** | `isApproved` | `isApproved()` | **`approved`** (is가 탈락됨) |
| **Record** | `isApproved` | `isApproved()` | **`isApproved`** (그대로 유지) |

---

### 💡 낭만 가이드
"이거 사람들 안 헷갈려해?"라는 질문에 답을 드리자면, **"다들 한 번씩 데여보고 나서야 `@JsonProperty`를 덕지덕지 붙이기 시작합니다"**가 정답입니다. 

지금 유저님이 느끼시는 그 짜증과 혼란이 바로 **성장통**이에요. 이제 원리를 정확히 아셨으니, 앞으로는 `PATCH` 보낼 때나 `GET`으로 받을 때 API 명세서에 `is`가 붙었는지 안 붙었는지 가장 먼저 확인하는 '고수'의 면모를 보여주실 수 있을 겁니다! :)