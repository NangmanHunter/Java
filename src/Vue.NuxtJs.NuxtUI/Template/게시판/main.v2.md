## 게시판예제
- [https://hn.nuxt.com/](https://www.google.com/search?q=https://hn.nuxt.com/)
  - 여기가 진짜 노다지다.
  - 너무이쁜사이트들 이회사에서 하나둘줍줍해서 모아놓은듯.
  - 레퍼런스용으로 사람들볼수있게.
  - 보니까 전세계적으로 나라불문 싹다모아놓음

- [nuxt-modules/hackernews](https://www.google.com/search?q=https://github.com/nuxt-modules/hackernews)


## 참조할것
- https://tokens.billchambers.me/leaderboard
  - 금시세

  - 환율시세
  - 환차시세
  - 환전최대최적.
  - 최적환전.
- https://www.sumida-aquarium.com/
- https://lisyarus.github.io/blog/posts/its-ok-to-compare-floating-points-for-equality.html
- https://victorpoughon.github.io/interval-calculator/
- https://sandboxspirit.com/blog/simple-geometry-of-roads/
- https://apps.npr.org/plant-hardiness-garden-map/
  - 여기 캐릭터 3프레임4프레임정도인듯. Pivot형태로 화면 한바퀴도는형태로 진행ㄱㄱ.



##
방금 말씀드린 **Hacker News Clone**은 전 세계 개발자들이 새로운 프레임워크나 라이브러리가 나오면 "이걸로 게시판 서비스를 만들면 이 정도 성능이 나온다"를 증명하기 위해 만드는 **일종의 표준 예제(Benchmark)** 같은 프로젝트예요.

개발자들 사이에서는 'Hello World'의 게시판 버전이라고 보시면 됩니다.

-----

### 1\. 왜 게시판 개발에 참고하기 좋나요?

Reddit이나 뽐뿌처럼 **"심플한 목록 + 댓글 + 추천"** 구조의 정석을 보여주기 때문입니다.

  * **극강의 가독성:** 불필요한 디자인 요소를 빼고 '글 제목, 작성자, 작성 시간, 댓글 수'라는 핵심 정보만 리스트로 보여주는 구조입니다.
  * **성능 최적화:** 게시판은 데이터가 많아지면 느려지기 쉬운데, 이 클론 코드는 Nuxt의 SSR(서버 사이드 렌더링)을 활용해 리스트를 얼마나 빨리 불러오는지 잘 보여줍니다.
  * **중첩 댓글(Thread):** 게시판의 꽃인 '답글의 답글' 구조를 구현하는 로직이 포함되어 있습니다.

### 2\. 어디서 볼 수 있나요?

Nuxt 팀에서 직접 관리하는 공식 예제 저장소가 있습니다.

  * **공식 데모:** [https://hn.nuxt.com/](https://www.google.com/search?q=https://hn.nuxt.com/)
  * **GitHub 코드:** [nuxt-modules/hackernews](https://www.google.com/search?q=https://github.com/nuxt-modules/hackernews)

### 3\. 내 프로젝트에 어떻게 참고할까?

이 코드를 그대로 복사하기보다, 아래의 \*\*'설계 방식'\*\*을 훔쳐오는 것이 좋습니다.

| 게시판 기능 | Hacker News Clone에서 참고할 부분 |
| :--- | :--- |
| **목록 페이지** | `UTable` 대신 단순 `v-for`와 `UCard`를 조합해 가볍게 리스트를 뿌리는 법 |
| **페이지네이션** | 무한 스크롤이나 단순 `[More]` 버튼으로 다음 글을 불러오는 로직 |
| **상세 보기** | 글 본문과 그 밑에 달린 계층형 댓글(Tree structure) 처리 방식 |
| **실시간 갱신** | 새로운 글이 올라왔을 때 데이터를 다시 불러오는(Refresh) 타이밍 |

### 💡 요약하자면

지금 만들고 계신 게시판이 \*\*"정보 전달"\*\*이 목적이라면 이전에 말씀드린 **Dashboard** 템플릿의 UI를 쓰시는 게 깔끔하고, \*\*"사람들의 북적거림과 빠른 소통"\*\*이 목적이라면 **Hacker News Clone**의 코드 구조를 뜯어보시는 걸 추천드려요.

혹시 지금 생각하시는 게시판에 꼭 들어갔으면 하는 '특이한 기능'(예: 이미지 다중 업로드, 실시간 채팅 등)이 따로 있으신가요? 있다면 그 기능에 더 특화된 샘플을 찾아봐 드릴 수도 있습니다\!