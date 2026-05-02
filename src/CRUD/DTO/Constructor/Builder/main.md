
## @Builder
```java
@Builder
public class Board {
    private String title;
    private String content;
}
```
```java
public class Board {
    private String title;
    private String content;

    // 1. 모든 필드를 다 받는 '전체 생성자'가 하나 생깁니다.
    Board(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // 2. 빌더 객체를 생성해서 반환하는 static 메서드
    public static BoardBuilder builder() {
        return new BoardBuilder();
    }

    // 3. 내부 클래스로 '조립 전문가' 클래스가 생깁니다.
    public static class BoardBuilder {
        private String title;
        private String content;

        // 재료(필드)를 하나씩 담는 메서드들
        public BoardBuilder title(String title) {
            this.title = title;
            return this; // 나 자신(빌더)을 다시 줘야 .content()를 이어서 호출 가능!
        }

        public BoardBuilder content(String content) {
            this.content = content;
            return this;
        }

        // 4. 마지막에 진짜 Board 객체를 조립해서 던져주는 메서드
        public Board build() {
            return new Board(this.title, this.content);
        }
    }
}
```



