웹 개발을 하다 보면 주소창에 있는 정보를 가져와야 할 때가 정말 많은데, 그때 사용하는 아주 표준적인 방법입니다. 하나씩 쉽게 풀어드릴게요.

---

## 1. `window.location.search`가 주는 것
브라우저 주소창에 있는 전체 URL 중에서 **`?` 기호부터 그 뒤에 오는 내용들(Query String)**을 싹 긁어오는 역할을 합니다.

예를 들어 주소창이 이렇다면:
`https://mysite.com/board-detail.html?id=123&mode=edit`

* `window.location.search`를 실행하면 딱 이 부분만 나옵니다:
* **`"?id=123&mode=edit"`**

즉, 주소창에 있는 **"데이터 뭉텅이"**를 문자열(String) 형태로 가져오는 것입니다.

---

## 2. 왜 `URLSearchParams`를 쓰는 걸까?
위에서 가져온 값은 그냥 단순한 '글자'일 뿐입니다. 여기서 "id가 뭐야?"라고 물었을 때 `123`이라는 숫자만 쏙 빼오려면 문자열을 자르고 붙이는 복잡한 작업을 해야 하죠.

이걸 **자바스크립트가 대신 해주는 도구**가 바로 `URLSearchParams`입니다.

```javascript
// 1. 주소창에서 "?id=123&mode=edit"를 가져와서 도구에 넣습니다.
const urlParams = new URLSearchParams(window.location.search);

// 2. 이제 도구(urlParams)를 사용해서 원하는 값을 편하게 꺼냅니다.
const boardId = urlParams.get('id');   // 결과: "123"
const mode = urlParams.get('mode');    // 결과: "edit"
```

---

## 3. 비유하자면 이렇습니다
* **`window.location.search`**: 주소창 뒤에 달린 **"봉인된 택배 박스"**입니다. 안을 보려면 박스째로 가져와야 하죠.
* **`new URLSearchParams(...)`**: 그 박스를 뜯어서 내용물을 분류해주는 **"분류기"**에 넣는 과정입니다.
* **`urlParams.get('id')`**: 분류기에서 **"id라는 이름표가 붙은 물건"**만 쏙 집어 올리는 동작입니다.

---

## 4. 왜 이걸 넣어야 하나요?
사용자가 게시판 목록에서 특정 글을 클릭했을 때, 상세 페이지(`board-detail.html`) 입장에서는 **"내가 지금 몇 번 글을 보여줘야 하지?"**라는 정보가 필요합니다. 

그 정보를 전달하는 가장 흔한 약속이 URL 뒤에 `?id=1` 처럼 붙여주는 것이고, 백엔드 서버에 "1번 데이터 줘!"라고 요청(fetch)하기 위해 주소창에서 이 번호를 캐내는 것입니다.



### 요약
`window.location.search`는 **주소창의 파라미터 문자열**을 가져오는 것이고, `URLSearchParams`는 그걸 **프로그래밍적으로 쓰기 좋게 파싱(분류)**해주는 고마운 도구입니다!

이제 `boardId`에 왜 그 값이 담기는지 이해가 가시나요? 이 번호를 가지고 아까 만드신 `fetch`를 호출하게 되는 구조인 거죠!