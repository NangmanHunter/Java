

### 1. 스프링 부트의 "자동 탐색" 약속
스프링 부트는 서버가 켜질 때 "정적인 파일(HTML, CSS, JS, 이미지)"을 찾기 위해 특정 폴더들을 미리 지정해두었습니다. 그 목록은 다음과 같습니다:

1.  `/static`
2.  `/public`
3.  `/resources` (이건 `src/main/resources` 안의 `resources` 폴더를 의미합니다)
4.  `/META-INF/resources`

이 폴더들 안에 있어야만 사용자가 브라우저에 `http://localhost:8080/index.html`이라고 쳤을 때 스프링이 **"아, 여기서 찾아주면 되는구나!"** 하고 파일을 꺼내줍니다.

---

### 2. 왜 `resources/` 바로 아래는 안 되나요?
`src/main/resources` 폴더는 사실 **"자바 실행 환경(Classpath)에 포함될 모든 설정 파일이 모이는 곳"**입니다. 여기에는 `application.properties` 같은 민감한 설정 파일도 들어가죠.

만약 `resources/` 바로 아래에 있는 파일들을 아무나 접근할 수 있게 열어두면, 보안상 위험할 수 있습니다. 그래서 스프링은 **"정적인 파일은 반드시 `static` 같은 약속된 하위 폴더에 넣어라. 그래야 내가 안전하게 바깥으로 보여줄게"**라고 규칙을 정해둔 것입니다.



---

### 3. "그냥 index.html"의 특별한 대접
스프링 부트는 `static/` 폴더 안에서 `index.html`이라는 이름을 발견하면, 이를 **Welcome Page(기본 메인 페이지)**로 인식합니다. 

* `static/index.html`이 있으면: `localhost:8080` 접속 시 바로 이 파일이 뜹니다.
* `resources/index.html` (바깥에) 있으면: `localhost:8080` 접속 시 404 에러가 납니다.

---

### 4. 만약 폴더를 바꾸고 싶다면?
물론 설정을 통해 바꿀 수는 있지만, 굳이 그럴 필요가 없습니다. 스프링 생태계에서는 **"설정보다 관례(Convention over Configuration)"**가 중요하기 때문이죠. 나중에 다른 개발자가 프로젝트를 봤을 때도 당연히 `static` 폴더를 먼저 뒤져볼 테니까요.

---

### 결론
지금은 고민하지 마시고 **`src/main/resources/static/index.html`** 경로를 유지해 주세요. 그래야 별도의 설정 없이 바로 브라우저에서 화면을 확인하실 수 있습니다. 

자, 이제 `static/index.html`을 만드셨다면 서버를 재시작하고 `http://localhost:8080`으로 들어가 보세요. 아까 만든 입력창이 잘 뜨나요?