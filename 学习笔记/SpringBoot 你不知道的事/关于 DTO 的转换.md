# 1. Alternatives
---
Given the following Data Object (DO) and Data Transfer Object (DTO), this article will demonstrate the various ways of performing the conversion between them.

```java
public class Student {
	private String name;
	private String email;
	private int age;
	private String matricNo;
	private double cgpa;
}

public record StudentDto(
	String name,
	String email,
	String matricNo
);
```

## 1.1. Setter & Converter
---
Using traditional setter is safe and the easiet to implement without any other dependencies and learning curves. For cleaner and reusable solution, define a Converter class for the centralized conversion logic management. However, it's verbose and requires manual effort for maintenance.

```java
public class StudentConverter {
	public StudentDto toDto(Student student) {
		if (student == null) {
			return null;
		}
		return new StudentDto(student.getName(), student.getEmail(), student.getMatricNo());
	}
	
	public Student toDo(StudentDto studentDto) {
		if (studentDto == null) {
			return null;
		}
		Student student = new Student();
		student.setName(studentDto.name());
		student.setEmail(studentDto.email());
		student.setMatricNo(studentDto.matricNo());
		return student;
	}
}
```

You might consider to use the chain methods for other possible syntax if you prefer them, for instances:

Using fluent setters
```java
public Student toDo(StudentDto studentDto) {
	if (studentDto == null) {
		return null;
	}
	return new Student()
				.setName(studentDto.name())
				.setEmail(studentDto.email())
				.setMatricNo(studentDto.matricNo());
}
```

Using builder pattern
```java
if (studentDto == null) {
		return null;
	}
	return new Student().builder()
				.name(studentDto.name())
				.email(studentDto.email())
				.matricNo(studentDto.matricNo())
				.build();
```

We can do this by annotating our `Student` class using either @Accessors or @Builder annotations from Lombok.

Let's dive into the details of these annotations.

### Lombok's `@Accessors`

The `@Accessors` annotation is used to configure how lombok generates and looks for getters, setters, and with-ers.

```java
@Accessors(
	/* boolean, default=false */
	fluent = false, 
	
	/* boolean, default=false unless fluent=true */
	chain = false,

	/* boolean */
	makeFinal = true
)
```

- `fluent`: If true, the getter for pepper is just `pepper()`, and the setter is `pepper(T new Value)`.
- `chain`: If true, generated setters will return `this` instead of `void`.
- `makeFinal`: If true, generated getters, setters, and with-ers are marked as `final`.

References: https://projectlombok.org/features/experimental/Accessors

### Lombok's `@Builder`

```java
@Builder(
	builderMethodName = "",
	access = AccessLevel.PACKAGE
)
```

Notes
- Each listed generated element will be silently skipped if that element already exists (disregarding parameter counts and looking only at names). This includes the _builder_ itself.

`@Builder.Default`

`@Singular`: `@Builder` can generate so-called 'singular' methods for collection parameters/fields. These take 1 element instead of an entire list, and add the element to the list. For example: 

```java
Person.builder()
	.job("Mythbusters")
	.job("Unchained Reaction")
	.build();
```

would result in the `List<String> jobs` field to have 2 strings in it. To get this behavior, the field/parameter needs to be annotated with `@Singular`.

Finally, applying `@Builder` to a class is as if you added `@AllArgsConstructor(access = AccessLevel.PACKAGE)` to the class and applied the `@Builder` annotation to this all-args-constructor. This only works if you haven't written any explicit constructors yourself or allowed lombok to create one such as with `@NoArgsConstructor`. If you do have an explicit constructor, put the `@Builder` annotation on the constructor instead of on the class. Note that if you put both `@Value` and `@Builder` on a class, the package-private constructor that `@Builder` wants to generate 'wins' and suppresses the constructor that `@Value` wants to make.


Builder.Default
`@Builder(builderMethodName = "")` is legal (and will suppress generation of the builder method) starting with lombok v1.18.8.

`@Builder(access = AccessLevel.PACKAGE)` is legal (and will generate the builder class, the builder method, etc with the indicated access level) starting with lombok v1.18.8.

@Builder(builderClassName = "HelloWorldBuilder", buildMethodName = "execute", builderMethodName = "helloWorld", toBuilder = true, access = AccessLevel.PRIVATE, setterPrefix = "set")

### Lombok's `@Builder` vs `@With`

`toBuilder`

### @Builder and @SuperBuilder


Reference: https://projectlombok.org/features/Builder

## 1.2. Reflection
---
### 1.2.1. `org.springframework.beans.BeanUtils`

### 1.2.2. Apache BeanUtils, Apache PropertyUtils

### 1.2.3. Method & Field

### 1.2.4. `VarHandle` & `MethodHandle`



## 1.3. Spring `Converter`/ `ConversionService`
---

## 1.4. Mapper
---
### 1.4.1. `ModelMapper`

### 1.4.2. `ObjectMapper`


## 1.6. MapStruct
---

## 1.7. Swagger
---

## 1.8. Spring Data JPA
---

### 1.8.1. Plugins
---


### 1.8.2. Repository-Level Projections
---
```java
public interface UserView { Long getId(); String getName(); String getEmail(); }

interface UserRepo extends JpaRepository<User, Long> {
    List<UserView> findByActiveTrue();
}
```


# Comparison
---