엔티티를 외부에 직접 노출하면 보안 문제(비밀번호 노출)뿐만 아니라, 엔티티 구조가 변경될 때 API 스펙이 통째로 흔들리는 위험이 있습니다. 

질문하신 **"어느 지점에서, 어떻게"** 가공하는지에 대한 가장 정석적인 가이드를 정리해 드릴게요.

---

## 1. 어느 지점에서 가공하는 것이 맞을까?

결론부터 말씀드리면, **컨트롤러(Controller)**가 엔티티를 **DTO(Data Transfer Object)**로 변환하여 반환하는 책임을 갖는 것이 일반적입니다.

* **서비스(Service):** 비즈니스 로직에 집중하며 엔티티를 다룹니다. 가급적 특정 API 스펙(DTO)에 종속되지 않는 것이 좋습니다.
* **컨트롤러(Controller):** 외부의 요청을 받고 응답을 나가는 '창구'입니다. 클라이언트가 원하는 데이터 형식(DTO)으로 최종 가공하는 역할을 수행합니다.

### 데이터 흐름
`DB` → `Entity` → **[Service]** → `Entity` → **[Controller]** → **`DTO` 변환** → `Client`

---

## 2. 어떻게 가공하는 것이 맞을까?

가장 권장되는 방법은 **DTO 클래스를 별도로 생성**하고, Java 8 이상의 **Stream API**를 사용하여 변환하는 방식입니다.

### ① DTO 클래스 생성 (Response용)
비밀번호 등 민감한 정보를 제외하고, 화면에 보여줄 필드만 정의합니다.

```java
public class MemberResponse {
    private Long id;
    private String name;
    private String email;

    // 생성자, Getter (또는 @Data / @Getter)
    public MemberResponse(Member member) {
        this.id = member.getId();
        this.name = member.getName();
        this.email = member.getEmail();
    }
}
```

### ② 컨트롤러 코드 수정
`Stream`을 사용하여 `Member` 리스트를 `MemberResponse` 리스트로 변환합니다.

```java
@GetMapping
public List<MemberResponse> list() {
    List<Member> members = memberService.findMembers();
    
    // Entity 리스트를 DTO 리스트로 변환
    return members.stream()
            .map(MemberResponse::new)
            .collect(Collectors.toList());
}
```

---

## 3. 왜 이렇게 해야 할까?
* **보안:** 비밀번호(`password`), 주민번호 등 민감한 필드를 원천 차단합니다.
* **유연성:** 엔티티의 필드명이 `userName`에서 `userId`로 바뀌어도, DTO에서 매핑만 유지해주면 API를 사용하는 프론트엔드 코드는 수정할 필요가 없습니다.
* **과도한 데이터 방지:** 엔티티에 20개의 필드가 있어도 실제 화면에서 3개만 쓴다면, DTO를 통해 네트워크 트래픽을 줄일 수 있습니다.

### 추가 팁: ModelMapper나 MapStruct
만약 변환해야 할 필드가 너무 많아 `new DTO(entity)` 방식이 번거롭다면, 객체 간 매핑을 자동화해주는 **MapStruct** 같은 라이브러리를 도입하는 것도 좋은 방법입니다. 

하지만 현재 단계에서는 위와 같이 **직접 DTO를 만들고 컨트롤러에서 변환**하는 흐름을 먼저 익히시는 것을 강력히 추천합니다. 간단하면서도 가장 확실한 방법이니까요.