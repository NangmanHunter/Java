## RestController
- [ ] Controller
- [x] RestController


## 타입
```
- `DTO`                 → Controller
- `ResponseEntity<DTO>` ← Controller
```


## 타입반환형
- [x] `record`
- [x] `DTO`
- [ ] `String`


## 컨트롤러반환형
- [x] `ResponseEntity<DTO>`
  - [ ] `ResponseEntity.ok(data)`
    - `ResponseEntity.ok(data)`
    - `ResponseEntity.status(HttpStatus.OK).body(data)`
  - [x] `ResponseEntity.status(HttpStatus.CREATED).body(data)`
    - `ResponseEntity.status(HttpStatus.CREATED).body(data)`
- [ ] `DTO`
- [ ] `String`


## OK
- [ ] `ResponseEntity.ok(data)`
- [x] `ResponseEntity.status(HttpStatus.OK).body(data)`
  - Created.이런것과 구조통일화위해 하위로서 진행ㄱㄱ.
  - 구조통일화>>코드줄임화
  - 구조통일화>>단순줄임화
  - 통일성통한최적화>>단순줄임화
  - 단일화>>개별화
  - 단순줄임화<<구조통일화
  - 단순줄임화(상위화)<<구조통일화(하위화)
  - `ResponseEntity.ok(data)`<<`ResponseEntity.status(HttpStatus.OK).body(data)`
  - `ResponseEntity.ok(data)`<<`ResponseEntity.status(HttpStatus.OK).body(data)`-개발
  - 단순줄임화(상위화)<<구조통일화(하위화)-개발
  - 단순줄임화(상위화)<<구조통일화(하위화)-개발.작업성
  - 단순줄임화(상위화)<<구조통일화(하위화)-작업성
  - 단순줄임화(상위화)<<구조통일화(하위화)-일관성
  - 단순줄임화(상위화)<<구조통일화(하위화)-일괄성
  - 단순줄임화(상위화)<<구조통일화(하위화)-일률성
  - 단순줄임화(상위화)<<구조통일화(하위화)-최적성
  - 단순줄임화(상위화)>>구조통일화(하위화)-법학
  - 단순줄임화(상위화)>>구조통일화(하위화)-법학.실익성


##
- 200👉`ResponseEntity.status(HttpStatus.OK).body(data);` 
- 201👉`ResponseEntity.status(HttpStatus.CREATED).body(data);` 

- 401👉`ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");` 





##
- `ResponseEntity<CommonResponse<DTO>>`
- 뒤가 길어지는 건 자바의 숙명(?) 같은 것입니다. 코드가 길어지는 것보다 정보가 누락되어 발생하는 버그나 소통 비용이 훨씬 더 비쌉니다. 가급적 길더라도 타입을 꼭 명시해 주세요
- 가장 '정석(Best Practice)'으로 통용되는 방식은 코드가 조금 길어지더라도 `ResponseEntity<CommonResponse<DTO>>` 처럼 타입을 명확히 명시하는 것입니다.
- Level별로 개념잡혀있음.
- 상태.응답.내용
- 상태.응답.객체
- 3레벨
- 3레벨응답
- 3레벨반환
- `ResponseEntity<CommonResponse<?>>`<<`ResponseEntity<CommonResponse<DTO>>`.현대적표준
- `ResponseEntity<CommonResponse<?>>`<<`ResponseEntity<CommonResponse<DTO>>`



##
- [ ] CommonResponse.Class
- [x] CommonResponse.Record
  - CommonResponse.Class<<Record


## DTO
- [ ] ㅇㅇResponse
- [ ] Map
- [ ] String



## Return
- `return ResponseEntity.status().body()`



## DTO
- 1메소드1DTO
- 1Method1DTO
- 1메소드1디티오
- 현업에서는 1메서드 1DTO(또는 재사용 가능한 DTO) 구조가 정석



## 200ㆍ201
- 200
  - Read
  - Update

  - Update.200 vs. 204
  - Update.200>>204

  - Delete.200>>204-CommonResponse
  - Delete.200>>204-CommonResponse.내스타일
  - Delete.200>>204-규격
  - Delete.200>>204-규격.CommonResponse
  - Delete.200>>204-규격.내스타일ㄱㄱ.
  - Delete.200>>204-규격.통일화ㄱㄱ.
- 201
  - Create

- 204
  - Delete
  - 좀더깔끔하게할때 이것하는듯.
  - 근데
  - 정석적형태는 일단은ㄴㄴ.
  - Delete.뭐안보낼때ㄱㄱ.
  - Delete.200<<204-단순




- CRUD.Status
- C.201
- R.200
- U.200
- D.200


구조
| CRUD   | Status |
| :----- | :----- |
| Create | 201    |
| Read   | 200    |
| Update | 200    |
| Delete | 200    |


