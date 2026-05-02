`i-lucide`로 시작하는 아이콘들은 **Lucide Icons**라는 라이브러리를 Nuxt UI가 아이콘 모듈(@nuxt/icon)을 통해 제공하는 것입니다.

이 아이콘들을 한눈에 확인하고 검색할 수 있는 가장 좋은 방법 3가지를 알려드릴게요.

### 1. Nuxt UI / Icon 전용 탐색기 (가장 추천)
Nuxt 팀에서 만든 공식 아이콘 검색 사이트입니다.
* **주소:** [icones.js.org](https://icones.js.org/collection/lucide)
* **방법:** 왼쪽 목록에서 **Lucide**를 선택하거나, 검색창에 원하는 단어(예: `user`, `home`, `settings`)를 입력하면 됩니다. 
* **팁:** 여기서 찾은 이름이 `lucide:user`라면, 코드에서는 `i-lucide-user`라고 쓰시면 됩니다.

### 2. Lucide 공식 홈페이지
아이콘의 원본 디자인과 목록을 보고 싶을 때 유용합니다.
* **주소:** [lucide.dev/icons](https://lucide.dev/icons)
* **방법:** 여기서 이름을 확인하고 앞에 `i-lucide-`를 붙여서 사용하세요. 
    * 예: 홈페이지에서 `arrow-right`를 찾았다면 -> `i-lucide-arrow-right`

### 3. VS Code 확장 프로그램 (개발 편의용)
코드를 짤 때 바로바로 보고 싶다면 **"Iconify IntelliSense"**라는 확장 프로그램을 설치해 보세요.
* 코드에 `i-lucide-`까지 입력하면 추천 목록과 함께 아이콘 모양이 미리보기로 뜹니다.

---

### 💡 간단한 규칙
Nuxt UI에서 아이콘을 쓸 때는 항상 이 형식을 따릅니다:
`i-{아이콘셋이름}-{아이콘이름}`

* **Lucide 아이콘:** `i-lucide-panel-left`
* **Heroicons:** `i-heroicons-bars-3`
* **Material Design:** `i-mdi-home`

지금 사용하시는 사이드바 토글 버튼의 `i-lucide-panel-left`처럼, Lucide 사이트에서 원하는 모양을 찾은 뒤 이름만 `i-lucide-` 뒤에 붙여주시면 바로 적용됩니다!