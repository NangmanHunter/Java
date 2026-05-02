


##
- [DataBase.H2](https://www.h2database.com/html/main.html)


##
- http://localhost:8080/h2-console
  - 이걸로들어가야함.
  - 단순히 console키면은 8082 기본포트가 달라져버림.
- Saved Settings: Generic H2 (Sever)
  - 이걸로들어가야함.


- ❌외부연결
  - 8082
  - 애당초 포트번호가 8082 이것부터 시작됨.
  - 여기서 뭐더없음. 불능임.


##
- http://localhost:8080/h2-console


- 루프백만됨
  - ✅`http://localhost:8080/h2-console`
  - ❌`http://172.30.1.44:8080/h2-console`
  - 자체보안때문에 막아놨다함.


##
- H2를 쓰실 때 `jpa.hibernate.ddl-auto: create` 옵션을 쓰면, **Entity 클래스만 잘 만들어두면 스프링이 알아서 MariaDB나 H2에 맞는 테이블을 생성**해 줍니다. 
- Member 부분을 맡으셨으니, 이제 이 설정이 끝난 상태에서 **`Member` 엔티티(클래스)**를 작성하시면 바로 DB에 테이블이 생기는 걸 확인하실 수 있을 거예요.


##
맞아요. H2 데이터베이스는 메모리에서 실행되는 아주 가벼운 DB라 테스트용으로 제격이죠. 이미 Gradle에 의존성(Library)을 추가하셨다면, `src/main/resources/application.yml`에 다음과 같이 설정해 주시면 됩니다.


---

## 1. application.yml 설정

H2는 두 가지 모드가 있는데, 보통 개발 중에는 **메모리 모드(In-memory)**를 사용합니다. 애플리케이션을 끄면 데이터가 날아가지만, 테스트하기엔 가장 깔끔하죠.

```yaml
spring:
  datasource:
    # 메모리 모드 주소 설정 (testdb라는 이름의 DB 생성)
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
    username: sa
    password:
    
  h2:
    console:
      enabled: true      # 웹에서 DB를 확인할 수 있는 콘솔 활성화
      path: /h2-console  # 콘솔 접속 주소 (http://localhost:8080/h2-console)

  jpa:
    hibernate:
      ddl-auto: create   # 앱 시작 시 테이블을 자동으로 생성 (테스트용)
    show-sql: true       # 콘솔에 실행되는 SQL 출력
    properties:
      hibernate:
        format_sql: true # SQL을 예쁘게 포맷팅해서 출력
        dialect: org.hibernate.dialect.H2Dialect # H2 전용 SQL 문법 사용
```

---

## 2. 제대로 설정됐는지 확인하는 법 (H2 Console)

설정을 마친 후 애플리케이션을 실행하고, 브라우저에서 아래 주소로 접속해 보세요.

> **주소:** `http://localhost:8080/h2-console`

1.  **JDBC URL:** `jdbc:h2:mem:testdb` (yml에 적은 것과 똑같이 입력해야 합니다.)
2.  **User Name:** `sa`
3.  **Password:** (비워둠)
4.  **Connect** 버튼 클릭!



---

## 3. Member 담당으로서의 팁

H2를 쓰실 때 `jpa.hibernate.ddl-auto: create` 옵션을 쓰면, **Entity 클래스만 잘 만들어두면 스프링이 알아서 MariaDB나 H2에 맞는 테이블을 생성**해 줍니다. 

Member 부분을 맡으셨으니, 이제 이 설정이 끝난 상태에서 **`Member` 엔티티(클래스)**를 작성하시면 바로 DB에 테이블이 생기는 걸 확인하실 수 있을 거예요.

혹시 엔티티 클래스 작성하는 코드도 필요하신가요? 아니면 Gradle에 추가한 의존성이 정확한지 한 번 봐드릴까요?