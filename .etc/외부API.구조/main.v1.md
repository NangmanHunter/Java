Spring Boot 프로젝트에서 외부 API와 통신하는 기능을 구현할 때, 유지보수와 확장성을 고려한 가장 표준적인 패키지 구조는 **레이어드 아키텍처(Layered Architecture)**를 기반으로 합니다.

일반적으로 다음과 같은 구조를 권장합니다.

### 1. 권장 패키지 구조

```text
src/main/java/com/example/project
├── domain/ (또는 하위 도메인별 패키지)
│   ├── controller/      # API 엔드포인트
│   ├── service/         # 비즈니스 로직
│   ├── repository/      # DB 접근 (JPA 등)
│   └── dto/             # 내부 및 클라이언트 응답용 DTO
├── global/              # 공통 설정 (Exception, Config, Util)
└── infra/               # 외부 시스템과의 연동 (핵심!)
    ├── external/        # 외부 API 연동 관련
    │   ├── client/      # RestTemplate, WebClient, FeignClient 인터페이스/구현체
    │   └── dto/         # 외부 API 응답을 받기 위한 전용 DTO
    └── config/          # API 클라이언트 관련 Bean 설정
```

---

### 2. 주요 구성 요소 설명

#### **infra/external 패키지 (중요)**
외부 API 데이터는 우리 시스템의 제어권 밖에 있습니다. 따라서 이를 도메인 로직과 분리하는 것이 핵심입니다.
* **Client (Interface/Implementation):** 실제 통신을 담당합니다. `RestTemplate`, `WebClient`, 혹은 `OpenFeign` 등을 사용하여 외부 데이터를 찔러보고 데이터를 가져옵니다.
* **External DTO:** 외부 API의 JSON 구조와 1:1로 매핑되는 객체입니다. 우리 쪽 서비스 레이어로 넘길 때는 이 DTO를 그대로 쓰지 않고, 내부에서 사용하는 **Domain DTO로 변환(Convert)**해서 넘겨주는 것이 좋습니다. (외부 API 스펙이 바뀌었을 때 우리 쪽 로직이 깨지는 것을 방지하기 위함입니다.)

#### **service 패키지**
`ExternalClient`를 호출하여 데이터를 받아온 뒤, 서비스에 필요한 비즈니스 처리를 합니다.
* 예: 외부에서 날씨 데이터를 가져와서 우리 DB의 사용자 선호도와 조합해 추천 리스트를 만드는 로직 등.

---

### 3. 통신 방식별 추천 도구

구조를 잡기 전, 어떤 도구를 사용할지에 따라 구현 방식이 살짝 달라집니다.

| 도구 | 특징 | 추천 상황 |
| :--- | :--- | :--- |
| **RestTemplate** | 전통적인 동기 방식 | 가장 대중적이고 구현이 쉬움 |
| **WebClient** | 비동기/논블로킹 지원 | 성능 최적화가 필요하거나 대규모 연동 시 |
| **OpenFeign** | 선언적 방식 (인터페이스만 정의) | 코드가 깔끔하고 생산성이 높음 (Spring Cloud 프로젝트 권장) |

### 4. 구현 팁: "추상화" 활용
`ExternalClient`를 인터페이스로 정의하고 구현체를 따로 두면, 나중에 외부 API 업체가 바뀌거나 테스트 코드를 짤 때(Mocking) 매우 유용합니다.

```java
// 예시: infra/external/client/WeatherClient.java
public interface WeatherClient {
    WeatherResponse getWeatherData(String city);
}
```

이런 식으로 구조를 잡으면 도메인 로직과 인프라 로직이 깔끔하게 분리되어 관리가 훨씬 편해질 거예요. 어떤 라이브러리(RestTemplate 등)를 쓸지 결정하셨나요? 그에 맞는 구체적인 코드 예시도 필요하시면 말씀해주세요!