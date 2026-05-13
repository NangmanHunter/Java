

## Proxy.Target
- ❌`/api`(Proxy)👉`localhost:80800`(Target)
  - `localhost:3000/api`👉`localhost:80800`
- ✅`/api`(Proxy)👉`localhost:80800/api`(Target)
- `localhost:3000/api`👉`localhost:80800/api`
  - 경로뒤에 같이붙는다.
  - 도메인만 다르게가준다.
  - 도메인만 다르게잡아준다.
  - 다변경ㄴㄴ.
  - 도메인만변경ㄱㄱ.



##
그 혼란, 모든 풀스택 개발자가 한 번은 겪는 '환경 변수 잔혹사'입니다. 특히 Nuxt처럼 SSR과 CSR이 공존하는 프레임워크는 서버(Node.js) 입장의 주소와 클라이언트(브라우저) 입장의 주소가 달라서 더 머리가 아프죠.

다들 매번 수동으로 고치지 않습니다. 보통 다음과 같은 **3가지 전략** 중 하나를 선택해서 자동화합니다.

---

### 1. Nuxt의 'Proxy' 기능을 활용 (가장 추천)

로컬 개발 환경에서도 Nginx가 있는 것처럼 속이는 방법입니다. `nuxt.config.ts`의 `nitro` 설정을 사용하면, 프론트엔드 서버가 중간에서 요청을 가로채 백엔드로 전달해줍니다.

```typescript
// nuxt.config.ts
export default defineNuxtConfig({
  nitro: {
    devProxy: {
      '/api': {
        target: 'http://localhost:8080', // 로컬 백엔드 주소
        changeOrigin: true,
      }
    }
  },
  runtimeConfig: {
    public: {
      apiBase: '/api' // 개발/운영 모두 '/api'로 통일!
    }
  }
})

```

* **장점:** 코드가 깔끔해집니다. 브라우저는 무조건 `/api`로만 던지면 되고, 개발할 때는 Nuxt가, 배포 시에는 Nginx가 이를 처리합니다.
* **결과:** `.env`를 매번 바꿀 필요가 없습니다.

---

### 2. .env 파일을 환경별로 분리

Docker Compose를 쓰신다면 로컬용과 서버용 `.env` 파일을 따로 관리하는 게 정석입니다.

* `.env.local` (내 컴퓨터에서 npm run dev 할 때)
* `.env.production` (Docker로 빌드해서 올릴 때)

그리고 `docker-compose.yml`에서는 명확하게 **컨테이너 내부용 환경변수**를 주입해버리는 거죠. 지금 작성하신 `environment` 섹션이 그 역할을 아주 잘하고 계신 겁니다.

문제는 "내 로컬 환경(Docker 밖)"에서의 설정이 컨테이너 설정과 충돌하는 것인데, 로컬에서는 아예 `.env` 파일을 읽지 않게 하거나 별도의 로컬 전용 값을 두는 방식을 씁니다.

---

### 3. '낭만' 있는 자동 조건문 (runtimeConfig 활용)

코드 안에서 현재 환경이 개발(dev)인지 운영(prod)인지에 따라 주소를 동적으로 할당할 수도 있습니다.

```typescript
// nuxt.config.ts
const isDev = process.env.NODE_ENV === 'development'

export default defineNuxtConfig({
  runtimeConfig: {
    public: {
      // 로컬 개발 중이면 localhost:8080, 아니면 Nginx 경로인 /api 사용
      apiBase: isDev ? 'http://localhost:8080' : '/api'
    }
  }
})

```

* **장점:** 한 곳에서 로직이 관리되므로 "왜 안 되지?" 하며 `.env`를 뒤질 일이 줄어듭니다.

---

### 💡 정리하자면

가장 "고수"들이 선호하는 방식은 1번(Proxy)입니다.
브라우저 입장에서는 내가 로컬인지 서버인지 신경 쓸 필요 없이 "내 주소 뒤에 /api만 붙여서 던지면 누군가(Nuxt or Nginx)가 알아서 백엔드로 보내주겠지"라고 믿게 만드는 구조거든요.

**지금 바로 해결하시려면:**

1. 로컬에서 `npm run dev`로 개발하실 때는 `NUXT_PUBLIC_API_BASE`를 잠시 `http://localhost:8080`으로 두시거나,
2. 위의 `nitro` 설정을 추가해서 로컬에서도 `/api`가 작동하게 만드시면 됩니다.

이제 왜 갑자기 안 됐는지 명확해지셨죠? 브라우저가 존재하지도 않는 `localhost:3000/api`라는 문을 계속 두드리고 있었던 셈입니다. :)