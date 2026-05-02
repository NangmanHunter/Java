가장 깔끔하게 **Nuxt 3**와 **Nuxt UI**를 바닥부터 설치해서 화면을 띄우는 법을 정리해 드립니다. 현재 겪고 계신 404 에러나 설정 꼬임을 방지하기 위해 **가장 표준적인 방법**으로 안내할게요.

---

## 1. Nuxt 프로젝트 생성
터미널(또는 CMD)을 열고 아래 명령어를 순서대로 입력하세요.

```bash
# 1. 새 프로젝트 생성 (프로젝트명: my-nuxt-app)
npx nuxi@latest init my-nuxt-app

# 2. 프로젝트 폴더로 이동
cd my-nuxt-app

# 3. 의존성 설치
npm install
```

## 2. Nuxt UI 모듈 설치
Nuxt 프로젝트 안에서 Nuxt UI와 필요한 종속성을 설치합니다.

```bash
npm install @nuxt/ui
```



## 3. 설정 파일 수정 (`nuxt.config.ts`)
프로젝트 루트에 있는 `nuxt.config.ts` 파일을 아래와 같이 수정하세요. (기존 내용 다 지우고 복사하셔도 됩니다.)

```typescript
// nuxt.config.ts
export default defineNuxtConfig({
  // 1. 모듈 등록
  modules: ['@nuxt/ui'],

  // 2. 개발 도구 및 호환성 설정
  devtools: { enabled: true },
  compatibilityDate: '2024-04-03',

  // 3. (선택) SSR을 끄고 싶다면 설정 (클라이언트 전용 앱)
  ssr: false
})
```

## 4. `app.vue` 작성
이제 아까 작성하셨던 코드를 넣으시면 됩니다. `app.vue` 파일의 내용을 전부 지우고 아래 코드로 교체하세요.

```vue
<template>
  <div class="min-h-screen bg-slate-50 flex items-center justify-center p-6">
    <UCard class="w-full max-w-lg shadow-xl">
      <template #header>
        <div class="flex justify-between items-center">
          <h1 class="text-2xl font-bold italic text-indigo-600">Nangman Hunter Boot Camp</h1>
          <UBadge color="primary" variant="subtle">v3 Stable</UBadge>
        </div>
      </template>

      <div class="space-y-6">
        <div class="p-4 bg-white border border-slate-200 rounded-lg">
          <p class="text-slate-600">
            "복잡한 설정 지옥을 탈출하셨습니다. 이제 기깔나게 코딩할 차례입니다."
          </p>
        </div>

        <div class="grid grid-cols-2 gap-4">
          <UButton block color="black" @click="test">검정 버튼 테스트</UButton>
          <UButton block color="orange" variant="outline">브랜드 버튼 테스트</UButton>
          <UButton block color="primary" variant="solid" class="col-span-2">
            이건 잘 나올 겁니다 (Primary)
          </UButton>
        </div>
      </div>
    </UCard>
  </div>
</template>

<script setup>
const test = () => alert('성공입니다! 이제 백엔드 API를 붙여보세요.')
</script>
```

## 5. 프로젝트 실행
마지막으로 서버를 실행합니다.

```bash
npm run dev
```

---

### ⚠️ 주의사항 및 팁
1.  **404 에러 방지:** 서버 실행 후 터미널에 뜨는 주소(보통 `http://localhost:3000`)로 정확히 접속했는지 확인하세요.
2.  **Color 이름:** Nuxt UI에서 `color="info"`, `color="warning"` 등은 Tailwind 색상 체계를 따릅니다. 기본적으로 `primary`는 **Green**으로 잡혀 있을 텐데, 이걸 **Indigo**로 바꾸고 싶다면 `app.config.ts` 파일(없으면 생성)에서 설정해야 합니다.
3.  **파일 확장자:** 반드시 `nuxt.config.ts`로 만드세요. `.js`보다 에러를 훨씬 잘 잡아줍니다.

이제 브라우저에서 기깔나는 화면이 보이시나요? 아까와 달리 설정이 아주 가벼워졌을 겁니다! 도움 필요하시면 또 말씀해 주세요.