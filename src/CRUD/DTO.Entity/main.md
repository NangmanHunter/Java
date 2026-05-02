
```
DTO                     Entity(C)    Entity(C)  
 →  Controller → Service → Repository →
 ←  Controller ← Service ← Repository ←
DTO                     Entity(R)    Entity(R)
```
- Create save 하고도 Entity반환형태.
- 생각해보니 너무도 자연스러운게. Entity중심으로 JPA가 잡아다 Update하든말든 이런걸함.
- 결국
- 모든반환타입이 Entity로 굴러가는듯함.
- 그래야지 객체상태 들고있을수있거든.
- 그래야지 영속성컨텍스트 될수있거든.

- delete는 void이다
  - 아마 반환없다고 하면은. 그부분 날리는형태로 진행할듯. 이걸세팅해놨을듯.

