
##

- :data에 없을경우처리
  - `<UTable v-else :data="boards ?? []" class="flex-1" />`
  - `<UTable v-else :data="boards || []" class="flex-1" />`
  - 이론상넣는데 굳이임. 별차이가없음.
  - 애당초 빈배열이면 걍 Table에서 `No data`이런식진행.;;
- any선언후 배열형태 + 
- Board선언후 배열형태
- 없을경우 빈배열넣어주기




##
그 빨간 줄은 **TypeScript(타입스크립트)**가 "이 `boards` 안에 진짜로 테이블에 넣을 수 있는 배열이 들어있는 게 확실해?"라고 의심하고 있기 때문에 생기는 현상입니다.

`useFetch`는 기본적으로 데이터의 형태를 `any`나 `null`로 추론할 때가 많아서, `UTable`이 요구하는 **배열(Array) 형식**과 맞지 않는다고 판단하는 것입니다.

이를 해결하는 가장 깔끔한 방법 3가지를 알려드릴게요.

---

### 1. `transform`으로 데이터 구조 확정하기 (추천)
API 응답이 비어있을 경우를 대비해 기본값을 빈 배열(`[]`)로 넘겨주면 빨간 줄이 사라집니다.

```typescript
// res가 없으면 빈 배열을 반환하도록 강제
const { data: boards, pending, error } = await useFetch('http://localhost:8080/api/boards', {
  transform: (res) => res || []
});
```

### 2. Generic으로 타입 정의하기
`boards`가 어떤 모양의 데이터를 가진 배열인지 직접 명시해주는 방법입니다. (개발자 입장에서 가장 안전합니다.)

```typescript
// 데이터 구조 정의
interface Board {
  id: string;
  title: string;
  writerName: string;
  views: number;
}

// useFetch 뒤에 <Board[]>를 붙여줍니다.
const { data: boards, pending, error } = await useFetch<Board[]>('http://localhost:8080/api/boards');
```

### 3. Template에서 "값이 있음"을 보장하기
만약 위 방법들이 귀찮다면, `:data`에 값을 넣을 때 "값이 없으면 빈 배열을 써라"고 명시해줄 수도 있습니다.

```html
<UTable v-else :data="boards ?? []" class="flex-1" />
```

---

### 💡 왜 빨간 줄이 나왔던 걸까요?
`useFetch`는 비동기로 데이터를 가져오기 때문에, 코드가 처음 읽히는 시점에는 `boards`의 값이 `null`일 가능성이 있습니다. `UTable`의 `:data` 속성은 **"무조건 배열이어야 함"**이라는 규칙이 있는데, `null`이 들어올 수도 있다고 하니 타입스크립트가 경고를 보낸 것입니다.



**참고:** 만약 API 서버(8080 포트)에서 데이터를 줄 때 `id`, `title` 같은 필드 외에 다른 이름으로 주고 있다면, 테이블에 아무것도 안 뜰 수 있습니다. 그럴 땐 지난번에 말씀드린 `columns` 설정을 추가해 보세요!