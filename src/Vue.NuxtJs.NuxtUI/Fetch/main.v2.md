

##
```typescript
const { data: payments } = await useFetch<Payment[]>('/api/admin/members', {
  default: () => [] 
})
```
```typescript
// 1. API로부터 데이터를 가져옵니다.
// useFetch는 Nuxt 3에서 권장하는 데이터 페칭 방식입니다.
const { data: payments } = await useFetch<Payment[]>('/api/admin/members', {
  // 필요한 경우 옵션 추가 (예: 서버 사이드 렌더링 여부 등)
  default: () => [] // 데이터가 로드되기 전 기본값 설정
})

// 2. 만약 기존 변수명 'data'를 그대로 유지하고 싶다면:
// const { data } = await useFetch<Payment[]>('/api/admin/members', { default: () => [] })
```


##
- Destructuring을 통해 이름을 변경할 수 있습니다.
- Destructuring
- 구조분해
- 구조분해할당
- { data: payments }
- 이런거일듯.


##
Nuxt 3의 데이터 페칭 방식인 `useFetch` 또는 `useAsyncData`를 사용하면 간단하게 구현할 수 있습니다. 

현재 정적으로 선언된 `data` 변수를 API 호출 결과로 대체하는 방법은 다음과 같습니다.

### 1. 




### 2. 전체적인 구조 (Script 부분)

전체적인 흐름은 다음과 같이 변경됩니다.

```vue
<script setup lang="ts">
import { h, resolveComponent } from 'vue'
import type { TableColumn } from '@nuxt/ui'

const UBadge = resolveComponent('UBadge')

type Payment = {
  id: string
  date: string
  status: 'paid' | 'failed' | 'refunded'
  email: string
  amount: number
}

// API 호출: 'data'라는 이름으로 바로 사용하거나, 
// Destructuring을 통해 이름을 변경할 수 있습니다.
const { data, pending, error } = await useFetch<Payment[]>('/api/admin/members', {
  lazy: false, // 필요에 따라 true로 설정하여 클라이언트 사이드에서만 로드 가능
  default: () => []
})

const columns: TableColumn<Payment>[] = [
  // ... 기존 컬럼 정의와 동일
]
</script>

<template>
  <!-- 로딩 상태 처리 (선택 사항) -->
  <div v-if="pending">Loading...</div>
  <div v-else-if="error">에러가 발생했습니다: {{ error.message }}</div>
  
  <UTable
    v-else
    ref="table"
    :data="data" 
    :columns="columns" 
    class="flex-1" 
  />
</template>
```

---

### 주요 포인트

*   **`useFetch` 사용**: Nuxt 엔진이 서버/클라이언트 환경에 맞춰 최적화된 요청을 보냅니다.
*   **Type Safety**: `useFetch<Payment[]>`와 같이 제네릭을 사용하여 API 응답 데이터에도 타입을 적용할 수 있습니다.
*   **반응형(Reactive)**: `useFetch`가 반환하는 `data`는 기본적으로 `ref` 객체이므로, 테이블의 `:data="data"`에 그대로 전달하면 API 로드가 완료되는 순간 테이블이 자동으로 갱신됩니다.
*   **에러 및 로딩 처리**: `pending`과 `error` 상태를 활용해 사용자에게 현재 데이터를 불러오는 중임을 알리는 UI를 추가하면 훨씬 친절한 화면이 됩니다.

이제 `/api/admin/members` 엔드포인트에서 `Payment` 타입 형식에 맞는 JSON 배열을 반환하기만 하면 정상적으로 작동할 거예요!</Payment[]></Payment[]>




#
`lazy: false`


##
Nuxt 3의 `useFetch`에서 `lazy` 옵션은 데이터를 가져오는 **'타이밍'**과 **'차단(Blocking)'** 여부를 결정하는 중요한 설정입니다.

쉽게 설명하자면, **"데이터가 올 때까지 화면 렌더링을 멈출 것인가, 아니면 일단 화면부터 보여줄 것인가?"**의 차이입니다.

---

### 1. `lazy: false` (기본값)
`lazy` 설정을 하지 않거나 `false`로 두면, Nuxt는 해당 데이터를 다 가져오기 전까지 **페이지 이동(Navigation)을 완료하지 않습니다.**

*   **동작:** 사용자가 링크를 클릭하면, 서버나 API에서 데이터를 모두 받아온 뒤에야 비로소 다음 페이지 화면이 나타납니다.
*   **장점:** 화면이 떴을 때 데이터가 이미 다 채워져 있으므로 "깜빡임"이나 "데이터 없음" 상태를 처리할 필요가 없습니다. (SEO에 유리)
*   **단점:** API 응답이 느리면 사용자는 클릭 후 한참 동안 이전 페이지에 머물게 되어 서비스가 답답하게 느껴질 수 있습니다.

### 2. `lazy: true`
`lazy: true`로 설정하면, 데이터를 가져오는 것과 상관없이 **일단 페이지 이동을 즉시 수행**합니다.

*   **동작:** 페이지가 바로 바뀌고, 데이터는 백그라운드에서 비동기로 가져옵니다. 
*   **장점:** 화면 전환이 매우 빠르고 매끄럽게 느껴집니다.
*   **단점:** 데이터가 도착하기 전까지는 `data`가 `null`인 상태이므로, 화면에 "로딩 중..." 메시지를 띄우거나 빈 테이블을 보여주는 처리가 반드시 필요합니다.

---

### 요약 비교

| 특징 | `lazy: false` (기본값) | `lazy: true` |
| :--- | :--- | :--- |
| **페이지 전환** | 데이터 수신 완료 후 전환 | 즉시 전환 |
| **사용자 경험** | 약간의 대기 시간이 발생할 수 있음 | 즉각적인 반응, 로딩 바 필요 |
| **데이터 상태** | `data`가 항상 존재함 (이미 가져옴) | 초기에는 `data`가 `null`일 수 있음 |
| **권장 상황** | SEO가 중요하거나 필수 데이터일 때 | 대시보드처럼 빠른 화면 전환이 중요할 때 |

### 코드 예시 (Lazy 사용 시)

만약 `lazy: true`를 쓰신다면, 데이터가 아직 없을 때를 대비해 다음과 같이 처리하는 것이 좋습니다.

```vue
<script setup>
const { data, pending } = await useFetch('/api/admin/members', {
  lazy: true
})
</script>

<template>
  <!-- pending이 true이면 로딩 표시, false이면 테이블 표시 -->
  <div v-if="pending">
    데이터를 불러오는 중입니다...
  </div>
  <UTable v-else :data="data" :columns="columns" />
</template>
```

**질문하신 코드에서는 `lazy: false`가 기본값이므로 굳이 적지 않으셔도 되지만, 적어준다면 "이 데이터는 반드시 로드된 후에 화면을 보여주겠다"는 의도를 명확히 하는 역할을 합니다.**