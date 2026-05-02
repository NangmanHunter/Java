##
- 패키지구조 동일
- `ㅇㅇTest.java`



##
테스트 코드를 작성해보는 건 정말 좋은 습관입니다! 특히 외부 사이트에 접속하는 `MetaDataService` 같은 기능은 URL이 유효한지, 내가 원하는 데이터를 잘 뽑아오는지 확인하는 과정이 필수적이죠.

스프링 부트 프로젝트라면 `src/test/java` 경로에 테스트 클래스를 만들게 됩니다.

---

### 1. 테스트 클래스 위치
패키지 구조는 메인 코드와 똑같이 맞춰주는 것이 관례입니다.
* **메인:** `src/main/java/org/nangman/linkviewer/MetaDataService.java`
* **테스트:** `src/test/java/org/nangman/linkviewer/MetaDataServiceTest.java`

---

### 2. 테스트 코드 작성 (JUnit 5 기준)

`src/test/java/org/nangman/linkviewer` 패키지를 만들고 아래 파일을 생성하세요.

```java
package org.nangman.linkviewer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest // 스프링 컨텍스트를 불러와서 @Service를 주입받을 수 있게 함
class MetaDataServiceTest {

    @Autowired
    private MetaDataService metaDataService;

    @Test
    @DisplayName("유튜브 링크를 넣었을 때 제목과 이미지를 잘 가져오는지 확인")
    void getLinkMetaData_Success() {
        // Given (준비)
        String url = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";

        // When (실행)
        Map<String, String> result = metaDataService.getLinkMetaData(url);

        // Then (검증)
        assertNotNull(result);
        assertFalse(result.get("title").isEmpty(), "제목이 비어있으면 안 됩니다.");
        assertTrue(result.get("image").startsWith("http"), "이미지 주소는 http로 시작해야 합니다.");
        
        System.out.println("추출된 제목: " + result.get("title"));
        System.out.println("추출된 이미지: " + result.get("image"));
    }

    @Test
    @DisplayName("잘못된 URL을 넣었을 때 에러 메시지가 나오는지 확인")
    void getLinkMetaData_Fail() {
        // Given
        String url = "https://this-is-invalid-url-12345.com";

        // When
        Map<String, String> result = metaDataService.getLinkMetaData(url);

        // Then
        assertTrue(result.containsKey("error"), "에러 키가 포함되어야 합니다.");
        System.out.println("에러 메시지: " + result.get("error"));
    }
}
```

---

### 3. 테스트 실행 방법
1.  클래스 이름 옆이나 메서드 이름 옆에 있는 **초록색 화살표(▶️)**를 누릅니다.
2.  **'Run MetaDataServiceTest'**를 클릭합니다.
3.  하단 **Run** 탭에 초록색 체크 표시가 뜨면 성공!

---

### 4. 코드 설명 (핵심 포인트)

* **`@SpringBootTest`**: 이 어노테이션이 있으면 스프링이 실제로 실행될 때처럼 모든 부품(`@Service` 등)을 준비해줍니다. 그래서 `@Autowired`로 `MetaDataService`를 가져다 쓸 수 있는 거죠.
* **`Assertions (assertNotNull, assertTrue...)`**: "이 결과는 반드시 이래야 해!"라고 선언하는 문장입니다. 만약 결과가 다르면 테스트가 실패(빨간불)하며 우리에게 경고를 줍니다.
* **Given / When / Then**: 테스트 코드를 짤 때 가장 많이 쓰이는 패턴입니다. 
    * **Given**: 테스트에 필요한 데이터 준비
    * **When**: 실제 기능을 실행
    * **Then**: 결과가 예상과 맞는지 확인

---

### 💡 팁: 실제 네트워크 없이 테스트하려면?
지금 짠 코드는 실제 인터넷에 연결되어 사이트를 긁어오는 **통합 테스트(Integration Test)**입니다. 만약 인터넷이 안 되는 환경이거나 외부 사이트 상태에 상관없이 내 로직만 테스트하고 싶다면 'Mocking'이라는 기술을 써야 하는데, 이건 조금 더 공부하신 뒤에 도전해보셔도 늦지 않습니다!

일단 이 코드로 테스트 돌려보시고, 콘솔창에 찍히는 유튜브 제목을 감상해보시겠어요? (위트 있게) 테스트 코드가 통과될 때의 그 초록색 불빛은 개발자에게 최고의 비타민이죠!