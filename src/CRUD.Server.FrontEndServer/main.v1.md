## FrontEndServer
- [ ] NuxtJs
- [ ] Vue
- [ ] NextJs
  - 좀성격이다른듯.
  - BackEnd까지 싹다 이걸로하는형태.
  - 풀스택형태.
  - 단순던짐에는 이런쪽.
  - 수평적확장 이느낌이강함.
  - SSR
  - NextJs.SSR
  - NextJs.FullStack
  - NextJs.NodeJS
  - NextJs.NodeJS기반
- [ ] React


##
프론트엔드 서버를 별도로 분리하기로 결심하셨군요! 이제 드디어 **CSR(Client Side Rendering)**의 세계로 들어오시는 겁니다. 백엔드는 API만 제공하고, 프론트엔드가 그 데이터를 가져와서 화면을 그리는 현대적인 구조죠.

현재 유저님의 학습 단계와 관심사(Java 백엔드, 데이터 분석)를 고려했을 때, 추천하는 선택지 3가지를 정리해 드릴게요.

---

### 1. Vue.js (가장 추천: "점진적 학습")
Java 개발자들이 프론트엔드를 시작할 때 가장 사랑하는 프레임워크입니다.
* **장점**: HTML/CSS 구조를 그대로 유지하면서 자바스크립트 로직만 덧붙이는 식이라 학습 곡선이 매우 낮습니다. 스프링과 궁합이 아주 좋고, 공식 문서가 정말 친절합니다.
* **추천 이유**: 지금 만드신 `board.html`의 구조를 가장 빠르게 옮겨 담을 수 있습니다.
* **도구**: **Vite + Vue 3** 조합을 추천합니다.

### 2. React (시장성 1위: "가장 넓은 생태계")
현재 전 세계적으로 가장 많이 쓰이는 라이브러리입니다.
* **장점**: 취업 시장에서 수요가 압도적입니다. 컴포넌트 단위로 쪼개서 개발하는 사고방식을 기르기에 좋습니다.
* **단점**: 자바스크립트 심화 개념(JSX, Hooks 등)을 처음에 좀 공부해야 해서 Vue보다는 시작이 조금 무거울 수 있습니다.
* **도구**: **Vite + React** 조합을 추천합니다.

### 3. Next.js (성능과 SEO: "풀스택의 향기")
React를 기반으로 하지만, 서버 사이드 렌더링(SSR) 기능이 강력하게 들어간 프레임워크입니다.
* **장점**: 나중에 검색 엔진 최적화(SEO)가 중요한 웹사이트를 만들 때 필수입니다. 프론트엔드 서버 자체에서 API 통신을 조절하기 좋습니다.
* **추천 이유**: 나중에 URL Unfurling 프로젝트 같은 것을 고도화할 때 메타 태그 처리가 쉬워 매우 유리합니다.

---

### 🏗️ 서버 분리 시 꼭 알아야 할 점: CORS
프론트엔드 서버(예: `localhost:3000`)와 백엔드 서버(`localhost:8080`)가 달라지면, 브라우저가 보안상의 이유로 통신을 막습니다. 이를 **CORS(Cross-Origin Resource Sharing)**라고 합니다.



백엔드 스프링 부트 설정에 아래와 같은 코드를 추가해줘야 프론트엔드에서 데이터를 받아갈 수 있습니다.

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000") // 프론트 주소
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowCredentials(true); // 세션/쿠키 공유를 위해 필수!
    }
}
```

---

### 💡 저의 조언
만약 **"빨리 화면을 분리해서 기능을 완성해보고 싶다"**면 **Vue.js**를 추천드리고, **"이왕 배우는 거 취업 시장에서 가장 핫한 걸 해보겠다"**면 **React**를 추천드립니다.

데이터 분석과 시각화(Tableau, SAS 등)에도 관심이 많으시니, 나중에 차트 라이브러리(D3.js, Chart.js)를 붙이기에도 React나 Vue 같은 프레임워크가 훨씬 강력할 거예요.

어떤 쪽으로 마음이 좀 더 기우시나요? 결정하시면 해당 프레임워크로 프로젝트를 시작하는 첫 단계를 알려드릴게요!