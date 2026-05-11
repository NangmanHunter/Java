

## AccessorKey



## columns
```.vue
const columns: TableColumn<Payment>[] = [
  { ...
  },
  {
    id: 'account_id',
    accessorKey: 'account.id'
  }
]
```


## Json.이중Key
```.vue
const data = ref<Payment[]>([
  { ...
  },
  {
    account: {
      id: '1',
      name: 'Account 1'
    }
  }
])

const columns: TableColumn<Payment>[] = [
  { ...
  },
  {
    id: 'account_id',
    accessorKey: 'account.id'
  }
]
```


## columns
- 실제보여지는것은 컬럼스 이다.
- 보여지는테이블을 따로 재정의화.
- 데이터불문 그냥정의화된형태.
- 여기다 임의로 내가필요에따라 넣어도 ㅇㅋ가 되게되는지점.

- 더꾸미고프면
- 각각 `cell: ({}) => {}` 이형태로 진행
- `cell: ({})`
- `cell: ({}) => {}`