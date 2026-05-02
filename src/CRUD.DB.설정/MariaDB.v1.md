## H2
- 일단 H2로 싹다하고.
- 그다음 MariaDB로 그때하나만 톡떨어내서 싹연결ㄱㄱ.
  - 아주부드럽게
  - 아주모듈적으로ㄱㄱ.



##
H2(메모리 DB)에서 실제 운영이나 개발 서버에서 사용하는 **MariaDB**로 전환하려면, `application.yml`의 설정값들을 MariaDB의 규격에 맞게 바꿔주어야 합니다.

먼저 **`build.gradle`** 파일에 MariaDB 커넥터 라이브러리가 추가되어 있는지 확인한 후, 아래와 같이 설정을 수정해 보세요.

---

### 1. application.yml 수정 가이드

기존 H2 관련 설정들을 MariaDB 정보로 교체합니다.

```yaml
spring:
  datasource:
    # MariaDB 주소 설정 (localhost는 본인 컴퓨터, 3306은 기본 포트, sample_db는 생성한 DB 이름)
    url: jdbc:mariadb://localhost:3306/sample_db
    driver-class-name: org.mariadb.jdbc.Driver
    username: root        # MariaDB 설치 시 설정한 ID (보통 root)
    password: password123 # MariaDB 설치 시 설정한 비밀번호

  jpa:
    hibernate:
      # create: 매번 테이블 삭제 후 생성 (위험)
      # update: 변경된 부분만 반영 (개발 시 권장)
      # none: 아무것도 안함 (운영 시 권장)
      ddl-auto: update 
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        # MariaDB 전용 방언(Dialect) 설정
        dialect: org.hibernate.dialect.MariaDBDialect
```

---

### 2. 주요 변경 사항 상세 설명

| 항목 | 변경 내용 | 이유 |
| :--- | :--- | :--- |
| **`url`** | `jdbc:mariadb://IP주소:포트/DB명` | 메모리가 아닌 실제 네트워크 주소를 통해 DB에 접속해야 합니다. |
| **`driver-class-name`** | `org.mariadb.jdbc.Driver` | MariaDB와 대화하기 위한 전용 드라이버를 사용합니다. |
| **`ddl-auto`** | `create` → **`update`** | MariaDB는 데이터가 계속 유지되므로, 매번 지우는 `create`보다는 바뀐 부분만 수정하는 `update`가 안전합니다. |
| **`dialect`** | `MariaDBDialect` | Hibernate가 MariaDB의 전용 SQL 문법(방언)을 쓰도록 알려줍니다. |

---

### 3. 꼭 체크해야 할 사항

1.  **Dependency 추가 (`build.gradle`):**
    MariaDB를 쓰려면 프로젝트에 드라이버 라이브러리가 있어야 합니다. 없으면 에러가 나니 꼭 확인하세요.
    ```gradle
    dependencies {
        runtimeOnly 'org.mariadb.jdbc:mariadb-java-client'
    }
    ```

2.  **데이터베이스 미리 생성:**
    H2는 메모리에 알아서 `testdb`를 만들어줬지만, MariaDB는 설치된 DB 서버에 접속해서 **`CREATE DATABASE sample_db;`** 명령어로 데이터베이스를 미리 만들어두어야 접속이 가능합니다.

3.  **H2 콘솔 비활성화:**
    MariaDB로 바꾸면 `spring.h2.console` 설정은 더 이상 동작하지 않습니다. 대신 **DBeaver**나 **HeidiSQL** 같은 전문 DB 툴을 사용해 데이터를 확인하는 것을 추천드려요.



지금 만들고 계신 주식 데이터 수집기나 세금 계산기를 나중에 실제로 배포하시려면, 데이터가 휘발되지 않는 이런 MariaDB 설정이 필수입니다. DB를 미리 설치하고 생성하셨나요? 혹시 설치 과정에서 막히는 부분이 있다면 알려주세요!