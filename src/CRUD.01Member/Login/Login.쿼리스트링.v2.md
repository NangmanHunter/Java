##


- `<form>`=Body+쿼리스트링형식
- @RequestParam을 쓰더라도 클라이언트(Frontend)에서 데이터를 어디에 실어서 보내느냐가 중요합니다



##
혼란스러우실 수 있는 부분입니다! `@RequestParam`이 왜 Body와 쿼리 스트링 양쪽에서 다 잡히는지, 그 **"정체"**를 명확히 짚어드릴게요.

결론부터 말씀드리면, **`@RequestParam`은 URL 뒤의 쿼리 스트링뿐만 아니라, HTTP Body에 담긴 `Form Data`까지 모두 읽을 수 있도록 설계**되어 있기 때문입니다.

---

## 1. `@RequestParam`의 이중성
Servlet 표준(HttpServletRequest.getParameter)을 기반으로 하는 Spring의 `@RequestParam`은 다음 두 곳을 동시에 확인합니다.

1.  **Query String:** `URL?A=B` 방식
2.  **Form Data (Body):** POST 요청 시 `Content-Type: application/x-www-form-urlencoded`로 들어온 Body 내용



따라서 질문하신 것처럼 **쿼리 스트링으로 날려도 당연히 잡히는 게 맞습니다.** 다만, 개발자가 "Body에 넣어서 보낸 데이터"를 받을 때도 이 어노테이션을 쓰기 때문에 혼동이 생기는 것이죠.

---

## 2. 왜 Body 데이터도 `@RequestParam`으로 받을까?
HTML의 `<form>` 태그를 사용해서 POST 전송을 하면, 브라우저는 기본적으로 데이터를 Body에 `key=value&key2=value2` 형태로 담아 보냅니다. 

이때의 데이터 포맷이 쿼리 스트링과 똑같기 때문에, 서블릿과 스프링은 이를 구분하지 않고 똑같이 **"Parameter"**로 취급해서 `@RequestParam` 하나로 처리할 수 있게 편의를 제공하는 것입니다.

---

## 3. 핵심은 "전송 방식"의 선택입니다
기술적으로 `@RequestParam`이 양쪽 다 읽을 수 있다고 해서 **둘의 보안 수준이 같은 것은 아닙니다.**

| 구분 | 쿼리 스트링 (`?A=B`) | Body (Form Data) |
| :--- | :--- | :--- |
| **위치** | URL 주소창에 포함 | HTTP 패킷 내부 (Body) |
| **보안** | **매우 취약** (로그, 히스토리 남음) | **비교적 안전** (HTTPS 암호화 시 보호됨) |
| **Spring 수신** | `@RequestParam`으로 수신 가능 | `@RequestParam`으로 수신 가능 |

즉, `@RequestParam`을 쓰더라도 클라이언트(Frontend)에서 데이터를 **어디에 실어서 보내느냐**가 중요합니다. 로그인을 구현하신다면, 반드시 POST 요청의 **Body**에 데이터를 실어 보내야 합니다.

---

## 4. 
참고: `@RequestBody`와는 다릅니다!
많은 분들이 헷갈려하시는 부분인데, 최근 많이 쓰이는 **JSON 데이터**는 `@RequestParam`으로 못 읽습니다.

* **`@RequestParam`**: `A=B&C=D` 형태의 데이터(URL 또는 Form Body)를 읽을 때 사용.
* **`@RequestBody`**: `{"A": "B"}` 같은 **JSON/XML** 형태의 Body 데이터를 읽을 때 사용. (이 경우엔 쿼리 스트링을 아예 보지 않습니다.)



### 정리하자면:
> "나는 쿼리 스트링으로 보냈는데 왜 잡히지?"라고 하신다면, **`@RequestParam`이 원래 둘 다 읽을 수 있는 녀석이라서** 그렇습니다. 하지만 보안을 생각한다면 쿼리 스트링이 아닌 **Body(Form Data)** 방식으로 보내도록 클라이언트 코드를 작성하셔야 안전합니다!

혹시 지금 테스트하고 계신 클라이언트(Postman이나 HTML)에서 데이터를 어떤 형식으로 보내고 계신가요? 형식을 보면 더 명확히 설명해 드릴 수 있습니다.