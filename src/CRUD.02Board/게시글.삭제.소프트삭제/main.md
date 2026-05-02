## SoftDelete
```java
// Service.CommentService.java
commentRepository.delete(comment);

// Entity.Comment.java
@SQLDelete(sql = "UPDATE comment SET is_deleted = true, deleted_at = CURRENT_TIMESTAMP WHERE comment_id = ?")
```
```java
// Service.CommentService.java
comment.delete();

// Entity.Comment.java
public void delete() {
        this.deleted = true;
}
```