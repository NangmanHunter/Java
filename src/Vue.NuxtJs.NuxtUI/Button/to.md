##
- `to`.`href`
- `to`.`anchor`
- `to`.`<a href>`


##
Nuxt UI에서 가장 간단하게 앵커(Anchor) 버튼을 만드는 방법은 **`UButton`** 컴포넌트를 그대로 사용하는 것입니다.

Nuxt UI의 `UButton`은 `to` 속성이나 `href` 속성을 감지하면 자동으로 내부에서 `NuxtLink`나 `<a>` 태그로 변환되어 렌더링됩니다.

---

### 1. 기본적인 사용법

단순히 외부 링크로 이동하거나 페이지 내 특정 ID로 이동하고 싶을 때 다음과 같이 작성합니다.

```vue
<template>
  <UButton 
    to="https://google.com" 
    target="_blank" 
    label="구글로 이동" 
  />

  <UButton 
    to="#section-id" 
    variant="link" 
    label="섹션으로 이동" 
  />
</template>
```

### 2. 버튼이 아닌 텍스트 링크 형태를 원할 때
버튼의 배경색이나 테두리 없이 순수하게 '링크' 느낌만 내고 싶다면 `variant="link"` 속성을 추가하면 됩니다.

```vue
<UButton 
  to="/about" 
  variant="link" 
  color="gray"
>
  소개 페이지로 이동
</UButton>
```

### 3. 왜 UButton을 쓰나요?
* **자동 감지:** `to` 속성을 넣으면 `NuxtLink`로 동작하여 SPA(Single Page Application) 최적화가 적용됩니다.
* **일관성:** 일반 버튼과 앵커 버튼의 디자인 시스템(색상, 크기, 로딩 상태 등)을 동일하게 유지할 수 있습니다.
* **편의성:** `label` 속성이나 아이콘(`icon`) 삽입이 매우 쉽습니다.

---

만약 UI 컴포넌트의 기능이 필요 없고 정말 **HTML 순수 앵커 태그**만 사용하고 싶다면, Nuxt 표준인 `<NuxtLink to="...">`를 사용하시는 것이 가장 깔끔합니다.