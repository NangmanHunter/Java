Nuxt(정확히는 Vue 3)에서 `v-model`은 컴포넌트 간의 **양방향 데이터 바인딩**을 구현하는 핵심 메커니즘입니다. Vue 3로 넘어오면서 기본 동작 방식과 명세가 이전 버전(Vue 2)과 달라졌으므로, 현재 표준 명세를 정리해 드립니다.

---

## 1. 기본 원리: Sugar Syntax
`v-model`은 내부적으로 **props를 통한 데이터 전달**과 **이벤트를 통한 데이터 수정**을 하나로 합친 문법 설탕(Syntactic Sugar)입니다.

기본적으로 다음과 같이 변환됩니다:
*   **Props:** `modelValue`라는 이름으로 전달
*   **Event:** `update:modelValue`라는 이름으로 발생



---

## 2. 컴포넌트에서의 사용법 (Script Setup 기준)

### 자식 컴포넌트 (Child.vue)
`defineModel` 매크로를 사용하면 가장 깔끔하게 구현할 수 있습니다 (Vue 3.4+ 권장 방식).

```vue
<script setup>
// v-model 데이터를 직접 다루는 모델 선언
const model = defineModel() 

function updateValue() {
  model.value = "새로운 값"
}
</script>

<template>
  <input v-model="model" />
</template>
```

### 부모 컴포넌트 (Parent.vue)
```vue
<template>
  <Child v-model="message" />
</template>

<script setup>
const message = ref('Hello Nuxt!')
</script>
```

---

## 3. 주요 명세 및 변경 사항

### 명칭의 표준화
*   **Default Prop:** `modelValue`
*   **Default Event:** `update:modelValue`
*   Vue 2에서 사용하던 `value`와 `input` 이벤트는 더 이상 기본값이 아닙니다.

### 다중 v-model (Multiple Bindings)
하나의 컴포넌트에 여러 개의 `v-model`을 사용할 수 있습니다. 이 경우 인자를 전달하여 구분합니다.

```vue
<!-- 부모 -->
<UserProfile 
  v-model:firstName="first" 
  v-model:lastName="last" 
/>

<!-- 자식 -->
<script setup>
const firstName = defineModel('firstName')
const lastName = defineModel('lastName')
</script>
```

### v-model 수식어 (Modifiers)
`.trim`, `.number`, `.lazy`와 같은 기본 수식어 외에 **커스텀 수식어**를 만들 수 있습니다.

```javascript
// 자식 컴포넌트에서 수식어 확인
const [model, modifiers] = defineModel({
  set(value) {
    if (modifiers.uppercase) {
      return value.toUpperCase()
    }
    return value
  }
})
```

---

## 4. Nuxt 특이사항
Nuxt 환경에서는 대개 `useState`나 `Pinia`와 함께 `v-model`을 사용하게 됩니다. 
*   **SSR 주의점:** 서버 사이드 렌더링 시점에 `v-model`에 바인딩된 값이 `undefined`가 되지 않도록 초기값을 명확히 설정하는 것이 중요합니다.
*   **FormKit / VeeValidate:** 복잡한 폼 구현 시 Nuxt 생태계에서는 명세 기반의 라이브러리를 활용해 `v-model`을 확장해서 쓰기도 합니다.

더 구체적으로 **커스텀 수식어** 제작법이나 **이전 방식(defineProps/defineEmits)**의 명세가 필요하시면 말씀해 주세요!