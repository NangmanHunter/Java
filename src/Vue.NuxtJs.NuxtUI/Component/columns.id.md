아하, Nuxt UI의 `UTable` 컴포넌트에서 발생하는 전형적인 에러를 만나셨군요. 

`Columns require an id when using a non-string header` 에러는 Nuxt UI가 각 컬럼을 고유하게 식별하지 못할 때 발생합니다. 특히 최신 버전의 Nuxt UI에서는 `key` 외에도 **`id`** 값을 명시적으로 요구하거나, 내부적으로 식별 가능한 구조를 선호합니다.

가장 빠르고 확실한 해결 방법은 `columns` 배열의 각 객체에 **`key`와 동일한 값을 `id`로 추가**해주는 것입니다.

### 해결 코드

`script` 부분의 `columns` 정의를 아래와 같이 수정해 보세요.

```javascript
const columns = [
  { key: 'id', label: 'ID', id: 'id' },
  { key: 'title', label: '제목', id: 'title' },
  { key: 'author', label: '작성자', id: 'author' },
  { key: 'date', label: '날짜', id: 'date' }
]
```

### 왜 이런 에러가 나나요?
Nuxt UI의 `UTable`은 내부적으로 각 컬럼의 상태(정렬, 필터링 등)를 추적하기 위해 고유한 `id`가 필요합니다. `label`에 단순한 문자열이 아닌 커스텀 슬롯이나 복잡한 데이터가 들어갈 가능성이 있을 때, 시스템이 안전하게 식별자를 확보하기 위해 이 `id`를 필수로 요구하게 됩니다.

---
**💡 팁:** 만약 나중에 컬럼이 많아져서 일일이 `id`를 넣기 귀찮다면, 아래처럼 선언 단계에서 맵핑해버리는 방법도 있습니다.

```javascript
const columns = [
  { key: 'id', label: 'ID' },
  { key: 'title', label: '제목' },
  { key: 'author', label: '작성자' },
  { key: 'date', label: '날짜' }
].map(column => ({ ...column, id: column.key })) // 자동으로 id를 key값으로 채워줌
```

이렇게 수정하신 후 새로고침하시면 에러 없이 게시판이 잘 뜨실 겁니다! 더 궁금한 점 있으시면 말씀해 주세요.