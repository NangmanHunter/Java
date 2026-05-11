Nuxt는 Vue 3를 기반으로 하므로, `v-model`의 공식 명세는 **Vue.js 공식 문서**가 가장 정확하고 표준입니다. 특히 Nuxt 3에서 권장하는 **Composition API(Script Setup)** 방식의 명세를 확인할 수 있는 주요 페이지들을 정리해 드립니다.

### 1. 폼 입력 바인딩 (기본 사용법)
가장 기초적인 `input`, `checkbox`, `select` 등에서의 사용법과 `.lazy`, `.number`, `.trim` 같은 수식어 명세가 적혀 있습니다.
*   **공식 문서:** [Vue.js - Form Input Bindings](https://vuejs.org/guide/essentials/forms.html)

### 2. 컴포넌트 v-model (커스텀 컴포넌트 제작)
본인이 만든 컴포넌트에 `v-model`을 적용하는 방법입니다. 최근 표준인 `defineModel` 매크로 사용법이 상세히 나와 있습니다.
*   **공식 문서:** [Vue.js - Component v-model](https://vuejs.org/guide/components/v-model.html)

### 3. API 레퍼런스 (상세 기술 명세)
`defineModel`이나 내부적인 `props`, `emits`의 구조 등 기술적인 세부 사항을 확인할 때 좋습니다.
*   **defineModel 명세:** [Vue.js API - defineModel()](https://vuejs.org/api/sfc-script-setup.html#definemodel)

### 4. Nuxt 공식 가이드
Nuxt 환경에서 Vue를 어떻게 활용하는지에 대한 전반적인 가이드입니다. 직접적인 `v-model` 전용 페이지보다는 Vue 3 기능이 Nuxt에 어떻게 통합되는지 설명합니다.
*   **Nuxt 문서:** [Nuxt - Vue.js Development](https://nuxt.com/docs/3.x/guide/concepts/vuejs-development)

**팁:** 최근 Nuxt 3(Vue 3.4+) 개발에서는 이전의 `props`와 `emits`를 따로 선언하는 방식보다 **`defineModel()`**을 사용하는 것이 표준이므로, 위 2번 링크의 내용을 중점적으로 보시면 됩니다.