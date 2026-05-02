

##
- `useRuntimeConfig()`
- `useRuntimeConfig()`.설정파일에서 `runtimeConfig`키를 가리키는경우
- `useRuntimeConfig()`.`nuxt.config.ts`ㆍ`runtimeConfig`


##
Nuxt 3에서 가장 자주 쓰이는 두 개의 **Composables**를 딱 짚으셨네요! 게시판을 만들 때 이 둘은 각각 **"지도(Route)"**와 **"환경설정(Config)"**의 역할을 합니다.

이해하기 쉽게 비유와 함께 핵심 개념을 정리해 드릴게요.

---

## 1. `useRuntimeConfig()` : 프로젝트의 '환경 설정 주머니'

프로젝트를 하다 보면 **API 주소**, **보안 키**, **환경 변수**처럼 코드 여기저기에 써야 하는 중요한 설정값들이 있죠. 이걸 한곳에 모아두고 안전하게 꺼내 쓰게 해주는 친구입니다.

### 🏠 비유: 집의 "공유 메모장"
가족 모두가 배달을 시킬 때 "우리 집 주소"를 알아야 하듯, 모든 페이지에서 서버 주소를 알아야 할 때 사용합니다.

* **설정 장소:** `nuxt.config.ts` 파일의 `runtimeConfig` 섹션.
* **특징:**
    * **public:** 프론트엔드(브라우저)와 백엔드(서버) 모두에서 접근 가능 (예: API Base URL).
    * **private:** 오직 서버(SSR)에서만 접근 가능 (예: DB 비밀번호, API Secret Key).

### 💻 실제 사용 예시
```javascript
// nuxt.config.ts
export default defineNuxtConfig({
  runtimeConfig: {
    public: {
      apiBase: 'http://localhost:8080' // 누구나 볼 수 있는 설정
    }
  }
})

// 컴포넌트 내부
const config = useRuntimeConfig()
console.log(config.public.apiBase) // 'http://localhost:8080'
```
**"코드에 직접 주소를 적지 않고 설정 파일에서 가져온다!"**가 핵심입니다.

---


## 3. 한눈에 비교하기

| 구분 | `useRuntimeConfig` | `useRoute` |
| :--- | :--- | :--- |
| **관점** | **애플리케이션 전체** (시스템 설정) | **현재 페이지** (사용자 위치) |
| **핵심 용도** | API 서버 주소, 환경 변수 관리 | URL 파라미터 추출, 현재 경로 확인 |
| **데이터 출처** | `nuxt.config.ts` | 브라우저 주소창 (URL) |
| **게시판 예시** | "서버 주소는 어디지?" | "지금 몇 번 게시글을 보고 있지?" |

---



이제 왜 `config.public.apiBase`를 쓰고 `route.path`를 쓰는지 감이 좀 오시나요? 이 둘만 잘 다뤄도 Nuxt에서 데이터 주고받는 건 문제없습니다! :)