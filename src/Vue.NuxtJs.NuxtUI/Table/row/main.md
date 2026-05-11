## `row`
- `row.original`
- `row.getValue('key')`


## `.getValue()`
- row.✅columns
- row.✅columns.❌data
- `row.getValue('key')`.✅columns.❌data
- 기본적으로 getValue 이부분은 columns에서 끌어오는형태. data에서 끌어오는것ㄴㄴ.
- data에는 정의돼있으나. columns에서 없는경우 못끌어옴.



- data에 있지만 columns에 없는 값은?
  - 만약 API에서 age라는 데이터를 줬는데, columns에는 age를 정의하지 않았다면 어떻게 될까요?
  - row.getValue('age'): 기본적으로 가져올 수 없습니다. TanStack Table은 columns에 정의되지 않은 키에 대해서는 값을 추적하지 않기 때문입니다.
  - 해결책: 이럴 때는 row.original을 사용해야 합니다.
  - `row.original.[dataKey]`

- 일례
  - `{{ row.getValue('id') }}` 안되는경우있음
  - columns에서 정의치않은경우
  - `{{ row.original.id }}`
  - 이렇게하면 data 키 부분에 바로접근가능. 그러고 그냥꺼내면됨ㄱㄱ.


- 결국
  - columns로갈려면.`.getValue()`
  - 더가서 data로갈려면.`.original`
