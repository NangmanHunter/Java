##
- `leadingIcon: 'text-primary'`
- `leadingIcon: 'text-orange-500'`
- `leadingIcon: 'w-8 h-8 text-orange-500'`


##
네, 맞습니다\! Tailwind CSS에서 제공하는 **모든 유틸리티 클래스**를 다 넣으실 수 있어요.

Nuxt UI는 내부적으로 Tailwind CSS를 사용하기 때문에, `:ui` 프롭 안에 넣는 값은 결국 HTML 태그의 `class="..."`에 들어가는 것과 똑같습니다.

-----

### 1\. 어떤 클래스를 넣을 수 있나요?

Tailwind CSS 문서에 있는 거의 모든 것을 조합해서 넣을 수 있습니다.

  * **색상:** `text-red-500`, `bg-blue-200`, `border-green-400`
  * **크기/간격:** `w-10` (너비), `h-10` (높이), `p-4` (패딩), `m-2` (마진)
  * **폰트:** `font-bold`, `text-lg` (글자 크기), `italic`
  * **기타:** `rounded-full` (둥글게), `shadow-lg` (그림자), `opacity-50`

### 2\. 어디를 수정할 수 있는지 찾는 법 (중요\!)

가장 어려운 게 "어떤 키값(`leadingIcon`, `wrapper` 등)에 넣어야 할까?" 하는 점일 텐데요. 이를 확인하는 가장 좋은 방법은 \*\*공식 문서의 'Config'\*\*를 보는 것입니다.

1.  \*\*[Nuxt UI 공식 문서](https://www.google.com/search?q=https://ui.nuxt.com/components/button%23config)\*\*에 접속합니다. (예시는 Button 컴포넌트)
2.  상단 탭이나 우측 메뉴에서 **Config** 버튼을 누릅니다.
3.  그러면 아래와 같은 구조의 코드가 나옵니다. 여기서 **왼쪽 키값**들이 바로 여러분이 `:ui="{ ... }"` 안에 넣을 수 있는 이름들입니다.

<!-- end list -->

```javascript
// 예시: Button의 기본 Config 구조
{
  "wrapper": "relative inline-flex",
  "base": "focus:outline-none disabled:cursor-not-allowed",
  "font": "font-medium",
  "rounded": "rounded-md",
  "size": {
    "sm": "text-xs",
    "md": "text-sm"
    // ...
  },
  "icon": {
    "base": "flex-shrink-0",
    "size": {
      "md": "h-5 w-5"
    }
  }
}
```

### 3\. 실전 예시: 버튼 커스텀하기

만약 "아이콘 크기를 키우고, 배경에 그림자를 넣고 싶다"면 이렇게 구성할 수 있습니다.

```vue
<UButton
  icon="i-heroicons-rocket"
  label="발사"
  :ui="{
    base: 'shadow-xl',          /* 버튼 전체에 그림자 추가 */
    rounded: 'rounded-none',    /* 테두리를 완전히 각지게 변경 */
    leadingIcon: 'w-8 h-8 text-orange-500' /* 아이콘 크기 키우고 주황색으로 */
  }"
/>
```

### 4\. 추천 문서 링크

참고하시면 좋은 문서 두 곳을 남겨드립니다.

  * **[Nuxt UI - The `ui` Prop (공식 가이드)](https://www.google.com/search?q=%5Bhttps://ui.nuxt.com/theming/the-ui-prop%5D\(https://ui.nuxt.com/theming/the-ui-prop\)):** 이 프롭을 어떻게 쓰는지 설명하는 공식 가이드입니다.
  * **[Tailwind CSS Quick Reference](https://tailwindcss.com/docs/utility-first):** 어떤 클래스 명칭이 있는지 찾을 때 쓰는 백과사전입니다. (예: '색상'을 찾으려면 좌측 메뉴의 'Text Color' 클릭)

-----

**꿀팁:** 개발자 도구(F12)로 해당 컴포넌트를 찍어보세요. 내가 넣은 클래스가 어느 HTML 태그에 붙어있는지 확인하면 어떤 키값을 써야 할지 훨씬 빨리 감을 잡을 수 있습니다\!