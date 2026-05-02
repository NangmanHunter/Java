현재 발생한 문제는 **Spring Boot 플러그인 버전과 현재 설치된 Gradle 버전 사이의 호환성** 때문입니다. 

Spring Boot `4.0.5` 버전(미래 버전 혹은 프리뷰)은 매우 높은 사양의 Gradle(8.14 이상)을 요구하고 있는데, 현재 프로젝트는 Gradle `8.10`을 사용하고 있어서 발생하는 충돌입니다.

Gradle 버전을 올리는 방법은 크게 두 가지가 있습니다.

---

### 1. Gradle Wrapper를 사용하여 업데이트 (권장)
프로젝트 터미널(IntelliJ 하단 Terminal)에서 아래 명령어를 입력하는 것이 가장 정확하고 빠릅니다. 이 방식은 프로젝트 자체의 Gradle 설정을 변경하므로 다른 환경에서도 동일한 버전이 유지됩니다.

```bash
# 8.14 버전으로 업데이트 하려는 경우
./gradlew wrapper --gradle-version 8.14
```
*(Windows 명령 프롬프트라면 `gradlew`를, PowerShell이나 Bash라면 `./gradlew`를 입력하세요.)*

명령어 실행 후 Gradle이 새 버전을 자동으로 다운로드하며 설정이 완료됩니다.

### 2. `gradle-wrapper.properties` 파일 직접 수정
터미널 명령어가 익숙하지 않다면 설정 파일을 직접 수정할 수 있습니다.

1. 프로젝트 구조에서 `gradle/wrapper/gradle-wrapper.properties` 파일을 엽니다.
2. `distributionUrl` 항목의 버전을 원하는 버전으로 수정합니다.

```properties
# 수정 전
distributionUrl=https\://services.gradle.org/distributions/gradle-8.10-bin.zip

# 수정 후 (예: 8.14)
distributionUrl=https\://services.gradle.org/distributions/gradle-8.14-bin.zip
```
3. 수정 후 IntelliJ 우측 상단의 **Gradle 새로고침 아이콘(코끼리 모양)**을 클릭하세요.

---

### 💡 현실적인 팁: Spring Boot 버전을 조정하기
만약 Gradle 8.14 이상을 구하기 어렵거나 환경 설정이 꼬인다면, **Spring Boot의 버전을 살짝 낮추는 것**이 더 쉬운 해결책일 수 있습니다. 

현재 사용 중인 Gradle `8.10`은 Spring Boot `3.3.x`나 `3.4.x` 버전과 아주 잘 맞습니다. `build.gradle`에서 버전을 아래와 같이 조정해 보세요.

```gradle
plugins {
    id 'org.springframework.boot' version '3.3.4' // 4.0.5 대신 안정적인 3.x 버전 사용
    id 'io.spring.dependency-management' version '1.1.6'
    id 'java'
}
```

**왜 이런 일이 생기나요?**
Spring Boot 팀은 최신 기능을 지원하기 위해 특정 버전 이상의 Gradle을 요구하도록 설계합니다. 마치 최신 게임을 돌리기 위해 그래픽 카드 드라이버를 업데이트해야 하는 것과 비슷한 원리라고 이해하시면 됩니다.