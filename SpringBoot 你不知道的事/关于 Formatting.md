- Checkstyle：按照 predefined rules 检查代码风格，会抛出警示，但不会进行格式化（Formatting）；简单来说，就是会让程序员不舒服，但是不会强制要求程序员更改代码。
- Spotless：按照 predefined rules 格式化代码；强制更改代码。

|Feature|Checkstyle|Spotless|
|---|---|---|
|**Primary Action**|**Checks** code and **reports** violations.|**Formats/Fixes** code automatically.|
|**Core Function**|Linter, style checker.|Code formatter.|
|**Output**|Reports (XML, HTML) listing violations.|Modified source code files (in `apply` mode), build failure (in `check` mode).|
|**Fixing Issues**|Manual correction required.|Automatic correction (via `apply` task).|
|**Configurability**|Highly configurable via XML rules.|Configurable to use different formatters and some global settings (e.g., line endings). Less granular rule-by-rule configuration.|
|**Language Focus**|Primarily Java.|Multi-language support (wraps various formatters for different languages).|
|**Granularity**|Can enforce very specific, granular rules (e.g., "no public fields").|Less about specific structural rules, more about applying a chosen formatter's holistic style.|
|**Purpose in CI**|Fail the build if code is not compliant.|Fail the build if code is not formatted, or auto-format as part of the build.|

- **Use Spotless for formatting:** This should be your first line of defense for code style. It ensures everyone's code looks the same automatically, which is a huge win for consistency and readability.
- **Use Checkstyle for deeper style and convention checks:** After Spotless ensures consistent _formatting_, Checkstyle can then enforce more nuanced rules that aren't strictly about formatting, but about code structure and conventions (e.g., "methods shouldn't be too long," "no magic numbers," "all public methods must have Javadoc").


**The ideal setup for a Spring Boot (or any Java) project often involves both:**

1. **Spotless** to ensure all code is consistently _formatted_ (e.g., using `googleJavaFormat()`).
2. **Checkstyle** to enforce additional _style and structural rules_ that a simple formatter doesn't cover. These rules often focus on readability, maintainability, and preventing common anti-patterns


