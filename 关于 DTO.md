DTO 之间转换有很多方法，如下
- 使用代码生成工具：使用 MapStruct
- 使用人工生成：Setter（可以用插件生成，或者放在 Converter 里人工处理）, Fluent Setter, Builder
- 使用反射，比如 Spring.BeanUtils, Apache BeanUtils, Apache PropertyUtils

那有没有通过 拉拽图表的方式，底部使用 MapStruct 进行生成？进一步的懒人方式：
- 

关于 DTO 生成
- Swagger
- Spring Data JPA