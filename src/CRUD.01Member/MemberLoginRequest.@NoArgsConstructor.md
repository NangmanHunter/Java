## @NoArgsConstructor
@NoArgsConstructor (기본 생성자): 스프링이 포스트맨에서 보낸 JSON 데이터를 자바 객체로 바꿀 때(역직렬화), 내부적으로 이 기본 생성자를 먼저 호출해서 객체를 만듭니다. 없으면 에러가 날 수 있으니 꼭 넣어주세요.