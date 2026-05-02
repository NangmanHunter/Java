## `index.html`
- `src/main/resources/static/index.html`






##
초경량으로 시작하시는군요! 좋습니다. Spring Boot는 별도의 컨트롤러 설정 없이도 특정 위치에 파일을 두면 자동으로 **Welcome Page**로 인식해서 뿌려줍니다.

가장 빠르고 간편한 방법 2가지를 알려드릴게요.

---

### 1. 정적 리소스 경로 (가장 추천)
단순히 HTML, CSS, JS로만 구성된 페이지라면 아래 경로에 `index.html`을 만드세요.

* **경로:** `src/main/resources/static/index.html`

스프링 부트는 서버가 실행될 때 `static` 폴더 아래에 있는 `index.html`을 찾아 브라우저가 `http://localhost:8080/`으로 접속했을 때 가장 먼저 보여줍니다.

### 2. 템플릿 엔진 경로 (Thymeleaf 사용 시)
나중에 서버에서 데이터를 받아와서 화면에 뿌려줄 계획(Thymeleaf 등 사용)이 있다면 이 경로를 씁니다.

* **경로:** `src/main/resources/templates/index.html`

**주의:** 이 경우에는 `static`과 달리 **컨트롤러**에서 직접 연결해줘야 합니다.
```java
@Controller
public class HomeController {
    @GetMapping("/")
    public String index() {
        return "index"; // templates/index.html을 찾아감
    }
}
```

---

### 💡 초경량 시작을 위한 팁

지금은 다른 설정 없이 **`src/main/resources/static/index.html`**에 파일을 만들고 아래 코드를 복사해서 넣은 뒤, 서버를 재시작해보세요.

```html
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>My Board Service</title>
</head>
<body>
    <h1>반갑습니다! 게시판 서비스 시작 페이지입니다.</h1>
    <p>이제 여기서 로그인과 게시글 목록으로 가는 링크를 만들면 되겠네요.</p>
</body>
</html>
```



---

### 🛠️ 확인 포인트
1.  **서버 재시작:** 정적 파일은 추가한 뒤 서버를 한 번 껐다 켜야 반영됩니다.
2.  **포트 확인:** 기본 설정이라면 `http://localhost:8080`에 접속했을 때 방금 만든 화면이 떠야 합니다.

성공적으로 화면이 뜨나요? 이제 이 `index.html`에서 아까 만든 API들을 호출하는 **로그인 버튼**이나 **글쓰기 버튼**을 만들어볼까요? 아니면 바로 CSS로 꾸며보고 싶으신가요?