

```java
// Version 1
// Cleanest, yet not runtime enforcement, where the ErrorCodeEnum can be anything.
public static void notNull(Object obj, ErrorCodeEnum erroCodeEnum, Pair<>...) {
	if(Validators.isNull(obj)) {
		throw new IfastPayException(erroCodeEnum.getMessage())
	}
}

AssertionUtil.notNull(obj, ErrorCodeEnum.ErrorCode, Pair<>...)

// Version 2
// For each exception that extends IfastPayException, it will need to have a static method to supply itself/ IfastPayException.
AssertionUtil.notNull(obj, ExactException.supply(ExactErrorCodeEnum, Pair<>...))

// Version 3
// There's a need to define a GeneralExceptionCollection.
AssertionUtil.notNull(obj, GeneralExceptionCollection.supply(ExactErrorCodeEnum, Pair<>...))

// Version 4
// Need to write verbose lambda expressions.
AssertionUtil.notNull(obj, () -> new ExactException(ExactErrorCodeEnum, Pair<>...))


// Version 5
// Can use @Configuration or other dependency injection approach
// The basic idea is that using AssertUtil.forErrors(ErrorCodeEnum) it will return a DomainAssert which provide type safety.

public final class DomainAssert<ErrorCodeEnum> {
	public static <E extends ErrorCodeEnum> DomainAssert<E> forErrors(ErrorCodeEnum) {
		return new DomainAssert<E>();
	}

	public void notNull(Obj obj, ErrorCodeEnum errorCodeEnum, Pair<>...) {
	}
}

```

1. **Message templating:** keep `template()` stable and parameterize with `Ctx`. Centralize rendering in the factory. That makes localization and redaction easy.
    
2. **Attach causes:** overload `isTrue(..., Throwable cause)` so you don’t lose low‑level diagnostics.
    
3. **DTO vs business rules:** keep Bean Validation annotations for DTO shape; use your `Assert*` for business invariants.
    
4. **Observability:** include `code`, plus a safe subset of `ctx`, in structured logs. Redact at the factory if needed.
    
5. **Testing:** add a small test that asserts every template’s placeholders are satisfiable by the `Key<?>`s you expect.
public class Context {

private final Map<String, Object> data = new HashMap<>();

public Context add(String key, Object value) { data.put(key, value); return this; }

public Map<String, Object> getData() { return data; }

}

AssertionUtil.assertThat(obj)

.notNull(PaymentErrorCode.PAYMENT_NOT_FOUND, paymentExceptionSupplier)

.isTrue(amount > 0, PaymentErrorCode.INVALID_AMOUNT, paymentExceptionSupplier)

.validate();


public interface AppErrorType {
  String code();
  String messageTemplate(); // e.g. "Card expired: {expiry}"
}

public final class Key<T> {
  private final String name;
  private final Class<T> type;
  private Key(String name, Class<T> type) { this.name = name; this.type = type; }
  public static <T> Key<T> of(String name, Class<T> type) { return new Key<>(name, type); }
  public String name() { return name; }
  public Class<T> type() { return type; }
}

public final class Ctx {
  private final Map<String, Object> map = new LinkedHashMap<>();
  public <T> Ctx put(Key<T> key, T value) {
    if (value != null && !key.type().isInstance(value)) {
      throw new IllegalArgumentException("Key " + key.name() + " expects " + key.type().getSimpleName());
    }
    map.put(key.name(), value);
    return this;
  }
  public Map<String,Object> toMap() { return Map.copyOf(map); }
  public static Ctx of() { return new Ctx(); }
}

public final class Keys {
  public static final Key<String> EXPIRY = Key.of("expiry", String.class);
  public static final Key<Long> USER_ID = Key.of("userId", Long.class);
}


@FunctionalInterface
public interface ExceptionFactory {
  IfastPayException create(AppErrorType type, Map<String, Object> ctx, Throwable cause);

  static ExceptionFactory defaultFactory() {
    return (type, ctx, cause) -> new IfastPayException(
        type.code(),
        MessageTemplates.render(type.messageTemplate(), ctx), // simple "{key}" -> value
        ctx,
        cause
    );
  }
}

public record Err(AppErrorType type, Ctx ctx) {
  public static Err of(AppErrorType type) { return new Err(type, Ctx.of()); }
  public Err with(Key<?> k, Object v) { ctx.put((Key)k, v); return this; }
}

- **Stable codes, variable messages:** make `code()` stable; keep messages templated and translate via `ResourceBundle` if needed. Render the final message in the factory.
    
- **Attach causes:** add overloads that accept a `Throwable cause` when asserting after catching lower‑level errors.
    
