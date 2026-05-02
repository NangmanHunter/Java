

## Constrctor
- [ ] @NoArgsConstructor
- [x] @NoArgsConstructor(access = AccessLevel.PROTECTED)
  - `(access = AccessLevel.PROTECTED)`
  - 이거는 철저히 딴데서 막기용이다.
- [ ] @RequiredArgsConstructor
- [x] @AllArgsConstructor


##
access = AccessLevel.PROTECTED 설정을 추가하면, 외부에서 new User()로 빈 객체를 만드는 실수를 방지하면서 프레임워크만 이 생성자를 쓰게 할 수 있어 안전합니다.
PROTECTED.상속받은경우에만
PROTECTED.동일패키지ㆍ상속받은경우에만


## @Builer
- @Builer
- 이것추후연마ㄱㄱ. 필요할듯ㄱㄱ.


## 빌더패턴
- [x] 빌더
- [ ] 생성자


