## 컴포넌트슬롯
통상경우
```
  <AppSidebar>
    📌Test
  </AppSidebar> 
```


layout경우
```
  <AppSidebar>
    Test
    <slot />
  </AppSidebar> 
```
- 📁pages
- 📁layouts
  - 이런데서 끌어쓸때
  - 사용시점경우에 slot데이터 정의화


컴포넌트슬롯선언
```
      ... 
      <!-- MainContent -->
      <slot />
      ...
```
- `📌Test`
- 이부분이 들어가는지점