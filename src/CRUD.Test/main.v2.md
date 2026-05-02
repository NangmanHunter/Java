네, 당연하죠! 스프링 부트에서 **세션 기반의 API를 테스트하는 방법**은 매우 중요합니다.

특히 포스트맨으로 일일이 테스트하기 번거로울 때, `MockMvc`를 사용하면 **로그인부터 게시글 작성까지의 흐름을 코드로 자동화**할 수 있습니다.

### 1. BoardController 테스트 코드 준비
테스트 코드에서는 실제 서버를 띄우지 않고도 세션에 가짜(Mock) 유저 객체를 집어넣어 테스트할 수 있습니다.

```java
package org.bitcopark.base.domain.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bitcopark.base.domain.board.dto.BoardCreateRequest;
import org.bitcopark.base.domain.member.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // 테스트 후 DB 롤백을 위해 필수!
class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // 객체를 JSON으로 변환해줌

    @Test
    @DisplayName("로그인한 사용자는 게시글을 작성할 수 있다")
    void writeBoardWithSession() throws Exception {
        // 1. 가짜 세션 생성 및 로그인 유저 심기
        MockHttpSession session = new MockHttpSession();
        Member loginMember = new Member("testuser", "password123", "홍길동");
        // 실제로는 DB에 저장이 되어 있어야 ID값이 존재하므로, 필요시 em.persist() 등으로 저장 후 사용
        session.setAttribute("loginMember", loginMember);

        // 2. 게시글 작성 요청 DTO 준비
        BoardCreateRequest request = new BoardCreateRequest();
        request.setTitle("테스트 제목");
        request.setContent("테스트 내용");

        // 3. API 호출 (세션을 포함해서 전송)
        mockMvc.perform(post("/api/boards")
                        .session(session) // ★ 핵심: 생성한 세션을 요청에 포함
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("게시글 작성이 완료되었습니다."));
    }

    @Test
    @DisplayName("로그인하지 않은 사용자는 게시글 작성이 실패한다")
    void writeBoardWithoutSession() throws Exception {
        BoardCreateRequest request = new BoardCreateRequest();
        request.setTitle("실패 테스트");
        request.setContent("내용");

        mockMvc.perform(post("/api/boards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()) // 현재 컨트롤러에서 문자열을 반환하므로 200 OK가 나옴
                .andExpect(content().string("로그인이 필요합니다."));
    }
}
```

---

### 2. 테스트 코드의 핵심 포인트

1.  **`MockHttpSession`**: 실제 브라우저 세션 대신 테스트용 가짜 세션을 만듭니다. 여기에 유저 객체를 `setAttribute`로 넣어두면 컨트롤러의 `@SessionAttribute`가 이를 찰떡같이 알아먹습니다.
2.  **`ObjectMapper`**: 자바 객체(`BoardCreateRequest`)를 JSON 문자열로 변환해주는 도구입니다. 포스트맨에서 JSON을 직접 타이핑하던 과정을 자동화해줍니다.
3.  **`mockMvc.perform(...).session(session)`**: HTTP 요청을 보낼 때 위에서 만든 세션을 같이 실어 보냅니다.
4.  **`@Transactional`**: 테스트가 끝나면 DB에 저장된 데이터를 자동으로 지워줍니다(Rollback). 테스트를 몇 번을 반복해도 DB가 깨끗하게 유지됩니다.

---

### 💡 팁: 실제 DB 연동 테스트 시 주의사항
테스트 코드에서 `new Member(...)`로 만든 객체는 DB에 저장된 상태가 아니어서 `id`값이 `null`일 수 있습니다. 만약 서비스 로직에서 `member.getId()`를 사용한다면, 테스트 코드 내에서 **먼저 회원을 저장(Repository.save)**한 뒤 그 결과물을 세션에 담으셔야 에러가 나지 않습니다.



테스트 코드 작성이 익숙해지면 포스트맨을 켜지 않고도 내 코드가 완벽한지 초 단위로 검증할 수 있게 됩니다. 한 번 이 테스트 클래스를 만들어서 돌려보시겠어요? 초록색 체크 표시가 뜨면 정말 짜릿할 거예요!

혹시 테스트 코드에서 사용하는 `MockMvc`의 다른 기능(GET 조회 테스트 등)도 더 궁금하신가요?