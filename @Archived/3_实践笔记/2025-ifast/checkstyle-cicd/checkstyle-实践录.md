---
updated: 2025-07-30T11:05:11.569+08:00
edited_seconds: 200
---
# 1. Git Hooks
---
当时做调研的时候，是找到了好几种设计 Git Hook 的方式，最后还是选择了 Git 这种传统的方式，根本原因也是因为零依赖，这对部分保密性较强的公司显得特别友好。

```bash
git config core.hookPaths
```



第二种方式就是使用 Husky，但是这个对
第三种就是 Husky for Gradle

# 2. Checkstyle
---

# 3. CI/CD

# 4. Gradle Script
---
```groovy
import org.gradle.api.internal.provider.DefaultProvider
//import com.ifastpay.GitUtils
//import com.ifastpay.StagedFileUtils

plugins {
    id 'java'
    id 'org.springframework.boot' version "${springBootVersion}"
    id 'io.spring.dependency-management' version "${springDependencyVersion}"
    id 'org.owasp.dependencycheck' version "${owaspDependencyCheckVersion}"
    id 'checkstyle'
}

ext {
    executeCommands = { List<String> cmd, File workingDir = rootProject.projectDir ->
        try {
            logger.info("[exec] ${cmd.join(' ')} (cwd=${workingDir})")
            def proc = new ProcessBuilder(cmd)
                    .directory(workingDir)
                    .redirectErrorStream(true)
                    .start()
            proc.waitFor()
            proc.exitValue() == 0
                    ? proc.inputStream.text.trim()
                    : ""
        } catch (Exception e) {
            logger.warn("[exec] failure: ${e.message}")
            ""
        }
    }
    
    gitDiffJava = { List<String> extraArgs = [],
                    File workingDir = rootProject.projectDir ->
        def cmd = ['git', 'diff',
                   '--name-only', '--diff-filter=ACMRTUXB'] +
                extraArgs +
                ['--', ':(glob)**/*.java']
        executeCommands(cmd, workingDir)
    }
    
    gitFetchBranch = { String remote = 'origin',
                       String branch,
                       File dir = rootProject.projectDir ->
        executeCommands(['git', 'fetch', '--no-tags', '--prune', remote, branch], dir)
    }

    fetchStagedJava = { Project requester ->
        // Priority 1: User manually provides files via -Pfiles
        def filesOverride = requester.providers.gradleProperty('files').orElse('').get().trim()

        if (filesOverride) {
            requester.logger.lifecycle("[checkstyleStaged] [${requester.path}] Using -Pfiles override")
            return filesOverride.replaceAll(/\s+/, '\n')
        }

        // Priority 2: GitLab CI/CD Merge Request context
        def env = System.getenv()
        def baseSha = env['CI_MERGE_REQUEST_TARGET_BRANCH_SHA']
        def headSha = env['CI_COMMIT_SHA']
        def defaultBranch = env['CI_DEFAULT_BRANCH'] ?: 'develop'

        if (baseSha && headSha) {
            requester.logger.lifecycle("[checkstyleStaged] [${requester.path}] GitLab MR context detected: ${baseSha}..${headSha}")
            def output = gitDiffJava([baseSha, headSha], requester.projectDir)
            if (output) {
                requester.logger.lifecycle("[checkstyleStaged] [${requester.path}] Found ${output.readLines().size()} Java file(s) in GitLab MR diff")
                return output
            }
        }

        // Priority 3: CI/CD Pipeline
        if (env['CI'] == 'true') {
            requester.logger.lifecycle("[checkstyleStaged] [${requester.path}] CI pipeline context – comparing against origin/${defaultBranch}")
            gitFetchBranch('origin', defaultBranch, project.projectDir)
            def output = gitDiffJava(["origin/${defaultBranch}", "HEAD"], requester.projectDir)
            if (output) {
                requester.logger.lifecycle("[checkstyleStaged] [${requester.path}] Found ${output.readLines().size()} Java file(s) in CI diff against ${defaultBranch}")
                return output
            }
        }

        // Priority 4: Local development (interactive staging)
        def stagedFiles = gitDiffJava(['--cached'], requester.projectDir)

        if (stagedFiles) {
            requester.logger.lifecycle("[checkstyleStaged] [${requester.path}] Found ${stagedFiles.readLines().size()} staged Java file(s)")
            return stagedFiles
        }

        // Priority 5: Local development - if no staged files, check unstaged changes
        def unstagedFiles = gitDiffJava([], requester.projectDir)

        if (unstagedFiles) {
            requester.logger.lifecycle("[checkstyleStaged] [${requester.path}] Found ${unstagedFiles.readLines().size()} unstaged Java file(s)")
            return unstagedFiles
        }

        requester.logger.lifecycle("[checkstyleStaged] [${requester.path}] No Java files found for checkstyle analysis")
        return ""
    }


    // In-memory cache and loader
    stagedJavaCache = null
    stagedJavaLock = new Object()

    /**
     * @param requester project calling the helper
     * @param useGlobalCache true → share Git diff for whole build
     */
    loadStagedJava = { Project requester, boolean useGlobalCache ->
        if (useGlobalCache) {
            synchronized (stagedJavaLock) {
                if (stagedJavaCache == null) {
                    stagedJavaCache = fetchStagedJava(requester)
                }
                return stagedJavaCache
            }
        }

        // Using module-local diff (no-caching)
        return fetchStagedJava(requester)
    }
}

allprojects {
    group = 'com.ifast.ipaymy'
    version = '0.0.1-SNAPSHOT'

    java {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    compileJava.options.encoding = 'UTF-8'

    if (!hasProperty('profile')) {
        ext.profile = 'local'
    }

    repositories {
        maven { url = 'https://plugins.gradle.org/m2/' }
        mavenCentral()
        maven {
            url = 'http://horizon.ifastfinancial.local:8003/nexus/content/groups/public'
            allowInsecureProtocol = true
        }
    }
}

subprojects {
    apply plugin: 'java'
    apply plugin: 'org.springframework.boot'
    apply plugin: 'io.spring.dependency-management'
    apply plugin: 'org.owasp.dependencycheck'
    apply plugin: 'checkstyle'

    dependencyManagement {
        imports {
            mavenBom "org.springframework.boot:spring-boot-dependencies:${springBootVersion}"
            mavenBom "org.springframework.cloud:spring-cloud-dependencies:${springCloudDependencyVersion}"
        }
    }

    dependencies {
        // Common Spring Boot Dependencies
        implementation 'org.springframework.boot:spring-boot-starter-actuator'
		implementation 'org.springframework.boot:spring-boot-starter-security'

        // Spring Cloud
        implementation 'org.springframework.cloud:spring-cloud-starter-openfeign'

        // Lombok
        compileOnly 'org.projectlombok:lombok'
        annotationProcessor 'org.projectlombok:lombok'

        // OpenAPI & Swagger
        implementation "io.swagger.core.v3:swagger-annotations:${swaggerAnnotationVersion}"
        implementation "org.openapitools:jackson-databind-nullable:${jacksonDatabindVersion}"
        implementation "org.springdoc:springdoc-openapi-starter-webmvc-ui:${openApiWebMvcUIVersion}"

        implementation 'jakarta.validation:jakarta.validation-api'

        // Testing
        testImplementation 'org.springframework.boot:spring-boot-starter-test'
        testCompileOnly 'org.projectlombok:lombok'
        testAnnotationProcessor 'org.projectlombok:lombok'

        // Utility Libraries
        implementation 'org.apache.commons:commons-lang3'
        implementation "org.apache.commons:commons-collections4:${apacheCommonCollectionVersion}"

        implementation "org.apache.httpcomponents:httpclient:${apacheHttpClientVersion}"
    }

    tasks.named('test') {
        useJUnitPlatform()
    }

    // Global Checkstyle configuration
    checkstyle {
        toolVersion = '10.26.1'
        configFile = file("${rootDir}/config/checkstyle/checkstyle.xml")
        ignoreFailures = false
    }

    tasks.withType(Checkstyle).configureEach {
        reports {
            // Export to both xml and html files
            xml.setRequired(true)
            html.setRequired(true)

            // Group them by subproject > taskName
            // For instances,
            // - my-modular-ws/reports/checkstyle/checkstyleMain.html
            // - my-modular-ws/reports/checkstyle/checkstyleTest.html
            xml.outputLocation = layout.buildDirectory.file("reports/checkstyle/${name}.xml")
            html.outputLocation = layout.buildDirectory.file("reports/checkstyle/${name}.html")
        }
    }

    // Perform the Checkstyle on the leaf projects only
    if (childProjects.isEmpty()) {
        tasks.register('checkstyleStaged', Checkstyle) {
            group = 'verification'
            description = "Run Checkstyle on only the staged Java files in ${path}"
            classpath = files()

            // 1. Import the extra property extensions
            def exec = rootProject.extensions.extraProperties

            // 2. Get the a list of Java files to be checked
            // Since everything in the ext block is stored as/ returned as an Object
            // We have to cast it explicitly using as Provider<String>
            def stagedText = exec.has('globalGitDiff')
                    ? exec.get('globalGitDiff') as Provider<String>
                    : providers.provider { exec.loadStagedJava(project, false) } as Provider<String>

            def candidateFiles = stagedText.map { txt ->
                if (!txt?.trim()) return []

                // This candidateFiles will keep only the staged Java files that reside within its own module
                txt.readLines()
                        .collect { it.trim() }
                        .findAll { it.endsWith('.java') }
                        .collect { rootProject.file(it) }
                        .findAll { it.exists() }
                        .findAll { it.toPath().startsWith(projectDir.toPath()) }
            }

            // 4. Wire the candidate list into the the Checkstyle
            // Gradle now sees real inputs
            def stagedJavaFiles = null;

            def getStagedJavaFiles = {
                if (stagedJavaFiles == null) {
                    stagedJavaFiles = candidateFiles.get();
                }
                return stagedJavaFiles
            }


            inputs.files {
                project.files(getStagedJavaFiles())
            }
            source {
                project.files(getStagedJavaFiles())
            }

            outputs.upToDateWhen { false }

            onlyIf {
                def candidates = getStagedJavaFiles()
                if (candidates.isEmpty()) {
                    logger.lifecycle("[checkstyleStaged] [${project.path}] SKIPPED – No matching Java files in this module")
                    return false
                } else {
                    logger.lifecycle("[checkstyleStaged] [${project.path}] Will run checkstyle on ${candidates.size()} file(s)")
                    return true
                }
            }
        }
    }
}

// Aggregate task at root to run all module-level 'checkstyleStaged'
tasks.register('checkstyleStaged') {
    group = 'verification'
    description = 'Run Checkstyle on staged Java files across all modules'

    def exec = rootProject.extensions.extraProperties;
    exec.globalGitDiff = rootProject.providers.provider {
        rootProject.extensions.extraProperties.loadStagedJava(rootProject, true)
    } as DefaultProvider
    exec.loadStagedJava(project, true)
    dependsOn subprojects.collectMany { subproject ->
        subproject.tasks.matching { it.name == 'checkstyleStaged' }
    }
}
```

