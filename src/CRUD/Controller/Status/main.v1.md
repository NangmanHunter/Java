## 일관성
- 일관성>>가독성
- 일관성>>가독성-`()`.있다없다 x2 .x2x2 ... 


## 
```java
// 200
return ResponseEntity.status(HttpStatus.OK).body(data);

// 201
return ResponseEntity.status(HttpStatus.CREATED).body(data);

// 204
return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
```
```java
// 200
return ResponseEntity.ok(data);

// 201
return ResponseEntity.status(HttpStatus.CREATED).body(data);

// 204
return ResponseEntity.noContent().build();
```


## 200
```java
return ResponseEntity.ok(data);
return ResponseEntity.status(HttpStatus.OK).body(data);
return ResponseEntity.status(200).body(data);
```


## 201
```java
return ResponseEntity.status(HttpStatus.CREATED).body(data);
return ResponseEntity.status(201).body(data);
```


## 204
```java
return ResponseEntity.noContent().build();
return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
return ResponseEntity.status(204).body(null);
```

