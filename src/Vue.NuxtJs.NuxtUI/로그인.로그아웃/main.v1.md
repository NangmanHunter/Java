당연히 가능합니다! Nuxt와 Vue에서는 **`v-if`**와 **`v-else`**라는 조건부 렌더링 문법을 사용하면 아주 간단하게 구현할 수 있습니다.

로그인 상태를 체크하려면 보통 **`Cookie`**를 확인하거나 전역 상태(Store)를 활용하는데, 지금 세션 방식을 쓰고 계시니 브라우저에 저장된 쿠키 존재 여부나 사용자 정보를 담은 `useState`를 활용하는 것이 좋습니다.

---

### 1. 전역 로그인 상태 만들기 (`composables/useAuth.js`)
로그인 상태는 여러 컴포넌트(로그인 버튼, 로그아웃 버튼, 게시판 등)에서 공통으로 알아야 하므로, Nuxt의 `useState`를 사용해 공유 상태를 하나 만듭니다.

```javascript
// composables/useAuth.js
export const useAuth = () => {
  // 전역 로그인 상태 (기본값: false)
  const isLoggedIn = useState('isLoggedIn', () => false)

  // 로그인 상태 업데이트 함수
  const setLoggedIn = (status) => {
    isLoggedIn.value = status
  }

  return {
    isLoggedIn,
    setLoggedIn
  }
}
```

---

### 2. 버튼을 모아둔 상위 컴포넌트 수정
`v-if`를 사용해 `isLoggedIn`이 `true`일 때와 `false`일 때 보여줄 버튼을 나눕니다.

```vue
<script setup>
const { isLoggedIn } = useAuth()
</script>

<template>
  <div class="flex gap-2">
    <template v-if="!isLoggedIn">
      <AuthLogInButton />
      <AuthSignUpButton />
    </template>

    <template v-else>
      <AuthLogoutButton />
    </template>

    <ModuleButton />
  </div>
</template>
```

---

### 3. 로그인/로그아웃 성공 시 상태 변경하기

#### 로그인 성공 시 (`handleLogin` 함수 내부)
로그인 응답이 "환영합니다"를 포함하면 상태를 `true`로 바꿔줍니다.

```javascript
const { setLoggedIn } = useAuth()

// ... handleLogin 내부
if (response.includes("환영합니다")) {
  setLoggedIn(true) // 로그인 상태로 변경
  await navigateTo('/board')
}
```

#### 로그아웃 버튼 클릭 시 (`AuthLogoutButton.vue`)
로그아웃은 서버 세션을 날리는 과정이 필요합니다.

```vue
<script setup>
const { setLoggedIn } = useAuth()

async function handleLogout() {
  try {
    const config = useRuntimeConfig()
    // 서버 로그아웃 API 호출 (세션 무효화)
    await $fetch('/api/members/logout', {
      baseURL: config.public.apiBase,
      method: 'POST',
      credentials: 'include'
    })
    
    setLoggedIn(false) // 로그인 상태 해제
    alert("로그아웃 되었습니다.")
    navigateTo('/login')
  } catch (error) {
    console.error("로그아웃 실패", error)
  }
}
</script>

<template>
  <UButton
    label="LogOut"
    icon="i-lucide-log-out"
    color="neutral"
    variant="outline"
    @click="handleLogout"
  />
</template>
```

---

### 💡 팁: 새로고침해도 유지하려면?
`useState`는 새로고침하면 초기화됩니다. 새로고침해도 로그인 상태를 유지하려면 `app.vue`나 `middleware`에서 **서버로부터 현재 사용자 정보를 가져오는 API**를 한 번 호출해서 `isLoggedIn` 값을 다시 채워주는 로직을 추가하면 완벽합니다.

지금 당장은 `isLoggedIn` 상태만 왔다 갔다 하게 만들어도 버튼 전환은 아주 잘 작동할 거예요!