
##
- `::new`.얘는 신기하게 왼쪽을참조한다.
- 왼쪽참조해서 그에대응한 객체만들어주는형태.


##
그 부분이 처음엔 정말 마법처럼 느껴질 수 있어요! 결론부터 말씀드리면, **`::new` 자체에는 정보가 없지만, 그 `::new`를 받아주는 '그릇(함수형 인터페이스)'이 무엇인지**를 보고 자바가 똑똑하게 판단합니다.

자바 컴파일러는 이 과정을 **타입 추론(Type Inference)**을 통해 해결합니다.

---

### 1. 그릇(인터페이스)을 보면 답이 나옵니다.
`User::new`라는 똑같은 코드를 써도, 왼쪽에 어떤 인터페이스가 오느냐에 따라 호출되는 생성자가 달라집니다.

* **파라미터가 없는 경우 (`Supplier`)**
  ```java
  // Supplier의 get()은 파라미터가 없음 -> 기본 생성자 User() 호출
  Supplier<User> s = User::new; 
  User user = s.get(); 
  ```

* **파라미터가 1개인 경우 (`Function`)**
  ```java
  // Function의 apply(T t)는 파라미터가 1개임 -> User(String name) 생성자 호출
  Function<String, User> f = User::new; 
  User user = f.apply("Nangman"); 
  ```

* **파라미터가 2개인 경우 (`BiFunction`)**
  ```java
  // BiFunction의 apply(T t, U u)는 2개임 -> User(String name, int age) 생성자 호출
  BiFunction<String, Integer, User> bf = User::new;
  User user = bf.apply("Nangman", 25);
  ```



---

### 2. 컴파일러의 판단 로직 (생각의 흐름)
컴파일러는 `User::new`를 만났을 때 속으로 이렇게 생각합니다.

1.  "오른쪽은 `User` 객체를 만들겠다는 거군."
2.  "왼쪽 그릇을 보니 `Function<String, User>`네?"
3.  "`Function`의 추상 메서드는 **입력이 1개(String)**고 **출력이 1개(User)**야."
4.  "그럼 `User` 클래스 안에서 **`String` 하나를 인자로 받는 생성자**를 찾아야지!"
5.  "찾았다! 이걸로 연결해주자."

**만약 해당하는 생성자가 없다면?** 컴파일 에러가 발생합니다.

---

### 3. 실무 예시 (Stream API)
실제로 가장 많이 쓰이는 `Stream.map()`을 예로 들어볼게요.

```java
List<String> names = List.of("Alice", "Bob");

// map은 Function 타입을 인자로 받습니다.
// Function은 입력을 하나 받아서 결과를 내놓는 구조죠.
names.stream()
     .map(User::new) // 여기서 User::new는 자동으로 User(String name)을 찾습니다.
     .toList();
```

여기서 `map`은 리스트의 요소(String)를 하나씩 꺼내서 전달해주기 때문에, 자바는 자동으로 "아, String 하나 받는 생성자를 쓰라는 거구나!"라고 알게 되는 것입니다.

---

### 요약하자면
`::new` 혼자서는 아무것도 결정할 수 없습니다. **그것을 대입받는 변수의 타입(함수형 인터페이스)**이 "야, 나는 파라미터 2개짜리 필요해!"라고 정해놓으면, `::new`는 그 요구에 맞는 생성자를 찾아서 변신하는 것입니다.

이 원리가 이해되시면 람다와 스트림을 훨씬 자유자재로 다루실 수 있을 거예요. 혹시 `Function`이나 `Supplier` 같은 인터페이스 종류가 아직 낯설게 느껴지시나요?