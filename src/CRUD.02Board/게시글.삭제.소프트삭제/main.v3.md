## @SQLDelete
- `@SQLDelete`
  - Update
- `@Where`
  - Select처리


## 2. 왜 굳이 이걸 써서 `delete()`를 호출할까?

"그냥 서비스 로직에서 `member.setDeleted(true)`라고 업데이트 치면 되는 거 아냐?"라고 하실 수 있습니다. 맞는 말씀이지만, `@SQLDelete`를 쓰면 다음과 같은 **개이득(?)** 포인트가 있습니다.

### ① 비즈니스 로직의 일관성
개발자 입장에서 삭제는 삭제입니다. 서비스 코드에 `memberRepository.updateIsDeletedTrue(id)` 같은 긴 이름을 가진 메서드를 만드는 대신, 표준인 `delete()`를 그대로 사용할 수 있어 코드가 깔끔해집니다.

### ② 연쇄 삭제(Cascade) 처리의 편리함
이게 진짜 중요합니다! 만약 부모 객체를 지울 때 자식 객체들도 같이 '논리 삭제(Soft Delete)' 되어야 한다면?
* 일반 업데이트 방식: 부모 지우고, 자식들 하나하나 찾아서 다 `setDeleted(true)` 해줘야 함.
* **@SQLDelete 방식:** 부모 엔티티에 `cascade = CascadeType.REMOVE`만 걸려있으면, JPA가 자식들의 `delete()`를 알아서 호출하고, 각각에 설정된 `@SQLDelete` 문이 실행되면서 **전부 알아서 업데이트**됩니다.

