# ForLoop
- [EnhancedForLoop](#enhancedforloop)
- [ForLoop](#forloop)

## EnhancedForLoop
```java
int[] arr={1,2,3};

for(int a: arr){
  System.out.println(a);
}
```


## ForLoop
Optimize
- ✅0<
- ❌0≤
- ❌1<
- ❌1≤


0<
```java
int[] arr={1,2,3};

for(int i=0; i<arr.length; i++){
  System.out.println(arr[i]);
}
```

0≤
```java
int[] arr={1,2,3};

for(int i=0; i<=arr.length-1; i++){
  System.out.println(arr[i]);
}
```

1<
```java
int[] arr={1,2,3};

for(int i=1; i<arr.length; i++){
  System.out.println(arr[i-1]);
}
```

1≤
```java
int[] arr={1,2,3};

for(int i=1; i<=arr.length-1; i++){
  System.out.println(arr[i-1]);
}
```
