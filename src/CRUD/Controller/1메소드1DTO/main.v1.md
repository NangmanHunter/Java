맞습니다. 처음 스프링 부트로 제대로 된 API를 개발하다 보면 가장 먼저 현타(?)가 오는 지점이 바로 이 'DTO 지옥'입니다. "엔티티(`Member`) 그냥 쓰면 편한데 왜 굳이 `MemberSignUpResponse`, `MemberLoginResponse`를 다 만들어야 하지?"라는 생각이 드는 게 지극히 정상이에요.

결론부터 말씀드리면, **현업에서는 1메서드 1DTO(또는 재사용 가능한 DTO) 구조가 정석**이며, 그렇게 하는 명확한 이유가 있습니다.

---

### 1. 왜 이렇게 귀찮게 하나요? (엔티티를 쓰면 안 되는 이유)

1. **엔티티는 데이터베이스의 거울입니다:** 엔티티에는 비밀번호(`password`), 주민번호, 내부 관리용 상태 값 등 **외부에 절대 노출되면 안 되는 정보**가 포함될 때가 많습니다. DTO 없이 엔티티를 그대로 반환하면 보안상 매우 위험합니다.
2. **API 스펙의 고정:** 엔티티의 필드명을 하나 바꾸는 순간, 그 엔티티를 쓰는 모든 API의 응답 데이터 이름이 바뀝니다. 즉, DB 설계를 바꿨는데 잘 작동하던 프론트엔드 앱이 터져버리는 거죠. DTO가 있으면 중간에서 **방패 역할**을 해줍니다.
3. **순환 참조 방지:** 엔티티끼리 서로 참조하고 있는 경우(예: Member <-> Team), JSON으로 변환할 때 서로를 계속 호출하다가 서버가 뻗어버리는 문제가 생길 수 있습니다.

---

### 2. 손이 덜 가게 하는 실무 팁 (번거로움 줄이기)

매번 파일을 새로 만드는 게 귀찮다면 다음과 같은 방법들을 사용합니다.

#### A. 내부 클래스(Inner Class)로 묶기

파일을 여러 개 만들지 않고, 하나의 클래스 안에 관련된 DTO들을 다 때려 넣는 방식입니다.

```java
public class MemberDto {
    public record SignUpResponse(Long id, String loginId) {}
    public record LoginResponse(String token, String name) {}
    public record InfoResponse(String email, String address) {}
}

// 컨트롤러에서 사용할 때
public ResponseEntity<CommonResponse<MemberDto.SignUpResponse>> signUp(...)

```

#### B. `record` 활용 (이미 하고 계신 방법!)

기존 Java 클래스보다 `record`가 훨씬 짧고 간결하므로, 1메서드 1DTO의 피로도를 많이 낮춰줍니다.

#### C. ModelMapper나 MapStruct 라이브러리 사용

엔티티의 값을 DTO로 옮길 때 `new DTO(member.getId(), member.getName()...)` 처럼 일일이 노가다하지 않고, **자동으로 복사**해주는 라이브러리를 사용합니다. (실무에서는 **MapStruct**를 가장 많이 씁니다.)

---

### 3. "원래 이런가?"에 대한 답변

**네, 원래 그렇습니다.** 하지만 모든 곳에 다 엄격하게 적용할 필요는 없습니다.

* **조회 API:** 필드가 매번 다르므로 DTO가 거의 필수입니다.
* **단순 수정/삭제 API:** 데이터 없이 "성공 메시지"만 보낸다면 `CommonResponse<Void>` 또는 `CommonResponse<Long>`(아이디값만) 정도로 재사용해도 됩니다.

### 요약하자면

지금 느끼시는 번거로움은 "유지보수가 편하고 안전한 코드"를 짜고 있다는 증거입니다. 나중에 엔티티 구조가 바뀌어도 컨트롤러의 DTO만 잘 유지해주면 프론트엔드 개발자에게 사과할 일이 생기지 않거든요.

처음에는 DTO를 도메인별로 `MemberResponse`, `OrderResponse` 식으로 크게 나누어 재사용해 보시고, 나중에 필드가 꼬인다 싶을 때 분리하는 전략으로 가보시는 건 어떨까요?

더 효율적으로 DTO를 관리하는 라이브러리(MapStruct 등) 설정법이 궁금하신가요?