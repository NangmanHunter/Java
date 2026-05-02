

##


##
## 2. `useRoute()` : 현재 페이지의 '상태 정보 창'

현재 사용자가 **어떤 주소에 있는지**, **주소창에 어떤 파라미터가 붙어있는지**를 알려줍니다.

### 📍 비유: "내비게이션 현재 위치 정보"
"지금 강남역 사거리에 있고, 속도는 60km이며, 목적지는 맛집이다"라는 정보를 실시간으로 보여주는 것과 같습니다.

* **주요 정보:**
    * `route.path`: 현재 전체 경로 (예: `/board/notice/123`)
    * `route.params`: URL의 가변 변수 (예: `[id].vue` 파일이라면 `{ id: '123' }`)
    * `route.query`: `?search=낭만` 처럼 물음표 뒤에 붙는 값

### 💻 실제 사용 예시
```javascript
const route = useRoute()

// /board/notice/5 페이지라면?
console.log(route.params.id) // '5' 가 출력됨
```
아까 댓글 기능 만드실 때 `route.path.split('/')`을 하셨던 이유도, 이 `useRoute`가 준 현재 주소 정보를 분석하기 위해서였던 거죠!

---
### 💡 팁: `useRouter`와 헷갈리지 마세요!
* **`useRoute` (정보):** "지금 어디에 있지?" (읽기 전용 상태창)
* **`useRouter` (동작):** "다른 페이지로 가자!" (이동 명령 핸들)
