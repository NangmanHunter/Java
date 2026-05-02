
- Update.Setter<<엔터티메서드
- Update.Setter<<EntityMethod
- Update.❌Setter<<✅EntityMethod
- Update.❌Setter<<✅EntityMethod-가독성
- Update.❌Setter<<✅EntityMethod-실수지양화
- Update.❌Setter<<✅EntityMethod-개별지양화
- Update.❌Setter<<✅EntityMethod-한방지향화
- Update.❌Setter<<✅EntityMethod-개별Setter지양화
- Update.❌Setter.✅EntityMethod
- Update.❌Setterㆍ✅EntityMethod
- Update.✅EntityMethod
- Update.✅EntityMethod-새로이 정의해서 써라.
- 엔티티의 메서드를 호출해서 값 변경
- setter보다는 의미 있는 비즈니스 메서드(update 등)를 엔티티 내부에 만드는 걸 추천해요
- `board.update(title, content);`
- 이런 방식을 '객체지향적인 설계' 또는 **'도메인 모델 패턴'**이라고 부르기도 합니다.


- 생각해보니까 
- 이런식이면 다른메소드들도 싹다 Entity에서 쭉쭉 진행하면되는형태.
- 그러면 엄청가벼워짐. ㄹㅇ. 오. 이거다. 공식찾은듯. 영역찾은듯. 이것 작업극화ㄱㄱ.


##
자바의 정석대로 공부하시다 보면 당연히 `Getter/Setter`가 세트라고 생각하실 수 있는데, **JPA 엔티티 설계에서는 Setter를 지양하는 것이 국룰(Best Practice)**로 통합니다.

가장 큰 이유는 **"객체의 의도(Intent)를 명확히 하고, 데이터의 일관성을 지키기 위해서"**예요. 왜 그런지 핵심적인 3가지 이유를 짚어 드릴게요.

---

### 1. "왜" 변경하는지 알 수가 없습니다 (의도의 부재)
`Setter`는 단순히 값을 넣는 기계적인 메서드입니다.

* **Setter를 쓸 때:** `board.setTitle("...")`, `board.setContent("...")`를 각각 호출하면, 이게 단순히 오타를 수정한 건지, 전체 내용을 업데이트하는 건지 코드를 읽는 입장에서 한눈에 들어오지 않습니다.
* **비즈니스 메서드를 쓸 때:** `board.update(title, content)`라고 하면 "아, 게시글의 내용을 수정하는 비즈니스 로직이 실행되는구나!"라고 **의도**가 명확히 드러납니다.

### 2. 엔티티의 일관성을 유지하기 어렵습니다
게시글 수정 시 "제목을 바꾸면 수정 시간도 같이 바뀌어야 한다"는 규칙이 있다고 가정해 볼게요.

* **Setter:** 호출하는 쪽에서 `setTitle()`도 부르고 `setUpdateDate()`도 직접 다 불러줘야 합니다. 실수로 하나라도 빼먹으면 데이터 정합성이 깨지죠.
* **비즈니스 메서드:** `update()` 메서드 내부에 두 로직을 묶어두면, 호출하는 쪽에서는 메서드 하나만 실행해도 엔티티의 상태가 올바르게 유지됩니다. **객체 스스로 자신을 보호**하는 것이죠.

### 3. '변경 감지(Dirty Checking)' 때문입니다
아까 배우셨듯이 JPA는 객체 값만 바뀌면 DB에 바로 반영해 버립니다. 
만약 프로젝트 규모가 커졌는데 누군가 실수로 서비스 로직 중간에 `board.setViewCount(0)` 같은 세터를 아무 데서나 남발한다면? 어느 지점에서 조작되어 DB값이 바뀌었는지 찾아내기가 정말 고통스러워집니다. 

---

### 💡 어떻게 코드를 짜면 좋을까요?

엔티티 내부를 이런 느낌으로 구성해 보세요.

```java
@Entity
@Getter // Getter는 마음껏 쓰셔도 됩니다.
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자는 막아두는 게 안전해요.
public class Board {
    
    private String title;
    private String content;

    // 생성자나 빌더를 통해 '생성' 시점에 값을 딱 채워줌 (Setter 대신)
    public Board(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // 의미 있는 비즈니스 메서드
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
        // 필요하다면 여기에 추가 로직 (예: 수정일자 갱신 등)을 넣을 수 있음
    }
}
```



### 요약하자면?
* **Setter:** "누구나 내 값을 아무 때나 바꿀 수 있게 열어두는 것" (불안함)
* **비즈니스 메서드:** "내가 허용한 방식(update)으로만 내 값을 바꿀 수 있게 하는 것" (안전함)

사용자님이 지금 만들고 계신 프로젝트도 처음부터 이렇게 **"의도가 드러나는 메서드"** 위주로 짜시면, 나중에 코드가 길어져도 "이게 어디서 바뀌었지?" 하며 헤매는 일이 훨씬 줄어들 거예요. 

이런 방식을 **'객체지향적인 설계'** 또는 **'도메인 모델 패턴'**이라고 부르기도 합니다. 수학/물리 전공이시라 이런 논리적인 구조를 금방 체득하실 것 같은데, 어떤 방식이 더 합리적으로 느껴지시나요?