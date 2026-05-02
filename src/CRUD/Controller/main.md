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


## 제네릭타입
- [x] `ResponseEntity<?>`
  - 일단작업위해ㄱㄱ.
- [ ] `ResponseEntity<Object>`
- [x] `ResponseEntity<CommonResponse<?>>`
  - 싹다규격화위해ㄱㄱ.
- [ ] `ResponseEntity<DTO>`


## 타입
- [ ] `ResponseEntity<?>`
- [ ] `ResponseEntity<Object>`
- [x] `ResponseEntity<CommonResponse<?>>`
- [ ] `ResponseEntity<DTO>`
- [ ] `DTO`
- [ ] `String`


## DTO
- [ ] ㅇㅇResponse
- [ ] Map
- [ ] String



## Return
- `return ResponseEntity.status().body()`