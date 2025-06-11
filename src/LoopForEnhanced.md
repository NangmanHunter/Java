# EnhancedForLoop
Alias
- EFL
## ArrayEFL
```java
//<Array>
//[기본형]
int[] arr1=new int[3];
arr1[0]=1;
arr1[1]=2;
arr1[2]=3;

System.out.print("Array 기본형 : ");
for(int a : arr1) {
    System.out.print(a+" ");
}System.out.println();



//[참조형]
String[] arr2=new String[3];
arr2[0]="1";
arr2[1]="2";
arr2[2]="3";

System.out.print("Array 참조형 : ");
for(String a : arr2) {
    System.out.print(a+" ");
}System.out.println();
```

## ListEFL
```java
//<List>
//[기본형]
//List<int> lst1=new ArrayList<int>();
//Syntax error, insert "Dimensions" to complete ReferenceType
//선언x
//출력x


//[대체형]
List<Integer> lst1=new ArrayList<Integer>();
lst1.add(1);
lst1.add(2);
lst1.add(3);

System.out.print("List 참조형(기본형대체형) : " );
for(Integer l : lst1) {
    System.out.print(l+" ");
}System.out.println();


//[참조형]
List<String> lst2=new ArrayList<String>();
lst2.add("1");
lst2.add("2");
lst2.add("3");

System.out.print("List 참조형 : " );
for(String l : lst2) {
System.out.print(l+" ");
}System.out.println();
```

## SetEFL
```java
//<Set>
//[기본형]
//Set<int> set1 =new HashSet<int>();
//Syntax error, insert "Dimensions" to complete ReferenceType
//선언x
//출력x


//[대체형]
Set<Integer> set1 =new HashSet<Integer>();
set1.add(1);
set1.add(2);
set1.add(3);

System.out.print("Set 대체형 : ");
for(Integer s : set1) {
    System.out.print(s+" ");
}System.out.println();
     

//[참조형]
Set<String> set2 =new HashSet<String>();
set2.add("1");
set2.add("2");
set2.add("3");

System.out.print("Set 참조형 : ");
for(String s : set2) {
	System.out.print(s+" ");
}System.out.println();
```
