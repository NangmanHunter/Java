

## TailwindCss.Color
- `red`
- `orange`
- `amber`
- `yellow`
- `lime`
- `green`
- `emerald`
- `teal`
- `cyan`
- `sky`
- `blue`
- `indigo`
- `violet`
- `purple`
- `fuchsia`
- `pink`
- `rose`
<!--  -->
- `slate`
- `gray`
- `zinc`
- `neutral`
- `stone`
- `taupe`
- `mauve`
- `mist`
- `olive`


##
아하, 이 부분이 처음 접하면 좀 막막할 수 있죠! **Nuxt UI**는 내부적으로 **Tailwind CSS**의 컬러 시스템을 그대로 가져다 씁니다. 그래서 사용할 수 있는 색상 이름들이 미리 정해져 있어요.

결론부터 말씀드리면, **[Tailwind CSS 공식 컬러 팔레트](https://tailwindcss.com/docs/customizing-colors)**에 있는 이름들을 그대로 쓰시면 됩니다.

---

### 1. `primary`와 `gray`에 쓸 수 있는 색상 목록

가장 많이 쓰이는 대표적인 색상들은 다음과 같습니다:

| 계열 | 사용할 수 있는 키워드 (app.config.ts용) |
| :--- | :--- |
| **붉은 계열** | `red`, `orange`, `amber`, `rose` |
| **푸른 계열** | `blue`, `sky`, `cyan`, `indigo` |
| **초록 계열** | `green`, `emerald`, `teal`, `lime` |
| **보라 계열** | `violet`, `purple`, `fuchsia` |
| **무채색 (Gray)** | `slate`, `cool`, `zinc`, `neutral`, `stone` |

> **Tip:** `gray` 설정의 경우, `slate`는 약간 푸른 빛이 도는 회색, `stone`은 따뜻한 느낌의 회색입니다. `neutral`이 가장 표준적인 회색이에요.

---

### 2. 설정 예시 (app.config.ts)

만약 "나는 장미빛 붉은색에 세련된 회색 느낌을 주고 싶다"면 아래처럼 적으시면 됩니다.

```typescript
// app.config.ts
export default defineAppConfig({
  ui: {
    primary: 'rose',   // 장미색 (붉은 계열)
    gray: 'slate'      // 약간 푸른 빛이 도는 차가운 회색
  }
})
```

---

### 3. 이게 어떻게 작동하는 건가요? (작동 원리)

Nuxt UI는 여러분이 `primary: 'red'`라고 설정하는 순간, 내부적으로 다음과 같은 스타일 클래스들을 자동으로 생성해서 컴포넌트에 입혀줍니다.

* `bg-primary-500` → 실제로는 `bg-red-500`이 됨
* `text-primary-600` → 실제로는 `text-red-600`이 됨
* `border-primary-200` → 실제로는 `border-red-200`이 됨

즉, **`primary`는 "이 웹사이트의 메인 테마 색상을 무엇으로 부를 것인가?"에 대한 별명(Alias)**이라고 생각하시면 쉬워요!

---

### 4. 목록을 직접 확인하고 싶다면?

1.  **Tailwind CSS 공식 문서:** [Colors 페이지](https://tailwindcss.com/docs/customizing-colors)를 보시면 색상별로 `50`부터 `950`까지 단계별 농도를 볼 수 있습니다. Nuxt UI는 여기서 이름만 따오면 나머지는 알아서 처리해줍니다.
2.  **Nuxt UI 공식 문서:** [Theming 페이지](https://ui.nuxt.com/getting-started/theming)에서도 확인 가능합니다.

처음에는 `rose`나 `orange`를 한 번 넣어보세요. 분위기가 확 바뀌는 걸 바로 느끼실 수 있을 거예요! 혹시 특정 브랜드의 정확한 HEX 코드(예: #FF0000)를 써야 하는 상황인가요? 그건 방법이 조금 다른데 필요하시면 알려주세요!