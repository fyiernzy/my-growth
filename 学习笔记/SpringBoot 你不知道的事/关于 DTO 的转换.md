---
updated: 2025-07-26T00:52:21.413+08:00
edited_seconds: 70
---
DTO 之间转换有很多方法，如下
- 使用代码生成工具：使用 MapStruct
- 使用人工生成：Setter（可以用插件生成，或者放在 Converter 里人工处理）, Fluent Setter, Builder
- 使用反射，比如 Spring.BeanUtils, Apache BeanUtils, Apache PropertyUtils

那有没有通过 拉拽图表的方式，底部使用 MapStruct 进行生成？进一步的懒人方式：

## Setter & Converter
---

## Reflection
---
VarHandle, MethodHandler, Method, Field, 

https://projectlombok.org/features/Builder
## Lombok's @Builder
---

## Lombok's @Accessors
---

## MapStruct
---

关于 DTO 生成
- Swagger
- Spring Data JPA

Builder.Default
`@Builder(builderMethodName = "")` is legal (and will suppress generation of the builder method) starting with lombok v1.18.8.

`@Builder(access = AccessLevel.PACKAGE)` is legal (and will generate the builder class, the builder method, etc with the indicated access level) starting with lombok v1.18.8.

@Builder(builderClassName = "HelloWorldBuilder", buildMethodName = "execute", builderMethodName = "helloWorld", toBuilder = true, access = AccessLevel.PRIVATE, setterPrefix = "set")


SuperBuilder
Accessors()
`lombok.accessors.chain` = [`true` | `false`] (default: false)

If set to `true`, any field/class that either doesn't have an `@Accessors` annotation, or it does, but that annotation does not have an explicit value for the `chain` parameter, will act as if `@Accessors(chain = true)` is present.

`lombok.accessors.fluent` = [`true` | `false`] (default: false)

If set to `true`, any field/class that either doesn't have an `@Accessors` annotation, or it does, but that annotation does not have an explicit value for the `fluent` parameter, will act as if `@Accessors(fluent = true)` is present.

`lombok.accessors.makeFinal` = [`true` | `false`] (default: false)

If set to `true`, any field/class that either doesn't have an `@Accessors` annotation, or it does, but that annotation does not have an explicit value for the `makeFinal` parameter, will act as if `@Accessors(makeFinal = true)` is present.

`lombok.accessors.prefix` += _a field prefix_ (default: empty list)

This is a list property; entries can be added with the `+=` operator. Inherited prefixes from parent config files can be removed with the `-=` operator. Any class that either doesn't have an `@Accessors` annotation, or it does, but that annotation does not have an explicit value for the `prefix` parameter, will act as if `@Accessors(prefix = {_prefixes listed in configuration_})` is present.

`lombok.accessors.capitalization` = [`basic` | `beanspec`] (default: basic)

Controls how tricky cases like `uShaped` (one lowercase letter followed by an upper/titlecase letter) are capitalized. `basic` capitalizes that to `getUShaped`, and `beanspec` capitalizes that to `getuShaped` instead.  
Both strategies are commonly used in the java ecosystem, though `beanspec` is more common.

`lombok.accessors.flagUsage` = [`warning` | `error`] (default: not set)

Lombok will flag any usage of `@Accessors` as a warning or error if configured.