- **Return‑value helpers:** `requireNonNull` promotes guard‑clause style and avoids repeating local variables.
    
- **No premature allocation:** keep context builders cheap; defer heavy message formatting to factory right before throwing.
    
- **Jakarta Bean Validation integration (optional):** for DTOs, validate with annotations and map violations to `AppErrorType` + `Ctx` in one place; for business rules, keep using `AssertUtil`.
    
- **Testing:** unit‑test the factory, the message rendering, and each enum’s expected context (keys present, types match). Consider a tiny test util that asserts an enum’s `messageTemplate` placeholders match registered `Key<?>`s.
    
- **Logging & observability:** include `code` and context keys in structured logs. Your global handler should expose `code`, a user‑facing `message`, and optionally `context` filtered for sensitive data.

- **Result/Either style:** return `Result<T, Err>` (e.g., Vavr `Either`) from validation paths and throw only at boundaries. Your global handler can still translate `Err` to `IfastPayException` for HTTP.
    
- **Guava/Spring Preconditions/Assert:** fine for internal invariants, but they lack your error code + context model.


Yes. **Jakarta Bean Validation (a.k.a. Bean Validation / JSR‑303/349/380, now `jakarta.validation.*`) is not limited to controllers.** You can (and should) use it across layers—DTOs, domain models, services, repositories, async consumers, etc. Below are practical ways to apply it “beyond the controller,” with patterns, caveats, and examples you can drop into Spring Boot or Jakarta EE/CDI.

---

## 1) Method‑level validation in the Service layer

Validate **method parameters and return values** on your services. Great for business‑facing preconditions that are still _data_ constraints (non‑nulls, ranges, formats), not complex domain rules.

**Spring Boot (3.x / Jakarta packages)**

```java
import jakarta.validation.constraints.*;
import org.springframework.validation.annotation.Validated;

@Validated // enables method validation for this bean (via AOP proxy)
@Service
public class PaymentService {

  public void transfer(
      @NotNull Long fromAccountId,
      @NotNull Long toAccountId,
      @Positive BigDecimal amount) { /* ... */ }

  @NotNull
  public Receipt process(@Valid PaymentRequest request) { /* ... */ }
}
```

- Spring Boot auto‑configures method validation; you **must** put `@Validated` on the bean (class or method).
    
- Violations raise `ConstraintViolationException` before your method body runs (or after it returns, for return‑value constraints).
    

**CDI / Jakarta EE**

```java
import jakarta.validation.constraints.*;
import jakarta.validation.executable.ValidateOnExecution;

@ValidateOnExecution // optional: fine-tune which methods are validated
@ApplicationScoped
public class PaymentService {
  public void transfer(@NotNull Long from, @NotNull Long to, @Positive BigDecimal amount) { … }
}
```

> Tip: Method validation won’t trigger on **self‑invocation** in proxy‑based frameworks (e.g., a method calling another method of the same bean). Split into another bean or use AspectJ if you need that path validated.

---

## 2) Domain model / aggregate validation

Put constraints on your **domain entities/value objects**—particularly for _invariants_ that must always hold.

```java
public final class Money {
  @NotNull @DecimalMin("0.00")
  private final BigDecimal amount;

  @NotBlank
  private final String currency;

  public Money(BigDecimal amount, String currency) { this.amount = amount; this.currency = currency; }
}
```

- Use `@Valid` on aggregate roots to **cascade** validation into nested objects:
    

```java
public class Order {
  @NotNull private Long id;
  @Valid @NotNull private Customer customer;
  @Valid @Size(min = 1) private List<OrderLine> lines;
}
```

**Manual, programmatic validation** when you’re changing state outside of web boundaries:

```java
import jakarta.validation.Validator;

@RequiredArgsConstructor
public class OrderDomainService {
  private final Validator validator;

  public void submit(Order order) {
    var violations = validator.validate(order, SubmitGroup.class);
    if (!violations.isEmpty()) throw new ConstraintViolationException(violations);
    // proceed
  }
}
```

Use **groups** to capture lifecycle‑specific rules (e.g., `DraftGroup`, `SubmitGroup`).

---

## 3) Persistence layer (JPA/Hibernate integration)

- You can put constraints on JPA entities and let Hibernate validate on **pre‑persist / pre‑update** (it integrates with Bean Validation by default).
    
- This catches bugs even if a path bypasses your controller/service.
    

Caveats:

- Don’t rely only on entity validation for _API ergonomics_—fail as early as possible (DTO/service), not only at persistence.
    
- Be careful with `@NotNull` vs DB `NOT NULL`. Keep them consistent; validation doesn’t replace database constraints.
    

