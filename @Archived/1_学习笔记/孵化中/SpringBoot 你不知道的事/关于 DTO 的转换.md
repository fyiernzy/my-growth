Given the following Data Object (DO) and Data Transfer Object (DTO), this article demonstrates different ways to convert between them:

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
) {

};
```

## 1. Manual Mapping with Converter
---
This is the most basic and dependency-free approach. It gives you full control and is easy to understand, but requires more boilerplate and manual maintenance.

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

You may prefer alternative syntax such as chained methods, for example:

Optional: Fluent Setters
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

Optional: Builder Pattern
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

This can be done by annotating the `Student` class with either `@Accessors` or `@Builder` from Lombok.

### 1.1. Lombok's `@Accessors`

The `@Accessors` annotation customizes how Lombok generates getters/setters/withers.

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

### 1.2. Lombok's `@Builder`

Lombok’s `@Builder` generates a builder class and fluent API to construct objects.

```java
// Use Case 1
@Builder(
	builderClassName = "HelloWorldBuilder",
	buildMethodName = "form", // Discourage
	builderMethodName = "init", // Discourage
	toBuilder = true,
	access = AccessLevel.PRIVATE
	setterPrefix = "set" // Discourage
)
public record Student(String name, String email);

Student.init() // Return Student.HelloWorldBuilder
	.setName("Student-Name")
	.setEmail("Student-Email")
	.form();
```

Using custom names like `init` or `form` is supported but not commonly recommended due to clarity and convention.

```java
@Builder(
	builderMethodName = "", // Discourage
)
public record Student(String name, String email);

new StudentBuilder()
		.name("Student-Name")
		.email("Student-Email")
		.build();
```

If `builderMethodName` is set to an empty string, Lombok won’t generate the typical `builder()` method. You’ll need to instantiate the builder manually (`new StudentBuilder()`).


Notes:
- For Lombok's `@Builder` to work:
	- If no constructor is defined, put `@AllArgsConstructor` together with `@Builder` on the class.
	- If constructors are explicitly defined, apply `@Builder` to the target constructor instead of the class.
- Lombok will silently skip elements (including the builder itself) if the method with the same name already exists, regardless of parameter counts.

#### 1.2.1. `@Builder.Default`

Used to provide a default value to a field or it will always get 0/ `null`/ `false`.

```java
@Builder
@AllArgsConstructor
public class Student {
	@Builder.Default
	private String matricNo = "UM001";
	private String name;
	private String email;
}

Student student = Student.builder()
					.name("Student-Name")
					.email("Student-Email")
					.build();

// Student{matricNo="UM001", name="Student-Name", email="Student-Email"}
System.out.println(student);
```

- Calling Lombok-generated constructors such as `@NoArgsConstructor` will also make use of the defaults specified using `@Builder.Default`.
- However, explicit constructors will no longer use the default values, unless it is set manually or calling on Lombok-generated  constructor such as `this();`

#### 1.2.2. `@Singular`

Annotating a `Collection<?>` field with `@Singular` instructs Lombok to generate two "adder" methods and a clear method for the field:
- One method adds a single element
- The other adds all elements from another collection
- No setter is generated to override the list

```java
@Builder
@AllArgsConstructor
public class Student {
	@Singular
	@Builder.Default
	private List<Result> results = List.of();
	private String name;
	private String email;
}

// .getResults() will return Result("math") and Result("add-math")
Student.builder()
	.result(new Result("math"))
	.results(List.of(new Result("add-math")))
	.build();
```

Notes:
- The resulting collection is immutable.
- Calling adder/clear methods on the builder after `build()` has no effect.
- It is recommended to use `@Singular` with collections under the `java.util` package.
- Full list of supported types: https://projectlombok.org/features/Builder#singular

```java
@Singular(
	value = ""
	ignoreNullCollections = false,
)
```
- `value`: Specifies the singular form of the field. Lombok will try to infer this by removing `s`/`es`. If unsuccessful or ambiguous, it will generate an error and require explicit specification.
- `ignoreNullCollections`: By default, Lombok does **not** allow `null` and will throw a `NullPointerException`. You can configure it to ignore `null` if needed.

Reference: https://javadoc.io/doc/org.projectlombok/lombok/latest/lombok/Singular.html

### 1.3. Lombok's `@Builder` vs `@With`

`toBuilder`, `@Builder.ObtainVia
`
If using `@Builder` to generate builders to produce instances of your own class (this is always the case unless adding `@Builder` to a method that doesn't return your own type), you can use `@Builder(toBuilder = true)` to also generate an instance method in your class called `toBuilder()`; it creates a new builder that starts out with all the values of this instance.

You can put the `@Builder.ObtainVia` annotation on the parameters (in case of a constructor or method) or fields (in case of `@Builder` on a type) to indicate alternative means by which the value for that field/parameter is obtained from this instance. For example, you can specify a method to be invoked: `@Builder.ObtainVia(method = "calculateFoo")`.



### 1.4. @Builder and @SuperBuilder


Reference: https://projectlombok.org/features/Builder

## 2. Reflection
---
### 2.1. `org.springframework.beans.BeanUtils`

### 2.2. Apache BeanUtils, Apache PropertyUtils

### 2.3. Method & Field

### 2.4. `VarHandle` & `MethodHandle`



## 3. Spring `Converter`/ `ConversionService`
---

## 4. Mapper
---
### 4.1. `ModelMapper`

### 4.2. `ObjectMapper`


## 5. MapStruct
---

## 6. Swagger
---

## 7. Spring Data JPA
---

### 7.1. Plugins
---


### 7.2. Repository-Level Projections
---
```java
public interface UserView { Long getId(); String getName(); String getEmail(); }

interface UserRepo extends JpaRepository<User, Long> {
    List<UserView> findByActiveTrue();
}
```


## 8. Comparison
---