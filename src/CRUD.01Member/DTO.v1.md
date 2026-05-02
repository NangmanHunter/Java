## DTO
- [ ] MemberDTO
  - 너무적다
    - signup인지
    - login인지
    - 구별불능
  - Entity Member도있다. 굳이.
- [ ] MemberRequestDTO
  - DTO윗선개념이라 무지성넣어주면 편하긴하나.
  - 컨벤션자체는 살아나긴하나. Controller Service Repository.
  - 표현자체로 개념자체로 실익성은 좀떨어짐.
  - 2표현1개념
  - 1개념2표현
  - 이렇기에 개념자체 표현자체 표현실익 떨어지는꼭.
  - 작업성 떨어지는것도 생각보다큰듯.
  - Entity마냥 아싸리 DTO 얘도 다르게 가줘버리는 그런형태로 접근이 더나을수도.
- [x] MemberRequest
  - 그냥 관례를 따르자
  - 그냥 관례를 최대한따르자.ㄱㄱ.
  - 틀린건없지만 관례는있다.


##
DTO(Data Transfer Object) 설계 방식에 정답은 없지만, 상황에 따라 **분리하는 방식**과 **이너 클래스(Inner Class)로 묶는 방식** 각각의 뚜렷한 장단점이 있습니다.

---

## 1. 개별 클래스로 완전 분리하는 방식
`UserSaveRequest.java`, `UserResponse.java` 처럼 파일을 각각 만드는 형태입니다.

### 장점
* **단일 책임 원칙(SRP) 준수:** 각 클래스가 하나의 목적(입력 또는 출력)만 가집니다.
* **유지보수 용이:** 프로젝트 규모가 커지고 Request/Response에 담기는 필드가 판이하게 달라질 때 관리가 편합니다.
* **스웨거(Swagger/OpenAPI) 가독성:** 문서화 도구에서 클래스명이 명확하게 노출됩니다.

### 단점
* **클래스 폭발:** CRUD 기능 하나를 만들 때마다 파일이 4~5개씩 생기므로 프로젝트 구조가 복잡해 보일 수 있습니다.

---

## 2. 하나의 클래스 안에 Request/Response를 묶는 방식 (추천)
보통 `UserDto`라는 클래스 안에 `static inner class`로 선언하는 방식입니다.

```java
public class UserDto {

    @Getter
    public static class Request {
        private String email;
        private String password;
    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String email;
    }
}
```

### 장점
* **응집도 향상:** 해당 도메인(User)과 관련된 데이터 구조를 한눈에 파악할 수 있습니다.
* **이름 관리 효율:** 클래스 파일을 뒤지지 않아도 `UserDto.Request` 식으로 직관적인 호출이 가능합니다.
* **생산성:** 파일 개수가 줄어들어 관리가 훨씬 간결해집니다.

### 단점
* **클래스 크기:** 도메인이 복잡해지면 내부 클래스가 너무 많아져 파일 하나가 수백 줄이 될 수 있습니다.

---

## 3. 어떤 것을 선택할까?

### **묶는 방식(Inner Class)을 추천하는 경우**
* **1인 개발 혹은 소규모 프로젝트:** 시간을 아끼고 구조를 단순하게 가져가는 것이 유리합니다.
* **데이터 필드가 비슷한 경우:** 생성(Create)과 수정(Update) 시 받는 데이터가 거의 같다면 굳이 파일을 나눌 필요가 없습니다.

### **분리하는 방식(Individual Class)을 추천하는 경우**
* **대규모 협업:** 여러 명이 동시에 같은 도메인을 건드릴 때 코드 충돌을 줄일 수 있습니다.
* **도메인 로직이 복잡할 때:** 입력받는 값과 출력하는 값의 형식이 완전히 다르고, 검증(Validation) 로직이 복잡하게 들어갈 때 적합합니다.

---

> **💡 한 줄 요약**
> 처음 시작하시거나 혼자 만드시는 단계라면, **`UserDto` 안에 `Request`, `Response`를 static inner class로 묶어서 관리하는 방식**을 먼저 시도해 보세요. 코드가 훨씬 깔끔하게 느껴지실 겁니다.

혹시 지금 만드시는 기능 중에서 Request와 Response의 필드 차이가 많이 나는 편인가요?