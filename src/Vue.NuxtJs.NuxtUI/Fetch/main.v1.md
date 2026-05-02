##
- [x] axios

- [x] useFetch
- [ ] $fetch

- [ ] fetch




##
Vue 3와 Nuxt UI(`UTable`)를 사용하고 계시군요! API에서 받아온 데이터를 테이블에 뿌려주려면 **1. 데이터를 담을 반응형 변수(ref)를 준비**하고, **2. `onMounted`나 `useFetch`를 통해 데이터를 가져와서 그 변수에 할당**해주면 됩니다.

현재 코드 구조에 맞춰서 수정하는 방법은 다음과 같습니다.

### 1. 코드 수정 예시 (fetch 사용)

```vue
<script setup lang="ts">
// 1. 초기값은 빈 배열로 설정합니다.
const data = ref([]);

// 2. API 호출 함수 작성
const fetchBoards = async () => {
  try {
    const response = await $fetch('/api/boards'); // API 호출
    // API 응답 데이터(id, title, writerName 등)를 ref에 할당
    data.value = response; 
  } catch (error) {
    console.error('데이터를 불러오지 못했습니다:', error);
  }
};

// 3. 컴포넌트가 마운트될 때 실행
onMounted(() => {
  fetchBoards();
});

// 만약 테이블 컬럼명을 커스텀하고 싶다면 columns 설정을 추가하세요
const columns = [
  { key: 'id', label: 'ID' },
  { key: 'title', label: '제목' },
  { key: 'writerName', label: '작성자' },
  { key: 'views', label: '조회수' } // API에 views가 있다면 추가
];
</script>

<template>
  <UTable :data="data" :columns="columns" class="flex-1" />
</template>
```

---

### 2. 핵심 포인트

* **데이터 매핑:** `UTable`은 `:data`로 들어온 배열의 각 객체 안에 있는 **Key 값**을 기준으로 데이터를 보여줍니다. 
    * API 결과가 `{ id: 1, title: '안녕', writerName: '홍길동' }` 이라면, 테이블은 자동으로 `id`, `title`, `writerName`이라는 컬럼을 찾으려고 할 거예요.
* **변수 할당:** API 통신이 성공하면 `data.value = 결과값` 처럼 `.value`를 통해 데이터를 교체해줘야 화면이 즉시 업데이트(반응형)됩니다.
* **Nuxt 방식 (`useFetch` 권장):** 만약 Nuxt 3를 사용 중이시라면 아래처럼 더 간단하게 쓸 수도 있습니다.

```typescript
const { data: boards } = await useFetch('/api/boards');
// 이후 <UTable :data="boards" /> 로 바로 사용 가능
```

### 3. 참고할 점
현재 제작 중인 플랫폼의 게시판 기능이라면, 나중에 데이터 양이 많아질 것을 대비해 **페이지네이션(Pagination)** 처리도 고려해 보시면 좋습니다. `UTable` 공식 문서에 페이지네이션 연동 방법이 잘 나와 있으니 참고해 보세요!

데이터 형식이나 API 응답 구조가 혹시 제가 예시로 든 것과 다른가요? 다르다면 그 부분을 말씀해 주시면 더 정확한 매핑 방법을 알려드릴게요.