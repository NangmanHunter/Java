각 방식의 구체적인 문법과 사용법을 정리해 드릴게요. 자바가 얼마나 '파이썬'스러워졌는지 확인해 보세요!

---

### 1. JShell (즉시 실행 모드)
터미널에서 바로 코딩하는 방식입니다. 클래스나 메서드 감싸기 없이 **문장(Statement)**만 던지면 됩니다.

* **특징:** 세미콜론(`;`) 생략 가능, 변수 즉시 할당.
* **실행 예시:**
    ```bash
    $ jshell
    |  Welcome to JShell -- Version 21
    jshell> int a = 10
    a ==> 10
    jshell> int b = 20
    b ==> 20
    jshell> a + b
    $3 ==> 30
    ```

### 2. 단일 소스 파일 실행 (Java 11+)
파일 하나에 코드를 짜고 컴파일 없이 바로 실행하는 방식입니다.

* **파일명:** `Hello.java` (클래스 이름과 달라도 상관없음)
* **코드 문법:** ```java
    public class Hello {
        public static void main(String[] args) {
            System.out.println("Hello, Scripting!");
        }
    }
    ```
* **실행:** `java Hello.java` (javac 과정 생략)

### 3. 유연해진 Main 메서드 (Java 21+ Preview)
가장 파격적인 변화입니다. 입문자나 스크립트 용도를 위해 **이름 없는 클래스(Unnamed Class)**와 **간소화된 Main**을 지원합니다.

* **코드 문법 (`Simple.java`):**
    ```java
    // 클래스 선언 필요 없음!
    // public static void main(String[] args) 대신:
    void main() {
        println("이건 자바인가 스크립트인가?"); 
    }
    ```
    > **참고:** `println`은 `System.out.println`을 생략할 수 있게 해주는 static import가 내부적으로 적용된 상태입니다. (JDK 21 미리보기 기능 활성화 필요)

### 4. JBang (라이브러리 포함 스크립트)
가장 강력한 스크립트 도구입니다. 코드 상단에 주석으로 필요한 라이브러리를 선언합니다.

* **코드 문법 (`ApiTest.java`):**
    ```java
    ///usr/bin/env jbang "$0" "$@" ; exit $?
    //DEPS com.fasterxml.jackson.core:jackson-databind:2.15.2

    import com.fasterxml.jackson.databind.ObjectMapper;

    public class ApiTest {
        public static void main(String[] args) {
            // Jackson 라이브러리를 즉시 사용 가능!
            ObjectMapper mapper = new ObjectMapper();
            System.out.println("JSON 라이브러리 로드 완료");
        }
    }
    ```
* **실행:** `jbang ApiTest.java` 또는 `./ApiTest.java` (실행 권한 부여 시)

---

### 한눈에 보는 문법 비교

| 구분 | 일반 Java (Legacy) | 최신 Java 스크립트 스타일 |
| :--- | :--- | :--- |
| **필수 요소** | 클래스, `static main`, `args` | `void main()` 또는 그냥 식(JShell) |
| **출력** | `System.out.println()` | `println()` (v21+) |
| **컴파일** | `javac` 후 `java` | `java 파일명.java` (즉시 실행) |
| **외부 라이브러리** | Maven/Gradle 설정 필수 | `//DEPS` 주석 (JBang 사용 시) |



가장 먼저 테스트해 보고 싶은 방식이 있으신가요? JDK 21의 **Unnamed Main Method** 기능을 직접 실행해볼 수 있는 커맨드를 알려드릴까요?