##

- [ ] 
- [ ] 
- [ ] 
```vue
<UTable v-else :data="boards ?? []" />
```
  - 단순히 알아서 Key값으로 뿌려주는형태



## TanStackTable
Columns
- 필드명 불일치: v3는 내부적으로 TanStack Table을 쓰기 때문에, key라는 이름 대신 accessorKey를 써야 데이터 배열에서 값을 찾아옵니다. key라고 적으면 데이터를 어디서 꺼내올지 몰라서 빈 칸(No data)으로 보일 수 있습니다.
- key를 **accessorKey**로 바꿔보세요.
- label을 **header**로 바꿔보세요.
- columns.`accessorKey`ㆍ`header`


- TanStack Table은 "Headless" 라이브러리라 설정이 아주 방대합니다. 처음부터 모든 문서를 다 읽기보다는, Nuxt UI v3 공식 문서의 Table 예제를 먼저 훑어보신 뒤에 "더 복잡한 정렬이나 필터링이 필요하다" 싶을 때 TanStack Table 본진 문서를 파보시는 걸 추천드려요.