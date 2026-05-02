

##
게시글 상세 페이지(`/board/[id]`)를 Nuxt UI와 Vue 3의 Composition API 방식으로 재구성해 드립니다.

바닐라 JS에서는 URL 파라미터를 직접 추출했지만, Nuxt에서는 **`useRoute()`**를 사용하고, 데이터는 **`v-model`**이나 **`{{ }}`(콧수염 문법)**으로 바인딩합니다.

### Nuxt UI 기반 게시글 상세 페이지 (`pages/board/[id].vue`)

```vue
<script setup>
// 1. 라우트 및 상태 관리
const route = useRoute()
const config = useRuntimeConfig()
const boardId = route.params.id // URL의 [id] 값을 자동으로 가져옴

const board = ref(null)
const loading = ref(true)

// 로그인 정보 (composables/useAuth.js에서 만든 상태 사용)
const { isLoggedIn } = useAuth()
// 실제 프로젝트라면 현재 로그인한 유저의 ID 정보도 useAuth에 들어있어야 함
const currentUserId = useState('userId', () => 1) 

// 2. 데이터 불러오기
async function fetchBoardDetail() {
  try {
    const data = await $fetch(`/api/boards/${boardId}`, {
      baseURL: config.public.apiBase,
      credentials: 'include'
    })
    board.value = data
  } catch (error) {
    alert("게시글을 찾을 수 없습니다.")
    navigateTo('/board')
  } finally {
    loading.value = false
  }
}

// 페이지 로드 시 실행
onMounted(() => {
  fetchBoardDetail()
})

// 수정 페이지 이동 함수
function goToEdit() {
  navigateTo(`/board/edit/${boardId}`)
}
</script>

<template>
  <UContainer class="py-10">
    <div v-if="loading" class="space-y-4">
      <USkeleton class="h-12 w-[250px]" />
      <USkeleton class="h-4 w-full" />
      <USkeleton class="h-[300px] w-full" />
    </div>

    <UCard v-else-if="board">
      <template #header>
        <div class="flex justify-between items-start">
          <div>
            <h1 class="text-3xl font-bold text-gray-900 dark:text-white">
              {{ board.title }}
            </h1>
            <div class="flex gap-3 mt-3 text-sm text-gray-500">
              <span>👤 작성자: {{ board.writerName }}</span>
              <span>🔢 번호: {{ board.id }}</span>
              <span>👁️ 조회수: {{ board.viewCount }}</span>
            </div>
          </div>
          
          <UButton
            v-if="currentUserId === board.writerId"
            icon="i-heroicons-pencil-square"
            color="orange"
            variant="soft"
            label="수정하기"
            @click="goToEdit"
          />
        </div>
      </template>

      <div class="min-h-[300px] py-4 text-lg leading-relaxed whitespace-pre-wrap">
        {{ board.content }}
      </div>

      <template #footer>
        <div class="flex justify-between items-center">
          <UButton
            icon="i-heroicons-arrow-uturn-left"
            color="gray"
            variant="ghost"
            label="목록으로 돌아가기"
            to="/board"
          />
          
          </div>
      </template>
    </UCard>
  </UContainer>
</template>
```

---

### 🛠 주요 변경 및 개선 사항

1.  **동적 라우팅 (`route.params.id`)**:
    * `URLSearchParams`를 쓸 필요가 없습니다. 파일명이 `[id].vue`라면 `route.params.id`에 해당 값이 자동으로 매핑됩니다.
2.  **데이터 바인딩 (`{{ board.title }}`)**:
    * `document.getElementById(...).innerText` 같은 노가다(?) 코드가 사라졌습니다. 데이터가 `board.value`에 담기면 HTML은 알아서 갱신됩니다.
3.  **조건부 렌더링 (`v-if`)**:
    * 수정 버튼을 숨기기 위해 스타일(`display: none`)을 조작하는 대신, `v-if="currentUserId === board.writerId"` 조건을 사용하여 HTML 구조 자체에서 제어합니다.
4.  **UX 향상 (`USkeleton`)**:
    * 데이터를 불러오는 동안 화면이 텅 비어있지 않도록 Nuxt UI의 **스켈레톤(Skeleton)** 컴포넌트를 사용하여 로딩 중임을 시각적으로 보여줍니다.
5.  **스타일링 (Tailwind CSS)**:
    * 별도의 `<style>` 태그 없이도 `class="whitespace-pre-wrap"` 등을 사용하여 기존에 의도했던 줄바꿈 기능을 그대로 구현했습니다.

### 💡 팁: 수정 페이지 경로
위 코드에서 `goToEdit` 함수는 `/board/edit/${boardId}`로 이동하도록 설정했습니다. 실제 프로젝트의 디렉토리 구조(`pages/board/edit/[id].vue`)에 맞춰 이 경로만 살짝 조절해 주시면 됩니다.