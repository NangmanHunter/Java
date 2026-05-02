

##
```ts
    colors: {
      primary: 'blue',
      neutral: 'slate'
    }
```
```ts
    colors: {
      primary: 'red',
      neutral: 'neutral'
    }
```
```ts
    colors: {
      primary: 'red',
      neutral: 'emerald'
    }
```
```ts
    colors: {
      primary: 'pink',
      neutral: 'zinc'
    }
```
```ts
    colors: {
      primary: 'rose',
      neutral: 'zinc'
    }
```


##
`primary` 값에 넣을 수 있는 추천 컬러들입니다:
* **`red`**: 강렬하고 정석적인 빨간색
* **`orange`**: 좀 더 따뜻하고 에너지가 느껴지는 색
* **`rose`**: 세련되고 약간 핑크빛이 도는 붉은색 (SaaS 템플릿에 잘 어울려요)


##
Nuxt UI Pro(SaaS 템플릿의 기반)를 사용 중이시군요! 이 템플릿에서 전체적인 색감(파란색 → 붉은색)을 바꾸는 방법은 의외로 간단합니다. CSS 파일을 일일이 수정하는 게 아니라, **`app.config.ts`** 파일이나 **`tailwind.config.ts`**에서 설정값만 툭 바꿔주면 됩니다.

가장 효과적인 방법 두 가지를 알려드릴게요.

---

### 1. 가장 빠른 방법: `app.config.ts` 수정
Nuxt UI는 `primary` 색상을 설정 파일에서 바로 지정할 수 있습니다. 프로젝트 루트 폴더에 있는 `app.config.ts` 파일을 열어보세요.

```typescript
// app.config.ts
export default defineAppConfig({
  ui: {
    primary: 'red',   // 'blue' 또는 'indigo'로 되어있을 부분을 'red'나 'orange'로 변경
    gray: 'neutral'   // 회색 톤도 필요하면 변경 가능
  }
})
```
* **효과:** 버튼, 링크, 하이라이트 등 템플릿 전반에 쓰인 파란색(`primary`)이 즉시 붉은색 계열로 바뀝니다.

---

### 2. 정밀한 조절: `tailwind.config.ts` 확인
만약 특정 섹션이나 배경색이 여전히 파란색이라면, Tailwind CSS의 설정에서 색상 확장이 어떻게 되어 있는지 확인해야 합니다.

```typescript
// tailwind.config.ts
import type { Config } from 'tailwindcss'

export default <Partial<Config>> {
  theme: {
    extend: {
      colors: {
        // 여기에 특정 파란색이 강제로 지정되어 있는지 확인하세요.
        // 보통은 Nuxt UI가 자동으로 primary를 매핑하지만, 
        // 직접 'blue'를 넣은 곳이 있다면 'red'로 수정해야 합니다.
      }
    }
  }
}
```

---

### 3. 코드 내 직접 수정 (특정 컴포넌트)
보내주신 `index.vue` 코드 안에서 특정 컴포넌트만 색을 바꾸고 싶다면, Nuxt UI 컴포넌트의 `color` 속성을 활용할 수 있습니다.

* **예시 (UPageCTA의 버튼 색상 등):**
    ```vue
    <UPageCTA
      v-bind="page.cta"
      variant="naked"
      class="overflow-hidden"
      :ui="{ base: 'text-red-500' }" 
    >
    ```
    *(다만, 전체 톤을 바꾸는 데는 1번 방법이 가장 깔끔합니다.)*

---



