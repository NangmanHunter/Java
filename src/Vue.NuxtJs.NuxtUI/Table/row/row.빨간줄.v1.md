

##
### 3. 간단한 팁
만약 복잡한 설정이 귀찮고 당장 에러만 끄고 싶다면 인라인으로 타입을 지정할 수도 있습니다 (비권장).

```typescript
cell: ({ row }: { row: any }) => { ... }
```


##
Nuxt UI(내부적으로 TanStack Table 사용)를 사용할 때 발생하는 이 오류는 TypeScript가 `cell` 함수의 인자인 `row` 객체의 타입을 추론하지 못해서 발생합니다.

이를 해결하기 위해 **가장 깔끔한 방법은 컬럼 정의에 타입을 명시**하는 것입니다.

---

### 1. 전용 인터페이스 정의 및 적용
먼저 게시판 데이터의 구조를 정의하고, `createColumnHelper` 또는 `ColumnDef` 타입을 사용하여 `columns`를 선언하세요.

```typescript
import type { ColumnDef } from '@tanstack/vue-table'

// 1. 데이터 타입 정의
interface Board {
  id: number
  title: string
  writerName: string
  createdAt: string // 또는 Date
  updatedAt: string
  viewCount: number
}

// 2. ColumnDef에 타입을 주입하여 columns 선언
const columns: ColumnDef<Board>[] = [
  { accessorKey: 'id', header: '번호' },
  { accessorKey: 'title', header: '제목' },
  { accessorKey: 'writerName', header: '작성자' },
  { 
    accessorKey: 'createdAt', 
    header: '작성일',
    // 이제 row가 Board 타입을 기반으로 추론됩니다.
    cell: ({ row }) => {
      // row.original을 사용하면 실제 데이터 객체에 안전하게 접근 가능합니다.
      const dateValue = row.getValue('createdAt') as string
      return new Date(dateValue).toLocaleString('en-US', {
        day: 'numeric',
        month: 'short',
        hour: '2-digit',
        minute: '2-digit',
        hour12: false
      })
    }
  },
  { accessorKey: 'updatedAt', header: '수정일' },
  { accessorKey: 'viewCount', header: '조회수' }
]
```

---

### 2. 왜 빨간 줄이 뜨나요?
*   **원인**: `const columns: any[]`라고 선언하면 배열 내부의 객체 구조를 알 수 없습니다. 특히 `cell: ({ row }) => ...` 부분에서 `row`가 어떤 구조를 가진 객체인지 TypeScript가 알 방법이 없으므로 "암시적 any" 에러를 내뱉는 것입니다.
*   **해결 핵심**: `any[]` 대신 `@tanstack/vue-table`에서 제공하는 **`ColumnDef<T>`** 타입을 사용하면 `row` 내부의 속성들이 자동으로 타이핑됩니다.



**하지만 프로젝트의 안정성을 위해 1번 방법(Interface 정의)을 강력히 추천합니다.** `useFetch`의 제네릭 부분도 `useFetch<Board[]>`로 수정하시면 데이터 바인딩 전체가 훨씬 안전해집니다.</Board[]></T>