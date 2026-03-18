/* 
Java 11+ 방식
- Bash
```bash
java Check.java 안녕하세요
```
```java
if (args.length > 0) {
    System.out.println("입력받은 인자: " + args[0]);
}
```


```
import java.nio.file.*;
import java.time.LocalDateTime;

...

// 현재 시간과 작업 디렉토리 출력 (자바 문법 그대로!)
System.out.println("현재 시간: " + LocalDateTime.now());
System.out.println("현재 경로: " + Paths.get(".").toAbsolutePath());
```
*/
/*
```java
public void method(int a){
    System.out.println(a);
}
public static void main(String[] args) {

    method(1);
}
```
- ❌ 에러의 원인: "Static vs Instance"
- main 메서드는 static이 붙어 있는 정적 메서드입니다. 반면, 작성하신 method는 static이 없는 인스턴스 메서드입니다.
- 자바에서 static 메서드는 프로그램이 시작되자마자 메모리에 바로 올라가지만, static이 없는 메서드는 객체(Instance)를 생성해야만 비로소 메모리에 존재하게 됩니다.
- 즉, main 입장에서는 "나는 이미 메모리에 있는데, method 너는 아직 태어나지도 않았잖아! (객체가 없잖아!)"라고 화를 내는 상황인 거죠.


✅ 해결 방법 1: static 붙여주기 (가장 간단)
method 앞에도 static을 붙여서 main과 같은 '정적' 레벨로 만들어줍니다.
```
public class Test {
    // static을 추가해서 main에서 바로 부를 수 있게 함
    public static void method(int a) {
        System.out.println(a);
    }

    public static void main(String[] args) {
        method(1); // 이제 잘 돌아갑니다!
    }
}
```


✅ 해결 방법 2: 객체를 생성해서 부르기 (정석)
Test라는 클래스를 실제로 '실체(객체)'로 만든 다음에 그 실체를 통해 메서드를 호출합니다.
```
public class Test {
    public void method(int a) {
        System.out.println(a);
    }

    public static void main(String[] args) {
        Test t = new Test(); // 1. 객체를 먼저 만든다
        t.method(1);        // 2. 만든 객체의 메서드를 부른다
    }
}
```


```
Test t = new Test();
t.method(1);
```
```
new Test().method(1);
```
*/


public class Test {

    public void method(int a){
        System.out.println(a);
    }
    public static void main(String[] args) {
        new Test().method(1);
    }
}