##
- Enum.대문자
- Enum.UppderCase
- Enum.✅대문자
- Enum.자바에서는 무조건 대문자로 가줘야되긴함.
- Enum.그나마 대응도 받아서 대문자로 변환해서 찾는형태.Enum자체를 절대로 바꾸지는 않는다는것.
- Enum.대문자.프론트대문자요청 vs. 백엔드소문자열어주기
- Enum.대문자.프론트대문자요청 vs. 백엔드대문자변환
- Enum.대문자.프론트대문자요청 vs. 프론트소문자요청ㆍ백엔드대문자변환
- Enum.대문자.프론트대문자요청 vs. 백엔드대문자변환화


##
- 백엔드에서 소문자도 받아줘서 진행하는형태. 이것도 ㅇㅋ이긴한데. 너무친절하다. 너무과잉친절. 여기서 기싸움할듯.
- 이건 나중작업으로가자. 지금 꼭중요한건 아닌듯.


##
**400 Bad Request**가 떴다는 건, 이제 백엔드와 연결은 성공했지만 백엔드가 **"네가 보낸 데이터(URL 파라미터) 형식이 내가 이해하는 거랑 달라!"**라고 거절한 상황입니다.

지금 가장 유력한 범인은 **Enum의 대소문자 매핑** 문제입니다.

---

### 1. 원인 분석
자바의 `enum BoardType { NOTICE, ... }`은 기본적으로 **대문자**입니다.
그런데 지금 프론트엔드 코드에서 요청을 이렇게 보내고 계시죠?

> `GET http://localhost:8080/api/boards/notice` (소문자)

스프링 부트는 기본적으로 문자열을 Enum으로 바꿀 때 **대문자/소문자를 엄격하게 구분**합니다. 그래서 `notice`라는 문자열을 받으면 "나는 `NOTICE`는 알아도 `notice`는 몰라!" 하면서 400 에러를 던지는 겁니다.

---

### 2. 해결 방법

#### 방법 A: 프론트엔드 URL 수정 (가장 간단)
요청 주소를 대문자로 바꿔보세요.
```typescript
// index.vue
const { data: boards, ... } = await useFetch<any[]>('/api/boards/NOTICE', { ... })
```

#### 방법 B: 백엔드에서 대소문자 무시 설정 (가장 친절)
사용자가 소문자로 요청해도 찰떡같이 알아듣게 만들고 싶다면, 백엔드 `BoardType` Enum에 설정을 하나 추가하면 됩니다.

```java
// BoardType.java
import com.fasterxml.jackson.annotation.JsonCreator;

public enum BoardType {
    NOTICE("공지사항"),
    COMMUNITY("자유게시판");

    // ... (기존 코드)

    // 소문자로 들어와도 대문자로 변환해서 매핑해주는 마법의 메서드
    @JsonCreator
    public static BoardType from(String value) {
        return BoardType.valueOf(value.toUpperCase());
    }
}
```

---

### 3. 확인 절차
1.  브라우저 주소창에 직접 `http://localhost:8080/api/boards/NOTICE`를 쳐보세요.
    * 여기서 데이터가 잘 나오면 100% 대소문자 문제입니다.
2.  만약 대문자로 쳐도 400 에러가 난다면, 컨트롤러의 `@PathVariable` 설정과 `BoardType`의 위치가 정확한지 다시 한번 대조해봐야 합니다.



**요약하자면:** 백엔드는 지금 "공지사항(NOTICE) 줘!"라고 해야 알아듣는데, 프론트엔드가 "공지사항(notice) 줘!"라고 속삭여서 못 알아듣고 있는 상태입니다. URL을 대문자로 바꿔서 다시 한번 새로고침 해보시겠어요?