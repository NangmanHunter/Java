<!-- 
- 첫번째 prop 기준으로 잡음
- 하위 다른거있으면 싹다날림

No data
- Table자체 빈배열경우 잡아다 진행해주는듯.
- 속성조차 아예없음.

방어코드
- `?? []` >> `|| []`
- 좀더 좁히게잡아줌
-->
```vue
<script setup lang="ts">
const { data: boards, pending, error } = await useFetch<any[]>('http://localhost:8080/api/boards');

</script>

<template>

  
    <div v-if="pending">로딩 중...</div>
    <div v-else-if="error">데이터를 불러오는 중 문제가 발생했습니다.</div>
    <UTable v-else :data="boards ?? []" class="flex-1" />
</template>
```