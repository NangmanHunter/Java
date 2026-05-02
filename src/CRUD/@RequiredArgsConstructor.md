`@RequiredArgsConstructor`는 **Lombok(롬복)**이라는 라이브러리에서 제공하는 어노테이션으로, **"final이 붙거나 @NonNull이 붙은 필드들을 파라미터로 받는 생성자"**를 자동으로 만들어주는 아주 편리한 도구입니다.

스프링 개발에서 생성자 주입(Constructor Injection)을 할 때 코드를 획기적으로 줄여주기 때문에 거의 필수적으로 사용됩니다.

---

## 1. 비포 & 애프터 (직접 비교)

이 어노테이션이 없을 때와 있을 때 코드가 어떻게 변하는지 보시면 바로 이해가 되실 겁니다.

### ❌ 사용하지 않을 때 (기존 방식)
의존성(Repository 등)이 늘어날 때마다 생성자 코드를 계속 수정해야 합니다.
```java
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    // 이걸 직접 다 타이핑해야 함... 필드가 많아지면 지옥 시작
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
}
```

### ✅ @RequiredArgsConstructor 사용할 때
Lombok이 컴파일 시점에 생성자를 대신 만들어줍니다.
```java
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    // 생성자 코드가 눈에는 안 보이지만 실제로는 존재함!
}
```

---

## 2. 왜 이걸 쓸까? (핵심 이유)

### ① 생성자 주입의 간소화
Spring에서 권장하는 의존성 주입 방식은 **생성자 주입**입니다. `@RequiredArgsConstructor`를 쓰면 새로운 Repository를 추가할 때 `private final ...` 한 줄만 적으면 끝납니다. 스프링이 생성자를 보고 자동으로 객체를 넣어주기(DI) 때문입니다.

### ② `final` 키워드와의 찰떡궁합
`final`로 선언된 필드는 반드시 초기화가 되어야 합니다. 이 어노테이션은 `final`이 붙은 필드만 골라서 생성자의 파라미터로 넣어주기 때문에, 객체가 생성될 때 의존성이 누락되는 것을 컴파일 단계에서 막아줍니다.

---

## 3. 작동 원리 (주의할 점)

Lombok은 아무 필드나 다 생성자에 넣지 않습니다. 딱 두 가지만 챙깁니다:
1.  **`final`**이 붙은 필드
2.  **`@NonNull`** 어노테이션이 붙은 필드

만약 `final`을 붙이지 않으면 `@RequiredArgsConstructor`는 해당 필드를 무시합니다. 그래서 스프링 빈 주입을 원하신다면 반드시 `private final`로 선언해야 한다는 점을 잊지 마세요!



---

**정리하자면:**
`MemberService` 예제에서 `@RequiredArgsConstructor`를 쓴 이유는, **"MemberRepository를 주입받기 위한 생성자 코드를 일일이 쓰기 귀찮으니 Lombok 네가 대신 만들어줘"**라고 시키는 것과 같습니다. 덕분에 코드가 훨씬 깔끔해지는 거죠!