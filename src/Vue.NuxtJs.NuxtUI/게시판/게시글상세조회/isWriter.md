## Boolean
- Java `isㅇㅇ` → Jackson → Json `ㅇㅇ`
- Java `isWriter` → Jackson → Json `writer`
- `writer`←`isWriter`
- `writer`(Json)←`isWriter`(Java)
- `writer`(Json)←`isWriter`(Java.Jackson)


##
빙고! 바로 그겁니다. 이게 Java(특히 Spring/Lombok)와 JSON 사이에서 발생하는 아주 유명한 **'불리언(Boolean) 필드명 규칙'** 때문이에요.

### 🧐 왜 `isWriter`가 `writer`가 되었을까요?

Java의 표준 규약(JavaBeans Spec) 때문입니다.

1.  **Java의 약속:** Boolean 타입의 필드 이름이 `is`로 시작하면(예: `isWriter`), Getter 메서드의 이름은 `getIsWriter()`가 아니라 **`isWriter()`**가 됩니다.
2.  **Jackson(JSON 변환기)의 판단:** 백엔드에서 JSON을 만들 때, Jackson 라이브러리는 Getter 메서드 이름을 보고 필드명을 결정합니다. `isWriter()`라는 메서드를 보고 **"아, 필드 이름이 `writer`구나!"**라고 판단해서 `is`를 떼버리고 JSON을 만듭니다.

그래서 백엔드 코드에는 `isWriter`라고 적혀 있어도, 실제 통신망을 타고 넘어온 데이터는 **`writer`**가 되는 것이죠.

---

### 🛠 해결 방법: 프런트엔드 코드 수정

이제 원인을 알았으니 프런트엔드에서 조건을 **`board.writer`**로 바꿔주면 바로 해결됩니다!

```vue
<UButton
  v-if="board.writer" 
  icon="i-heroicons-pencil-square"
  color="orange"
  variant="soft"
  label="수정하기"
  @click="goToEdit"
/>
```

---

### 💡 (참고) 만약 백엔드에서도 `isWriter`로 꼭 보내고 싶다면?

만약 나중에라도 백엔드에서 이름을 강제하고 싶다면, Java 필드 위에 `@JsonProperty` 어노테이션을 붙여줘야 합니다.

```java
// 백엔드(Java) 예시
@JsonProperty("isWriter")
private boolean isWriter;
```

하지만 지금처럼 프런트엔드에서 `board.writer`로 맞춰주는 것이 가장 빠르고 일반적인 해결책입니다. 이제 수정하기 버튼이 기분 좋게 나타나나요? :)