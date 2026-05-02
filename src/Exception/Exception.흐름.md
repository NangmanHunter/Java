##
- Exception.error.500


##
Spring Boot와 같은 프레임워크에서 예외(Exception)가 발생했을 때의 흐름은 마치 **"역방향 추적"**과 같습니다. 서비스에서 던진 예외가 어떻게 컨트롤러를 거쳐 클라이언트까지 도달하는지 단계별로 설명해 드릴게요.

---

## 1. 예외 흐름의 기본 구조

일반적인 호출 방향이 `Controller -> Service -> Repository`라면, 예외가 발생했을 때 던져지는(throw) 방향은 그 반대입니다.

1.  **Service**: 로직 수행 중 문제가 생기면 `throw new RuntimeException("에러 발생");`과 같이 예외를 던집니다.
2.  **Controller**: 호출했던 서비스 메서드에서 예외가 발생하면, 컨트롤러는 그 지점에서 실행을 멈추고 예외 객체를 상위(서블릿 컨테이너)로 다시 던집니다.
3.  **Exception Handler**: 별도의 처리가 없다면 스프링의 기본 전략(`BasicErrorController`)이 작동하고, 직접 설정했다면 `@RestControllerAdvice`가 가로챕니다.

---

## 2. 컨트롤러가 받는 것

서비스에서 예외를 던지면, 컨트롤러는 해당 메서드의 나머지 코드를 실행하지 않습니다.

*   **따로 try-catch를 안 했을 때**: 컨트롤러는 서비스가 던진 예외 객체를 그대로 받아서 자기 자신을 호출한 **서블릿 컨테이너(Tomcat 등)** 쪽으로 넘겨버립니다.
*   **전달되는 내용**: 예외 타입(예: `NullPointerException`), 에러 메시지, 그리고 에러가 발생한 지점의 정보(Stack Trace) 등이 담긴 객체입니다.

---

## 3. 화면(JSON)에 출력되는 내용

스프링 부트의 기본 설정 상태에서 예외를 처리하지 않고 클라이언트에 전달하면, 다음과 같은 형태의 **Standard Error Response**를 보게 됩니다.

### 기본 JSON 응답 (White Label Error Page)
```json
{
  "timestamp": "2026-04-30T16:50:59.000+00:00",
  "status": 500,
  "error": "Internal Server Error",
  "path": "/api/board/save"
}
```
*   **상태 코드**: 특별히 지정하지 않으면 서버 내부 오류인 **500**번이 나갑니다.
*   **내용**: 보안상 실제 어떤 에러가 났는지는 구체적으로 보여주지 않는 것이 기본 전략입니다.

---

## 4. 실무에서 사용하는 방식: @RestControllerAdvice

보통 실무에서는 컨트롤러가 예외를 직접 처리하게 두지 않고, **공통 예외 처리기**를 만들어 사용합니다.



1.  **서비스**에서 커스텀 예외(예: `BoardNotFoundException`)를 던집니다.
2.  **GlobalExceptionHandler**(`@RestControllerAdvice`)가 이 예외를 가로챕니다.
3.  개발자가 정의한 깔끔한 JSON 포맷으로 변환하여 클라이언트에게 응답합니다.

### 예시 응답 포맷
```json
{
  "code": "BOARD_001",
  "message": "해당 게시글을 찾을 수 없습니다.",
  "detail": "Requested ID: 10"
}
```

### 요약하자면
*   **흐름**: Service(Throw) → Controller(Pass) → GlobalHandler(Catch/Response).
*   **컨트롤러의 역할**: 직접 잡지 않으면 그냥 "에러 났대요!" 하고 윗선으로 보고하는 전달자 역할을 합니다.
*   **결과**: 최종적으로는 JSON 형태의 에러 객체가 HTTP 상태 코드와 함께 클라이언트(브라우저나 앱)에 전달됩니다.