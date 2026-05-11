##
- type.단수>>복수
- type.단수형>>복수형
- Type.단수형>>복수형
- Type정의.단수형>>복수형


##
타입 이름 정의에 정답은 없지만, **프로그래밍 관례(Naming Convention)**상으로는 **`Member` (단수형)**를 사용하는 것이 훨씬 더 좋습니다.

그 이유를 3가지 정도로 요약해 드릴게요.

---

### 1. 객체의 "청사진"이기 때문입니다
타입 정의는 배열 전체를 정의하는 것이 아니라, 그 안에 들어갈 **'항목 하나'가 어떻게 생겼는지**를 정의하는 것입니다. 
*   `Member`: "이것은 회원 **한 명**의 정보이다." (직관적)
*   `Members`: "이것은 회원 **여러 명**의 정보이다?" (혼란스러움)

### 2. 배열 정의 시 훨씬 자연스럽습니다
코드를 작성할 때 단수형을 써야 타입의 의미가 명확해집니다.

```typescript
// Good: "Member 타입의 객체들이 담긴 배열"
const members = ref<Member[]>([]) 

// Bad: "Members 타입의 객체들이 담긴 배열? (복수의 복수?)"
const members = ref<Members[]>([]) 
```

### 3. `v-for` 문에서의 가독성
템플릿에서 반복문을 돌릴 때 단수/복수 구분이 확실해야 코드가 읽기 편합니다.

```vue
<!-- Member(타입)를 기반으로 members(배열)에서 member(한 개)를 꺼낸다 -->
<tr v-for="member in members" :key="member.id">
```

---

### 추천하는 타입 정의 패턴

보통 실무에서는 아래와 같이 정의하고 사용합니다.

```typescript
// 1. 단수형으로 핵심 인터페이스 정의
interface Member {
  id: string
  loginId: string
  isApproved: boolean
  // ...
}

// 2. 만약 배열 타입을 따로 이름 붙이고 싶다면 (선택 사항)
type MemberList = Member[]

// 3. 실제 사용
const { data: members } = await useFetch<Member[]>('/api/admin/members')
```

### 요약
*   **`Member` (단수형) 추천!**
*   이유: 타입은 데이터 하나하나의 형태를 규정하는 것이기 때문입니다.
*   변수명(배열)은 **`members` (복수형)**로 지어서 "이 안에는 `Member`들이 들어있다"는 것을 표현하시면 됩니다.

이렇게 구성하시면 다른 개발자가 코드를 봐도 "아, `members` 배열 안에는 `Member` 타입의 객체들이 있구나!" 하고 바로 이해할 수 있습니다.