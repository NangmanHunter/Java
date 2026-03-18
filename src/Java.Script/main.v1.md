

다행히 최신 자바 버전들(특히 Java 21 이상)은 스크립트처럼 가볍게 쓸 수 있는 기능들을 대거 도입했습니다. "자바는 무겁다"는 편견을 깨줄 몇 가지 방법을 소개해 드릴게요.

---

### 1. JShell: 터미널에서 즉시 실행
클래스 선언, `public static void main` 같은 복잡한 형식을 다 무시하고 그냥 코드만 넣어서 확인해볼 수 있는 **REPL(Read-Eval-Print Loop)** 도구입니다.

* **실행 방법:** 터미널에 `jshell` 입력
* **특징:** 변수를 선언하면 즉시 메모리에 저장되고, `;`(세미콜론)을 생략해도 알아서 실행해 줍니다. 간단한 알고리즘 테스트나 API 확인용으로 최고예요.


### 2. 소스 코드 바로 실행 (Single-File Source Code)
Java 11부터 도입된 기능으로, `.java` 파일을 컴파일(`javac`)하지 않고 바로 실행(`java`)할 수 있습니다. 

```bash
# 예전 방식: javac Main.java -> java Main
# 현재 방식:
java Main.java
```
파일 안에 `public class`가 없어도 실행 가능해져서, 정말 스크립트 파일 하나 쓰듯이 관리할 수 있습니다.

### 3. Java 21+ 의 'Unnamed Main Methods'
최신 자바에서는 입문자와 스크립트 사용자를 위해 코드를 극단적으로 줄여주는 미리보기 기능을 제공합니다.

* **과거:** ```java
    public class HelloWorld {
        public static void main(String[] args) {
            System.out.println("Hello, World!");
        }
    }
    ```
* **현재 (미리보기):**
    ```java
    void main() {
        println("Hello, World!");
    }
    ```
클래스 선언도 필요 없고, `System.out`을 매번 칠 필요도 없습니다. 훨씬 스크립트답죠?

### 4. 외부 라이브러리 활용 (JBang)
자바 진영에서 스크립팅을 위해 가장 추천하는 도구는 **[JBang](https://www.jbang.dev/)**입니다.

* **강점:** Maven이나 Gradle 설정 없이 코드 상단에 주석으로 의존성(`//DEPS ...`)을 적어주면 알아서 라이브러리를 내려받고 실행해 줍니다.
* **예시:** `jbang hello.java` 한 줄이면 외부 라이브러리를 사용하는 복잡한 자바 코드도 스크립트처럼 돌아갑니다.

---



### 요약하자면
* **단순 테스트:** `jshell`
* **파일 하나로 관리:** `java MyScript.java`
* **라이브러리까지 필요하다면:** `JBang` 사용

혹시 지금 바로 테스트해 보고 싶은 특정 기능이나 코드가 있으신가요? 그걸 스크립트 스타일로 변환해 드릴 수도 있습니다!