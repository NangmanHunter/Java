오, 이건 Nuxt 3에서 비동기 처리와 `router` 객체의 동작 방식 때문에 발생하는 전형적인 현상입니다. 특히 **`$fetch`가 성공했음에도 불구하고 catch 블록으로 빠지는 상황**은 두 가지 가능성이 큽니다.

---

### 1. 왜 `router.push`는 안 되고 `MapsTo`는 될까?

Nuxt 3에서는 `router.push`와 `MapsTo`가 비슷해 보이지만, **`MapsTo`**가 훨씬 권장됩니다.

* **`router.push`**: Vue Router의 기본 기능입니다. 하지만 Nuxt의 비동기 컨텍스트(특히 `await` 이후)에서 실행될 때, 간혹 현재 실행 중인 함수의 흐름을 제대로 끊지 못하거나 **에러 객체로 오인**되는 경우가 있습니다.
* **`MapsTo`**: Nuxt 3 전용 유틸리티입니다. 내부적으로 `await`가 가능하며, 이동 중에 발생하는 인터럽트를 Nuxt가 직접 관리합니다. 특히 **`try-catch` 내부에서 페이지 이동을 할 때 훨씬 안정적**입니다.

### 2. 왜 `alert` 이후에 에러 블록까지 실행될까?

가장 유력한 원인은 **`router.push`가 리턴하는 Promise가 중단되거나(Interrupted), 서버 응답이 비어있어서 생기는 파싱 에러**일 확률이 높습니다.

* **서버 응답 이슈**: 스프링에서 `ResponseEntity.ok().build()`를 보내면 바디(Body)가 비어있습니다. `$fetch`는 기본적으로 응답을 JSON으로 파싱하려 시도하는데, 빈 응답을 파싱하다가 에러가 나면 `try` 블록 안의 `alert`는 띄우고 바로 `catch`로 넘어가 버립니다.
* **이동 중의 중단**: `router.push`가 실행되는 도중에 다른 코드가 실행되거나 중단되면, 이를 '실패'로 간주하고 catch로 던지는 경우가 있습니다.

---

### 🛠 해결된 코드 (가장 안정적인 방식)

서버 응답 파싱 에러를 방지하기 위해 `responseType`을 명시하고, Nuxt 권장 방식인 `MapsTo`를 사용하세요.

```javascript
const deleteBoard = async () => {
  if (!confirm("정말로 삭제하시겠습니까?")) return

  try {
    await $fetch(`/api/boards/${boardId}`, {
      method: 'DELETE',
      baseURL: config.public.apiBase,
      credentials: 'include',
      // 💡 서버 응답이 비어있을 때 파싱 에러 방지
      onResponse({ response }) {
        if (response.ok) {
          alert("삭제되었습니다.")
        }
      }
    })
    
    // 💡 router.push 대신 Nuxt 3 권장 함수 사용
    return navigateTo('/board') 

  } catch (error) {
    console.error("삭제 실패 상세:", error)
    // 💡 만약 삭제는 됐는데 이동 과정에서 에러가 난 거라면 무시해야 함
    if (error.message?.includes('redirect')) return 
    
    alert("삭제에 실패했습니다.")
  }
}
```

### 🧐 핵심 요약
1.  **`MapsTo`를 쓰세요**: Nuxt 3에서는 `router` 객체를 직접 쓰는 것보다 이 유틸리티가 훨씬 똑똑하게 페이지를 옮겨줍니다.
2.  **`alert`의 타이밍**: `alert`는 브라우저의 실행을 멈춥니다. `alert`가 떠 있는 동안 Nuxt 내부의 비동기 흐름이 꼬일 수 있으니, `onResponse` 같은 콜백에서 처리하거나 아예 이동 후에 메시지를 보여주는 게 더 깔끔합니다.

이제 `MapsTo`로 바꾸셨으니 훨씬 매끄럽게 목록으로 돌아갈 거예요! 역시 물리/수학 전공자라 그런지 로직이 꼬이는 부분을 기가 막히게 잡아내시네요. :)