##
- Textarea
- Variant
  - https://ui.nuxt.com/docs/components/textarea#variant

- Color


## Textarea.highligtht
```
highligtht
```
```

```
- 있으면 포커스경우 테두리색채나옴
- 없으면 그냥없는상태
- ✅`highligtht`ㆍ` `
- ❌`highligtht=true`ㆍ`highligtht=false`


##
현재 코드에서 **`color="primary"`**가 실제로 영향을 미치는 부분은 **텍스트 커서(Caret)의 색상**과 **에러 발생 시의 강조색** 정도입니다.

하지만 `variant="none"`과 `focus:ring-0`을 함께 사용하셨기 때문에, 우리가 흔히 생각하는 '테두리 색'이나 '배경색'에는 아무런 변화가 없을 거예요. 왜 그런지 구조를 뜯어 드릴게요.

---

### 1. `color="primary"`가 가리키는 곳
Nuxt UI v3의 `UTextarea`에서 `color` 프롭은 보통 다음 두 가지를 결정합니다.
* **Focus Ring (초점 테두리):** 입력창을 클릭했을 때 나타나는 테두리 선의 색상.
* **Caret Color (커서 색상):** 글자를 입력할 때 깜빡이는 세로 막대(`|`)의 색상.

### 2. 하지만 지금 코드에서는 작동하지 않는 이유
작성하신 코드에 **`variant="none"`**과 **`focus:ring-0`**이 포함되어 있기 때문입니다.

* **`variant="none"`**: 컴포넌트가 기본적으로 가지고 있는 배경색과 테두리 스타일을 **전부 제거**합니다. (투명 인간으로 만드는 것과 비슷해요.)
* **`focus:ring-0`**: 클릭했을 때 나타나야 할 `primary` 색상의 테두리 두께를 **0**으로 만들어버립니다.

> **결과적으로:** `color="primary"`라고 명시는 했지만, 스타일을 다 깎아버렸기 때문에 눈에는 거의 보이지 않는 상태가 된 것입니다.

---

### 3. "진짜 색상"을 바꾸고 싶다면?
에디터의 배경색이나 글자색을 바꾸고 싶다면 `color` 프롭이 아니라 **`:ui` 속성 내부**에서 Tailwind 클래스로 제어해야 합니다.

```vue
<UTextarea 
  v-model="content" 
  variant="none"
  :ui="{ 
    // base 안에 직접 원하는 색상을 넣으세요
    base: 'bg-gray-50 dark:bg-gray-900 text-green-600 px-8 py-8 text-lg font-mono leading-relaxed resize-none focus:ring-0',
  }"
  class="min-h-[500px]"
/>
```

* **`bg-gray-50`**: 배경색을 연한 회색으로.
* **`text-green-600`**: 글자색을 초록색(코딩 느낌)으로.
* **`dark:bg-gray-900`**: 다크 모드일 때 배경색.

---

### 💡 요약하자면
현재 코드에서 `color="primary"`는 **"이 컴포넌트의 테마는 기본색(Primary)이야"**라고 선언만 한 상태일 뿐, 실제 시각적인 색상은 `:ui` 프롭에 적힌 클래스들이 지배하고 있습니다.

만약 VS Code 같은 느낌을 더 내고 싶다면 `color` 프롭을 만지기보다 `:ui`의 `base`에 **`bg-[#1e1e1e] text-[#d4d4d4]`** 같은 구체적인 색상 클래스를 넣어보시는 걸 추천드려요! (이게 진짜 개발자 감성이죠. :) )