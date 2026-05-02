좋습니다! 파일명을 직관적인 `board-detail.html`로 변경하고, 이에 맞춰 목록 페이지의 연결 링크까지 깔끔하게 정리해 드릴게요.

### 1. 게시글 상세 페이지 (`src/main/resources/static/board-detail.html`)

이 파일은 URL 뒤에 붙는 `?id=번호`를 해석해서 해당 게시글의 상세 내용을 화면에 그려줍니다.

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
        /* white-space: pre-wrap; 은 줄바꿈을 그대로 보여줍니다 */
        .content { font-size: 1.1rem; line-height: 1.6; color: #333; min-height: 250px; white-space: pre-wrap; padding: 10px 0; }
        .footer { margin-top: 30px; border-top: 1px solid #eee; padding-top: 20px; }
        .btn { padding: 10px 20px; border-radius: 6px; cursor: pointer; border: none; font-weight: bold; text-decoration: none; display: inline-block; }
        .btn-list { background-color: #e4e6eb; color: #050505; }
        .btn-list:hover { background-color: #d8dadf; }
    </style>
</head>
<body>

<div class="container">
    <div class="header">
        <h1 class="title" id="title">데이터를 불러오는 중...</h1>
        <div class="info" id="info">작성자: - | 번호: -</div>
    </div>

    <div class="content" id="content">
        내용을 로딩하고 있습니다. 잠시만 기다려 주세요.
    </div>

    <div class="footer">
        <a href="/board.html" class="btn btn-list">목록으로 돌아가기</a>
    </div>
</div>

<script>
    window.onload = function() {
        // 브라우저 주소창에서 id 파라미터 추출
        const urlParams = new URLSearchParams(window.location.search);
        const boardId = urlParams.get('id');

        if (!boardId) {
            alert("잘못된 접근입니다.");
            window.location.href = '/board.html';
            return;
        }

        loadDetail(boardId);
    };

    async function loadDetail(id) {
        try {
            const response = await fetch(`/api/boards/${id}`);
            
            if (!response.ok) {
                throw new Error("게시글을 찾을 수 없습니다.");
            }

            const board = await response.json();

            // 데이터 바인딩
            document.getElementById('title').innerText = board.title;
            document.getElementById('info').innerText = `작성자: ${board.writerName} | 글 번호: ${board.id}`;
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

### 2. 목록 페이지 연결 수정 (`board.html` 수정)

이제 `board.html` 파일 내 자바스크립트에서 링크 주소를 새 파일명인 **`board-detail.html`**로 맞춰줘야 합니다. `fetchBoards` 함수 내부의 `forEach` 부분을 아래처럼 업데이트해 주세요.

```javascript
// board.html 파일의 스크립트 수정 부분
boards.forEach(board => {
    const item = `
        <div class="board-item">
            <div class="board-title">
                <a href="/board-detail.html?id=${board.id}" style="text-decoration:none; color:inherit; cursor:pointer;">
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

### 3. 확인 사항

1.  **파일명**: `src/main/resources/static/board-detail.html`로 저장하셨나요?
2.  **API 경로**: 서버(Controller)에 `@GetMapping("/{id}")` API가 잘 준비되어 있는지 확인해 보세요.
3.  **데이터 전달**: `detail` 페이지에서 `board.writerName` 등을 사용하고 있으니, 서버가 보내주는 JSON 데이터에 해당 필드명이 포함되어 있어야 합니다.

이제 목록에서 제목을 클릭하면 `board-detail.html?id=1` 같은 주소로 이동하면서 상세 내용을 띄워줄 거예요. 혹시 클릭했는데 "로딩 중..."에서 멈춰 있다면, `F12`를 눌러 **네트워크(Network)** 탭에서 404나 500 에러가 나고 있지 않은지 확인해 보세요!