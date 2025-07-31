---
updated: 2025-07-30T11:05:11.569+08:00
edited_seconds: 200
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

- [ ] Settle Git Hooks properly (Check + Add comments)
- [ ] Settle Checkstyle's rules properly
- [ ] Settle Visualization
- [ ] Settle Git CI/CD





## Setting Git Hooks

| Feature                             | 🟢 Husky for Java (Gradle plugin)           | 🟡 Native `.git/hooks`                     | 🔵 `.githooks/` + `core.hooksPath`             |
| ----------------------------------- | ------------------------------------------- | ------------------------------------------ | ---------------------------------------------- |
| **Works across team members**       | ✅ Yes (via Gradle sync, automated)          | ❌ No (not version-controlled by default)   | ✅ Yes (if `.githooks/` is tracked + setup run) |
| **Version-controllable**            | ✅ Yes (hook files and Gradle config in VCS) | ❌ No                                       | ✅ Yes (you commit `.githooks` folder)          |
| **Initial Setup Effort**            | ⚠️ Medium (add Gradle plugin + config)      | ✅ Low (directly edit `.git/hooks`)         | ⚠️ Medium (requires setup script or command)   |
| **Integration with Gradle tasks**   | ✅ Strong (uses Gradle tasks directly)       | ⚠️ Manual (you must call Gradle manually)  | ⚠️ Manual (but flexible)                       |
| **Hook Language Flexibility**       | ✅ Any shell-compatible (Bash, Sh, etc.)     | ✅ Any shell-compatible                     | ✅ Any shell-compatible                         |
| **Cross-IDE Consistency**           | ✅ Yes                                       | ❌ No                                       | ✅ Yes                                          |
| **Automation in CI/CD**             | ✅ Easy to replicate with Gradle             | ⚠️ Hard to reproduce without syncing hooks | ✅ Reproducible with config                     |
| **Tooling Support (IDE detection)** | ✅ Most IDEs pick up Gradle-managed tools    | ❌ Limited                                  | ⚠️ Needs configuration                         |

Compare between Husky for Java (Gradle) and .githooks

|Feature|🔵 `.githooks/` (via `core.hooksPath`)|🟢 Husky for Java (Gradle Plugin)|
|---|---|---|
|**Version-controlled hooks**|✅ Yes (committed to repo)|✅ Yes (hook scripts in repo, configured via Gradle)|
|**Auto installation for contributors**|❌ No (each dev must run `git config core.hooksPath .githooks`)|✅ Yes (automatically installed via Gradle plugin)|
|**Build tool integration**|⚠️ Manual (need to call `./gradlew` inside hook)|✅ Strong (hooks trigger Gradle tasks natively)|
|**Cross-platform compatibility**|✅ Yes, if shell scripts are portable|✅ Yes, Gradle handles platform abstraction|
|**Ease of adoption**|⚠️ Moderate (requires team awareness + manual setup)|✅ Easy (just clone & run `./gradlew build`)|
|**CI/CD integration**|⚠️ Manual (unless `core.hooksPath` is also configured in CI setup)|✅ Easy (hooks as part of Gradle tasks, works in CI)|
|**IDE support**|✅ Hooks work once set up|✅ Integrated with Gradle, often picked up by IDEs like IntelliJ|
|**Language/framework agnostic**|✅ Yes (works in any repo)|❌ Tied to Gradle (Java/Kotlin/Scala)|
|**Learning curve**|⚠️ Medium (must know Git internals, hook lifecycle)|⚠️ Medium (must know Gradle + plugin config)|

---

### 🔵 `.githooks/` Approach

#### 🔧 Usage:

1. Create a `.githooks/` directory in your project root.
    
2. Place hook files inside (e.g., `.githooks/pre-commit`).
    
3. Make them executable: `chmod +x .githooks/*`
    
4. Set the hook path:
    
    ```bash
    git config core.hooksPath .githooks
    ```
    

#### 💪 Strengths:

- Fully portable & Git-native.
    
- Language-agnostic (not tied to Java/Gradle).
    
- Simple structure and clear ownership of scripts.
    

#### ❗ Weaknesses:

- Needs each team member to **run a setup command** manually.
    
- Hooks won’t work unless the `core.hooksPath` is set.
    
- No integration with Gradle unless you explicitly call it in the scripts.
    

---

### 🟢 Husky for Java (e.g., using `gradle-git-hooks` or `gradle-husky` plugin)

#### 🔧 Usage:

1. Add the plugin to your `build.gradle.kts`:
    
    ```kotlin
    plugins {
        id("com.star-zero.gradle.githook") version "1.2.1"
    }
    
    githook {
        hooks {
            "pre-commit" {
                task("spotlessCheck")
            }
            "pre-push" {
                task("checkstyleMain")
            }
        }
    }
    ```
    
2. Run `./gradlew githookInstall` (or automate this in the build lifecycle).
    

#### 💪 Strengths:

- Automatically installs `.git/hooks` with content derived from Gradle config.
    
- Triggers Gradle tasks directly (`spotlessCheck`, `test`, etc).
    
- All logic lives in `build.gradle(.kts)` — no need to manage raw shell scripts.
    
- Excellent for **Java-first projects**.
    

#### ❗ Weaknesses:

- Gradle-only. Not usable in non-Gradle setups (e.g., Maven).
    
- Might conflict with other `.git/hooks` unless managed carefully.
    
- Harder to use custom shell logic unless wrapped in Gradle tasks.
    

---

### 📝 Summary

|Use Case|Recommended|
|---|---|
|Java/Gradle project with CI and auto setup|🟢 Husky|
|Polyglot team or non-Java developers in the same repo|🔵 `.githooks/`|
|You need Git-native behavior and shell-level flexibility|🔵 `.githooks/`|
|You want Gradle to enforce checks automatically|🟢 Husky|

---

Let me know if you want a ready-made template (for either method) to drop into your project.

# Husky For Java (Gradle)
---
