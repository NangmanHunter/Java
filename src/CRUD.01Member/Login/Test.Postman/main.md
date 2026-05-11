## 
- Post
- Body
- `x-www-form-urlencoded`
  - ✅`form-data`
  - ✅`x-www-form-urlencoded`
  - ❌`raw`
  - `.formLogin`이라서 form형태만 잡나봄.
  - 그냥보내면 튕겨져나옴.


##
왜 raw(JSON)는 안 되고 form-data만 될까?
- 스프링 시큐리티의 기본 필터인 UsernamePasswordAuthenticationFilter는 내부적으로 HttpServletRequest.getParameter("username") 방식을 사용합니다.
- form-data / x-www-form-urlencoded: 이 방식은 데이터가 key=value 형태로 전달되므로 getParameter()로 바로 읽을 수 있습니다.

- raw (JSON): 데이터가 HTTP Body에 통째로 들어있기 때문에 getParameter()로는 읽을 수 없고, 별도의 Object Mapper(Jackson 등)를 써서 직접 읽어줘야 합니다.