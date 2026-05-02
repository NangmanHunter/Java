회원가입처럼 **특정 버튼을 눌렀을 때만(Event-driven)** 실행되어야 하는 로직에는 사실 지금 사용 중인 **`$fetch`가 더 적절**합니다.

하지만 Nuxt의 `useFetch`를 사용하면서도 기능을 동일하게 유지하고 싶다면, `immediate: false` 옵션을 활용해 '준비 상태'로 만들었다가 필요할 때 호출하는 방식을 쓸 수 있습니다.

---

### `useFetch`로 변경한 코드

`useFetch`를 쓰면 상태(Pending, Error, Data)를 직접 관리하지 않아도 되어서 코드가 조금 더 선언적으로 변합니다.

```vue
<script setup>
const form = reactive({
  loginId: '',
  password: '',
  name: ''
})

const config = useRuntimeConfig()

// 1. useFetch 정의 (바로 실행되지 않도록 watch: false, immediate: false 설정)
const { data, error, pending, execute } = await useFetch('/api/members/signup', {
  baseURL: config.public.apiBase,
  method: 'POST',
  body: form,
  immediate: false, // 페이지 로드 시 바로 실행 방지
  watch: false      // form 값이 바뀐다고 자동으로 재요청 방지
})

// 2. 가입 로직
async function handleSignup() {
  if (!form.loginId || !form.password || !form.name) {
    alert("모든 필드를 입력해주세요.")
    return
  }

  // execute()를 호출하면 위에서 정의한 useFetch가 실행됩니다.
  await execute()

  if (error.value) {
    alert("가입 실패: " + (error.value.data || "서버 오류"))
  } else {
    alert(data.value || "가입 성공!")
    await navigateTo('/')
  }
}
</script>

<template>
  <UButton 
    block 
    label="가입하기" 
    :loading="pending" 
    @click="handleSignup" 
  />
  </template>
```

---

### 왜 회원가입에는 `$fetch`가 더 나을까요?

결론부터 말씀드리면, **회원가입, 게시글 작성, 삭제** 같은 '액션'에는 `$fetch`를 쓰는 것이 업계 표준에 더 가깝습니다. 그 이유는 다음과 같습니다:

1.  **동작 방식의 차이:**
    * `useFetch`: "이 데이터는 이 페이지의 **상태**야"라고 정의하는 느낌입니다. (주로 데이터 조회/SSR에 최적화)
    * `$fetch`: "이 **동작(Action)**을 수행해"라고 명령하는 느낌입니다.
2.  **중복 호출 위험:** `useFetch`는 기본적으로 반응형 변수(body 등)가 바뀌면 자동으로 다시 요청을 보낼 수 있습니다. 설정을 잘못하면 타이핑할 때마다 회원가입 API가 호출될 수도 있습니다.
3.  **직관성:** 회원가입은 성공 후 보통 다른 페이지로 떠나버리는 일회성 액션입니다. 굳이 `pending`이나 `data`를 페이지 전역 상태로 유지할 필요가 적습니다.


### 요약
* **단순함이 목적이라면:** 지금의 `$fetch` 방식을 유지하는 게 가장 깔끔합니다.
* **UI 피드백(로딩 처리 등)을 쉽게 하고 싶다면:** `useFetch`의 `pending` 상태를 활용하는 위 방식이 좋습니다.


지금 만드시는 **모듈형 게시판**에서 리스트를 불러올 때는 `useFetch`를, 글을 저장하거나 수정할 때는 `$fetch`를 섞어서 써보세요. 그게 가장 "Nuxt다운" 구조입니다!