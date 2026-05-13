

##
Nuxt 3에서 `$fetch`와 `useFetch`는 비슷해 보이지만, **데이터를 호출하는 시점과 방식**에서 아주 큰 차이가 있습니다. 결론부터 말하자면, "단순한 이벤트 처리냐, 페이지 초기 로딩용 데이터냐"의 차이예요.


## 💡 한눈에 비교하기

| 구분 | $fetch | useFetch |
| --- | --- | --- |
| **성격** | 라이브러리 함수 (Action) | 컴포저블 (State management) |
| **반환값** | 응답 데이터 그 자체 | `{ data, pending, error, refresh }` |
| **SSR 최적화** | X (중복 호출 발생 가능) | O (Payload 공유로 효율적임) |
| **사용 시점** | 클릭 이벤트, 비동기 메서드 내부 | 컴포넌트 `setup()` 최상단 |

---

## 언제 무엇을 쓸까요?

### ✅ useFetch를 써야 할 때

페이지에 접속하자마자 화면에 보여줘야 하는 리스트나 상세 정보 데이터.

```javascript
// 페이지 로드 시 바로 데이터를 가져옴
const { data, pending } = await useFetch('/api/posts')

```

### ✅ $fetch를 써야 할 때

사용자가 버튼을 눌렀을 때 실행되는 API 요청 (로그인, 게시글 작성 등).

```javascript
const submitData = async () => {
  // 이벤트 핸들러 안에서는 $fetch가 정석!
  const response = await $fetch('/api/login', {
    method: 'POST',
    body: { user: 'gemini' }
  })
}

```

> **핵심 요약:**
> "서버 사이드 렌더링(SSR)의 이점을 챙기면서 페이지 데이터를 가져오고 싶다"면 **`useFetch`**,
> "단순히 특정 시점에 데이터를 주고받고 싶다"면 **`$fetch`**를 선택하세요!

혹시 지금 작성 중인 코드에서 어떤 상황에 적용할지 고민 중이신가요? 구체적인 케이스를 알려주시면 더 정확히 가이드해 드릴게요.