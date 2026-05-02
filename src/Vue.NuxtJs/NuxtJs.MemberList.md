이제 본격적으로 **Nuxt.js(프론트엔드)**와 **Spring Boot(백엔드)**를 연결해 보겠습니다.

가장 먼저 할 일은 `app.vue` 파일을 수정해서 백엔드에 저장된 회원 목록을 가져와 화면에 그리는 것입니다. 이 과정에서 Nuxt의 강력한 데이터 페칭 도구인 `useFetch`를 사용하게 됩니다.

---

### 1. `app.vue` 코드 작성

`frontend` 폴더에 있는 `app.vue` 파일을 열고 기존 내용을 모두 지운 뒤 아래 코드를 복사해서 붙여넣으세요.

```vue
<template>
  <div>
    <h1>회원 목록 (Nuxt + Spring Boot)</h1>
    
    <div v-if="pending">데이터를 불러오는 중...</div>

    <div v-else-if="error" style="color: red;">
      에러가 발생했습니다: {{ error.message }}
    </div>

    <ul v-else>
      <li v-for="member in members" :key="member.id">
        <strong>{{ member.name }}</strong> ({{ member.loginId }})
      </li>
    </ul>
  </div>
</template>

<script setup>
// useFetch를 사용하여 Spring Boot API 호출
// baseURL은 나중에 설정 파일로 뺄 수 있지만, 일단 직접 적어줍니다.
const { data: members, pending, error } = await useFetch('http://localhost:8080/api/members', {
  method: 'GET'
})
</script>

<style scoped>
h1 {
  color: #2c3e50;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  padding: 10px;
  border-bottom: 1px solid #eee;
}
</style>
```

---

### 2. 작동 원리 설명 (백엔드 개발자 시점)

1.  **`<script setup>`**: Vue 3의 최신 문법으로, 자바스크립트 로직이 들어가는 곳입니다.
2.  **`useFetch`**: Nuxt가 제공하는 마법 같은 함수입니다.
    * 첫 번째 인자로 API 주소를 넣습니다.
    * 반환값으로 `data`(결과값), `pending`(로딩 상태), `error`(에러 정보)를 객체 구조 분해 할당으로 받습니다.
    * **추상화**: 유저님이 직접 `fetch`를 쓰고 `await response.json()`을 할 필요가 없습니다. Nuxt가 알아서 JSON으로 변환까지 다 해줍니다.
3.  **`v-for`**: 리스트를 순회하며 화면을 만드는 문법입니다. 자바의 `for-each`문과 비슷하다고 생각하시면 됩니다.

---

### 3. 실행 전 체크리스트 (매우 중요!)

이 코드가 정상적으로 작동하려면 두 가지가 준비되어야 합니다.

1.  **Spring Boot 실행 중**: 당연히 8080 포트에서 백엔드가 돌아가고 있어야 합니다.
2.  **CORS 설정**: 아까 만든 `WebConfig.java`가 제대로 적용되어 있어야 브라우저가 데이터를 차단하지 않습니다. (포트 3000번 허용 확인)

---

### 4. 확인하기

이제 브라우저에서 `http://localhost:3000`을 새로고침해 보세요.

* 만약 DB에 회원이 있다면 이름과 아이디가 리스트로 뜰 것입니다.
* 만약 빈 화면이거나 에러가 난다면, 브라우저에서 `F12`를 눌러 **Console** 탭을 확인해 보세요. (CORS 에러인지, 주소 문제인지 바로 알 수 있습니다.)



성공하셨나요? 만약 목록이 잘 나온다면, 다음 단계로는 **"Nuxt에서 로그인 폼을 만들고 실제 세션 로그인 처리하기"**를 해보겠습니다. 이게 프론트-백 분리 구조에서 가장 까다로우면서도 재미있는 부분이거든요!