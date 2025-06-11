# Print
- [Print](#print)
- [PrintLn](#println)
- [PrintF](#printf)
- [정렬형식](#%EC%A0%95%EB%A0%AC%ED%98%95%EC%8B%9D)
- [정렬방법](#%EC%A0%95%EB%A0%AC%EB%B0%A9%EB%B2%95)
## Print
```java
System.out.print("1");
System.out.print("2");
System.out.print("3");

// [출력결과]
// 123

// 줄바꿈x
```


## PrintLn
```java
System.out.println("1");
System.out.println("2");
System.out.println("3");

// [출력결과]
// 1
// 2
// 3

// 줄바꿈o
```


## PrintF
```java
int num1=1;
int num2=2;
		
System.out.println(num1 + " + " + num2 + " = " + (num1+num2));
System.out.printf("%d + %d = %d", num1, num2, num1+num2);

// [출력결과]
// 1 + 2 = 3
// 1 + 2 = 3
	
// [사용목적]
// 코드축약

// ------------------------------------------------------------
// println=줄바꿈o
// printf =줄바꿈x

//System.out.println();
//System.out.printf(" /n" , );
// "/n" -> 줄바꿈 !!!

System.out.printf("%d + %d = %d", num1, num2, num1+num2);
System.out.printf("%d + %d = %d", num1, num2, num1+num2);
//[출력결과]
//1 + 2 = 31 + 2 = 3

System.out.printf("%d + %d = %d\n", num1, num2, num1+num2);
System.out.printf("%d + %d = %d", num1, num2, num1+num2);
//[출력결과]
//1 + 2 = 3
//1 + 2 = 3
```


## 정렬형식
```java
/*
[%형]
%d : 정수형
%o : 8진수
%x : 16진수

%c : 문자
%s : 문자열

%f : 실수(소수점 6자리)
%e : 지수형태
%g : 대입값그대로
%b : 논리형


*/
		int num=16;
		System.out.printf("%d \n",num);
		System.out.printf("%o \n",num);
		System.out.printf("%x \n",num);
		// [출력결과]
		// 16
		// 20
		// 10
		
		char ch='A';
		System.out.printf("%c \n", ch);
		// [출력결과]	
		// A
		
		String st="문자열";
		System.out.printf("%s \n", st);
		// [출력결과]		
		// 문자열
		
		float fl=16f;
		System.out.printf("%f \n", fl);
		// [출력결과]
		// 16.000000
		
		double pi=3.14E1;
		System.out.printf("%f \n", pi);
		System.out.printf("%e \n", pi);
		// [출력결과]
		// 31.400000 
		// 3.140000e+01
```
```java
/*

[\형]
\n : 줄바꿈
\t : tab띄어쓰기
\\ : 역슬래시
\' : 작은따옴표
\" : 큰따옴표
\u : 유니코드

*/

		System.out.println("\\"); // 백슬래시 출력방법
		System.out.println("a \t b \t c \t d"); // tab띄우기
		System.out.println(" \" "); //쌍따옴표 단순문자출력
		System.out.println(" \' "); //홑따옴표 단순문자출력
		System.out.println(" ' ");  //홑따옴표 단순문자출력(되긴함)
		System.out.println("\u0041"); // 유니코드(16진수)번호로출력
```


## 정렬방법
```java
System.out.print();   <- (x)
System.out.printf();  <- (o)
System.out.println(); <- (x)
```