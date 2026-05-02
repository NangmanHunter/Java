좋습니다! 이제 공들여 만든 **회원가입 -> 로그인 -> 게시글 작성** 시나리오를 포스트맨(Postman)으로 테스트해 볼 차례입니다.

세션이 유지되어야 하므로 아래 **순서**대로 진행하는 것이 핵심입니다.

---

### 1단계: 회원가입 (Member Signup)
먼저 DB에 유저가 있어야 로그인을 하겠죠?

* **Method**: `POST`
* **URL**: `http://localhost:8080/api/members/signup` (또는 설정하신 주소)
* **Body** -> **raw** -> **JSON**:
    ```json
    {
        "loginId": "testuser",
        "password": "password123",
        "name": "홍길동"
    }
    ```
* **확인**: `200 OK` 또는 성공 메시지가 오는지 확인합니다.

---

### 2단계: 로그인 (Member Login) - 중요!
이 단계에서 서버가 **세션 쿠키**를 구워줍니다.

* **Method**: `POST`
* **URL**: `http://localhost:8080/api/members/login`
* **Body** -> **raw** -> **JSON**:
    ```json
    {
        "loginId": "testuser",
        "password": "password123"
    }
    ```
* **확인**: "홍길동님, 환영합니다!" 메시지가 나오는지 보세요.
* **꿀팁**: 포스트맨 하단의 **Cookies** 탭을 클릭해 보세요. `JSESSIONID`라는 값이 생성되어 있다면 세션이 성공적으로 만들어진 겁니다.

---

### 3단계: 게시글 작성 (Board Write)
이제 **`memberId` 없이** 글을 써봅니다. 서버가 세션 쿠키를 보고 작성자를 알아낼 거예요.

* **Method**: `POST`
* **URL**: `http://localhost:8080/api/boards`
* **Body** -> **raw** -> **JSON**:
    ```json
    {
        "title": "드디어 성공인가요?",
        "content": "세션으로 글쓰기 테스트 중입니다."
    }
    ```
* **확인**: "게시글 작성이 완료되었습니다."가 나오면 성공!

---

### 4단계: 게시글 목록 조회 (Board List)
작성자 이름이 제대로 나오는지 최종 확인합니다.

* **Method**: `GET`
* **URL**: `http://localhost:8080/api/boards`
* **확인**: 응답 JSON 결과가 아래처럼 나오는지 보세요.
    ```json
    [
        {
            "id": 1,
            "title": "드디어 성공인가요?",
            "writerName": "홍길동"
        }
    ]
    ```

---

### 🚨 테스트 중 에러가 난다면? (응급 처치)

1.  **400 Bad Request**: JSON 오타(쉼표 등)가 있거나, DTO의 필드명과 JSON의 Key가 다른지 확인하세요.
2.  **"로그인이 필요합니다"**: 포스트맨이 쿠키를 제대로 보내지 않은 경우입니다. 로그인을 다시 하거나, 포스트맨 우측 상단의 `Cookies`를 다 지우고 2단계부터 다시 해보세요.
3.  **500 에러**: 인텔리제이 콘솔에 찍힌 로그를 확인하세요. `NullPointerException`이 났다면 세션에서 유저를 못 가져왔을 확률이 높습니다.

자, 이제 1단계부터 하나씩 날려보시겠어요? 어디서 막히거나 예상과 다른 결과가 나오면 바로 말씀해 주세요!