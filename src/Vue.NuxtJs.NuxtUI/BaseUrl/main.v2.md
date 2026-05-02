##
`.env`


##
그 빨간 줄은 **`nuxt.config.ts`** 설정 파일에 해당 값이 정의되어 있지 않거나, TypeScript가 그 값이 존재한다는 것을 인지하지 못해서 발생하는 문제입니다.

Nuxt에서 `useRuntimeConfig()`를 통해 환경 변수를 안전하게 쓰려면 설정을 먼저 해줘야 합니다.

---

### 1. 해결 방법: `nuxt.config.ts` 설정
`nuxt.config.ts` 파일로 가셔서 `runtimeConfig` 부분을 아래와 같이 작성해 주세요. (이미 있다면 `public` 안에 내용을 추가하세요.)

```typescript
// nuxt.config.ts
export default defineNuxtConfig({
  runtimeConfig: {
    // public 안에 넣어야 클라이언트(브라우저)에서도 접근 가능합니다.
    public: {
      apiBase: process.env.NUXT_PUBLIC_API_BASE || 'http://localhost:8080'
    }
  },
  // ... 기타 설정
})
```

이렇게 설정하고 나면 `config.public.apiBase`의 빨간 줄이 사라집니다.

---

### 2. 왜 빨간 줄이 나왔나요? (원인)

1.  **정의되지 않은 속성:** `useRuntimeConfig()`는 기본적으로 비어있는 객체를 반환합니다. `nuxt.config.ts`에 명시하지 않으면 TypeScript는 "공공(public) 데이터 안에 `apiBase`라는 게 어딨어?"라고 따지는 것입니다.
2.  **타입 추론:** Nuxt는 `nuxt.config.ts`에 쓴 내용을 바탕으로 타입을 자동으로 생성(`.nuxt/types/schema.d.ts`)합니다. 설정을 완료하면 Nuxt가 다시 빌드되면서 타입을 인식하게 됩니다.

### 3. 임시 해결 (빨리 테스트해보고 싶을 때)
만약 설정 파일을 건드리기 귀찮고 바로 확인만 하고 싶다면, 아래와 같이 강제로 타입을 지정하거나 직접 주소를 넣을 수도 있습니다. (하지만 1번 방법이 정석입니다.)

* **직접 주소 입력:**
  ```typescript
  const { data: boards } = await useFetch<any[]>('/api/boards', {
    baseURL: 'http://localhost:8080'
  })
  ```
* **타입 단언 (비추천):**
  ```typescript
  const config = useRuntimeConfig() as any
  ```

---

### 💡 팁: `.env` 파일 활용하기
나중에 서버 배포 주소가 바뀌면 코드를 일일이 고치기 힘듭니다. 프로젝트 루트에 `.env` 파일을 만들고 아래처럼 관리하는 습관을 들이시면 좋습니다.

```text
# .env 파일
NUXT_PUBLIC_API_BASE=http://localhost:8080
```

이렇게 하면 `nuxt.config.ts`가 이 값을 읽어오고, 코드에서는 `config.public.apiBase`로 우아하게 쓸 수 있습니다. 빨간 줄 해결되셨나요? 만약 계속 나온다면 **VS Code를 한 번 껐다 켜거나 `Ctrl+Shift+P`를 눌러 `Restart Vue Server`**를 실행해 보세요!