

##
Nuxt 3에서 **Composables(컴포저블)**은 한 마디로 **"여러 컴포넌트에서 재사용할 수 있는 상태(데이터)와 로직을 모아놓은 함수"**를 말합니다.

Vue 3의 Composition API를 활용한 개념인데, Nuxt에서는 이를 더 편리하게 사용할 수 있도록 특정 규칙을 만들어 두었습니다.

---

### 1. 왜 사용하는 걸까요?
기존에는 여러 컴포넌트에서 똑같은 기능(예: 로그인 체크, 다크모드 전환, API 호출 로직)이 필요할 때 코드를 복사해서 붙여넣어야 했습니다.

* **문제점:** 코드가 중복되고, 수정할 때 모든 파일을 다 찾아다녀야 함.
* **해결책:** 로직을 `composables` 폴더에 딱 한 번만 정의해두고, 필요한 컴포넌트에서 불러와서 쓰자!

### 2. Composables의 핵심 특징
1.  **자동 임포트 (Auto-imports):** Nuxt의 가장 큰 장점입니다. `composables/` 폴더 안에 파일을 만들면, 컴포넌트에서 별도의 `import` 구문 없이 바로 함수를 사용할 수 있습니다.
2.  **상태 공유 (useState):** 일반적인 변수와 달리 `useState`를 사용하면 여러 컴포넌트가 **동일한 데이터**를 바라보게 할 수 있습니다. (아까 만든 로그인 상태가 대표적인 예시입니다.)
3.  **명명 규칙:** 관례적으로 함수의 이름은 **`use`**로 시작합니다. (예: `useAuth`, `useFetch`, `useUser`)

---

### 3. 구조로 보는 예시



#### (1) 정의하기 (`composables/useCounter.js`)
```javascript
export const useCounter = () => {
  // 전역적으로 공유될 상태
  const count = useState('count', () => 0)

  const increment = () => count.value++
  const decrement = () => count.value--

  return {
    count,
    increment,
    decrement
  }
}
```

#### (2) 컴포넌트에서 사용하기 (`pages/index.vue`)
```vue
<script setup>
// import 할 필요 없음! 바로 사용 가능
const { count, increment } = useCounter()
</script>

<template>
  <div>
    <p>현재 숫자: {{ count }}</p>
    <button @click="increment">증가</button>
  </div>
</template>
```

---

### 4. Composables vs Utils (차이점)
폴더 구조를 보다 보면 `utils` 폴더도 있는데, 둘은 용도가 다릅니다.

* **Composables:** Vue의 **상태(Ref, Reactive, useState)**나 **생명주기(onMounted 등)**를 다룰 때 사용합니다. (예: 로그인 상태 관리)
* **Utils:** 상태와 상관없는 **순수 계산 함수**나 **포맷팅**에 사용합니다. (예: 날짜를 "방금 전"으로 변환하기, 숫자 콤마 찍기)

---

### 요약
> **"로직 전용 가방"**이라고 생각하시면 됩니다. 

공통적으로 쓰이는 데이터(상태)와 그 데이터를 요리하는 함수들을 가방(`composables`)에 넣어두고, 페이지나 컴포넌트 어디서든 꺼내 쓰는 방식입니다.

아까 만든 `useAuth`도 **"로그인 상태라는 데이터"**와 **"상태를 바꾸는 로직"**을 담은 가방인 셈이죠! 이제 좀 감이 오시나요?