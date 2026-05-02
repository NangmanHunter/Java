기존 HTML 코드를 그대로 `.vue` 파일로 옮기면 로드가 안 되거나 스타일이 깨지는 게 당연합니다. **Vue/Nuxt의 동작 방식**과 **순수 HTML/JS의 동작 방식**이 완전히 다르기 때문인데요, 크게 3가지 이유가 있습니다.

### 1. 왜 깨지고 안 돌아가나요?

* **DOM 접근 방식:** Vue는 `document.getElementById`로 값을 가져오지 않습니다. 대신 **`v-model`**이라는 양방향 데이터 바인딩을 사용합니다.
* **컴포넌트 구조:** `<script>` 태그에 `setup` 속성이 빠져 있거나, Nuxt UI 컴포넌트(`UInput`, `UButton`)가 아닌 일반 HTML 태그를 쓰고 있어서 Nuxt UI의 스타일 혜택을 못 보고 있습니다.
* **라우팅:** `window.location.href` 대신 Nuxt 전용 라우터인 `MapsTo`를 써야 페이지 전환이 매끄럽습니다.

---

### 2. Nuxt 3 스타일로 수정한 코드 (추천)

기존 로직을 유지하면서 Nuxt UI와 Vue 3 문법으로 깔끔하게 바꾼 버전입니다.

```vue
<script setup>
// 1. 상태 관리 (v-model과 연결)
const form = reactive({
  loginId: '',
  password: '',
  name: ''
})

// 2. 가입 로직
async function handleSignup() {
  if (!form.loginId || !form.password || !form.name) {
    alert("모든 필드를 입력해주세요.")
    return
  }

  try {
    // Nuxt 내장 $fetch 사용 (BaseURL 설정이 되어있다면 더 편리함)
    const response = await $fetch('/api/members/signup', {
      method: 'POST',
      body: form
    })

    alert("가입 성공!")
    // 3. 페이지 이동 (Nuxt 방식)
    await navigateTo('/') 
  } catch (error) {
    alert("가입 실패: " + error.data)
  }
}
</script>

<template>
  <div class="signup-wrapper">
    <UCard class="signup-container">
      <template #header>
        <h2 class="text-center font-bold text-xl">회원가입</h2>
      </template>

      <div class="space-y-4">
        <UInput v-model="form.loginId" placeholder="아이디" />
        <UInput v-model="form.password" type="password" placeholder="비밀번호" />
        <UInput v-model="form.name" placeholder="이름" />
        
        <UButton block label="가입하기" @click="handleSignup" />

        <div class="links text-center text-sm text-gray-500 mt-4">
          이미 계정이 있으신가요? 
          <ULink to="/" class="text-primary font-medium">로그인</ULink>
        </div>
      </div>
    </UCard>
  </div>
</template>

<style scoped>
/* Scoped를 붙여야 이 컴포넌트에만 스타일이 적용됩니다 */
.signup-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f4f7f6;
}
.signup-container {
  width: 100%;
  max-width: 350px;
}
</style>
```

---

### 3. 무엇이 바뀌었나요?

1.  **`<script setup>`:** Vue 3의 최신 문법입니다. `import` 없이도 `reactive`, `ref` 등을 바로 쓸 수 있고 코드가 훨씬 간결합니다.
2.  **`v-model`:** `document.getElementById`는 이제 잊으셔도 됩니다. `v-model="form.loginId"`라고 쓰면 입력창의 값이 실시간으로 JavaScript 변수에 동기화됩니다.
3.  **`UCard`, `UInput`, `UButton`:** 직접 CSS로 `border-radius`나 `box-shadow`를 잡을 필요 없이, Nuxt UI 컴포넌트를 쓰면 알아서 깔끔한 디자인이 적용됩니다.
4.  **`scoped` 스타일:** `<style scoped>`라고 쓰면 이 파일의 CSS가 다른 페이지(index.vue 등)에 영향을 주지 않습니다. 기존에 스타일이 깨졌던 이유 중 하나가 전역 스타일 오염이었을 확률이 높습니다.

### 💡 팁
현재 백엔드(MemberController)가 `/api/members/signup`으로 되어 있는데, Nuxt의 프록시 설정이 안 되어 있다면 주소를 `http://localhost:8080/api/...` 처럼 풀 주소로 적거나 `nuxt.config.ts`에서 API 경로 설정을 확인해 보세요!

이제 이 코드를 한번 붙여넣어 보시겠어요? 훨씬 "프론트엔드 개발자"스러운 코드가 될 겁니다.