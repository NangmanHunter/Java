## ㅇㅇApplication
- `ㅇㅇApplication`
  - `Application`
  - `ㅇㅇApplication`
  - `ㅇㅇApplication`.`@SpringBootApplication`
  - `CccApplication`.`@SpringBootApplication`
  - `CccApplication.java`.`@SpringBootApplication`
  - `A/B/Ccc/CccApplication.java`.`@SpringBootApplication`
  - `A/B/C/CApplication.java`.`@SpringBootApplication`
  - `Aaa/Bbb/Ccc/CccApplication.java`.`@SpringBootApplication`
  - Main메소드 넣는부분.
  - 관례적으로잡힌듯. 이것진행ㄱㄱ.
- ```java
  @SpringBootApplication
  public class ㅇㅇApplication {
      public static void main(String[] args) {

      }
  }
  ```






## 모으기어려움
- 스프링은 한마디로 모을수가없구나...
- 기능별로 모으기에는 너무도 강력한형태...;;
- 모을수가없음.
- 싹다충돌남.

- 별도서버
  - 결국 별도서버로 잡아다 처리해주는게 더맞을듯.
  - 1서버ㄴㄴ. 필연적으로 겹칠수밖에없음.
  - 재원서버로 포워딩하든 리버스프