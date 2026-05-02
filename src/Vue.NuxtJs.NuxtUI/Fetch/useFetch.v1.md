오류가 발생하는 가장 가능성 높은 이유는 **`useFetch`가 반환하는 `data`의 객체 구조** 때문입니다.

Nuxt 3의 `useFetch`는 데이터를 바로 내뱉는 게 아니라, `data`, `pending`, `error`, `refresh` 같은 상태를 포함한 **Ref 객체**를 반환합니다. `UTable`은 순수 배열(Array)을 기대하는데, Ref 객체 전체를 전달하면 테이블이 데이터를 읽지 못해 터지거나 아무것도 표시되지 않습니다.

아래 해결 방법들을 확인해 보세요.

---

### 1. `.value` 없이 넘겼을 때의 문제 (가장 흔한 원인)
`UTable`에 넣을 때는 Ref의 실제 값인 배열을 넘겨줘야 합니다. 하지만 Template 안에서는 자동으로 언래핑(Unwrapping)이 되므로, 보통은 **데이터가 null일 때** 발생하는 오류일 확률이 높습니다.

```vue
<template>
  <UTable :data="boards || []" class="flex-1" />
</template>
```

### 2. API 응답 구조 확인 (Mapping)
API에서 데이터를 줄 때, 만약 `[{...}, {...}]` 형태가 아니라 `{ success: true, list: [...] }` 처럼 한 번 감싸서 보내고 있다면 `UTable`이 인식하지 못합니다. 이럴 때는 `transform` 옵션을 사용하세요.

```typescript
const { data: boards } = await useFetch('/api/boards', {
  // API 응답 구조가 { list: [...] } 형태라면 list만 골라냅니다.
  transform: (res) => res.list || res 
});
```

### 3. 클라이언트/서버 하이드레이션 오류
`await useFetch`를 `<script setup>` 최상단에서 사용하면, 해당 컴포넌트는 **비동기 컴포넌트**가 됩니다. 이를 사용하는 부모 컴포넌트에서 `<Suspense>`로 감싸주지 않으면 렌더링 시점에 데이터가 없어 오류가 날 수 있습니다.

안전하게 사용하려면 다음과 같이 구조를 잡는 것이 좋습니다.

```vue
<script setup lang="ts">
// 1. fetch로 데이터를 가져옵니다.
const { data: boards, pending, error } = await useFetch('/api/boards');
</script>

<template>
  <div class="flex flex-col h-full">
    <div v-if="pending">로딩 중...</div>
    
    <div v-else-if="error">데이터를 불러오는 중 문제가 발생했습니다.</div>
    
    <UTable v-else :data="boards" class="flex-1" />
  </div>
</template>
```

---

### 체크리스트
1. **콘솔(F12) 확인:** 에러 메시지에 `data.value is null` 혹은 `Invalid prop: type check failed for prop "data"` 같은 문구가 있나요?
2. **API 경로:** `/api/boards` 경로가 서버(server/api/boards.ts)에 정확히 존재하고, JSON 배열을 반환하나요?
3. **가상 스크롤/UI 라이브러리:** Nuxt UI 버전에 따라 `:rows`를 사용해야 하는 경우도 있습니다. (`:data`가 아니라 `:rows="boards"`)

혹시 브라우저 콘솔에 뜨는 **빨간색 에러 메시지**를 복사해서 보여주시면 원인을 더 정확히 짚어드릴 수 있습니다!