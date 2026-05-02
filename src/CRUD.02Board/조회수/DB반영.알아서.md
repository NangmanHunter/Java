## 
- JPA알아서 vs. DB쪽에바로직접

- [ ] 더디체킹
  - JPA알아서보고 알아서 Update하는형태
  - 트랜잭션지점
  - 트랜잭션지점.Service부분

  - 메모리단위에서 `+1`하고


  - 여기서 Repository 이부분하고 느려서 터지는듯. 흠결되는형태인듯.
  - 편의상 Service지점에 놓았지만.
  - Repository까지 왔다갔다해야되서. Service지점에서 오다가. CPU가 가로채져 인터럽트생겨 다른일하러가면 그때 삑사리나는형태.

  - 편하면 쓰레기이다.
  - 건하성님 맞말.
  - 아...이게 처음에는 좋았는데. 좋은게 좋은게 아니구나... 라는걸 더느끼는듯.
  - 편하지만 다시금 원래대로 원상대로 예전방식 레거시방식으로 더돌아가야되겠구나싶음.


- [x] 쿼리직접수정 
  - 동시성
  - 쿼리직접수정 
  - Repository 여기서 직접쿼리 고치는형태.



- 다른게아니라
  - 조회수같은것들은 뭐 쉽게쉽게해서 진행할수는있음.
  - 그러나
  - 표같은것들이있음.
  - 순번
  - 이런것들은 이런식으로 동일번호 나와버리면 아싸리 다털리는격임.
  - 이런것 습관화ㄴㄴ.
  - 편하다고 습관화ㄴㄴ. 아 난맘에안든다.ㄹㅇ.



##
정확하고 날카로운 지적입니다! "객체의 상태만 바꿨는데 어떻게 DB에 반영될까?"라는 의문은 JPA를 공부할 때 가장 중요하고 핵심적인 질문 중 하나예요.

결론부터 말씀드리면, **JPA의 '더티 체킹(Dirty Checking, 변경 감지)'**이라는 메커니즘 덕분에 Repository를 직접 건드리지 않아도 DB에 반영됩니다.

### 1. 왜 Repository 호출 없이도 DB가 업데이트될까?
JPA는 엔티티를 조회하면 그 시점의 상태를 별도로 복사해서 보관해둡니다(스냅샷). 그리고 트랜잭션이 끝나는 시점에 현재 엔티티의 상태와 처음 스냅샷을 비교하죠.

* **동작 원리:**
    1.  `findById`로 DB에서 `Board`를 가져오면 JPA가 이를 관리(영속 상태)하기 시작합니다.
    2.  `board.addViewCount()`를 호출하면 메모리상에 있는 객체의 값(`viewCount`)이 바뀝니다.
    3.  **트랜잭션이 끝나는 시점**에 JPA가 "어? 처음 가져왔을 때보다 값이 바뀌었네?"라고 감지합니다.
    4.  JPA가 알아서 `UPDATE` 쿼리를 만들어 DB에 날려줍니다.

그래서 별도로 `boardRepository.save(board)`를 호출할 필요가 없습니다. (오히려 호출하지 않는 것이 관례입니다.)



---

### 2. 하지만 이 방식의 잠재적 문제 (동시성)
작성하신 방식은 **'애플리케이션에서 숫자를 읽어서 +1을 한 뒤 다시 저장'**하는 방식입니다. 만약 아주 짧은 찰나에 두 명의 사용자가 동시에 클릭하면 어떤 일이 벌어질까요?

1.  A사용자가 조회수 10을 읽음.
2.  B사용자가 조회수 10을 읽음.
3.  A가 11로 바꾸고 DB에 저장.
4.  B도 (본인이 읽은 10을 기준으로) 11로 바꾸고 DB에 저장.
5.  결과적으로 조회수는 12가 되어야 하는데 **11에 머물게 됩니다.** (Lost Update 문제)

---

### 3. 해결책: Repository에서 직접 쿼리 날리기
위와 같은 동시성 문제를 방지하고, 성능을 더 높이고 싶다면 **DB가 직접 숫자를 올리게** 만드는 것이 가장 확실합니다. 이때 `BoardRepository`를 수정하게 됩니다.

**BoardRepository.java 수정:**
```java
public interface BoardRepository extends JpaRepository<Board, Long> {
    
    @Modifying // 데이터 변경이 일어나는 쿼리임을 명시
    @Query("update Board b set b.viewCount = b.viewCount + 1 where b.id = :id")
    int updateViewCount(@Param("id") Long id);

    // 기존 메서드들...
    List<Board> findByMember(Member member);
}
```

**Service.java 수정:**
```java
@Transactional
public Board findOne(Long boardId) {
    // 1. DB에서 직접 조회수 증가 (Update 쿼리 실행)
    boardRepository.updateViewCount(boardId);
    
    // 2. 조회수 증가가 반영된 객체를 가져와서 반환
    return boardRepository.findById(boardId)
            .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
}
```

### 요약하자면:
1.  **현재 방식:** JPA의 변경 감지 덕분에 DB 반영은 잘 됩니다. (소규모에 적합)
2.  **Repository 수정 방식:** 동시성 문제를 방지하고 성능상 이점이 있습니다. (실무/대규모에 적합)

수학적으로 보면 `x = x + 1` 연산을 애플리케이션 메모리에서 하느냐, DB 엔진 내부에서 원자적(Atomic)으로 처리하느냐의 차이입니다. `bitcopark-base` 프로젝트의 완성도를 높이고 싶다면 **3번 방식(Repository 직접 쿼리)**을 시도해 보시는 걸 추천드려요!