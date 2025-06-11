# Condition
## IfCondition
```java
[if문]
·구성
1출력*1결과
1조건

1결과
1조건

1조건
1결과

1조건
1출력결과

1조건대상*조건결과
1출력결과

·형식
if(조건)
{결과}

if(조건){결과}


·조건*조건대상
조건=조건식
조건=조건대상*조건결과
조건=조건대상+조건결과
조건(if문)=조건대상(switch문)+조건결과
조건(if문*조건)=조건대상(switch문*조건대상)+조건결과
if문 <-> switch문


[if-else문]
·구성
2출력*2결과
1조건

·형식
if(조건)
{1결과}
else {2결과{

if(조건){1결과}
else {2결과}

if(조건){1결과}
else    {2결과}

·무결과
if(){}
else{}

if(){}

동일구문!!!
-코드줄임
-코드경제
-코드최적


[if-else if-else문]
·구성
3출력*3결과
2조건

·형식
if(1조건)
{1결과}
else if(2조건){2결과)
else{3결과}

if(1조건){1결과}
else if(2조건){2결과)
else{3결과}

if     (1조건){1결과}
else if(2조건){2결과)
else          {3결과}


if      (1조건) {1결과}
else if (2조건) {2결과)
else            {3결과}


[if-else if-else if-else문]
·구성
4출력*4결과
3조건


if      (1조건) {1결과}
else if (2조건) {2결과)
else if (3조건) {3결과)
else            {4결과}


[if-else if-else if-else if-else문]
5출력*5결과
4조건


if      (1조건) {1결과}
else if (2조건) {2결과)
else if (3조건) {3결과)
else if (4조건) {4결과)
else            {5결과}

...

[if-else if-else if-else if- ... -else문]
·구성
n출력*n결과
n-1조건

n+1출력*n+1결과
n조건

·형식
if      (1조건) {1결과}
else if (2조건) {2결과)
else if (3조건) {3결과)
else if (4조건) {4결과)
...
else if (n조건) {n결과)
else            {n+1결과}


if      (1조건) {1결과}
else if (2조건) {2결과)
else if (3조건) {3결과)
else if (4조건) {4결과)
...
else if (n-1조건) {n-1결과}
else              {n결과}


[요약]
1결과->if(){}
2결과->if(){} else{}
3결과->if(){} else if(){} else{}
4결과->if(){} else if(){} else if(){} else{}
5결과->if(){} else if(){} else if(){} else if(){} else{}
...
n결과->if(){} else if(){} else if(){} else if(){} ... else if(){} else{}
```

## SwitchCondition
```java
switch(조건대상) {
case    :     : break;
default : 
}


switch(조건대상) {
case 조건결과 : 출력결과 : break;
default       : 
}


switch(조건대상) {
case 조건결과 : 1출력결과 : break;
default       : 2출력결과
}


switch(조건대상) {
case 1조건결과 : 1출력결과 : break;
case 2조건결과 : 2출력결과 : break;
default        : 3출력결과
}


switch(조건대상) {
case 1조건결과 : 1출력결과 : break;
case 2조건결과 : 2출력결과 : break;
...
case n-1조건결과 : n-1출력결과 : break;
default        : n출력결과
}
```


```java
동일결론
동일결론*다조건

[if문]or조건*다조건 -> if( || )
[sw문]or조건*다조건 -> case 값1 : case 값2 : 

if( 조건1 || 조건2 ){}
switch( 조건대상 ) { case 조건결과1 : case 조건결과2 :       : break;
                    default                        :                }
                    
if<->switch
switch가 상당히 제한적인듯함.
조건대상이 공통된경우에 한해서활용가능.
더하여
```