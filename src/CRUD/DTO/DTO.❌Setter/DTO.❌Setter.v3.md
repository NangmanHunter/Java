

## 4. Jackson과 함께 쓸 때의 꿀조합

앞서 질문하신 "Jackson 사용 시 Setter 없이 쓰는 법"과 연결해 보면, DTO를 보통 이렇게 만듭니다.

```java
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Jackson을 위한 기본 생성자
@AllArgsConstructor // 데이터 주입을 위한 전체 생성자
public class UserDto {
    private String name;
    private int age;
}
```

이렇게 하면:
1.  **Jackson**은 `@NoArgsConstructor`로 객체를 생성한 뒤 리플렉션으로 값을 채웁니다.
2.  **개발자**인 당신은 `@AllArgsConstructor`를 통해 데이터를 한 번에 넣어 객체를 생성할 수 있습니다.
3.  **Setter**가 없으니 객체가 생성된 이후에 값이 변할 걱정 없이 안전하게 데이터를 전달할 수 있습니다!


Jackson = @NoArgsConstructor + Reflection
