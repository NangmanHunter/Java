오, 아주 날카로운 질문입니다! 객체지향의 **상속(Upcasting/Downcasting)** 개념과 연결해서 생각하셨는데, 사실 빌더 패턴은 상속보다는 **"도우미 객체를 통한 위임"**에 가깝습니다.

결론부터 말씀드리면, `BoardBuilder`는 `Board`를 상속받은 부모나 자식이 아니라, **`Board`를 만들기 위해 잠시 고용된 "조립 전문가(Helper Class)"**입니다.

---

### 1. `BoardBuilder`는 어디서 튀어나온 건가요?

Lombok의 `@Builder` 어노테이션을 붙이면, 컴파일 시점에 우리 눈에는 보이지 않지만 클래스 내부에 **내부 클래스(Inner Class)**로 `BoardBuilder`라는 클래스가 자동으로 만들어집니다.

```java
public class Board {
    private String title;
    // ... 필드들

    // 1. static 메서드로 빌더 객체를 생성해서 반환함
    public static BoardBuilder builder() {
        return new BoardBuilder();
    }

    // 2. 클래스 안에 숨겨진 '조립 전문가' 클래스
    public static class BoardBuilder {
        private String title;
        
        public BoardBuilder title(String title) {
            this.title = title;
            return this; // 계속해서 설정을 이어가기 위해 '나 자신'을 반환함 (Chaining)
        }

        // 3. 마지막에 호출하는 핵심 메서드
        public Board build() {
            // 조립이 끝나면 드디어 진짜 'Board' 객체를 생성해서 반환함
            return new Board(this.title, ...);
        }
    }
}
```

---

### 2. 업캐스팅/다운캐스팅 개념과의 차이

질문하신 **캐스팅(Casting)**은 "나와 뿌리가 같은(상속 관계) 객체" 사이에서 일어나는 일이지만, 빌더는 **"타입이 완전히 다른 두 객체"** 사이의 협력입니다.

* **`builder()` 호출 시:** `Board` 클래스에서 `BoardBuilder`라는 **완전히 새로운 타입의 객체**를 만들어서 건네주는 것입니다. (업캐스팅 X)
* **`.build()` 호출 시:** `BoardBuilder`가 자기가 들고 있던 재료들을 싹 모아서 **최종 결과물인 `Board` 객체를 새로 생성**해서 던져주는 것입니다. (다운캐스팅 X)



---

### 3. 왜 이런 번거로운 짓을 할까요? (핵심 이유)

비유를 들자면 이렇습니다.
* **생성자:** "완성된 샌드위치를 한꺼번에 입에 넣어주는 것" (재료 순서 틀리면 큰일 남)
* **빌더:** "쟁반(`BoardBuilder`)을 하나 받고, 거기에 햄 넣고, 치즈 넣고(`title()`, `content()`), 마지막에 포장(`build()`)해서 샌드위치(`Board`)로 완성하는 과정"

이 과정에서 `BoardBuilder`는 오직 **"재료를 임시로 담아두는 쟁반"** 역할만 수행합니다. 마지막에 `.build()`를 눌러야 비로소 우리가 원하는 진짜 `Board` 타입이 튀어나오는 거죠.

---

### 4. 정리하자면
* **`BoardBuilder`**는 상속 관계가 아니라, `Board` 클래스 안에 정의된 **별도의 보조 클래스**입니다.
* **`.build()`**는 타입을 변환하는 게 아니라, 보조 객체가 가진 데이터로 **진짜 객체를 생성(new)**해서 반환하는 동작입니다.

이제 왜 갑자기 `BoardBuilder`가 튀어나오고, 마지막에 `.build()`를 해야 하는지 감이 좀 오시나요? 이게 처음에 보면 마법 같지만, 사실은 롬복이 뒤에서 열심히 클래스를 하나 더 만들어주고 있는 거랍니다! 편의점 도시락 조립 키트라고 생각하시면 편해요. :)