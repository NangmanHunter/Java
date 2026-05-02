JPA에서 순환 참조가 발생하는 가장 전형적인 상황인 **'게시글(Post) - 댓글(Comment)'** 관계를 코드로 보여드릴게요. 이 코드를 그대로 실행하면 JSON 변환 시 서버가 멈추게 됩니다.

---

## 1. 순환 참조가 발생하는 위험한 코드

### 게시글 엔티티 (Post)
```java
@Entity
public class Post {
    @Id @GeneratedValue
    private Long id;
    private String title;

    // Post는 Comment 리스트를 가집니다.
    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();
}
```

### 댓글 엔티티 (Comment)
```java
@Entity
public class Comment {
    @Id @GeneratedValue
    private Long id;
    private String content;

    // Comment는 다시 Post를 참조합니다. -> 여기서 순환 발생!
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
}
```

### 무한 루프의 흐름
컨트롤러에서 `return post;`를 하는 순간 다음과 같은 연쇄 반응이 일어납니다.

1.  `Post` 객체를 JSON으로 바꾼다 → `{"id": 1, "title": "안녕", "comments": [...]}`
2.  `comments` 리스트 안의 `Comment`를 바꾼다 → `{"id": 10, "content": "반가워", "post": {...}}`
3.  `Comment` 안의 `post`를 다시 바꾼다 → `{"id": 1, "title": "안녕", "comments": [...]}`
4.  **1번으로 돌아가 무한 반복...** 결국 `StackOverflowError` 발생.



---

## 2. 해결책: DTO로 고리 끊기

이 문제를 해결하는 가장 깔끔한 방법은 **순환 고리가 끊어진 별도의 클래스(DTO)**를 만드는 것입니다.

### 응답용 DTO 클래스
```java
// 게시글 응답용 DTO
public class PostResponseDto {
    private Long id;
    private String title;
    private List<CommentResponseDto> comments; // 엔티티가 아닌 DTO 리스트를 가짐
}

// 댓글 응답용 DTO
public class CommentResponseDto {
    private Long id;
    private String content;
    // 여기서 Post 정보를 포함하지 않음으로써 고리를 끊음!
}
```

### 서비스 계층에서의 변환
```java
@Transactional(readOnly = true)
public PostResponseDto getPost(Long id) {
    Post post = postRepository.findById(id).orElseThrow();

    // 엔티티를 DTO로 변환 (직접 변환하거나 ModelMapper, MapStruct 사용)
    return new PostResponseDto(post); 
}
```

---

## 3. 왜 이렇게 해야 하나요?

수학에서 집합 $A$의 원소로 다시 집합 $A$를 정의하면 무한 집합이 되어버리는 것과 비슷합니다. 

* **엔티티:** 데이터베이스의 관계를 표현하기 위해 서로 연결되어 있을 수밖에 없습니다.
* **DTO:** "내가 화면에 보여주고 싶은 데이터"만 딱 잘라서 정의한 스냅샷입니다.

이렇게 DTO를 사용하면 **순환 참조 해결**뿐만 아니라, 앞서 고민하셨던 **의도치 않은 DB 수정(Side Effect)**까지 한 번에 방어할 수 있습니다. 

지금 만들고 계신 프로젝트에서도 엔티티 구조가 서로를 참조하고 있다면, 지금 바로 **ResponseDto**를 도입해 보시는 것을 추천합니다. 코드가 훨씬 견고해질 거예요!