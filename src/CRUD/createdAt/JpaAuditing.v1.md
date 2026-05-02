게시판(Board) Entity에서 생성일자, 수정일자, 삭제일자는 데이터 관리와 이력 추적을 위해 필수적인 요소들입니다. Spring Data JPA를 사용하고 있다면 직접 필드를 관리하기보다 **JPA Auditing** 기능을 활용하는 것이 훨씬 효율적입니다.

### 1. 필드 구성 방식

보통 아래와 같은 필드명과 타입을 사용합니다.

* **생성일자 (`createdAt`)**: 데이터가 처음 insert 될 때 기록.
* **수정일자 (`updatedAt`)**: 데이터가 수정(update) 될 때마다 갱신.
* **삭제일자 (`deletedAt`)**: 실제 데이터를 DB에서 지우지 않고 '삭제됨' 표시만 하는 **Soft Delete(논리 삭제)**를 할 때 사용합니다.

### 2. 추천하는 구현 방법 (BaseEntity 활용)

모든 Entity마다 날짜 필드를 일일이 넣으면 코드가 중복됩니다. 공통 부모 클래스를 만들어 상속받는 구조가 깔끔합니다.



#### ① 공통 클래스 만들기
```java
@Getter
@MappedSuperclass // 테이블로 생성되지 않고, 상속받는 자식 클래스에게 매핑 정보만 제공
@EntityListeners(AuditingEntityListener.class) // JPA Auditing 활성화
public abstract class BaseTimeEntity {

    @CreatedDate // 생성 시 자동 저장
    @Column(updatable = false) // 수정 시에는 변경되지 않도록 설정
    private LocalDateTime createdAt;

    @LastModifiedDate // 수정 시 자동 저장
    private LocalDateTime updatedAt;

    // 삭제일자는 Soft Delete를 사용하는 경우에만 직접 추가
    private LocalDateTime deletedAt;
}
```

#### ② Board Entity에서 상속받기
```java
@Entity
public class Board extends BaseTimeEntity {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    
    // 이제 createdAt, updatedAt 필드를 따로 선언하지 않아도 자동으로 포함됩니다.
}
```

### 3. 필수 설정 (Main Class)
JPA Auditing 기능이 작동하려면 메인 클래스나 설정 클래스에 `@EnableJpaAuditing` 어노테이션을 반드시 추가해야 합니다.

```java
@EnableJpaAuditing
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

---

### 💡 몇 가지 팁

1.  **타입 선택**: 시간대 정보가 포함된 `LocalDateTime`을 사용하는 것이 표준입니다.
2.  **삭제일자(deletedAt) 처리**: 
    * 진짜로 `DELETE` 쿼리를 날려 데이터를 지울 거라면 `deletedAt` 필드는 필요 없습니다. 
    * 하지만 실무에서는 실수로 지운 데이터를 복구하기 위해 `deletedAt`에 값이 있으면 삭제된 것으로 간주하는 **Soft Delete** 방식을 많이 씁니다. 이때는 삭제 서비스 로직에서 `deletedAt = LocalDateTime.now()`를 호출해 주면 됩니다.
3.  **DB 반영**: 앞서 말씀드린 것처럼, 이미 서비스 중인 DB라면 `ALTER TABLE board ADD COLUMN created_at datetime;` 등의 SQL을 통해 컬럼을 먼저 생성해 주어야 코드와 싱크가 맞습니다.

이렇게 구성하면 매번 `board.setCreatedAt(now)` 같은 코드를 짤 필요 없이 JPA가 알아서 시간을 관리해 주어 매우 편리합니다. `bitcopark-base` 프로젝트의 게시판 구조에도 이 방식을 적용해 보세요!