---

## 4) Messaging / scheduled jobs / adapters

Anywhere you deserialize or accept input (Kafka consumers, SQS workers, CRON tasks), validate the payload before processing:

```java
public void onMessage(@Valid PaymentEvent event) { … }
// or
var violations = validator.validate(event);
if (!violations.isEmpty()) { /* dead-letter / log / map to domain error */ }
```

---

## 5) Custom, domain‑specific constraints

For rules that are still _declarative_:

- **Class‑level** constraints when multiple fields interact (e.g., `@DateRangeValid`).
    
- **Cross‑parameter** constraints on methods (e.g., `from != to`).
    
- **Container element** constraints: `List<@NotBlank String> tags`.
    

```java
@Target({ TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = DateRangeValidator.class)
public @interface ValidDateRange { String message() default "..."; Class<?>[] groups() default {}; Class<? extends Payload>[] payload() default {}; }

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, Booking> {
  public boolean isValid(Booking b, ConstraintValidatorContext ctx) {
    return b.getStart().isBefore(b.getEnd());
  }
}
```

---

## 6) Mapping to your exception model (`IfastPayException`)

You can let `ConstraintViolationException` bubble to your **global exception handler** and convert it to your own error format/code, or you can catch and wrap nearer to the source.

**Spring example (controller advice):**

```java
@RestControllerAdvice
public class GlobalHandler {
  @ExceptionHandler(ConstraintViolationException.class)
  ResponseEntity<ApiError> onViolation(ConstraintViolationException ex) {
    var ctx = ex.getConstraintViolations().stream().collect(toMap(
      v -> v.getPropertyPath().toString(), ConstraintViolation::getMessage
    ));
    var e = new IfastPayException("VALIDATION", "Request validation failed", ctx);
    return ResponseEntity.badRequest().body(ApiError.from(e));
  }
}
```

If you want **per‑field error codes**, put codes in messages (e.g., `{code:PAY-001}`) or use custom constraint annotations that carry a code attribute, then extract it in the handler.

---

## 7) Choosing _what_ belongs to Bean Validation vs your `AssertUtil`

A pragmatic split that works well:

- **Bean Validation:** shape/format/range/existence constraints that are stable and declarative (nullability, sizes, regex, numeric ranges, nested object validity). Use everywhere—DTOs, domain, method params.
    
- **AssertUtil (your domain asserts):** _business_ invariants that need domain context or lookups (e.g., “FX rate must be live for pair X at time T”). These often need services/repositories and return **domain error codes** (your `FxErrorCode` etc.).
    

This keeps controllers slimmer and services expressive, without bending Bean Validation into complex imperative rules.

---

## 8) Operational considerations

- **Fail‑fast:** If desired, validate first violation only.
    
    - Hibernate Validator property: `hibernate.validator.fail_fast=true` (via config) or programmatically.
        
- **Performance:** The `Validator` is thread‑safe; inject and reuse. Avoid validating the same object repeatedly in the same call chain.
    
- **i18n / message interpolation:** Back messages by bundles; or interpolate to your domain error format and map to `IfastPayException`.
    
- **AOP/proxy limits:** As mentioned, method validation won’t fire on self‑invocation. Also, final classes/methods may skip proxying depending on your proxy type.
    
- **Testing:** Unit test constraints with the `Validator` directly; for method validation, write tests that assert `ConstraintViolationException` on invalid params/returns.
    

---

## Minimal end‑to‑end sketch (Spring Boot)

```java
// DTO
public record TransferRequest(
  @NotNull Long from,
  @NotNull Long to,
  @Positive BigDecimal amount) {}

// Service
@Validated
@Service
public class TransferService {
  public Receipt transfer(@Valid TransferRequest req) { /* ... */ return new Receipt(...); }

  @NotNull
  public Receipt getReceipt(@NotNull String id) { /* ... */ return found; }
}

// Global exception mapping
@RestControllerAdvice
class GlobalHandler {
  @ExceptionHandler(ConstraintViolationException.class)
  ResponseEntity<ApiError> onViolation(ConstraintViolationException ex) { /* map to IfastPayException */ }
}
```

---

### Bottom line

**Yes**—Jakarta Validation is a general‑purpose validation mechanism. Use it at service boundaries, inside the domain, in persistence hooks, and in async adapters. Pair it with your `AssertUtil` for richer domain rules, and map `ConstraintViolationException` centrally to your `IfastPayException` so your error payloads stay consistent everywhere. If you share your stack (Spring/Quarkus/Jakarta EE) and a couple of concrete cases, I can show exactly how to wire it in your project.