설레는 마음으로 첫 테스트를 하셨는데 `Bad Request (400)` 에러를 만나셨군요! 괜찮습니다. 처음 Spring Boot로 API를 만들면 누구나 겪는 아주 흔한 과정입니다.

**400 Bad Request**는 쉽게 말해 "네가 보낸 데이터 형식이 내(서버)가 기대한 형식이랑 달라!"라는 뜻입니다. 포스트맨에서 확인해야 할 **3가지 핵심 체크포인트**를 알려드릴게요.

---

### 1. Header 설정 (Content-Type)
Spring Boot의 `@RequestBody`는 기본적으로 **JSON** 데이터를 받습니다. 포스트맨이 "내가 보내는 건 JSON이야"라고 서버에 알려줘야 합니다.

* **Headers** 탭 클릭
* **Key**: `Content-Type`
* **Value**: `application/json` 이 설정되어 있는지 확인하세요.
    * *(팁: 보통 Body 탭에서 'raw'와 'JSON'을 선택하면 자동으로 설정됩니다.)*

### 2. Body 데이터 형식 (JSON)
가장 실수가 많은 부분입니다. 자바의 `Member` 엔티티 필드명과 **정확히 똑같은 이름**으로 JSON을 작성해야 합니다.

* **Body** 탭 클릭 -> **raw** 선택 -> 우측 드롭다운에서 **JSON** 선택
* 아래와 같이 입력해 보세요 (중괄호와 따옴표 필수!):

```json
{
    "loginId": "testuser",
    "password": "password123",
    "name": "홍길동"
}
```
> **주의:** 엔티티 필드명이 `loginId`라면 JSON도 반드시 `loginId`여야 합니다. 대소문자까지 맞춰주세요.



---

### 3. URL 및 Method 확인
우리가 컨트롤러에 설정한 주소와 매칭되는지 다시 확인합니다.

* **Method**: `POST` (GET으로 되어있지 않은지 확인!)
* **URL**: `http://localhost:8080/api/members/signup`
    * `@RequestMapping("/api/members")` + `@PostMapping("/signup")` 이 합쳐진 주소입니다.

---

### 💡 만약 그래도 안 된다면? (원인 파악 꿀팁)

인텔리제이(IntelliJ) 아래쪽 **Console** 창을 확인해 보세요. 빨간색 글씨로 에러 로그가 찍혀 있을 겁니다.

1.  **"JSON parse error"**: JSON 오타가 났거나 형식이 틀렸을 때.
2.  **"Field 'loginId' is null"**: JSON의 키 이름이 엔티티 필드명과 다를 때.
3.  **"Method Not Allowed (405)"**: 컨트롤러는 POST인데 포스트맨에선 GET으로 보냈을 때.

---

### 🛠️ 실무 꿀팁: 로그 확인하기
컨트롤러 메서드 첫 줄에 `System.out.println("member = " + member.getLoginId());`를 추가해 보세요. 포스트맨을 쐈을 때 이 글자가 콘솔에 찍히는지 확인하는 것만으로도 어디서 막혔는지 금방 알 수 있습니다.

설정을 고치고 다시 한번 보내보시겠어요? 그래도 안 되면 콘솔에 찍힌 **에러 메시지**를 복사해서 알려주시면 바로 해결해 드릴게요!