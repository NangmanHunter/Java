## Record
```java
public record BoardCreateResponse(long boardId, String message) { }
```
```java
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public final class BoardCreateResponse {
    private final long boardId;
    private final String message;
}
```
```java
public final class BoardCreateResponse extends java.lang.Record {
    // 1. 모든 필드는 private final (불변)
    private final long boardId;
    private final String message;

    // 2. 전체 필드를 초기화하는 생성자 (Canonical Constructor)
    public BoardCreateResponse(long boardId, String message) {
        this.boardId = boardId;
        this.message = message;
    }

    // 3. Getter 메서드 (get이라는 접두사 없이 필드명과 동일하게 생성됨)
    public long boardId() { return this.boardId; }
    public String message() { return this.message; }

    // 4. equals, hashCode, toString 자동 구현
    @Override
    public boolean equals(Object o) { ... }
    @Override
    public int hashCode() { ... }
    @Override
    public String toString() { ... }
}
```


##
- 레코드를 안 쓸 이유가 없다 
- 추상화를 너무 잘해놔서 이거 안 쓰는 게 오히려 더 멍청하다 
- 너무 잘 만들어 놓고 너무 좋게 만들어놔서 DTO로서 바로쓰자 