## 문자
- 숫자지양.문자지향
- 숫자.문자-추구
- 왜 문자지향하는지 좀알겠음. 정보성이 한방적으로 바로튀어나올수있음.
- 한두개는 숫자>>문자. 그러나 볼륨크면 숫자<<문자. 압도적으로 문자가 더나음.
- 직감력이 문자자체로서 전달되는 개념들이있음. 이게 숫자는 필연적한계가있음. 메세지전달 감각전달 의미전달 이게안되기에.
- 보드.타입.이것도 종래는 숫자 이런걸로 쭉쭉가는게 더낫다라고 생각됐다가도.다시보니 그냥 명명형태로가는게 더좋다라고 너무도보임.

- 직감력
  - 명명으로가면 그래도 대충은 대량은 바로알수있기에. 이게큼. 
  - 직감력을 한두개라도 바로살릴수있다라는것.
- 경제성
  - 숫자가 더드라이해서 더좋은줄알았으나. 잘보면 숫자는 필연적으로 맵핑테이블이필요함. 그말은 표자체로 대응을 봐야되고. 그것못보면 전혀볼수없다라는것. 역으로 정보성이 더늘어나는형태.
  - 코드단에서 싹다한방에 해결아닌. 
  - 오히려 외부적인 명세적인 다른정보성을 더가져와서 더짱박아 더진행해야하기에 더독이라는.
  - 진짜그러네...ㄹㅇ. 정보성 하나더 알기위해. 나머지 다른리소스를 더가져와야된다라는것. 이게너무독임.


## boardType
- `type` vs. `boardType`
- `type`>>`boardType`-
- `type`>>`boardType`-필드까지 구별화 한정화 과하다생각
- `type`>>`boardType`-어차피 Board Entity로 구분되기에 필드까지 구별실익 없다생각함.
- `type`<<`boardType`-필드자체를 Enum으로 만들어버리는 이런경우. Class가 나와버리는경우 그러면 개별 독립된 유일성 잡아줘야함. 그렇기에 유일성으로 유일화로 진행해줘야되고. 이경우에는 선택아닌 필수가되버림.
- `type`<<`boardType`-
- `type`<<`boardType`-DB유일성.고유성 이런것 중요시한다함.
- `type`<<`boardType`-정렬성
- `type`<<`boardType`-`정렬성`
- `type`<<`boardType`-`정렬성`.`Entity/Board`ㆍ`Entity/BoardType`
- `type`<<`boardType`-`정렬성`.이게 제일큰듯. 이것만생각해도 이런식으로 가는게맞다.
- `type`<<`boardType`



- `type`>>`boardType`-객체지향설계화
- `type`>>`boardType`-객체지향적
- `type`>>`boardType`-객체지향적.인간한테 편한형태.
- `type`>>`boardType`-객체지향적.인간한테 편한형태.DB한테는 완전최적화는 아닌형태
- `type`>>`boardType`-모양은이쁜데 그다음작업성이 너무귀찮아짐. 하나하나 더쳐줘야함. 이게너무번거로움.
- `type`<<`boardType`-DB까지 일원화
- `type`<<`boardType`-DB는 속성고유화 컬럼고유화 이쪽에 더강하게 잡혀있는듯.
- `type`<<`boardType`-DB 컬럼고유성 엄청강하게생각함
- `type`<<`boardType`-DB 컬럼자체로 명명되어 그자체로 자체직감성을 워낙살리고파 더그런듯.
- `type`<<`boardType`-DB 무엇보다 조인하고 이러면 여기서 바로직감력 살리냐마냐 이걸로그렇게된듯함.
- `type`<<`boardType`-DB.아 ... 알았다. ... 컬럼 외래키 긁어오고 이러면서 컬럼명을 그대로가져옴. 그러다보니 컬럼명이 겹치면 삑사리나는형태. 그래서 `테이블명.컬럼명` 이런식으로 가져와서 우회형태가능.
- `type`<<`boardType`-DB.근데도 아싸리 달라버리면 이작업 `테이블명.컬럼명` 안해도됨 `컬럼명` 이것자체로 바로 쓸수있는것. 이게강한듯. 아. 이거구나...
- `type`<<`boardType`-JPA도 그대로 가져가기에. 


- 전략
- Java.`type` + DB.`boardType`-이런식진행

- `type`>>`boardType`-프론트쪽
- `type`ㆍ`boardType`-백엔드쪽
- `type`ㆍ`boardType`-백엔드쪽.애매하다.
- `type`ㆍ`boardType`-백엔드쪽.Class-`type`<<`boardType`.필드-`type`>>`boardType`
- `type`<<`boardType`-DB쪽

- `type`<<`boardType`-백엔드쪽
- `type`<<`boardType`-백엔드쪽.정하자ㄱㄱ.
- `type`<<`boardType`-백엔드쪽.JPA도 이쪽으로 싹다 최적화맞춰져있음.
- `type`<<`boardType`-백엔드쪽.타입ㆍ변수 이들관계도 규칙성 파스칼ㆍ카멜 그대로살릴수있음.
- `type`<<`boardType`-백엔드쪽.클래스명조차도 특히 Enum도 그대로 규칙성 싹다 그대로갈수있음. 이것대로ㄱㄱ.

- `type`<<`boardType`-결론 백엔드도 이쪽으로간다ㄱㄱ.


- `type`>>`boardType`-Url
  - `board/type`>>`board/boardType`-Url
  - `type`<<`boardType`-Url.얘도 그냥받아올때 boardType으로 가면되긴함. 그럼에도 url창에는 카멜ㄴㄴ.
- `type`<<`boardType`-프론트.Json
- `type`<<`boardType`-백엔드
- `type`<<`boardType`-DB


## boardId
- `type`<<`boardType`-백엔드쪽.예전에 id 이것도 `board_id` 이것대응위해 boardId 이렇게가는게 더맞는듯. 추후바꿀것ㄱㄱ.
- `id`<<`boardId`