---
updated: 2025-07-29T09:47:12.026+08:00
edited_seconds: 117
---
- [ ] Coding Style
	- [ ] How to use Checkstyle & specify the rules?
	- [ ] How to integrate Checkstyle into the gitlab cicd?
		- [ ] How to perform testing on latest CICD safely?
	- [ ] Is there any other tools to visualize Checkstyle (since currently it's merely command line tools)

What abilities does Checkstyle provide
Minimum Configuration
How to specify my own need?
What need are required?
How to see the difference? Is anyone making this website?
How to visualize Checkstyle?

Define a 
```kotlin
val lombokVersion = "1.18.38"

dependencies {
    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")
}

val versions = mapOf(
    "lombok" to "1.18.38",
    "junit" to "5.10.0"
)

dependencies {
    compileOnly("org.projectlombok:lombok:${versions["lombok"]}")
    annotationProcessor("org.projectlombok:lombok:${versions["lombok"]}")
    testImplementation(platform("org.junit:junit-bom:${versions["junit"]}"))
}
```

```kotlin
group = "com.gyhd"
version = "1.0-SNAPSHOT"
```

`group` and `version` can be used directly without specifying `val` because they are predefined extension properties of the `Project` class in Gradle. So we are assigning rather than declaring a new variables.