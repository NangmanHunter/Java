
##
fetch.catch.error.생각보다좁다
fetch.catch.error.아예없는경우
fetch.catch.error.ConnectionRefused
fetch.catch.error.ErrorConnectionRefused
fetch.catch.error.ERR_CONNECTION_REFUSED
fetch.catch.error.`ERR_CONNECTION_REFUSED`


##
`$fetch`.catch.error.400ㆍ500


##
`fetch`를 사용할 때 `catch`로 빠지는 경우는 생각보다 범위가 좁습니다. 핵심은 **"서버와 대화를 시도조차 못 했거나, 대화 도중 줄이 끊겼을 때"**라고 보시면 됩니다.

구체적으로 어떤 상황에서 `catch`가 호출되는지 정리해 드릴게요.

---

### 1. `catch`로 가는 경우 (네트워크 레벨의 실패)
서버가 응답을 보내주지 못하는 물리적인 오류 상황들입니다.

* **네트워크 단절:** 인터넷 연결이 끊겨 있거나 비행기 모드일 때.
* **잘못된 도메인:** `http://this-site-does-not-exist.com` 처럼 존재하지 않는 도메인에 요청할 때 (DNS 에러).
* **서버 다운:** 서버 자체가 꺼져 있어서 연결 시도(Connection Refused)가 실패할 때.
* **CORS 위반:** 보안 정책상 허용되지 않은 출처에서 데이터를 요청하여 브라우저가 차단할 때.
* **요청 중단(Abort):** `AbortController`를 사용해 강제로 요청을 취소했을 때.

### 2. `catch`로 가지 **않는** 경우 (HTTP 상태 코드)
서버가 "야, 너 그거 권한 없어(403)" 혹은 "서버 터졌어(500)"라고 **대답이라도 해준다면** `fetch`는 성공(`try` 블록)으로 처리합니다.

* **404 Not Found:** 페이지를 못 찾아도 서버가 "없어"라고 응답한 것이므로 성공.
* **500 Internal Server Error:** 서버 내부 오류가 나도 응답이 온 것이므로 성공.
* **401/403:** 인증 에러가 나도 성공.

---

### 💡 한눈에 보는 비교



| 구분 | 상황 | `fetch` 결과 |
| :--- | :--- | :--- |
| **물리적 차단** | 랜선 뽑힘, 도메인 오타, 방화벽 차단 | **`catch`로 이동 (Reject)** |
| **논리적 에러** | 404(없음), 500(서버터짐), 400(잘못된요청) | **`try` 유지 (Resolve)** |

---

### 🛠 그래서 실제로는 어떻게 코딩하나요?
`fetch`는 400, 500번대를 에러로 안 잡아주기 때문에, 보통 아래처럼 **`response.ok`**라는 속성을 써서 수동으로 에러를 던져줍니다.

```javascript
try {
  const res = await fetch('https://api.example.com/data');

  // response.ok는 상태 코드가 200~299일 때만 true입니다.
  if (!res.ok) {
    // 여기서 직접 에러를 던지면 catch 블록으로 점프합니다!
    throw new Error(`서버 에러 발생! 상태코드: ${res.status}`);
  }

  const data = await res.json();
  console.log(data);

} catch (error) {
  // 1. 네트워크가 죽었을 때
  // 2. 위에서 throw new Error로 던졌을 때 둘 다 여기서 잡힙니다.
  console.error("에러 발생:", error.message);
}
```

**요약하자면:**
`fetch`의 `catch`는 **"서버에 닿지도 못했을 때"**만 작동하는 안전장치라고 이해하시면 가장 정확합니다! 그 외의 400, 500번대 에러는 `response.ok`로 직접 걸러내야 해요.