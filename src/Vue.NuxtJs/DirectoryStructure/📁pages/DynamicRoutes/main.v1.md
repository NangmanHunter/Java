`board/notice`와 `board/community`를 하나의 파일 구조에서 `[type]`이라는 변수로 관리하고 싶으시다면, **Nuxt의 동적 파라미터(Dynamic Parameters)** 폴더를 만드시면 됩니다.

구조를 다음과 같이 잡으시면 됩니다.

---

### 1. 권장 폴더 구조

`pages` 폴더 안에 `board` 폴더를 만들고, 그 안에 대괄호로 감싼 `[type]` 폴더를 만듭니다.

```text
pages/
  board/
    [type]/          <-- 여기서 notice, community 등을 다 받음
      index.vue      <-- 목록 페이지 (board/notice)
      [id].vue       <-- 상세 페이지 (board/notice/1)

```

이렇게 구성하면 끝입니다! 아주 간단하죠?

---

### 2. 코드에서 꺼내 쓰는 법

이제 `board/notice`나 `board/community` 중 어디로 접속하든, `[type]` 폴더 안의 파일들이 실행됩니다. 코드에서는 이렇게 꺼냅니다.

**`pages/board/[type]/index.vue` (목록 페이지)**

```vue
<script setup lang="ts">
const route = useRoute()

// URL이 /board/notice 이면 "notice"
// URL이 /board/community 이면 "community"
const boardType = route.params.type as string

console.log('현재 게시판 타입:', boardType)
</script>

<template>
  <div>
    <h1>{{ boardType === 'notice' ? '공지사항' : '자유게시판' }}</h1>
  </div>
</template>

```

**`pages/board/[type]/[id].vue` (상세 페이지)**

```vue
<script setup lang="ts">
const route = useRoute()

const boardType = route.params.type as string // "notice"
const boardId = route.params.id as string     // "1"

console.log(`${boardType} 게시판의 ${boardId}번 글을 보고 계십니다.`)
</script>

```

---

### 💡 주의할 점 (Validation)

이렇게 `[type]`을 쓰면 사용자가 주소창에 `board/abcde`라고 아무렇게나 쳐도 이 페이지가 열리게 됩니다. 특정 게시판 타입만 허용하고 싶다면 `definePageMeta`의 `validate` 기능을 쓰면 안전합니다.

```javascript
// pages/board/[type]/index.vue
definePageMeta({
  validate: async (route) => {
    // 허용된 타입 리스트
    const allowedTypes = ['notice', 'community', 'qna']
    // 현재 경로의 type이 리스트에 있는지 확인
    return allowedTypes.includes(route.params.type as string)
  }
})

```

이렇게 설정해두면 `board/wrong` 같은 잘못된 주소로 들어왔을 때 자동으로 404 페이지를 띄워줍니다.

이제 폴더 이름을 `[type]`으로 바꾸러 가보실까요? 🛠️