## BaseUrl
- [x] 설정파일
  - config
  - `nuxt.config.ts`
- [ ] 플러그인
  - plugin


##
- fetch.❌BaseUrl
- fetch.❌BaseUrl-이런옵션이없음
- $fetch.✅BaseUrl-이런옵션이있음


##
Nuxt 3에서 `$fetch`나 `useFetch`를 사용할 때 **BaseURL**을 설정하는 방법은 크게 두 가지가 있습니다. 프로젝트의 규모나 상황에 맞춰 선택하시면 됩니다.


### 1. `nuxt.config.ts`에서 전역 설정 (가장 권장)
프로젝트 전체에서 공통으로 사용할 API 주소를 설정할 때 사용합니다. `runtimeConfig`를 활용하면 개발 환경(Local)과 배포 환경(Production)의 주소를 다르게 관리하기 편리합니다.


설정
```typescript
// nuxt.config.ts
export default defineNuxtConfig({
  runtimeConfig: {
    public: {
      apiBase: 'http://localhost:8080' // 환경 변수(process.env.API_BASE)로 관리하면 더 좋습니다.
    }
  }
})
```


사용
```typescript
const config = useRuntimeConfig()

const signup = async () => {
  const response = await $fetch('/api/members/signup', {
    baseURL: config.public.apiBase,
    method: 'POST',
    body: form
  })
}
```
- `const config = useRuntimeConfig()`
- `baseURL: config.public.apiBase,`




### 2. 커스텀 `$fetch` 인스턴스 만들기 (플러그인 방식)
매번 `baseURL`을 적어주는 게 번거롭다면, 아예 기본 설정이 포함된 **나만의 API 클라이언트**를 만들 수 있습니다.

`plugins/api.ts` 파일을 생성하여 설정합니다.

```typescript
// plugins/api.ts
export default defineNuxtPlugin(() => {
  const api = $fetch.create({
    baseURL: 'http://localhost:8080',
    onRequest({ request, options }) {
      // 필요한 경우 헤더에 토큰을 넣는 등 공통 로직 추가 가능
      // options.headers = options.headers || {}
    }
  })

  return {
    provide: {
      api
    }
  }
})
```

이제 컴포넌트에서 아주 간결하게 호출할 수 있습니다.

```typescript
const { $api } = useNuxtApp()

const response = await $api('/api/members/signup', {
  method: 'POST',
  body: form
})
```

---

### 💡 팁: 왜 이렇게 하나요?
* **유지보수:** 나중에 서버 주소가 `https://api.example.com`으로 바뀌어도 `nuxt.config.ts` 한 곳만 수정하면 됩니다.
* **코드 간결성:** 모든 호출 코드에서 `http://localhost:8080`을 일일이 적지 않아도 되어 실수를 방지할 수 있습니다.

지금 진행 중인 **모듈형 게시판**이나 **URL 언펄링(Link Preview)** 프로젝트에서도 백엔드 API 서버 주소를 이렇게 관리하시면 훨씬 깔끔하게 코드를 짤 수 있을 거예요. 특히 Nuxt 3와 Spring Boot를 조합하신다면 첫 번째 방식이 가장 표준적입니다.