결론부터 말씀드리면, **`[id].vue` 말고도 엄청나게 많습니다!** Nuxt의 파일 시스템 라우팅은 단순히 숫자 ID뿐만 아니라, 유저님이 상상하시는 거의 모든 방식의 URL 구조를 변수로 만들 수 있어요. `route.params`는 그 대괄호 `[]` 안에 적은 **이름**을 그대로 키(Key)값으로 가져오게 됩니다.

가장 유용하게 쓰이는 3가지 패턴을 정리해 드릴게요.

---

### 1. 이름은 내가 정하기 (Named Params)
파일 이름을 꼭 `[id].vue`로 지을 필요가 없습니다. 데이터의 성격에 따라 이름을 자유롭게 지으면, 그게 곧 `route.params`의 이름이 됩니다.

* **파일명:** `pages/board/[category]/[postSerialNumber].vue`
* **실제 주소:** `/board/notice/55`
* **결과:** * `route.params.category` → `'notice'`
    * `route.params.postSerialNumber` → `'55'`
    > 이렇게 하면 코드를 볼 때 "이게 게시글 번호구나"라고 훨씬 직관적으로 알 수 있죠.

### 2. 여러 개를 한 번에 낚아채기 (Catch-all Routes)
대괄호 안에 점 세 개(`...`)를 찍으면, 그 하위의 모든 경로를 하나의 배열로 싹 다 긁어옵니다.

* **파일명:** `pages/files/[...slug].vue`
* **실제 주소:** `/files/2026/april/report.pdf`
* **결과:**
    * `route.params.slug` → `['2026', 'april', 'report.pdf']` (배열로 들어옵니다!)
    > 폴더 구조가 깊어지는 자료실이나 블로그 카테고리 만들 때 정말 유용합니다.

### 3. 있어도 되고 없어도 되는 변수 (Optional Params)
대괄호를 두 번 감싸면(`[[ ]]`), 해당 자리에 값이 없어도 페이지가 터지지 않고 정상 작동합니다.

* **파일명:** `pages/user/[[nickname]].vue`
* **주소 1:** `/user/nangman` → `route.params.nickname`은 `'nangman'`
* **주소 2:** `/user` → 페이지는 뜨지만 `route.params.nickname`은 `undefined`
    > "마이페이지"처럼 내 정보를 볼 때와 남의 정보를 볼 때 같은 페이지를 쓰고 싶을 때 사용합니다.

---

### 💡 아까 에러가 났던 이유 (복습)

유저님의 파일 구조가 만약 `pages/board/notice/[id].vue` 처럼 **중간 폴더명이 고정**되어 있었다면:
1.  Nuxt는 `notice`를 변수(`params`)로 인식하지 않고 그냥 **고정된 길**로 인식합니다.
2.  그래서 `route.params.boardType`을 호출했을 때 "어? 나는 `id` 말고는 아는 변수가 없는데?" 하고 `undefined`를 뱉었던 거예요.



---

### 🛠️ 낭만 팁
만약 게시판 종류마다 다른 페이지를 보여주기 싫고, 하나의 파일로 모든 게시판을 처리하고 싶다면 구조를 이렇게 바꿔보세요:

**`pages/board/[boardType]/[id].vue`**

이렇게 만들면:
* `/board/notice/1` → `params: { boardType: 'notice', id: '1' }`
* `/board/community/5` → `params: { boardType: 'community', id: '5' }`

이제 `route.params.boardType`을 코드 어디서든 자유롭게 꺼내 쓸 수 있게 됩니다. 아주 강력하죠? :)