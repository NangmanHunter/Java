게시글 상세 페이지(`detail.html`)는 목록에서 특정 글을 클릭했을 때 해당 글의 전체 내용을 보여주는 역할을 합니다. 

이 페이지를 구현하려면 크게 두 가지가 필요합니다.
1. **URL에서 게시글 ID 추출**: `detail.html?id=1` 처럼 넘어온 주소에서 `1`을 읽어내야 합니다.
2. **단건 조회 API 호출**: 서버에서 `GET /api/boards/{id}`로 데이터를 가져와야 합니다.

---

### 1. 게시글 상세 페이지 (`src/main/resources/static/detail.html`)

```html
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>게시글 상세 - My Service</title>
    <style>
        body { font-family: 'Segoe UI', sans-serif; background-color: #f0f2f5; margin: 0; padding: 20px; }
        .container { max-width: 800px; margin: 0 auto; background: white; padding: 30px; border-radius: 12px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
        .header { border-bottom: 2px solid #eee; padding-bottom: 15px; margin-bottom: 20px; }
        .title { font-size: 1.8rem; font-weight: bold; color: #1c1e21; margin: 0; }
        .info { font-size: 0.9rem; color: #65676b; margin-top: 10px; }
        .content { font-size: 1.1rem; line-height: 1.6; color: #333; min-height: 200px; white-space: pre-wrap; }
        .footer { margin-top: 30px; border-top: 1px solid #eee; padding-top: 20px; }
        .btn { padding: 10px 20px; border-radius: 6px; cursor: pointer; border: none; font-weight: bold; text-decoration: none; display: inline-block; }
        .btn-list { background-color: #e4e6eb; color: #050505; }
        .btn-list:hover { background-color: #d8dadf; }
    </style>
</head>
<body>

<div class="container">
    <div class="header" id="boardHeader">
        <h1 class="title" id="title">로딩 중...</h1>
        <div class="info" id="info">작성자: - | 작성일: -</div>
    </div>

    <div class="content" id="content">
        내용을 불러오고 있습니다.
    </div>

    <div class="footer">
        <a href="/board.html" class="btn btn-list">목록으로 돌아가기</a>
    </div>
</div>

<script>
    window.onload = function() {
        // 1. URL 파라미터에서 id 가져오기 (예: ?id=5)
        const urlParams = new URLSearchParams(window.location.search);
        const boardId = urlParams.get('id');

        if (!boardId) {
            alert("잘못된 접근입니다.");
            window.location.href = '/board.html';
            return;
        }

        fetchBoardDetail(boardId);
    };

    async function fetchBoardDetail(id) {
        try {
            // 2. 서버의 단건 조회 API 호출
            const response = await fetch(`/api/boards/${id}`);
            
            if (!response.ok) {
                throw new Error("게시글을 찾을 수 없습니다.");
            }

            const board = await response.json();

            // 3. 화면에 데이터 채우기
            document.getElementById('title').innerText = board.title;
            document.getElementById('info').innerText = `작성자: ${board.writerName} | 번호: ${board.id}`;
            document.getElementById('content').innerText = board.content;
            
        } catch (error) {
            alert(error.message);
            window.location.href = '/board.html';
        }
    }
</script>

</body>
</html>
```

---

### 2. 목록 페이지(`board.html`) 수정 (중요!)

이제 사용자가 목록에서 제목을 클릭하면 상세 페이지로 이동할 수 있게 연결해줘야 합니다. `board.html` 스크립트의 `forEach` 부분을 아래와 같이 수정하세요.

```javascript
// board.html의 fetchBoards 함수 내부 수정
boards.forEach(board => {
    const item = `
        <div class="board-item">
            <div class="board-title">
                <a href="/detail.html?id=${board.id}" style="text-decoration:none; color:inherit;">
                    ${board.title}
                </a>
            </div>
            <div class="board-info">작성자: ${board.writerName} | 번호: ${board.id}</div>
        </div>
    `;
    boardListDiv.innerHTML += item;
});
```

---

### 3. Controller 확인 (서버 쪽)

`BoardController`에 게시글 하나만 가져오는 API가 있는지 꼭 확인하세요! 아마 아래와 비슷한 코드가 있어야 합니다.

```java
@GetMapping("/{id}")
public BoardResponse getBoard(@PathVariable Long id) {
    return boardService.findOne(id); // 단건 조회 로직
}
```

---

### 💡 깨알 팁: `white-space: pre-wrap;`
CSS에 넣은 이 속성이 꽤 중요합니다. 유저가 글을 쓸 때 줄바꿈(엔터)을 친 내용이 상세 페이지에서도 그대로 보이게 해주는 마법의 속성입니다. 이게 없으면 아무리 엔터를 쳐도 화면에는 한 줄로 길게 나오거든요.

이제 **목록 -> 상세 -> 목록**으로 이어지는 부드러운 흐름이 완성되었습니다! 한 번 테스트해보시겠어요? 상세 페이지에서 글이 잘 보인다면, 다음에는 글 **수정**이나 **삭제** 기능을 추가해보는 것도 재미있을 것 같네요!