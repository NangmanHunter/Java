

##
- Swagger.`CommonResponse<?>`<<`CommonResponse<DTO>`


##
Swagger(스웨거)는 한마디로 "개발자를 위한 API 설명서(명세서) 자동 생성 도구"입니다.

보통 백엔드 개발자가 API를 만들면, 프론트엔드 개발자에게 "이 주소로 요청을 보내면 이런 데이터가 올 거야"라고 알려줘야 하죠. 예전에는 이걸 엑셀이나 메모장에 일일이 적어서 줬지만, Swagger를 쓰면 **내가 짠 코드를 분석해서 웹 페이지 형태의 문서로 알아서 만들어줍니다.**

---

### 1. Swagger가 하는 핵심 역할

1. **시각화된 문서 제공:** 웹 브라우저에서 접속 가능한 UI(Swagger UI)를 통해 어떤 API가 있는지, 어떤 파라미터가 필요한지 보여줍니다.
2. **API 테스트 기능:** 포스트맨(Postman) 같은 별도의 프로그램 없이도 웹 페이지상에서 버튼만 누르면 API를 직접 호출해볼 수 있습니다.
3. **코드 기반 자동 갱신:** 내가 소스코드(DTO의 필드명 등)를 수정하면 Swagger 문서도 자동으로 업데이트됩니다.

---

### 2. 왜 반환 타입을 명시해야 할까요?

아까 질문하신 `ResponseEntity<CommonResponse<SignUpResponse>>`처럼 타입을 명확히 적어야 하는 이유가 바로 여기에 있습니다.

* **`<?>`나 `Object`로 반환하면:** Swagger는 "아, 뭔가 공통 응답이 오긴 하는데 그 안에 `data`가 무슨 모양인지 모르겠어"라고 판단하고 문서에 내용을 표시하지 못합니다.
* **`<SignUpResponse>`라고 명시하면:** Swagger가 `SignUpResponse` 클래스를 슥 훑어서 "아! 여기엔 `id`라는 숫자랑 `loginId`라는 글자가 담겨 있구나!"라고 문서에 예쁘게 표출해줍니다.

---

### 3. 프론트엔드 개발자가 보는 화면 (예시)

Swagger를 적용하면 프론트엔드 개발자는 다음과 같은 정보를 웹에서 바로 확인합니다.

> **POST /signup**
> * **Request Body:** `member` (JSON)
> * **Responses:** `201 Created`
> * **Response Model (구조):**
> ```json
> {
>   "status": "SUCCESS",
>   "message": "회원가입 완료!",
>   "data": {
>     "id": 1,
>     "loginId": "string"
>   }
> }
> 
> ```
> 
> 
> 

---

### 4. 어떻게 사용하나요?

Spring Boot 환경에서는 보통 `springdoc-openapi`라는 라이브러리를 의존성에 추가하기만 하면 바로 사용할 수 있습니다.

```gradle
// build.gradle 예시
implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0'

```

이 라이브러리를 넣고 서버를 띄운 뒤 `http://localhost:8080/swagger-ui/index.html`에 접속하면 여러분이 만든 API 명세서가 짠 하고 나타납니다.

**요약하자면:**
Swagger는 "내가 만든 API를 남들이(혹은 미래의 내가) 쓰기 편하게 자동으로 설명서를 만들어주는 고마운 비서"라고 생각하시면 됩니다!

이 Swagger 문서를 더 예쁘게 꾸미기 위해 DTO 필드에 설명을 다는 법도 궁금하신가요?

