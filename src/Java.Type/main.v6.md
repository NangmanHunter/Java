

### 1. 자바 언어 명세(JLS) 공식 사이트
* **전체 목록:** 
* **Java SE 17 언어 명세 직접 링크:** [The Java Language Specification, Java SE 17 Edition](https://docs.oracle.com/javase/specs/jls/se17/html/index.html)

---



##
자바 언어의 공식 설계도라고 할 수 있는 **Java Language Specification (JLS)** 사이트와 해당 내용을 확인할 수 있는 정확한 위치를 알려드릴게요.

가장 최신 표준인 **Java SE 17** 이상의 명세를 기준으로 보시면 좋습니다.



### 2. 질문하신 "덩어리"들을 정의하는 핵심 챕터
명세서 사이트에 들어가시면 왼쪽 목차에서 다음 장들을 확인해 보세요. 개발자님이 말씀하신 그 "딱 그 자리"에 대한 정의가 나옵니다.

* **제8장: Classes (클래스)** ([링크](https://docs.oracle.com/javase/specs/jls/se17/html/jls-8.html))
    * 여기에 일반적인 `class`뿐만 아니라, **Record Classes(§8.10)**와 **Enum Classes(§8.9)**가 포함되어 있습니다. 명세상으로 레코드는 **"Special kind of class"**로 분류됩니다.
* **제9장: Interfaces (인터페이스)** ([링크](https://docs.oracle.com/javase/specs/jls/se17/html/jls-9.html))
    * 인터페이스와 어노테이션 타입(`@interface`)을 정의합니다.
* **제7.6절: Top Level Type Declarations**
    * 패키지 선언 바로 아래, 우리가 `.java` 파일을 만들었을 때 가장 바깥쪽 껍데기에 쓰는 `public class...`, `public record...` 등을 **최상위 타입 선언**이라고 부른다는 규칙이 명시되어 있습니다.

---

### 3. 명세서에서 사용하는 용어 요약
명세서를 읽다 보면 그 "자리"를 다음 단어들로 표현하는 것을 보실 수 있습니다.

1.  **Type Declaration (타입 선언):** 새로운 참조 타입을 도입하는 행위 그 자체입니다.
2.  **Entity (엔티티):** 클래스, 인터페이스, 레코드 등을 통칭하여 부를 때 쓰는 추상적인 표현입니다.
3.  **Reference Type (참조 타입):** 기본 타입(`int`, `boolean` 등)과 구별되는, 메모리 주소를 참조하는 모든 객체 지향적 구조체들의 총칭입니다.

수학으로 치면 **"정의(Definition)"** 파트를 찾고 계신 것과 같습니다. 위 링크들을 통해 자바라는 언어가 클래스와 레코드를 어떤 철학으로 구분해 두었는지 원문을 확인해 보시면 훨씬 갈증이 해소되실 거예요!