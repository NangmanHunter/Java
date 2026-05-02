
##
- https://tanstack.com/table/v8/docs/api/core/table
- https://tanstack.com/table/v8/docs/api/core/row



##
TanStack Table에서 `Row` 객체는 단순한 데이터 뭉치가 아니라, 테이블의 **상태(State)**와 **로직(Logic)**을 담고 있는 핵심 인터페이스입니다.

가장 궁금해하시는 **`Row` 인터페이스의 주요 명세**를 정리해 드릴게요.

---

### 1. Row 객체의 핵심 구조 (TypeScript 명세)

TanStack Table 내부에서 정의된 `Row<TData>` 인터페이스의 핵심 구성 요소는 다음과 같습니다. (여기서 `TData`는 우리가 만든 `Board` 같은 데이터 타입입니다.)

| 속성 / 메서드 | 타입 | 설명 |
| :--- | :--- | :--- |
| **`id`** | `string` | 행의 고유 식별자 (보통 인덱스 번호) |
| **`index`** | `number` | 원본 데이터 배열에서의 순서 |
| **`original`** | **`TData`** | ⭐ **우리가 서버에서 받은 순수 데이터 객체** |
| **`depth`** | `number` | 하위 행이 있을 경우(Tree 구조)의 깊이 |
| **`getValue(columnId)`** | `function` | 특정 컬럼의 값을 가져오는 함수 |
| **`getIsSelected()`** | `function` | 이 행이 선택(체크)되었는지 여부 확인 |
| **`getCanSelect()`** | `function` | 이 행이 선택 가능한지 여부 확인 |
| **`getVisibleCells()`** | `function` | 현재 렌더링되고 있는 모든 셀(Cell) 객체 리턴 |



---

### 2. 왜 이렇게 만들었을까? (작동 원리)

TanStack Table은 **"테이블을 데이터베이스처럼 다루겠다"**는 철학을 가지고 있습니다.

1.  **`original` (Raw Data):** 사용자가 백엔드에서 가져온 데이터는 건드리지 않고 원형 그대로 보존합니다.
2.  **`Row` (Smart Wrapper):** `original` 데이터를 감싸서, 이 데이터가 테이블 위에서 어떤 상태(정렬됐는지, 선택됐는지, 확장됐는지)인지 관리하는 **매니저** 역할을 합니다.

그래서 우리가 슬롯이나 함수에서 데이터를 꺼낼 때 `row.title`이 아니라 **`row.original.title`** 혹은 **`row.getValue('title')`**을 사용하는 규격이 생긴 것입니다.

---

### 3. 실무에서 가장 많이 쓰는 명세 활용법

게시판을 만드실 때 이 세 가지만 딱 알고 계시면 명세를 다 본 것이나 다름없습니다.

```typescript
// 1. 상세 페이지 이동 (원본 데이터 접근)
const id = row.original.id; 

// 2. 행 번호 표시 (인덱스 접근)
// index는 0부터 시작하므로 보통 1을 더해서 씁니다.
const rowNumber = row.index + 1;

// 3. 특정 조건에 따른 스타일링
// 예: 현재 행이 선택되었다면 배경색 변경
const isSelected = row.getIsSelected();
```

---

### 4. 더 깊은 명세가 궁금하다면?

이 `Row` 인터페이스에 대한 공식 영문 명세는 TanStack Table 공식 가이드의 **[Row API]** 섹션에서 확인하실 수 있습니다.

* **공식 문서 주소:** [TanStack Table Row API Reference](https://tanstack.com/table/v8/docs/api/core/row)

문서를 보시면 생각보다 훨씬 많은 메서드(`getCanExpand`, `getLeafRows` 등)가 있는데, 이는 TanStack이 단순 목록뿐만 아니라 **엑셀 수준의 복잡한 그리드**를 지원하기 위해 설계되었기 때문입니다. 

지금처럼 일반적인 게시판을 만드실 때는 **`original`**과 **`index`** 정도만 마스터하셔도 충분합니다! :)