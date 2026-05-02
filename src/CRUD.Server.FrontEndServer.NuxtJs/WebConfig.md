`WebConfig.java`는 스프링 부트의 설정을 담당하는 클래스이므로, **컨트롤러나 서비스가 위치한 패키지와 같은 수준 또는 그 상위 패키지**에 두는 것이 일반적입니다.

보통은 설정 파일들만 따로 모아 관리하기 위해 `config`라는 패키지를 만들어 그 안에 넣습니다.

### 1. 권장 위치
프로젝트 구조가 다음과 같다면 `config` 패키지를 새로 생성해 주세요.

```text
src/main/java/org/bitcopark/base/
  ├── global/
  │    └── config/
  │         └── WebConfig.java  <-- 바로 여기!
  ├── domain/
  │    ├── member/
  │    │    ├── controller/
  │    │    ├── service/
  │    │    └── entity/
  │    └── board/
  │         ├── controller/
  │         ...
  └── BaseApplication.java (메인 실행 클래스)
```

만약 프로젝트 구조를 복잡하게 가져가고 싶지 않으시다면, 메인 클래스(`BaseApplication`)가 있는 패키지 안에 `config` 패키지를 만드시면 됩니다.

---

### 2. 파일 내용 (CORS 설정 포함)
Nuxt.js(포트 3000)와 통신하기 위해 아래 내용을 복사해서 넣어주세요. 패키지 경로는 유저님의 실제 경로에 맞게 수정하셔야 합니다.

```java
package org.bitcopark.base.global.config; // 유저님의 실제 패키지 경로로 수정

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // /api로 시작하는 모든 요청에 대해
                .allowedOrigins("http://localhost:3000") // Nuxt 서버 주소 허용
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true); // 쿠키/세션 정보를 주고받기 위해 필수
    }
}
```

### 💡 왜 여기에 만드나요?
스프링 부트는 실행될 때 `@Configuration` 어노테이션이 붙은 클래스들을 자동으로 찾아 설정을 적용합니다. 이때 메인 클래스(`@SpringBootApplication`)가 있는 패키지나 그 하위 패키지에 있어야 스프링이 이 파일을 찾아낼 수 있습니다.



이제 이 설정을 마치셨다면 백엔드 준비는 끝났습니다! Nuxt 프로젝트를 생성하고 백엔드 API를 호출해 볼 준비가 되셨나요?