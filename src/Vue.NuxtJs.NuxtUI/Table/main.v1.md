##
- Table.알아서 컬럼 이쁘게잡아줌.
- 대문자로 진행해줌.


##
오, 예리하시네요! `UTable`이 똑똑하게 동작하고 있는 건데요. 결론부터 말씀드리면 **Nuxt UI의 `UTable` 컴포넌트가 컬럼 이름을 자동으로 읽어서 "보기 좋게" 가공**해준 결과입니다.

동작 원리는 이렇습니다.

### 1. Nuxt UI의 자동 포맷팅 (Capitalization)
`UTable`에 별도의 `columns` 설정을 하지 않고 데이터(`rows`)만 던져주면, 컴포넌트는 내부적으로 다음과 같은 과정을 거칩니다.

1.  데이터(`boards`)의 첫 번째 객체에서 **Key값**을 모두 추출합니다. (예: `id`, `title`, `writerName`)
2.  이 Key값들을 사용자에게 보여주기 위해 **첫 글자를 대문자로 바꾸고(PascalCase/Capitalize)**, 가독성을 높입니다.
    * `id` → **Id**
    * `title` → **Title**
    * `writerName` → **Writer Name** (CamelCase인 경우 띄어쓰기까지 넣어주기도 함)

### 2. 왜 이렇게 하나요?
백엔드 개발자가 쓰는 DB 필드명이나 JSON 키값(`writer_id`, `viewCount`)은 프로그램용이라서 그대로 화면에 노출하면 사용자 입장에서 투박해 보일 수 있기 때문입니다. 그래서 `UTable`이 기본적으로 "예쁘게" 표기해 주는 기능을 가지고 있습니다.

### 3. 내가 원하는 한글 이름으로 바꾸려면?
"Id", "Title" 대신 **"번호", "제목"** 처럼 한글로 정확하게 표기하고 싶다면, 아까 설명해 드린 것처럼 **`columns` 속성**을 명시해 주면 됩니다.

```vue
<script setup lang="ts">
const { data: boards, pending, error } = await useFetch<any[]>('http://localhost:8080/api/boards');

// 직접 정의하면 자동 대문자 처리를 무시하고 내가 쓴 글자대로 나옵니다.
const columns = [
  { key: 'id', label: '번호' },
  { key: 'title', label: '제목' },
  { key: 'writerName', label: '작성자' },
  { key: 'viewCount', label: '조회수' }
]
</script>

<template>
  <div v-if="pending">로딩 중...</div>
  <div v-else-if="error">에러 발생</div>
  
  <UTable 
    v-else 
    :rows="boards ?? []" 
    :columns="columns" 
    class="flex-1" 
  />
</template>
```

---

### 요약
> **"Nuxt UI가 센스 있게 첫 글자를 대문자로 바꿔서 보여주고 있는 것"**입니다. 

하지만 실제 서비스에서는 보통 영문 대문자보다는 한글로 바꾸는 경우가 많으니, 위와 같이 `columns`를 정의해서 쓰시는 걸 추천드려요! 

혹시 또 예상치 못하게 자동으로 바뀌는 부분이 있나요? (참고로 `UTable`은 데이터가 아예 없을 때도 `Empty`라는 문구를 자동으로 띄워주기도 합니다.)