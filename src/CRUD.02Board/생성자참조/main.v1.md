##
- `::new`
- `::new`.생성자참조
- `::new`.생성자참조.타입추론
- `::new`.ConstructorReference.생성자참조
- 생성자 참조(Constructor Reference)
- 생성자 참조
- 생성자참조
- Constructor Reference
- ConstructorReference


기존방식
- `.map(b -> new BoardResponse(b.getId() ... )`
- 람다방식
- 할당방식


##
자바에서 `::new`는 **생성자 참조(Constructor Reference)**라고 부르는 문법입니다. 람다식(Lambda Expression)을 더 간결하게 줄여 쓴 형태라고 이해하시면 됩니다.

정확히 어떤 것과 같은지, 단계별로 비교해 드릴게요.

---

### 1. 코드 비교 (무엇과 같은가?)

만약 `User`라는 클래스가 있고, 이를 생성하는 코드를 짠다면 아래 세 줄은 **완벽히 동일한 동작**을 합니다.

* **1단계 (익명 구현 객체):** 람다 이전의 방식
    ```java
    Function<String, User> f = new Function<String, User>() {
        @Override
        public User apply(String name) {
            return new User(name);
        }
    };
    ```
* **2단계 (람다식):** 훨씬 간결해진 방식
    ```java
    Function<String, User> f = (name) -> new User(name);
    ```
* **3단계 (생성자 참조):** `::new`를 사용한 극한의 간결함
    ```java
    Function<String, User> f = User::new;
    ```

---

### 2. 작동 원리
`::new`는 "컴파일러야, 네가 알아서 매개변수 확인해서 **해당하는 생성자**를 호출해줘"라는 뜻입니다.

* **매개변수가 없는 경우:** `Supplier<User> s = User::new;` → `new User()` 호출
* **매개변수가 한 개인 경우:** `Function<String, User> f = User::new;` → `new User(String name)` 호출
* **배열인 경우:** `Function<Integer, int[]> f = int[]::new;` → `new int[size]` 호출



---

### 3. 실무에서 왜 쓸까? (Stream API와의 조합)

주로 리스트를 다른 객체 리스트로 변환할 때 가장 많이 사용합니다. 현재 공부 중이신 **Spring Boot/JPA** 환경에서 DTO 변환 시 자주 보게 되실 코드입니다.

```java
// Entity 리스트를 Response DTO 리스트로 변환할 때
List<BoardResponse> responses = boardList.stream()
    .map(BoardResponse::new) // 각 Board 엔티티를 이용해 새로운 DTO 객체 생성
    .toList();
```

위의 `.map(BoardResponse::new)`는 `.map(board -> new BoardResponse(board))`와 완전히 같은 의미입니다.

---

### 4. 주의할 점 (엄밀한 구분)
* **실행 시점:** `::new`를 썼다고 해서 그 즉시 객체가 생성되는 것이 아닙니다. 람다식처럼 **함수형 인터페이스의 메서드가 호출되는 시점**에 `new`가 실행됩니다.
* **타입 추론:** 컴파일러가 대입되는 인터페이스(예: `Function`, `Supplier`)를 보고 어떤 생성자를 쓸지 결정하므로, 인터페이스 타입이 명확해야 합니다.

**요약하자면:**
`::new`는 **"단순히 받은 매개변수를 그대로 생성자에 넘겨서 객체를 만드는 람다식"**의 축약형입니다. 코드를 훨씬 깔끔하게 만들어주죠!

이 문법을 스트림(Stream) 코드 작성 중에 발견하신 건가요? 아니면 함수형 인터페이스를 공부하시다 궁금해지신 건가요?