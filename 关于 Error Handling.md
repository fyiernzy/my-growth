---
updated: 2025-07-23T23:55:21.240+08:00
edited_seconds: 8
---
```prompt
Read through my notes, analyze my note/experience regarding making the checking in Java, especially the SpringBoot context. Suggest any other ways that can be used to perform such checking. Show me the widely adopted ways that is practiced in large company such as Alibaba, Tencent, ByteDance, GAYMAN, etc and compare their pros and cons. Also, critique my current implementation to point out the weaknesses and suggest improvement based on your critique.
```

目前做 Checking 的方法有很多，设计思路也有很多，比如：

最古老的方法就是 if then throw，如下：
```java
if(obj == null) {
	throw IllegalArgumentException();
}
```

这个方法是肯定没问题的，只是 if-then-else 通常需要写三行 code，如果要检查很多的话会显得很乱，这些 trivial 的 defensive programming 反而会成为 noise。所以明智的写法是将其缩短成一行：

```java
Assertions.notNull(obj, new IllegalArgumentException());
```

那这个写法最大的问题就是他会先 initialize Exception，不管最后有没有 throw，这会造成资源浪费（因为 initialize 也会需要 memory）==（ #问题 他会浪费多少 memory？如果我有七个checking，他会先 initialize 7 个 exception？）==，所以比较好的办法就是用 supplier，他会等到真的需要的时候才 initialize

```java
Assertions.notNull(obj, () -> new IllegalArgumentException());
```

当然在这个情况下我们也可以用 `Optional`，如下：

```java
Optional.ofNullable(obj)
.orElseThrow(() -> new IllegalArgumentException());
```

但我觉得这个写法不好有两个原因：
1. Initialize Optional 是需要额外开销的，远不如调用 Assertions 的 static method 来得直接；
2. Optional 的语义本来就不是拿来做检测；他是为了进行一系列的map/filter 操作的。对我来说，他的真正作用是 return as something and do some other stuff where there's a null value -> default value, throw error.
3. Optional 能够检测的场景太少，只能 check null

综合所有，我会觉得 Assertions 的写法是很不错的。

Assertions 的写法：

```java
@Contract("null, _ -> fail")
public static void isTrue(boolean condition, Supplier<RuntimeException> exception) {
	if(!condition) {
		throw exception.get();
	}
}
```

这里只有 isTrue 是需要定义的，其他的诸如 notBlank, notEmpty, isFalse 都是用回 isTrue 而已，因此省略。选用了 Assertions 的写法之后，我们可以用一些 Helper/ Anti-Corruptions layers，那就是 Validators 和 ExceptionMsg，如下：

```java
Validators.isBlank(string);
```

Validators 的作用就是用来 hide underlying packages & standardize api call & anti-corruption layer 的，
- anti-corruption layer：假如有一天 Apache Common Lang 移除了（或者 Deprecated）了某个方法，如果没有这层 Validators，那我们会 refactor 到脑壳爆炸；
- hide underlying packages：要 check 这个要用 packageA，要 check 那个要用 packageB，要 check 那个又需要 packageC，这样的话我们要记得很多 package 的用法和写法。有了这层 layer 我们只需要记得 Validators 而已，用  dot 和 autocompletion 就很方便。
- standardize api call：看到 A 用 pkgA check empty, B 用 pkgB, C 用 != null && .isBlank，那就很乱，读Code 的人之后的人也会不知道要怎么写。所以统一写法可以让人心情愉悦。

ExceptionMsg 目前我看到的是大部分的 checking 其实都是
- invalid argument
- entity not found

这些他们都有相对固定的写法，如果一直重复的话就很麻烦，也需要很沉重的认知负担；用了 ExceptionMsg，比如

```java
public static String entityNotFound(String entity, String criteria, Object value) {
	return String.format("Entity %s not found with criteria %s=%s", entity, criteria, value);
}
```

这样用的人就可以直接
 Exception.entityNotFound(entity, criteria, value)，好用又好记得，还可以减少 verbosity，何乐而不为？如果要更 bdd，可以这样，当然这就有点罗嗦了，但是更加 BDD。
 
```java
public static String entityNotFound(EntityLabel entity, CriteriaLabel criteria, ObjectLabel object) {
	return String.format("Entity %s not found with criteria %s=%s", entity.val, criteria.val, value.val);
}

public static EntityLabel entity(String entity) {
	return new EntityLabel(entity);
}

public static CriteriaLabel criteria(String criteria) {
	return new CriteriaLabel(criteria);
}

public static ObjectLabel object(Object object) {
	return new ObjectLabel(object);
}

record EntityLabel(String val){
}
record CriteriaLabel(String val){}
record ObjectLabel(Object val)

ExceptionMsg.entityNotFound(entity("student"), criteria("id"), object(id));
```

随着我们对接的人越来越专业，我们会 return error code instead of exception type，所以下一层的处理方式会是定义 ErrorCode + GenericException + GlobalExceptionHandler + ErrorResponse

```java
@RequiredArgsConstructor
public enum ErrorCode {
	NOT_FOUND("0001");
	private final String code;
}

@Getter
public class GenericException extends RuntimeException {
	private final ErrorCode errorCode;
	private final String message;
	
	public GenericException(ErrorCode erroCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}
}

@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(GenericException.class)
	public ErrorResponse handleGenericException(GenericException ex) {
		return ErrorResponse.builder(ex, 
			ProblemDetail.forStatusAndDetail(HttpStatusCode.INTERNAL_SERVER_ERROR, ex.getMessage());
		)
	}
}
```

## 返回值 Return Value
---
除了上述提到的 ResponseEntity，其实 SpringBoot 还有 ErrorResponse 和 ProblemDetails
- ErrorResponse
- ResponseEntity
- ProbleDetails

## Result Pattern vs Try Catch
---
那还有没有什么高阶玩法呢？当然有！


Result Pattern vs Try Catch


