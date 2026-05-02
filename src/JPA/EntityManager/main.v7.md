네, 그 부분이 처음에 보면 정말 마법 같고 이해가 안 가는 게 정상입니다! "인터페이스만 만들고 구현 클래스(`implements`)는 안 만들었는데, 어떻게 실행이 되고 데이터가 저장되지?"라는 의문이 드는 게 당연하죠.

결론부터 말씀드리면, **스프링(Spring Data JPA)이 실행 시점에 가짜 구현체(Proxy)를 몰래 만들어서 끼워넣기 때문**입니다.

---

### 1. "나는 인터페이스만 만들었는데?" (개발자의 입장)
사용자는 아래처럼 인터페이스만 정의합니다. 
```java
public interface BoardRepository extends JpaRepository<Board, Long> {
    // 텅 비어있거나 쿼리 메서드만 있음
}
```

### 2. "내가 대신 만들어줄게!" (스프링의 입장)
애플리케이션이 실행될 때, 스프링은 `@EnableJpaRepositories` (보통 자동 설정됨) 기능을 통해 `JpaRepository`를 상속받은 인터페이스들을 다 찾아냅니다. 

그리고 **JDK Dynamic Proxy**라는 기술을 써서, 메모리상에 **`BoardRepository`의 구현체**를 실시간으로 만들어냅니다. 이때 이 가짜 객체가 내부적으로 사용하는 실제 로직이 바로 아까 말씀드린 **`SimpleJpaRepository`**입니다.



---

### 3. 실제 동작 구조 (런타임 시점)

여러분이 `boardRepository.save(board)`를 호출하면 실제로 일어나는 일은 다음과 같습니다.

1.  **Proxy 객체:** "오, `save`를 호출했네? 이 요청은 공통 기능이니까 내가 들고 있는 `SimpleJpaRepository`한테 전달해야지."
2.  **SimpleJpaRepository:** "내가 진짜 `save` 로직을 가지고 있어. 내 안에 있는 `EntityManager`를 꺼내서 `em.persist()`를 실행할게!"
3.  **DB:** 데이터 저장 완료.

---

### 4. 눈으로 확인하는 방법
이게 진짜인지 궁금하다면, 프로젝트 코드 아무 데서나 `boardRepository`를 주입받아 **클래스 이름**을 출력해 보세요.

```java
@SpringBootTest
class RepositoryTest {
    @Autowired
    BoardRepository boardRepository;

    @Test
    void test() {
        System.out.println(boardRepository.getClass().getName());
        // 결과: com.sun.proxy.$Proxy... (이런 식으로 나옵니다)
    }
}
```
출력 결과가 `BoardRepository`가 아니라 **`$Proxy`** 어쩌구라고 나올 거예요. 이게 바로 스프링이 만든 "가짜 일꾼"이고, 이 녀석이 내부적으로 **`SimpleJpaRepository`**를 사용하고 있는 겁니다.

---

### 요약하자면
* **사용자:** "메뉴판(인터페이스)만 만든다."
* **스프링:** "그 메뉴판을 보고 실제 요리사(Proxy + SimpleJpaRepository)를 복제해서 배치한다."
* **작동:** 사용자가 메뉴를 고르면(메서드 호출), 복제된 요리사가 내부에 숨겨둔 칼(`EntityManager`)을 꺼내서 요리한다.

그래서 질문자님이 보신 `JpaRepository` 코드에는 `EntityManager`가 없었던 것이고, 실제 일은 스프링이 뒤에서 몰래 만든 구현체가 하고 있었던 거예요. 이 "대리인(Proxy) 패턴" 덕분에 우리가 귀찮게 `implements`를 안 해도 되는 거랍니다!

이 구조가 이해되시나요? 아니면 스프링이 어떻게 인터페이스만 보고 쿼리를 만들어내는지(예: `findByTitle`) 그 원리가 더 궁금하신가요?