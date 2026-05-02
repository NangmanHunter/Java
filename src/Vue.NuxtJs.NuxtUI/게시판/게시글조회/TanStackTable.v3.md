##
column.accessorKeyㆍheaderㆍcell


##
`accessorKey`가 **"무엇을(What)"** 가져올지 정하는 것


**`cell`**은 그 가져온 데이터를 **"어떻게(How)"** 보여줄지 결정하는 녀석입니다.

쉽게 말해, 테이블의 각 **칸(Cell)** 하나하나를 그리는 **화가**라고 생각하시면 돼요.

---

### 1. `cell`의 역할: 데이터 가공소
데이터베이스에는 `2024-03-27T10:00:00`이라고 저장되어 있지만, 화면에는 `2024.03.27`이라고 예쁘게 보여주고 싶을 때가 있죠? 이때 `cell` 옵션을 사용합니다.

```typescript
const columns = [
  {
    accessorKey: 'viewCount',
    header: '조회수',
    // cell 함수를 통해 데이터를 가공함
    cell: ({ row }) => {
      const count = row.getValue('viewCount')
      return `${count.toLocaleString()}회` // 숫자 세자리마다 콤마 찍고 '회' 붙이기
    }
  }
]
```

### 2. 왜 `cell`이라는 개념이 따로 있나요?
TanStack Table(v3)에서는 한 칸(Cell)을 구성하는 요소가 매우 다양하기 때문입니다.



* **Header**: 세로줄의 맨 윗부분 (제목)
* **Row**: 가로줄 전체 (데이터 한 세트)
* **Cell**: 가로와 세로가 만나는 **딱 한 칸**

이 '한 칸'에 단순히 글자만 넣을 수도 있지만, 버튼을 넣거나, 색깔을 칠하거나, 상태에 따라 아이콘을 바꿔야 하는 경우를 모두 처리하기 위해 `cell`이라는 설정값을 제공하는 것입니다.

---

### 3. 슬롯(Slot)과의 차이점
Nuxt UI를 쓰다 보면 `<template #title-data>` 같은 **슬롯**과 이 **`cell` 속성**이 헷갈릴 수 있습니다.

* **`cell` 속성 (Script 단)**: 간단한 텍스트 가공이나 로직 위주로 처리할 때 편합니다. (TypeScript 파일이나 script setup 안에서 처리)
* **슬롯 (Template 단)**: `NuxtLink`나 `UButton` 같은 **Vue 컴포넌트를 직접 넣거나 복잡한 HTML 구조**를 짤 때 훨씬 편합니다.

> **💡 핵심 요약**
> `cell`은 **"원시 데이터(Raw Data)를 화면용 데이터(Display Data)로 변환하는 필터"**라고 이해하시면 가장 정확합니다!

이제 `accessorKey`로 데이터 주소 찾고, `header`로 이름 붙이고, `cell`이나 `slot`으로 예쁘게 꾸미면 테이블 마스터가 되시는 겁니다! :)