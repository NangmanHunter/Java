## Entity.DTO변환


##
Library.ModelMapper>>MapStruct
Library.ModelMapper(컴파일)>>MapStruct(런타임)


##
그 번거로움, 모든 개발자가 똑같이 느낍니다! 필드가 20개만 되어도 `get`, `set` 하다가 하루가 다 가죠. 엔티티를 DTO로 자동 변환하는 방법은 크게 두 가지가 있습니다.

현업에서 가장 많이 쓰는 **`ModelMapper`**와 **`MapStruct`**를 소개해 드릴게요.

---

## 1. MapStruct (가장 추천: 컴파일 시점 변환)
요즘 가장 핫한 라이브러리입니다. 인터페이스만 만들면 **컴파일 할 때 구현 클래스를 자동으로 생성**해줍니다. 성능이 매우 빠르고 타입 안정성이 높습니다.

### 설정 방법
**`build.gradle`**에 추가:
```gradle
dependencies {
    implementation 'org.mapstruct:mapstruct:1.5.5.Final'
    annotationProcessor 'org.mapstruct:mapstruct-processor:1.5.5.Final'
}
```

### 매퍼 인터페이스 생성
```java
@Mapper(componentModel = "spring") // 스프링 빈으로 등록
public interface BoardMapper {
    // 소스(Board)의 getMember().getName()을 타겟(BoardResponse)의 writerName에 매핑
    @Mapping(source = "member.name", target = "writerName")
    BoardResponse toResponse(Board board);
    
    List<BoardResponse> toResponseList(List<Board> boards);
}
```

### 컨트롤러에서 사용
```java
@RestController
@RequiredArgsConstructor
public class BoardController {
    private final BoardMapper boardMapper; // 주입받기
    private final BoardService boardService;

    @GetMapping
    public List<BoardResponse> list() {
        List<Board> boards = boardService.findBoards();
        return boardMapper.toResponseList(boards); // 한 줄로 끝!
    }
}
```

---

## 2. ModelMapper (런타임 시점 변환)
설정이 가장 간단합니다. 하지만 실행 시점에 리플렉션을 사용하므로 MapStruct보다는 미세하게 느리고, 매핑 오류를 실행해봐야 알 수 있다는 단점이 있습니다.

### 설정 방법
**`build.gradle`**: `implementation 'org.modelmapper:modelmapper:3.1.1'`

### 컨트롤러에서 사용
```java
@GetMapping
public List<BoardResponse> list() {
    List<Board> boards = boardService.findBoards();
    return boards.stream()
            .map(board -> modelMapper.map(board, BoardResponse.class))
            .collect(Collectors.toList());
}
```
* 단, `writerName`처럼 이름이 다른 필드는 추가 설정(`PropertyMap`)이 필요할 수 있습니다.

---

## 3. 라이브러리 없이 하는 '생성자 방식' (꿀팁)
라이브러리 추가가 부담스럽다면, **DTO 안에 생성자**를 만들어두는 방식도 많이 씁니다. 컨트롤러 코드가 훨씬 깨끗해집니다.

**BoardResponse.java**
```java
@Getter
public class BoardResponse {
    private Long id;
    private String title;
    private String writerName;

    // Entity를 인자로 받는 생성자 추가
    public BoardResponse(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.writerName = board.getMember().getName();
        // ... 나머지 필드들
    }
}
```

**Controller.java**
```java
@GetMapping
public List<BoardResponse> list() {
    return boardService.findBoards().stream()
            .map(BoardResponse::new) // 생성자 참조로 깔끔하게 변환
            .collect(Collectors.toList());
}
```

---

### 💡 나의 선택 가이드
1.  **"성능이 중요하고 필드가 계속 늘어날 것 같다"** → **MapStruct** (강력 추천)
2.  **"설정 귀찮고 아주 작은 프로젝트다"** → **ModelMapper**
3.  **"외부 라이브러리 쓰는 게 찜찜하다"** → **DTO 내부 생성자 방식**

개발자님 상황에서는 **MapStruct**를 한 번 적용해 보시는 걸 추천드려요. 처음에만 조금 생소하지, 한 번 맛들리면 수동 매핑은 절대 못 하실 거예요! 어떤 방식이 가장 끌리시나요?