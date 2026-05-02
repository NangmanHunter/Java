
## 소문자URL
- [ ] ConverterFactory
  - 모든Enum
- [x] 전역설정
- [ ] Enum
  - Enum.StaticFactroyMethod
- [ ] PathVariable String
  - String으로받아 UpperCase로 변환해서 진행



##
네, 당연히 가능합니다! 스프링 부트에서 이 문제를 해결하는 가장 세련된 방법은 **`Converter`**를 등록하는 것입니다.

지금 400 Bad Request가 났던 이유는 스프링이 URL의 `"notice"`(String)를 `BoardType.NOTICE`(Enum)로 변환하려고 할 때, 대소문자가 다르면 "다른 글자"라고 판단하기 때문입니다. 이걸 중간에서 가로채서 **"다 대문자로 바꿔서 매핑해!"**라고 설정해 주면 됩니다.

---

### 1. 전역 컨버터(Converter) 설정 (강력 추천)

이 설정을 추가하면 프로젝트 내의 **모든 Enum**에 대해 URL에서 소문자로 보내도 대문자로 자동 변환됩니다. `config` 패키지에 클래스를 하나 만드세요.

```java
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        // String -> BoardType 변환 시 대문자로 변환해주는 컨버터 등록
        registry.addConverter(new Converter<String, BoardType>() {
            @Override
            public BoardType convert(String source) {
                return BoardType.valueOf(source.toUpperCase());
            }
        });
    }
}
```

이렇게 하면 프론트에서 `/api/boards/notice`로 보내도 백엔드 컨트롤러의 `@PathVariable BoardType boardType`에 `NOTICE` 값이 성공적으로 들어옵니다.

---

### 2. 특정 Enum에만 적용하고 싶을 때 (`@JsonCreator`)

만약 전역 설정이 부담스럽고 `BoardType`에만 적용하고 싶다면, Enum 클래스 안에 이 코드를 넣으시면 됩니다.

```java
public enum BoardType {
    NOTICE, COMMUNITY;

    @com.fasterxml.jackson.annotation.JsonCreator
    public static BoardType from(String value) {
        return BoardType.valueOf(value.toUpperCase());
    }
}
```
> **참고:** `@JsonCreator`는 주로 `@RequestBody`로 받을 때 작동하지만, 일부 환경에서는 경로 변수 처리 시에도 유용합니다. 하지만 URL 경로 변수에는 **방법 1(Converter)**이 훨씬 확실한 해결책입니다.

---

### 3. 왜 이 방식이 좋을까요?

* **RESTful 관례 준수:** URL은 `/api/boards/notice`처럼 소문자로 유지할 수 있습니다.
* **자바 표준 준수:** 코드에서는 `NOTICE`처럼 대문자 Enum 상수를 그대로 사용할 수 있습니다.
* **프론트엔드 편의성:** 프론트엔드 개발자(본인!)가 `.toUpperCase()`를 일일이 신경 쓰지 않아도 됩니다.

---

### 💡 팁: 더 완벽한 컨버터
만약 프로젝트에 Enum이 많아질 것 같다면, 모든 Enum에 공통으로 적용되는 `ConverterFactory`를 만드는 게 좋습니다. 하지만 지금은 `bitcopark-base`를 빠르게 돌려보는 게 중요하니, **방법 1**을 적용해서 `notice` 소문자 URL이 잘 작동하는지 먼저 확인해 보세요!



이제 프론트엔드에서 URL을 다시 소문자로 바꾸고 테스트해 보시겠어요? 201 Created가 뜨면 성공입니다!