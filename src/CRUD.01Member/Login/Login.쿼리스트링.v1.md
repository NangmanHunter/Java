결론부터 말씀드리면, **로그인 같은 민감한 정보를 다룰 때 POST 방식의 URL에 쿼리 스트링(`?A=B`)을 포함하는 것은 보안상 매우 위험하며 권장되지 않는 방식**입니다.

POST 메서드를 사용하더라도 데이터를 URL에 붙여서 보내면 POST의 핵심적인 보안 장점을 스스로 포기하는 것과 같습니다. 왜 위험한지, 그리고 어떻게 처리해야 하는지 핵심을 정리해 드릴게요.

---

## 1. 왜 위험한가요?

POST 요청을 보낼 때 데이터를 URL 뒤에 붙이는 쿼리 스트링 방식은 다음과 같은 노출 위협이 있습니다.

* **브라우저 히스토리(방문 기록):** 브라우저는 URL 전체를 기록에 남깁니다. 로그인을 쿼리 스트링으로 처리하면 아이디와 비밀번호가 브라우저 기록에 평문으로 남게 되어, 공용 PC 등에서 누구나 확인할 수 있습니다.
* **서버 로그:** 웹 서버(Nginx, Apache 등)나 애플리케이션 서버의 액세스 로그(Access Log)에는 보통 요청 URL이 그대로 기록됩니다. 관리자나 서버 탈취자가 로그를 통해 비밀번호를 볼 수 있게 됩니다.
* **Referer 헤더 노출:** 해당 페이지에서 외부 링크를 클릭해 다른 사이트로 이동할 때, 브라우저가 보내는 `Referer` 헤더에 이전 페이지의 URL(비밀번호 포함)이 담겨 전달됩니다. 외부 사이트 운영자에게 내 비밀번호를 전달하는 꼴이 됩니다.
* **캐싱:** 프록시 서버나 CDN 등에서 해당 URL을 캐싱할 경우 데이터가 예상치 못한 곳에 저장될 수 있습니다.

---

## 2. 올바른 방식: Message Body 사용

Spring 개발 시 POST 방식의 목적은 데이터를 **Request Body(본문)**에 숨겨서 보내는 것입니다. HTTP 패킷을 뜯어보지 않는 이상 겉으로는 데이터가 드러나지 않으며, HTTPS(SSL/TLS) 암호화를 적용하면 본문 내용은 전송 과정에서도 안전하게 보호됩니다.

### 잘못된 예시 (위험)
```http
POST /login?username=user1&password=1234 HTTP/1.1
Host: example.com
```

### 올바른 예시 (권장)
데이터를 URL이 아닌 본문에 담습니다.
```http
POST /login HTTP/1.1
Host: example.com
Content-Type: application/x-www-form-urlencoded

username=user1&password=1234
```

---

## 3. Spring에서의 처리 방법

Spring Controller에서 본문으로 전달된 데이터를 받을 때는 다음과 같은 어노테이션을 주로 사용합니다.

* **`@RequestParam`**: HTML Form 전송(`application/x-www-form-urlencoded`) 방식으로 본문에 담겨 올 때 사용합니다.
* **`@RequestBody`**: JSON 방식(`application/json`)으로 데이터를 주고받을 때 사용합니다. 현대적인 웹 서비스나 API 개발에서 가장 많이 쓰입니다.

### 코드 예시
```java
@PostMapping("/login")
public String handleLogin(@RequestParam String username, @RequestParam String password) {
    // URL에 노출되지 않고 HTTP Body를 통해 전달된 값을 처리합니다.
    // 비즈니스 로직 수행...
    return "loginSuccess";
}
```

---

## 요약
1.  **로그인 시 쿼리 스트링 사용은 절대 금물**입니다. (보안 취약점)
2.  POST 방식을 쓸 때는 반드시 데이터를 **HTTP Body**에 담아서 전송하세요.
3.  개발자 도구의 Network 탭에서 내가 보낸 요청의 `Payload` 부분에 데이터가 있고, `General`의 `Request URL`에는 데이터가 없는지 확인하는 습관을 들이는 것이 좋습니다.