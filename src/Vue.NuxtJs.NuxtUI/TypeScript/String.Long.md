## 
- Long으로 보낸거. String으로 받아줄것ㄱㄱ.
- `id: String`
- `id: String`.`Long id;`
- `id: String`(TypeScript).`Long id;`(Java)


##
Java나 다른 언어의 `Long` 타입(64비트 정수)을 JavaScript/TypeScript로 가져올 때 주의해야 할 점이 있습니다. 결론부터 말씀드리면, **TypeScript에서는 `number` 또는 `string`으로 정의**하며, 상황에 따라 선택이 달라집니다.

---

### 1. TypeScript에는 `long` 타입이 없습니다
JavaScript/TypeScript의 기본 숫자 타입인 `number`는 **64비트 부동소수점(Double)** 방식입니다. 이 방식은 $2^{53}-1$ (약 9,000조) 이상의 정수를 정확하게 표현하지 못하고 값이 깨질 수 있습니다.

*   **API에서 보낸 ID가 9,000조보다 작다면:** 그냥 `number`를 쓰셔도 됩니다.
*   **API에서 보낸 ID가 매우 큰 숫자(Snowflake ID 등)라면:** 반드시 **`string`**으로 처리해야 합니다.

---

### 2. 권장하는 정의 방식

#### 방법 A: `string`으로 정의 (가장 안전함)
서버에서 `Long` 타입을 보낼 때, 라이브러리 설정(예: Jackson의 `@JsonSerialize`)에 따라 이미 문자열로 넘어오고 있을 확률이 높습니다. ID는 계산할 일이 없으므로 문자열이 가장 안전합니다.

```typescript
type Member = {
  id: string  // 서버에서 "460012345678901234" 형태로 올 때
  loginId: string
  isApproved: boolean
}
```

#### 방법 B: `number`로 정의
일반적인 Auto-increment ID(1, 2, 3...)라면 `number`로 충분합니다.

```typescript
type Member = {
  id: number
  loginId: string
  // ...
}
```

#### 방법 C: `bigint` (최신 방식)
정말로 큰 숫자를 숫자로 다뤄야 한다면 ES2020의 `bigint`를 쓸 수 있지만, JSON 직렬화 과정에서 에러가 발생하기 쉬워 **ID 용도로는 추천하지 않습니다.**

---

### 3. 실무 팁: 서버에서 어떻게 내려주나요?
만약 백엔드에서 `Long` 타입을 그대로 숫자로 내려주면, 브라우저가 JSON을 파싱하는 순간 숫자가 반올림되어 **ID 끝자리가 바뀌는 대참사**가 발생할 수 있습니다.

> **예시:**
> 서버 전송: `9223372036854775807`
> 브라우저 수신: `9223372036854776000` (값 왜곡!)

따라서 **ID가 `Long`일 경우 백엔드에서 문자열(`String`)로 변환해서 보내주는 것이 업계 표준**입니다. 

**확인해 보세요:**
브라우저 개발자 도구(F12) -> Network 탭에서 `/api/admin/members` 응답을 확인했을 때, `id` 값이 `"4600"`처럼 따옴표가 있다면 **`string`**으로 정의하시고, 따옴표 없이 `4600`이라면 **`number`**로 정의하시면 됩니다.

---

### 적용 예시 (string인 경우)
```typescript
const { data: members, refresh } = await useFetch<Member[]>('/api/admin/members', {
  // ... 생략
})

// 호출 시에도 string으로 처리
await $fetch(`/api/admin/members/${member.id}/approve`, { ... })
```

**결론:** 보통 ID는 계산(더하기, 빼기)할 일이 없으므로 **`string`으로 정의하는 것이 가장 속 편하고 안전한 선택**입니다! 현재 코드에서 `id: string`으로 이미 되어 있다면 그대로 두셔도 무방합니다.