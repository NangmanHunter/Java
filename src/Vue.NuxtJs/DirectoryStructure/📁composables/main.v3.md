
##
* **기존 방식 (Options API):** 데이터, 메서드, 계산된 속성이 정해진 칸(data, methods, computed)에 나누어져 있어 로직이 흩어짐.
* **Composables 방식:** 관련된 데이터와 로직을 하나의 함수 안에 묶어서 관리함.

### 2. Nuxt에서의 특별한 점: 자동 임포트(Auto-imports)
Nuxt는 프로젝트 루트의 `composables/` 디렉토리에 있는 파일들을 자동으로 인식합니다.

* `composables/useCounter.ts`라는 파일을 만들면,
* 별도의 `import` 구문 없이도 어느 컴포넌트에서나 바로 `useCounter()`를 호출해 사용할 수 있습니다.

### 3. 간단한 예시 코드
만약 숫자를 세는 기능을 만든다면 다음과 같이 작성합니다.

**`composables/useCounter.ts`**
```typescript
export const useCounter = () => {
  const count = useState('count', () => 0) // 전역 상태 유지

  const increment = () => count.value++
  const decrement = () => count.value--

  return {
    count,
    increment,
    decrement
  }
}
```

**`app.vue` (컴포넌트)**
```vue
<script setup>
// import 할 필요가 없습니다!
const { count, increment } = useCounter()
</script>

<template>
  <button @click="increment">현재 숫자: {{ count }}</button>
</template>
```

---

### 4. Nuxt UI에서의 활용
Nuxt UI 라이브러리를 사용할 때도 내부적으로 많은 Composable들이 작동합니다. 예를 들어:
* `useToast()`: 알림창을 띄우는 로직을 호출할 때
* `useModal()`: 모달의 열림/닫힘 상태를 제어할 때

### 요약하자면
* **Composables**는 로직의 **공유함**이다.
* 이름은 보통 **`use`**로 시작하는 관례가 있다 (예: `useAuth`, `useFetch`).
* **재사용성**이 높아지고, 코드가 **깔끔**해지며, **테스트**하기 좋아진다.

개발 중인 프로젝트에서 게시판의 페이징 처리나 검색 필터 로직 등을 `composables/`로 빼두시면 코드가 훨씬 "낭만" 있게 관리될 거예요. 궁금한 구현 예시가 더 있다면 말씀해 주세요!