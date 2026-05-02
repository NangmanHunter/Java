## StaticImport
- Alt+Enter
  - IntelliJ
  - Alt+Enter


##
`assertNotNull`에 빨간 불이 들어온 이유는 **스태틱 임포트(Static Import)**가 빠져있기 때문입니다!

자바에서 `assertNotNull`이나 `assertTrue` 같은 메소드들은 특정 클래스(`Assertions`) 안에 정의된 **정적 메소드**입니다. 그래서 그냥 쓰면 자바가 "이게 어디서 온 명령이야?"라고 묻는 것이죠.

---

### 1. 해결 방법: 파일 맨 위에 임포트 추가

파일 최상단(package 선언 아래)에 다음 코드를 직접 복사해서 넣어주세요.

```java
import static org.junit.jupiter.api.Assertions.*;
```

* **`import static`**: 클래스 이름을 생략하고 메소드 이름만으로 바로 쓰겠다는 뜻입니다.
* **`.*`**: Assertions 클래스 안에 있는 모든 검증 메소드(`assertNotNull`, `assertEquals`, `assertTrue` 등)를 가져오겠다는 의미입니다.

---

### 2. 인텔리제이 단축키로 해결하기 (추천)

일일이 타이핑하기 귀찮다면 인텔리제이의 마법 같은 기능을 써보세요.

1.  빨간색으로 뜬 `assertNotNull` 글자에 커서를 둡니다.
2.  **`Alt + Enter`** (Windows) 또는 **`Option + Enter`** (Mac)를 누릅니다.
3.  메뉴에서 **`Add static import for 'org.junit.jupiter.api.Assertions.assertNotNull'`**를 선택합니다.

---

### 3. 왜 `@SpringBootTest`는 되는데 이건 안 됐을까?

* **`@SpringBootTest`**: 이건 **클래스(Annotation)**라서 일반적인 `import`로 가져옵니다.
* **`assertNotNull`**: 이건 클래스가 아니라 **함수(Method)**입니다. 자바에서는 다른 클래스의 함수를 내 것처럼 가져다 쓰려면 `static import`라는 특별한 처리가 필요합니다.



### 💡 확인해 볼 점
만약 `Alt + Enter`를 눌렀는데도 임포트 제안이 안 뜬다면, 아까 말씀드린 **`build.gradle`**의 `testImplementation 'org.springframework.boot:spring-boot-starter-test'`가 여전히 제대로 로드되지 않았을 가능성이 큽니다.

`import static`을 수동으로 넣었을 때 빨간색이 사라지는지 확인해 보세요! 성공하면 이제 진짜 테스트를 돌려볼 준비가 끝난 겁니다. 초록불 보러 가시죠! (위트 있게) "빨간불은 멈추라는 게 아니라, 임포트하라는 신호입니다."