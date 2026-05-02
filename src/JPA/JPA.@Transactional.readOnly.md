## Transactional.ReadOnly
- `@Transactional`<<`@Transactional(readOnly = true)`
- `@Transactional`<<`@Transactional(readOnly = true)`ㆍ`@Transactional`
- `@Transactional`(Class)<<`@Transactional(readOnly = true)`(Class)ㆍ`@Transactional`(Method)


##
JPA와 Spring Data JPA를 사용하시면서 `@Transactional(readOnly = true)` 옵션을 사용하는 건 아주 좋은 습관입니다! 단순히 "읽기 전용"이라는 표시 이상의 기술적인 이점들이 숨어있거든요.

일반 `@Transactional`과 어떤 점이 다른지 핵심 위주로 콕콕 집어드릴게요.

---

## 1. 더티 체킹(Dirty Checking) 생략 (성능 최적화)
가장 큰 차이점은 **플러시(Flush) 모드**의 변화입니다. 

* **일반 모드:** 트랜잭션이 끝날 때 JPA는 영속성 컨텍스트에 저장된 엔티티의 초기 상태(스냅샷)와 현재 상태를 일일이 비교합니다. 변경된 게 있다면 `UPDATE` 쿼리를 날리죠.
* **readOnly = true:** 스프링이 하이버네이트(Hibernate)의 플러시 모드를 `MANUAL`로 설정합니다. 즉, **"어차피 안 바꿀 거니까 변경 감지용 스냅샷도 찍지 말고, 비교도 하지 마!"**라고 명령하는 겁니다. 덕분에 메모리 사용량도 줄고 CPU 연산도 아낄 수 있습니다.



## 2. DB 부하 분산 (Master-Slave 구조)
서비스 규모가 커지면 데이터베이스를 **쓰기용(Master)**과 **읽기용(Slave/Replica)**으로 분리하곤 합니다.

* 이때 `readOnly = true`가 설정되어 있으면, 스프링이 이를 감지하여 **읽기 전용 DB(Slave)로 커넥션**을 보낼 수 있도록 설정할 수 있습니다. 
* 쓰기 트래픽과 읽기 트래픽을 물리적으로 분리해주는 결정적인 기준이 됩니다.

## 3. 데이터 일관성 (DB 엔진 레벨)
일부 DB(MySQL 등)에서는 읽기 전용 트랜잭션에 대해 **스냅샷 격리 수준**을 최적화하여 제공합니다. 
* 트랜잭션이 시작된 시점의 데이터를 일관되게 보여주면서도, 다른 트랜잭션의 쓰기 작업과 충돌을 최소화해 동시 처리량을 높여줍니다.

---

## 요약 비교표

| 구분 | `@Transactional` | `@Transactional(readOnly = true)` |
| :--- | :--- | :--- |
| **변경 감지** | 작동함 (스냅샷 비교 발생) | **작동 안 함 (성능 향상)** |
| **Flush** | 커밋 시점에 자동 Flush | **자동 Flush 발생 안 함** |
| **DB 부하** | Master DB 집중 | Slave DB 분산 가능 |
| **의도 노출** | 읽기/쓰기 모두 가능함을 암시 | **이 메서드는 조회 전용임**을 명시 |

### 💡 꿀팁
실무에서는 **클래스 상단**에 기본적으로 `@Transactional(readOnly = true)`를 붙여두고, 데이터를 저장하거나 수정해야 하는 메서드에만 따로 `@Transactional`을 붙여서 사용하는 방식을 많이 권장합니다. 실수로 데이터를 변경하는 코드를 넣어도 반영되지 않으니 훨씬 안전하겠죠?

혹시 지금 프로젝트에서 별도의 읽기 전용 DB(Slave)를 따로 구축해서 사용하고 계신가요?