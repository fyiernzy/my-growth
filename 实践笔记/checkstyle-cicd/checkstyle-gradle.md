```groovy
// Root build.gradle (Groovy)

plugins {
  // Apply but do not force on root; we'll apply to subprojects with Java only
  id 'checkstyle' apply false
}

ext {
  // Use the Checkstyle version you want; keep or remove if already set elsewhere
  checkstyleVersion = '10.17.0'
}

// Ensure Java projects get Checkstyle configured
subprojects { subproj ->
  plugins.withType(JavaPlugin) {
    subproj.apply plugin: 'checkstyle'

    subproj.checkstyle {
      toolVersion = rootProject.checkstyleVersion
      // Use whichever one you already use in your project:
      // Option A: a single config file
      // config = resources.text.fromFile("${rootDir}/config/checkstyle/checkstyle.xml")
      // Option B: a directory of configs (Gradle <8 style)
      // configDirectory = file("${rootDir}/config/checkstyle")
      ignoreFailures = false
      showViolations = true
    }

    // A Checkstyle task that runs only on staged (or explicitly provided) files in THIS module
    subproj.tasks.register('checkstyleStaged', Checkstyle) { t ->
      group = 'verification'
      description = "Run Checkstyle on staged Java files under ${subproj.path}"

      // Checkstyle itself does not need classpath; keep empty to avoid wiring sourceSets
      classpath = subproj.files()

      // Configure reports (Gradle 7+ API is 'required.set(true)')
      reports { r ->
        r.xml.required.set(true)
        r.html.required.set(true)
        r.xml.outputLocation.set(subproj.layout.buildDirectory.file("reports/checkstyle/staged.xml"))
        r.html.outputLocation.set(subproj.layout.buildDirectory.file("reports/checkstyle/staged.html"))
      }

      // Defer file detection to execution time
      doFirst {
        // 1) Accept explicit list via -Pfiles=path1,path2,...
        def explicit = (subproj.findProperty('files') ?: '').toString().trim()
        List<File> candidates = []

        if (!explicit) {
          // 2) Otherwise, auto-detect STAGED Java files via git
          def out = new ByteArrayOutputStream()
          subproj.exec {
            workingDir rootProject.projectDir
            // ACMRTUXB = add/copy/modify/rename/typechange/unmerged/unknown/broken
            commandLine 'git', 'diff', '--name-only', '--cached', '--diff-filter=ACMRTUXB', '--', '*.java'
            standardOutput = out
            // Not failing if repo is clean or not a git repo
            ignoreExitValue true
          }
          explicit = out.toString('UTF-8')
            .readLines()
            .collect { it.trim() }
            .findAll { it }
            .join(',')
        }

        if (explicit) {
          // Map to files under root, then keep only those inside this subproject
          def allFiles = explicit.split(',')*.trim()
            .findAll { it && it.endsWith('.java') }
            .collect { rootProject.file(it) }
            .findAll { it.exists() }

          def moduleDir = subproj.projectDir.canonicalFile
          candidates = allFiles.findAll { f -> f.canonicalFile.path.startsWith(moduleDir.path + File.separator) }
        }

        if (candidates.isEmpty()) {
          logger.lifecycle("No staged Java files under ${subproj.path}; skipping Checkstyle.")
          t.enabled = false
          return
        }

        // Point Checkstyle to the chosen files only
        t.setSource(subproj.files(candidates))
      }
    }
  }
}

// Aggregate task at root that triggers per-module tasks (only those with Java plugin)
tasks.register('checkstyleStaged') {
  group = 'verification'
  description = 'Run Checkstyle on staged Java files across all modules'

  // Depends only on subprojects that actually have the task (i.e., Java modules)
  dependsOn {
    subprojects.findAll { it.tasks.findByName('checkstyleStaged') != null }
               .collect { it.tasks.named('checkstyleStaged') }
  }
}

```

```bash
#!/usr/bin/env bash
set -euo pipefail

FILES=$(git diff --cached --name-only --diff-filter=ACMRTUXB -- '*.java' || true)
if [ -z "$FILES" ]; then
  echo "No staged Java files; skipping Checkstyle."
  exit 0
fi

# Convert newline list to comma-separated for -Pfiles
CSV=$(echo "$FILES" | tr '\n' ',' | sed 's/,$//')
./gradlew -q -Pfiles="$CSV" checkstyleStaged

```