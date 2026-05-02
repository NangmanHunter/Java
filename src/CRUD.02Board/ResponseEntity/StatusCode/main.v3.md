네, 정확한 지적입니다! 컨트롤러에서 **DTO(혹은 일반 객체)를 직접 반환하면 기본적으로 스프링이 `200 OK`를 응답**하도록 설정되어 있습니다. 

하지만 `ResponseEntity`를 쓰지 않고도 `201`이나 `204`를 보낼 수 있는 방법이 아예 없는 건 아닙니다. 다만, 조금 '딱딱한' 방식이 됩니다.

---

## 1. DTO를 반환하면서 상태 코드를 바꾸는 방법
`@ResponseStatus` 어노테이션을 사용하면 DTO만 던지면서도 상태 코드를 지정할 수 있습니다.

```java
@PostMapping
@ResponseStatus(HttpStatus.CREATED) // 무조건 201 Created로 응답
public UserDto createUser(@RequestBody UserDto userDto) {
    return userService.save(userDto);
}
```

### 이 방식의 치명적인 단점 (유연성 부족)
위처럼 어노테이션을 붙여버리면, 그 메서드는 **어떤 상황에서도 무조건 201만** 내뱉게 됩니다. 
* "성공하면 201, 이미 데이터가 있으면 200" 같은 **조건별 분기**가 불가능합니다.
* 비즈니스 로직 결과에 따라 응답을 동적으로 바꿀 수 없기 때문에 실무에서는 결국 `ResponseEntity`로 넘어가게 됩니다.

---

## 2. 왜 ResponseEntity가 "유연함"의 대명사인가?
`ResponseEntity`는 객체이기 때문에 코드 안에서 `if-else` 문으로 상태 코드를 마음껏 주무를 수 있습니다.



```java
@PostMapping("/save")
public ResponseEntity<UserDto> save(@RequestBody UserDto dto) {
    if (service.isAlreadyExist(dto)) {
        return ResponseEntity.status(HttpStatus.OK).body(dto); // 이미 있으면 200
    }
    UserDto saved = service.save(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(saved); // 새로 만들었으면 201
}
```

---

## 3. 요약 및 비교

| 반환 방식 | 상태 코드 제어 | 특징 |
| :--- | :--- | :--- |
| **DTO만 반환** | **기본 200** | 가장 간결하지만 상태 제어가 안 됨. |
| **DTO + @ResponseStatus** | **고정된 코드** | 코드는 깔끔하지만, 상황에 따른 동적 응답 불가. |
| **ResponseEntity** | **동적 제어** | 코드 안에서 200, 201, 400 등을 자유롭게 결정 가능. |

---

### 결론
"DTO만 던져도 201, 204를 보낼 수는 있지만, **프로그램의 상황(로직)에 따라 유연하게 코드를 바꾸고 싶다면 `ResponseEntity`가 정답**"이라고 보시면 됩니다.

`204 No Content` 같은 경우는 보통 돌려줄 DTO 자체가 없기 때문에 `ResponseEntity.noContent().build()`를 쓰는 게 가장 깔끔한 표준이기도 하고요. 

지금 만들고 계신 API 중에서 성공했을 때 단순히 200 말고 다른 코드를 주고 싶은 특정 케이스가 있으신가요?