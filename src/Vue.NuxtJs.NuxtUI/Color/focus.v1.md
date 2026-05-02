## `focus`
- `ui.base.focus:ㅇㅇ-ㅇㅇ-ㅇㅇ`
- `ui.base.focus:ㅇㅇ-ㅇㅇ-00`


##
아하, 안 먹히는 이유를 알 것 같습니다! Nuxt UI v3의 `UInput`이나 `UTextarea`에서 `:ui` 내부의 `focus` 속성은 **특정 조건(컴포넌트 내부 로직)**에 따라 클래스가 갈아끼워지는 방식인데, `variant="none"`과 직접적인 Tailwind 클래스가 충돌하면서 우선순위에서 밀렸을 가능성이 커요.

이럴 땐 **`base`** 속성 안에 Tailwind의 **`focus:` 접두사**를 직접 때려 넣는 방식이 가장 확실하고 직관적입니다. 

코드를 아래와 같이 바꿔보세요. 이번엔 절대 안 먹힐 수가 없습니다.

---

### 🛠 가장 확실한 해결 코드

```vue
<template>
  <div class="flex flex-col border border-gray-200 dark:border-gray-800 rounded-lg">
    
    <UInput 
      v-model="title"
      placeholder="제목을 입력하세요..."
      variant="none"
      :ui="{ 
        // base에 직접 focus: 클래스를 넣습니다.
        base: 'px-8 py-6 text-3xl font-bold bg-transparent outline-none focus:ring-2 focus:ring-inset focus:ring-primary-500 transition-all'
      }"
    />

    <UTextarea 
      v-model="content" 
      :rows="20" 
      autoresize
      placeholder="여기에 내용을 입력하세요..."
      variant="none" 
      :ui="{ 
        // 마찬가지로 base에 focus: 클래스를 몰아넣습니다.
        base: 'px-8 py-8 text-lg font-mono leading-relaxed resize-none outline-none focus:ring-2 focus:ring-inset focus:ring-primary-500 transition-all'
      }"
      class="min-h-[500px] border-t border-gray-100 dark:border-gray-800"
    />
    
  </div>
</template>
```

---

### 🧐 왜 이전 방식이 안 먹혔을까요?

1.  **우선순위 문제**: `variant="none"`을 쓰면 Nuxt UI가 내부적으로 가지고 있는 `focus` 관련 상태 클래스들이 아예 비활성화되거나, 우리가 `base`에 넣은 다른 클래스들에 가려질 때가 있습니다.
2.  **가장 확실한 방법**: `ui: { focus: '...' }`라는 주머니에 넣어서 라이브러리가 꺼내주길 기다리는 대신, **`base`**라는 "무조건 적용되는 주머니"에 **`focus:ring-2`**를 직접 넣어버리는 게 가장 강력합니다.

### 💡 팁: `outline-none` 추가
브라우저 기본 테두리(보통 파란색이나 검은색 굵은 선)가 방해할 수 있으니 `outline-none`을 함께 넣어주면 우리가 설정한 `primary` 색상의 `ring`만 깔끔하게 보입니다.

이제 제목이나 내용을 클릭하면 **슈슉-** 하고 `primary` 색상 테두리가 안쪽으로 기분 좋게 생길 거예요! 다시 한 번 확인해 보시겠어요? :)