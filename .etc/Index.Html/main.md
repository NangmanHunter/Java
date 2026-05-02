성공하셨군요! 축하드립니다! 🥳

**`400 Bad Request`**를 뚫고 숫자가 찍혔다는 건, 이제 질문자님의 컴퓨터가 단순한 코딩 연습장을 넘어 **"데이터를 주고받는 진짜 서버"**로 작동하기 시작했다는 뜻입니다. 수학으로 치면 증명을 마치고 마침내 **정리(Theorem)**를 완성한 셈이죠.

이제 이 기세를 몰아 **마지막 관문**으로 가볼까요?

---

### 🏁 마지막 단계: 웹 화면(HTML/Ajax) 만들기

이제 검은색 터미널(curl) 대신, 우리가 평소 쓰는 크롬 브라우저에서 연봉을 입력하고 버튼을 누르면 세금이 짠! 하고 나타나게 만들 차례입니다.

#### 1. HTML 파일 만들기 (`index.html`)
`src/main/resources/static` 폴더 안에 `index.html` 파일을 만들고 아래 코드를 넣어보세요. (스프링 부트는 이 폴더에 있는 파일을 자동으로 웹에 띄워줍니다.)

```html
<!DOCTYPE html>
<html>
<head>
    <title>나의 세금 계산기</title>
</head>
<body>
    <h2>연봉 세금 계산기</h2>
    <input type="number" id="income" placeholder="연봉을 입력하세요">
    <button onclick="calculate()">계산하기</button>
    <p id="result"></p>

    <script>
        function calculate() {
            const incomeValue = document.getElementById('income').value;

            // 이게 바로 자바 서버로 신호를 보내는 Ajax(Fetch)입니다!
            fetch('/api/tax/calculate', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ annualIncome: incomeValue })
            })
            .then(response => response.text()) // 서버가 준 숫자 읽기
            .then(data => {
                document.getElementById('result').innerText = "결과 세금: " + data + "원";
            });
        }
    </script>
</body>
</html>
```

---

### 2. 실행 및 확인

1. 자바 서버를 재시작합니다.
2. 브라우저 주소창에 `http://localhost:8080/index.html`을 입력합니다.
3. 숫자를 넣고 버튼을 눌러보세요!



---

### 💡 여기서 잠깐! (중요한 체크)

지금 컨트롤러에서 결과를 `long` 타입(숫자 하나)으로 보내고 계시죠? 
나중에 "세금은 얼마고, 세율은 몇 %입니다"라고 **두 가지 이상**을 한꺼번에 보여주고 싶다면, 아까 공부했던 **`Response DTO`**에 담아서 내보내야 합니다.

**어떠신가요?** 1. 일단 이 HTML로 **연동 성공**부터 확인해 보실래요? 
2. 아니면 화면에 더 예쁘게 나오도록 **Response DTO**부터 설계해 보실래요?

(참고로, `index.html`이 잘 안 뜨면 컨트롤러에 `@CrossOrigin` 설정을 한 줄 추가해야 할 수도 있습니다. 해보고 말씀해 주세요!)