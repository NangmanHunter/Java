# Operator
- [ArithMeticOperator](#arithmeticoperator)
- [IncrementDecrementOperator](#incrementdecrementoperator)
- [LoGicalOperator](#logicaloperator)
- [TerNaryOperator](#ternaryoperator)
- [ComParisonOperator](#comparisonoperator)
## ArithMeticOperator
```java
+ : 더하기
- : 빼기
* : 곱
/ : 몫
% : 나머지(mod)
```


## IncrementDecrementOperator
```java
//[증가연산자]
++a
a++ 

//[감소연산자]
--a
a--



//[전위연산자]
int a=10;
int b=a++;
System.out.println("b=" + b);
System.out.println("a=" + a);
//출력결과
b=11
a=11

//[후위연산자]
int a=10;
int b=a++;
System.out.println("b="+ b);
System.out.println("a="+ a);
//출력결과
b=10
a=11


//[대입연산자]
//[할당연산자]
a=10

//[복합대입연산자]
a+=10
a-=10
a*=10
a/=10
a%=10
```


## LoGicalOperator
```java
[논리연산]
&&
||

[비교연산]
==
!=
```

charㆍString
```java
<char>
[연산자]
==  <- (o)

[ex.]
**Java
char ch='안';
ch='안';
boolean bool1= (ch=='안');
System.out.println(bool1);

**Console창
true



<String>
[연산자]
==         <- (x)
.equlas( ) <- (o)

[ex.]
**Java
String str="안녕 !";
str.equals("안녕 !");
boolean bool2=str.equals("안녕 !");
System.out.println(bool1);

**Console창
true
```


## TerNaryOperator
```java
[단항연산] 
부정연산
!

[이항연산]
일반적연산

[삼항연산]
 ? :
```

## ComParisonOperator
```java
int num=1;

//[비교연산]
num==1;

//[할당연산]
num=1;

//[양자구별]
//비교연산자  <-> 할당연산자
//    ==     <->      =
//  num==1   <->   num=1
//num이 1인지 <-> num에 1대입
```

