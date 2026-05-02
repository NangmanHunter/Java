## 회원탈퇴
- id넘기기
  - Member넘기기<<id넘기기
  - 중량넘기기<<경량넘기기

- deleteById
  - `delete`<<`deleteById`


##
전체적인 구조는 깔끔하게 잘 잡으셨네요! 세션에서 로그인 정보를 받아와서 삭제를 진행하는 흐름은 좋습니다. 하지만 실제 운영 환경이나 JPA의 동작 원리를 고려했을 때 몇 가지 **치명적인 포인트**와 **개선하면 좋은 점**들이 보여요.

한번 짚어드릴게요.

### 1. 세션 무효화 (Invalidate Session)
현재 코드에서는 DB에서 회원 데이터를 지우지만, **사용자의 브라우저에 남아있는 세션**은 그대로 유지됩니다. 즉, DB에는 내가 없는데 로그인 상태는 유지되는 기현상이 발생할 수 있어요.

* **해결:** 컨트롤러에서 `HttpSession`을 받아 `session.invalidate()`를 호출해줘야 합니다.

### 2. JPA 영속성 컨텍스트 문제 (객체 불일치)
서비스 단의 `memberRepository.delete(loginMember)` 부분입니다. 세션에 저장되어 있던 `loginMember` 객체는 현재 트랜잭션의 영속성 컨텍스트가 관리하는 객체가 아닐 확률이 높습니다(준영속 상태).

* 보통은 `id`값만 받아서 `deleteById(id)`를 쓰거나, 서비스에서 `findById`로 조회한 뒤 삭제하는 것이 더 안전하고 명확합니다.

### 3. @DeleteMapping의 경로
현재 `@DeleteMapping("/delete")`로 되어 있는데, RESTful한 설계를 지향한다면 보통은 리소스를 명시합니다. (예: `/api/members/me` 또는 `/api/members/{id}`) 하지만 이건 취향이나 프로젝트 규칙에 따라 다를 수 있어요.

---

### 개선된 코드 제안

#### [Controller]
```java
@DeleteMapping("/delete")
public ResponseEntity<Void> deleteMember(
        @SessionAttribute(name = "loginMember", required = false) Member loginMember,
        HttpServletRequest request) {

    if (loginMember == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 로그인 안 됨
    }

    // 1. 서비스 호출 (DB 삭제)
    memberService.delete(loginMember.getId());

    // 2. 세션 강제 종료
    HttpSession session = request.getSession(false);
    if (session != null) {
        session.invalidate();
    }

    return ResponseEntity.noContent().build(); // 204 No Content (성공했지만 줄 데이터는 없음)
}
```

#### [Service]
```java
@Transactional
public void delete(Long memberId) {
    // 객체 전체를 넘기기보다 ID를 넘겨서 삭제하는 것이 JPA 처리에 더 깔끔합니다.
    if (!memberRepository.existsById(memberId)) {
        throw new IllegalArgumentException("존재하지 않는 회원입니다.");
    }
    memberRepository.deleteById(memberId);
}
```

---

### 추가로 고민해 볼 점: "소프트 딜리트(Soft Delete)"
실제 실무에서는 회원 데이터를 `DELETE` 쿼리로 완전히 날려버리는(Hard Delete) 경우보다, `is_deleted` 같은 컬럼을 두고 **탈퇴 상태만 업데이트**하는 방식을 많이 씁니다.

* **이유:** 나중에 CS 처리나 데이터 통계, 혹은 실수로 탈퇴했을 때의 복구 가능성 때문입니다.

지금 만들고 계신 프로젝트가 학습용이고 "완전 삭제"가 목적이라면 현재 작성하신 코드를 보완하는 것만으로도 충분합니다! 10년 공부하셨던 끈기라면 이런 예외 처리들도 금방 익숙해지실 거예요.

혹시 탈퇴할 때 연관된 다른 게시글이나 댓글들도 같이 지워지게 설정(Cascade)하셨나요?




