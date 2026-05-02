

##
- 아 이게 그거인듯. `new ㅇㅇ` 이부분을 새로운 상수마냥 잡아서 바로보이게 택갈이한형태로 만들고자 그렇게만든듯.
- 그냥했으면 매번 "공지사항" "자유게시판" 이런걸로 매번넣어야되나. 이게 한글이면서 설명이기에. 그자체로 객체특정이 잘안보이는듯. 

- 그래서 객체특정위해 앞단부분을 명명화 상수화 처럼만들어서 진행하게 만든듯함.
- 결국 사용자편의 개발자편의 이것위해 진행하는듯함.
- `NOTICE("공지사항")`: 이 부분이 바로 `new BoardType("공지사항")`을 호출하는 것과 똑같습니다.


##
```java
BoardType type = BoardType.NOTICE;

System.out.println(type);               // 출력: NOTICE (컴퓨터가 식별하는 이름)
System.out.println(type.getDescription()); // 출력: 공지사항 (사람이 보는 이름)
```


##
가장 헷갈려 하시는 부분이 **"메서드 호출처럼 생겼는데 어떻게 필드(description)에 값이 들어가는가?"**일 거예요.

이해를 돕기 위해 이 코드가 내부적으로 어떻게 동작하는지 일반 클래스 문법으로 풀어서 보여드릴게요.

---

### 1. Enum의 실제 모습 (비밀 풀기)
사실 자바에서 `enum`은 **특수한 형태의 클래스**입니다. 우리가 쓴 코드는 자바 컴파일러에 의해 대략 다음과 같은 모습으로 바뀝니다.

```java
// 우리가 쓴 enum은 사실 이런 클래스입니다.
public class BoardType {
    // 1. 우리가 적은 NOTICE("공지사항")은 사실 미리 만들어진 '자기 자신'의 객체입니다.
    public static final BoardType NOTICE = new BoardType("공지사항");
    public static final BoardType COMMUNITY = new BoardType("자유게시판");

    private final String description;

    // 2. @RequiredArgsConstructor가 만들어준 생성자입니다.
    private BoardType(String description) {
        this.description = description;
    }
}
```

* **`NOTICE("공지사항")`**: 이 부분이 바로 `new BoardType("공지사항")`을 호출하는 것과 똑같습니다.
* 괄호 안의 `"공지사항"`이라는 문자열이 생성자의 파라미터로 전달되어 `this.description`에 저장되는 원리입니다.

---

### 2. 왜 이렇게 쓰나요? (활용법)

이렇게 구성해두면, 나중에 코드 어디서든 `BoardType.NOTICE.getDescription()`을 호출해서 **"공지사항"**이라는 예쁜 한글 이름을 꺼내 쓸 수 있습니다.





### 3. 정리하자면
* **`NOTICE`**: 이 Enum의 진짜 이름(ID 역할). DB에는 주로 이 값이 저장됩니다.
* **`("공지사항")`**: 생성자에 넘겨주는 인자값입니다.
* **`description`**: 그 인자값을 담아두는 바구니(필드)입니다.

즉, **`NOTICE`라는 상수 안에 "공지사항"이라는 추가 정보를 문신처럼 새겨놓았다**고 생각하시면 편해요!

이제 `BoardType` 안에 적힌 괄호와 `description`의 관계가 조금 보이시나요? 다른 궁금한 점이 생기시면 편하게 말씀해주세요!