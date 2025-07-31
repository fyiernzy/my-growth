---
updated: 2025-07-30T12:00:52.525+08:00
edited_seconds: 0
---
Together with the build.gradle.kts, validate the bash script for its intended purpose (is the logic correct without any local flaws?), robustness (able to handle exceptions, normal flow, edge cases gracefully without showing errors directly and is able to show appropriate message), friendliness (is the developer can use this commad effortlessly and have enough information when there's error), maintainability & readability (Is the code arrangement and design easy to maintain and debug) and performance (is this pre-commit hook is able to perform well when there's 100 or more java file to be staged?). Then, suggest possible fix and improvements

security, performance