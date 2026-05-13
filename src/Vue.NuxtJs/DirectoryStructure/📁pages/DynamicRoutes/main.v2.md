`definePageMeta`는 Nuxt 3에서 제공하는 컴파일러 매크로(Compiler Macro)입니다.

이 함수는 아무 곳에서나 쓸 수 있는 게 아니라, **`pages/` 폴더 안에 있는 Vue 컴포넌트(페이지) 파일의 `<script setup>` 블록** 내에서만 사용할 수 있도록 설계되어 있습니다.

---

### 1. 정의하는 위치 (어디에 쓰나요?)

반드시 **해당 페이지 파일의 최상단** 근처에 작성해야 합니다.

* **파일 위치:** `pages//*.vue` (컴포넌트 폴더인 `components/`에서는 작동하지 않습니다.)
* **작성 위치:** `<script setup>` 안에서 가장 윗부분에 두는 것이 관례입니다.

```vue
<script setup lang="ts">
// 페이지 최상단에 작성
definePageMeta({
  layout: 'custom',      // 레이아웃 설정
  middleware: 'auth',    // 미들웨어 설정
  keepalive: true,       // 페이지 상태 유지 여부
  // ... 기타 설정
})

const route = useRoute()
// 나머지 로직
</script>

```

---

### 2. 왜 `import`가 없나요?

`definePageMeta`는 `useFetch`나 `useRoute`처럼 런타임에 실행되는 일반 함수라기보다, **Nuxt가 빌드될 때 페이지의 설정을 읽어내기 위한 신호(Macro)** 역할을 합니다.

* 따라서 `import { definePageMeta } from '#imports'` 같은 코드를 쓸 필요가 없습니다.
* Nuxt가 코드를 읽을 때 "아, 이 페이지는 이런 설정을 가지고 있구나"라고 자동으로 판단해서 라우터 설정에 반영해 줍니다.

---

### 3. 주요 활용 사례

보통 다음과 같은 설정을 주입할 때 사용합니다.

* **레이아웃 결정:** `layout: 'default'` 또는 `layout: false`
* **미들웨어 연결:** 특정 페이지 진입 전 권한 체크(`middleware: ['auth']`)
* **사용자 정의 데이터:** `key: 'value'` 형태로 저장하면 `route.meta.key`로 꺼내 쓸 수 있습니다.
* **유효성 검사:** 앞에서 설명한 `validate` 속성을 통해 잘못된 파라미터(`[type]`, `[id]`) 접근을 차단합니다.

---

### 4. 주의사항 (중요!)

1. **변수 접근 불가:** `definePageMeta` 내부에서는 `<script setup>` 안에 선언된 로컬 변수나 함수를 직접 참조할 수 없습니다. 왜냐하면 이 함수는 컴파일 타임에 페이지 밖으로 "추출"되어 처리되기 때문입니다.
* ❌ 잘못된 예시: `const color = 'red'; definePageMeta({ theme: color })`


2. **페이지 파일 전용:** `app.vue`나 `components/MyButton.vue` 같은 곳에서는 동작하지 않습니다. 오직 **라우팅이 발생하는 페이지 파일**에서만 의미가 있습니다.

---

### 💡 정리하자면

`definePageMeta`는 "이 페이지의 성격(레이아웃, 보안, 데이터 검증)을 정의하는 명세서"라고 보시면 됩니다.

지금 `[type]` 폴더 구조를 만드셨다면, 그 안의 `index.vue`나 `[id].vue` 파일 상단에 이 매크로를 써서 `notice`, `community`가 맞는지 검증하는 코드를 넣으시면 딱 좋습니다!