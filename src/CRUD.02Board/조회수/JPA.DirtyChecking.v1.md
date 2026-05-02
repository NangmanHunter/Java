##
✅DirtyChecking
```java
@Transactional(readOnly = true)
... class

@Transactional
... method()
```

❌DirtyChecking
```java
@Transactional(readOnly = true)
... class

... method()
```


##
DirtyChecking.자동Update
✅DirtyChecking.자동Update


##
정확히 짚으셨습니다! 범인은 바로 **`@Transactional(readOnly = true)`** 설정과 **트랜잭션의 부재**입니다.

현재 작성하신 코드에서 `findOne` 메서드가 왜 DB에 반영되지 않는지, 그리고 어떻게 고쳐야 하는지 깔끔하게 정리해 드릴게요.

---

## 1. 원인 분석: Dirty Checking이 작동하지 않음

JPA에는 엔티티의 상태 변화를 감지해서 자동으로 `UPDATE` 쿼리를 날려주는 **Dirty Checking(변경 감지)** 기능이 있습니다. 하지만 이 기능이 작동하려면 두 가지 조건이 필요합니다.

1.  **영속성 컨텍스트(Transaction) 내부에 있을 것:** 엔티티가 트랜잭션 안에서 관리되어야 합니다.
2.  **ReadOnly 상태가 아닐 것:** 읽기 전용 트랜잭션에서는 스냅샷 비교를 하지 않아 성능을 최적화하므로, 변경 사항을 DB에 쓰지 않습니다.

### 현재 코드의 문제점
* 클래스 상단에 `@Transactional(readOnly = true)`가 선언되어 있습니다.
* `findOne` 메서드에는 별도의 `@Transactional` 어노테이션이 없습니다. 
* 결과적으로 `findOne`은 **읽기 전용 트랜잭션**에서 실행되거나, 아예 트랜잭션 없이 실행됩니다. JPA는 "아, 이건 조회만 하고 끝낼 거구나"라고 판단해서 `board.addViewCount()`로 객체 값을 바꿔도 DB에 반영하지 않는 것이죠.

---

## 2. 해결 방법: `@Transactional` 추가

`findOne` 메서드 위에 `@Transactional`을 붙여주기만 하면 됩니다. 이렇게 하면 메서드가 종료되는 시점에 JPA가 "어? `viewCount`가 바뀌었네?" 하고 자동으로 `UPDATE` 쿼리를 날립니다.

```java
/**
 * 게시글 단건 조회 (조회수 증가 포함)
 */
@Transactional // readonly = true를 덮어씌웁니다.
public Board findOne(Long boardId) {
    Board board = boardRepository.findById(boardId)
            .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
    
    board.addViewCount(); // 이제 트랜잭션 종료 시점에 DB에 반영됩니다!
    return board;
}
```

---

## 3. 한 걸음 더: 조회수 로직의 고민

지금처럼 `findOne`에서 조회수를 올리면 발생할 수 있는 소소한 문제들이 있습니다.

* **동시성 문제:** 게시글 하나를 동시에 100명이 클릭하면, JPA 변경 감지 특성상 조회수가 정확히 100이 안 오르고 누락될 수 있습니다. (이 경우 벌크성 쿼리 `@Modifying`을 사용합니다.)
* **새로고침 문제:** 사용자가 F5만 눌러도 조회수가 계속 올라갑니다. (보통 쿠키나 세션을 활용해 방어하곤 하죠.)



### 요약하자면?
지금은 **`@Transactional`**만 추가하셔도 원하시는 대로 DB에 뙇! 반영될 겁니다. 클래스 상단의 `readOnly = true`는 조회 성능 최적화에는 좋지만, 수정을 동반하는 메서드에서는 반드시 위처럼 따로 선언해주어야 한다는 점 잊지 마세요!

수정해 보시고 또 안 되면 말씀해 주세요. 잘 하고 계십니다!