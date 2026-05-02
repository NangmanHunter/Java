##
- [x] PostCss
  - 이것설치하는게 제일나은듯


##
NuxtJS(특히 Tailwind CSS와 함께 사용할 때)에서 Lint 에러가 나면 코딩 흐름이 툭툭 끊기죠. 보통 두 가지 케이스입니다. 하나는 **VS Code 자체의 CSS 검사**와 충돌하는 경우고, 다른 하나는 **ESLint/Prettier** 설정 문제예요.

가장 깔끔하게 해결하는 방법들을 정리해 드립니다.

---

## 1. VS Code 기본 CSS Lint 끄기 (가장 추천)
Tailwind를 쓰면 `@tailwind`, `@apply` 같은 지시어를 쓰게 되는데, VS Code는 이걸 "듣도 보도 못한 문법"이라며 빨간 줄을 긋습니다. 이걸 꺼버리고 **Tailwind CSS IntelliSense** 확장 프로그램에 맡기는 게 정석입니다.

1.  VS Code에서 `Ctrl + ,` (설정)를 누릅니다.
2.  `unknown`을 검색합니다.
3.  **Css > Lint: Unknown At Rules** 설정을 `ignore`로 변경합니다. (SCSS나 Less를 쓰신다면 그것들도 같이 바꿔주세요.)

## 2. `.vscode/settings.json` 설정
프로젝트 협업 시에도 동일하게 적용되도록 프로젝트 루트에 설정을 박아두는 방법입니다.

```json
{
  "css.lint.unknownAtRules": "ignore",
  "scss.lint.unknownAtRules": "ignore",
  "tailwindCSS.includeLanguages": {
    "plaintext": "ts",
    "vue": "html"
  }
}
```

---

## 3. ESLint와 Tailwind CSS 충돌 해결
만약 ESLint가 클래스 순서나 문법으로 시비를 거는 거라면, 전용 플러그인을 설치해서 "합법적"으로 인정받게 해줘야 합니다.

**설치:**
```bash
npm install -D eslint-plugin-tailwindcss
```

**`.eslintrc.cjs` (또는 `.eslintrc.json`) 설정:**
```javascript
module.exports = {
  extends: [
    '@nuxt/eslint-config',
    'plugin:tailwindcss/recommended' // Tailwind 전용 룰 추가
  ],
  rules: {
    // 클래스 순서 강제가 짜증난다면 꺼버릴 수도 있습니다.
    'tailwindcss/classnames-order': 'off' 
  }
}
```

---

## 4. `app.css` 파일의 경고 해결
`app.css` 상단에 들어가는 `@tailwind` 지시어에서 에러가 난다면, 파일 맨 위에 아래 주석을 한 줄 넣어보세요. 해당 파일만 검사를 건너뜁니다.

```css
/* stylelint-disable-next-line at-rule-no-unknown */
@tailwind base;
@tailwind components;
@tailwind utilities;
```

---

## 💡 팁: Nuxt UI를 쓰신다면?
Nuxt UI는 내부적으로 스타일을 다 처리해주기 때문에, 사실 **`app.vue` 내부에 직접 스타일을 길게 쓸 일이 적을수록 좋습니다.** 혹시 "이 속성은 듣보잡 속성입니다" 같은 에러가 `template` 안에서 나는 건가요? 아니면 `<style>` 태그 안에서 나는 건가요? 구체적인 에러 문구를 알려주시면 바로 맞춤형 처방을 해드릴게요!