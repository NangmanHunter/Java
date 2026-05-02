- https://vuesax.com/docs/components/Sidebar.html#default


## `flex-1`
- flex-1: slot을 감싸는 div에 이 클래스를 주면, 사이드바가 차지하고 남은 오른쪽 전체 공간을 이 녀석이 다 가져갑니다.
- 만약 그 안에 <div class="flex-1">이나 <div class="w-full"> 같은 클래스가 본문 영역을 감싸고 있다면, 그 코드가 **"사이드바 내부에서 빈 공간"**을 만들고 있는 것입니다.


- AppSidebar
- ✅`div class="flex"`
- ❌`div class="flex flex-1"`
  - `flex-1` 지워줘버리면 공간차지하는거 싹줄어듬.



- flex-1
- flex-0
  - 이러면 싹줄어듬.


## layout
- 얘가 근본적으로 layout이다
- component쪽보다는 layout으로 보는게 더맞음.
- 특히 버튼같은거 확실히 독립공간차지하고.
- component로가버리면 그하위단을 못씀. 버튼밑공간을 아예못씀. 아예 덩어리화되서 그자체로서 못건드림.
- 철저히 더 layout으로 와줘야함.
- 아... 이런거구나. 
- 그래서 분류자체도 layout에 잡혀있음.
- layout.Sidebar
- Layout.Sidebar


- 이게
  - 문서자체는 Components로 잡혀있으나.
  - 실상은ㄴㄴ.
  - 보니까 Components로 싹다 다잡아놓은형태이긴함.
  - 이게 단위성으로 가는형태이고.
  - 이거를 더올려서 layout으로 끌어가든말든 그런형태인듯.
  - 문제는 문서볼경우 컴포넌트로 잡혀 있다라고 하는 것 이게 혼선 원인임
  - 그래서 철저히 더 다르게 봐줄것 
  - 그리고 무엇보다 `미완성`이다 
  - 미완성이라 개념이 분화가 덜됐고 너무미약하다.
  - `layout`.`component`로 문서작성돼있음. 보여서 layout으로 끌어오든ㄱㄱ.
  - 특히 Slidbar 이건 암만봐도 레이아웃이다.ㄱㄱ.
  - 하단버튼 여기를 제어를못함.
  - 여기를 건들려면 그코드안에서 더들어가야함.
  - 근데 그게불능이다. component에서는
  - 그렇다면 layout여기에서만 가능하고. 그렇기에 layout이다ㄱㄱ.

  - 아...
  - 아닌가본데?!.
  - 애당초 layout도 component처럼 slot 이거를쓸수있음.
  - 아... 그러네
  - 이걸 레이아웃으로 안가도 되는구나. ...