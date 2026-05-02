## Getter.Boolean
- `boolean.isㅇㅇ`
- `boolean.isㅇㅇ()`



##
그건 자바의 **Bean 규약(Getter/Setter 규칙)** 때문입니다. 

보통 `String`이나 `Long` 같은 타입은 앞에 `get`을 붙이지만, **`boolean` 타입은 `get` 대신 `is`를 붙여서 Getter를 만드는 것이 표준**입니다.

### 1. 원인 분석
유저님이 `Member` 엔티티에서 필드 이름을 `private boolean isApproved;`라고 지으셨다면, 자바(특히 롬복이나 자동 생성 기능)는 다음과 같이 메서드를 만듭니다.

*   **필드명:** `isApproved`
*   **표준 Getter:** `isApproved()` (⭕️)
*   **잘못된 호출:** `getIsApproved()` (❌)

즉, 필드 이름 자체가 `is`로 시작하는 `boolean`일 경우, `get`을 또 붙이지 않고 필드명 그대로를 메서드 이름으로 사용하게 됩니다.

---

### 2. 해결 방법

#### **방법 A: 메서드 이름을 수정하기**
가장 간단한 방법입니다. `get`을 떼고 `isApproved()`를 호출하세요.

```java
public AdminApproveResponse(Member member) {
    this.id = member.getId();
    this.loginId = member.getLoginId();
    // getIsApproved() 대신 isApproved() 사용
    this.isApproved = member.isApproved(); 
}
```

#### **방법 B: 엔티티의 Getter 확인 (Lombok)**
만약 `Member` 엔티티에 `@Getter`를 사용 중이시라면, 롬복이 어떻게 메서드를 만들었는지 확인해 보세요.
*   필드가 `boolean approved;` 이면 → `isApproved()` 생성
*   필드가 `boolean isApproved;` 이면 → `isApproved()` 생성 (중복 `is` 방지)

---

### 3. 팁: `Boolean` (대문자) vs `boolean` (소문자)
혹시 엔티티에서 원시 타입인 `boolean`이 아니라 래퍼 클래스인 **`Boolean`**을 사용하셨나요?

*   **`boolean` (소문자)**: Getter가 `isApproved()`로 생성됨.
*   **`Boolean` (대문자)**: Getter가 `getIsApproved()`로 생성됨. (객체 취급을 하기 때문)

만약 엔티티 필드가 `Boolean`(대문자)인데도 빨간 줄이 난다면, 롬복이 설치되지 않았거나 인식이 안 된 것일 수 있습니다. 하지만 대부분은 **소문자 `boolean`을 쓰면서 관습적으로 `get`을 붙여서 발생하는 문제**입니다.

### 💡 결론
`member.isApproved()`로 바꿔보세요! 바로 빨간 줄이 사라지는 쾌감을 느끼실 수 있을 겁니다. :)