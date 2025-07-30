import org.gradle.api.plugins.quality.Checkstyle

plugins {
    id("java")
    id("io.freefair.lombok") version "8.14"
    id("org.sonarqube") version "5.1.0.4882"
    id("com.star-zero.gradle.githook") version "1.2.1"
    id("checkstyle")
}

group = "com.gyhd"
version = "1.0-SNAPSHOT"

val lombokVersion = "1.18.38"

repositories {
    mavenCentral()
}

sonarqube {
    properties {
        property("sonar.projectKey", "my-gradle-spring-app-key")
        property("sonar.projectName", "My Gradle Spring App")
        property("sonar.sources", "src/main/java")
        property("sonar.tests", "src/test/java")
        property("sonar.java.binaries", "build/classes/java/main")
        // property("sonar.coverage.jacoco.xmlReportPaths", "${layout.buildDirectory.get()}/reports/jacoco/test/jacocoTestReport.xml")
    }
}

checkstyle {
    toolVersion = "10.12.4"
    config = resources.text.fromFile("config/checkstyle/checkstyle.xml")
}

githook {
    hooksDir = file("$rootDir/githooks/")
    failOnMissingHooksDir = true
    createHooksDirIfNotExist = true
    hooks {
        register("pre-commit") {
            task = "lint test"
            shell = "echo 1"
        }
        register("pre-push") {
            task = "someTask"
            shell = "someShell"
        }
    }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")
    checkstyle("com.puppycrawl.tools:checkstyle:${checkstyle.toolVersion}")
}

tasks.test {
    useJUnitPlatform()
}

// --- staged files properties ---


val filesListProp = "checkstyle.files.list" // newline-delimited file list path
val filesCsvProp  = "checkstyle.files"      // comma-separated paths

tasks.register<Checkstyle>("checkstyleStaged") {
    description = "Runs Checkstyle only on files passed via -P$filesListProp or -P$filesCsvProp."
    group = "verification"

    // Avoid needing JavaPluginExtension / SourceSet
    classpath = files()

    val stagedPaths: List<String> = when {
        providers.gradleProperty(filesListProp).isPresent -> {
            val listFile = file(providers.gradleProperty(filesListProp).get())
            if (listFile.exists()) listFile.readLines() else emptyList()
        }
        providers.gradleProperty(filesCsvProp).isPresent ->
            providers.gradleProperty(filesCsvProp).get().split(',')
        else -> emptyList()
    }

    val stagedFiles = stagedPaths
        .map { it.trim() }
        .filter { it.endsWith(".java") }
        .map { file(it) }
        .filter { it.exists() }

    onlyIf { stagedFiles.isNotEmpty() }
    setSource(stagedFiles)
    include("**/*.java")

    reports {
        html.required.set(true)
        xml.required.set(false)
        html.outputLocation.set(layout.buildDirectory.file("reports/checkstyle/staged.html"))
    }

    isIgnoreFailures = false
}

// Hide per-violation console output for all Checkstyle tasks (incl. checkstyleStaged)
tasks.withType<Checkstyle>().configureEach {
    // Gradle exposes this as a boolean property; in Kotlin DSL it's "isShowViolations".
    // It is deprecated but still works and is the intended switch for console spam.
//    @Suppress("DEPRECATION")
    isShowViolations = false
    // keep failing on violations:
    isIgnoreFailures = false
}
