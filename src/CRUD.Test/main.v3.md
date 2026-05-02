

## Test실행
TestRun
Test.Run
Test.Run.`@Test`.초록버튼
실행은 어떻게 하나요?
Test.Run.01.클래스 이름 옆이나 메서드(`@Test`) 옆에 있는 **초록색 재생 버튼(▶️)**을 누릅니다.
Test.Run.02.하단 `Run` 탭에서 로그가 올라가면서 체크 표시가 뜨면 성공입니다!


##
아, 유저님이 보여주신 그 코드는 스프링 부트 프로젝트를 생성할 때 자동으로 만들어지는 **'기본 테스트 클래스'**입니다!

그 클래스는 단순히 "내 스프링 부트 프로젝트가 에러 없이 잘 실행(로딩)되는가?"만 확인하는 용도예요. 우리가 만든 회원가입이나 게시글 기능을 테스트하려면 **새로운 테스트 파일을 만들어야 합니다.**

---

### 1. 어디에 만드나요?
프로젝트 폴더 구조를 보시면 `src/main/java`와 대칭되는 **`src/test/java`** 폴더가 있습니다. 거기에 패키지 경로를 똑같이 맞춰서 만들어주는 게 정석입니다.

* **실제 코드**: `src/main/java/org/bitcopark/base/domain/board/controller/BoardController.java`
* **테스트 코드**: `src/test/java/org/bitcopark/base/domain/board/controller/BoardControllerTest.java`

---

### 2. 어떻게 작성하나요? (복사해서 붙여넣어 보세요)
아까 제가 알려드린 코드를 `BoardControllerTest`라는 이름의 새 클래스 파일로 만드시면 됩니다. 유저님의 환경에 맞춰서 가장 핵심적인 부분만 다시 추려드릴게요.

```java
package org.bitcopark.base.domain.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bitcopark.base.domain.board.dto.BoardCreateRequest;
import org.bitcopark.base.domain.member.entity.Member;
import org.bitcopark.base.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class BoardControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired MemberRepository memberRepository; // 회원 저장을 위해 필요

    @Test
    void 게시글작성_테스트() throws Exception {
        // 1. 테스트용 회원 미리 저장 (DB에 있어야 ID가 생김)
        Member member = new Member("test", "1234", "테스터");
        memberRepository.save(member);

        // 2. 가짜 세션에 로그인 정보 심기
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("loginMember", member);

        // 3. 게시글 데이터 준비
        BoardCreateRequest request = new BoardCreateRequest();
        request.setTitle("테스트 제목");
        request.setContent("테스트 내용");

        // 4. API 호출 및 검증
        mockMvc.perform(post("/api/boards")
                        .session(session) // 세션 주입!
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
```

---



---

### 💡 왜 `BaseApplicationTests`에 안 쓰나요?
물론 거기에 다 몰아넣어도 돌아는 가지만, 나중에 기능이 많아지면 관리가 불가능해집니다.
* **Member 관련 테스트**는 `MemberControllerTest`에,
* **Board 관련 테스트**는 `BoardControllerTest`에

이렇게 **도메인별로 나누어 관리**하는 것이 나중에 취업해서 실무에 나갔을 때도 칭찬받는 깔끔한 방식입니다.

지금 바로 `src/test/java` 하위에 패키지 하나 만들어서 이 코드를 돌려보시겠어요? 에러 메시지가 뜬다면 그대로 복사해서 저에게 보여주세요!