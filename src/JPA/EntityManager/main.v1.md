JPA(Jakarta Persistence)가 지원하는 함수는 크게 **표준 함수(JPQL)**와 **하이버네이트(Hibernate) 같은 구현체가 추가로 제공하는 함수**로 나뉩니다.

공식 문서와 주요 함수 리스트를 확인하는 방법을 정리해 드릴게요.

---

### 1. JPA(JPQL) 표준 함수 공식 문서
JPA의 표준 명세(Jakarta Persistence Specification)에서 정의하는 함수들입니다. 모든 JPA 구현체에서 공통으로 사용할 수 있습니다.

* **공식 명세서:** [Jakarta Persistence Spec - Query Language](https://jakarta.ee/specifications/persistence/3.1/jakarta-persistence-spec-3.1#a4663)
* **주요 함수 카테고리:**
    * **문자열:** `CONCAT`, `SUBSTRING`, `TRIM`, `LOWER`, `UPPER`, `LENGTH`, `LOCATE`
    * **산술:** `ABS`, `SQRT`, `MOD`, `SIZE` (컬렉션 크기), `INDEX` (List 순서)
    * **날짜/시간:** `CURRENT_DATE`, `CURRENT_TIME`, `CURRENT_TIMESTAMP`
    * **기타:** `COALESCE` (null 처리), `NULLIF`, `CASE` 문

---

### 2. 하이버네이트(Hibernate) 확장 함수
대부분의 프로젝트에서 사용하는 하이버네이트는 표준보다 훨씬 많은 함수를 지원합니다. 특히 **Hibernate 6.0**부터는 표준 SQL 함수를 거의 다 지원하도록 대폭 확장되었습니다.

* **공식 문서:** [Hibernate User Guide - Query Language Functions](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#hql-functions)
* **특징:** `CAST`, `EXTRACT`, `YEAR`, `MONTH`, `DAY` 등 데이터베이스에 의존적인 함수들을 JPQL(HQL) 내에서 자바 함수처럼 쓸 수 있게 해줍니다.

---

### 3. 한눈에 보기 편한 요약 테이블
학습용으로 빠르게 참고하고 싶다면 아래 사이트들이 정리가 잘 되어 있습니다.

| 출처 | 특징 | 링크 |
| :--- | :--- | :--- |
| **Baeldung** | 예제 코드와 함께 설명되어 있어 실무 적용이 쉬움 | [Link](https://www.baeldung.com/jpa-jpql-aggregate-functions) |
| **Wikibooks** | 표 형태로 함수 목록과 사용법이 깔끔하게 정리됨 | [Link](https://en.wikibooks.org/wiki/Java_Persistence/JPQL#Functions) |
| **ObjectDB** | JPQL의 모든 문법을 사전식으로 나열함 | [Link](https://www.objectdb.com/java/jpa/query/jpql/function) |

---

### 💡 꿀팁: 문서에도 없는 DB 전용 함수를 쓰고 싶다면?
만약 표준이나 구현체 문서에 없는 DB 전용 함수(예: MySQL의 `GROUP_CONCAT` 등)를 써야 한다면, JPA 2.1부터 지원하는 `function()` 기능을 사용하세요.

```sql
-- 사용 예시 (JPQL)
SELECT function('group_concat', e.name) FROM Employee e
```

혹은 QueryDSL을 사용 중이라면 `Expressions.stringTemplate()` 등을 활용해 SQL 함수를 직접 호출할 수도 있습니다.

더 궁금한 특정 함수군(예: 날짜 처리, 통계 함수 등)이 있으신가요?