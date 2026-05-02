##
- `Script.Template.Style`

- Vue 공식 스타일 가이드([Priority C: Recommended](https://www.google.com/search?q=https://vuejs.org/style-guide/rules-recommended.html%23single-file-component-top-level-element-order))에서는 **스크립트 - 템플릿 - 스타일** 순서를 가장 권장합니다.


##
Vue.js 공식 문서에서는 이를 \*\*"싱글 파일 컴포넌트(SFC) 스타일 가이드"\*\*라고 부르며, 권장하는 순서가 명확히 정해져 있습니다.

### 1\. 권장되는 작성 순서


```vue
<script setup>
/* 로직 (Script) */
</script>

<template>
  </template>

<style scoped>
/* 스타일 (Style) */
</style>
```

-----

### 2\. 왜 이 순서인가요? (이유)

1.  **로직 우선 확인:** 컴포넌트가 어떤 데이터를 쓰고 어떤 기능을 하는지(Script)를 먼저 파악한 뒤, 화면 구조(Template)를 보는 것이 코드를 이해하는 데 더 빠르기 때문입니다.
2.  **`script setup`의 확산:** Vue 3로 넘어오면서 `<script setup>`이 표준이 되었고, 많은 개발자들이 변수와 로직을 상단에 두는 방식을 선호하게 되었습니다.
3.  **스타일은 항상 마지막:** 스타일은 보통 양이 매우 길어질 수 있기 때문에, 핵심 로직과 구조를 방해하지 않도록 맨 아래에 두는 것이 관례입니다.

-----

### 3\. 다른 순서도 가능한가요?

네, 사실 기술적으로는 순서가 바뀌어도 에러가 나지 않습니다. 과거 Vue 2 시절에는 **Template - Script - Style** 순서가 국민 룰이었기 때문에, 여전히 이 순서를 사용하는 프로젝트도 많습니다.

  * **Template 우선:** "화면이 어떻게 생겼는지"가 제일 궁금한 개발자들이 선호합니다.
  * **Script 우선 (공식 권장):** "이 컴포넌트가 무슨 일을 하는지"가 제일 궁금한 개발자(특히 대규모 프로젝트)들이 선호합니다.

-----

### 4\. 공식 문서 및 설정 팁

  * **공식 문서:** [Vue.js 스타일 가이드 - SFC 최상위 엘리먼트 순서](https://www.google.com/search?q=https://vuejs.org/style-guide/rules-recommended.html%23single-file-component-top-level-element-order)
  * **자동 정렬 방법:** 매번 순서를 신경 쓰기 귀찮다면, **ESLint**나 **Prettier** 설정으로 강제할 수 있습니다.
      * `eslint-plugin-vue`를 사용하면 `vue/component-tags-order` 규칙을 통해 팀원 모두가 똑같은 순서로 코드를 짜도록 강제할 수 있어 협업에 매우 유리합니다.

**팁:** 효율과 딥 워크를 중시하신다면, 하나를 정해서(가급적 공식 권장안인 **Script -\> Template**) 손에 익히시는 것이 코드 생산성을 유지하는 데 큰 도움이 됩니다\!

지금 하시는 프로젝트에서는 어떤 순서가 더 편하게 느껴지시나요? 보통 백엔드 경험이 있으시면 로직이 먼저 보이는 Script 우선 방식을 선호하시더라고요.