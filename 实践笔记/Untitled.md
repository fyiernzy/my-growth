---
updated: 2025-07-29T16:59:41.835+08:00
edited_seconds: 0
---
Setting up **pre-commit** or **pre-push hooks** is a powerful way to automatically enforce code quality (e.g., Checkstyle) before code is committed or pushed. You have a few options depending on your team’s workflow and tools.

---

## ✅ Common Choices for Git Hooks

### 1. **Manual Git Hooks (local `.git/hooks/`)**

- Native to Git.
    
- Located in `.git/hooks/pre-commit` or `.git/hooks/pre-push`.
    
- Written in Bash (or any executable language).
    
- **Only works locally** unless you share them via setup scripts.
    

#### Example: `.git/hooks/pre-commit`

```bash
#!/bin/bash
echo "Running Checkstyle..."
./gradlew checkstyleMain
if [ $? -ne 0 ]; then
  echo "Checkstyle violations found. Commit aborted."
  exit 1
fi
```

> Make the file executable: `chmod +x .git/hooks/pre-commit`

---

### 2. **[Husky](https://github.com/typicode/husky) (via Node.js)** — popular in polyglot teams

- Easier to manage across teams.
    
- Stores hooks in your repo.
    
- Widely used in JS/Java hybrid codebases.
    

#### Example `package.json` (you can still run Gradle commands)

```json
{
  "husky": {
    "hooks": {
      "pre-commit": "./gradlew checkstyleMain"
    }
  }
}
```

---

### 3. **[pre-commit framework](https://pre-commit.com/)**

- Language-agnostic.
    
- Used in Python-heavy teams, but works with any command.
    
- You define all hooks in `.pre-commit-config.yaml`.
    

#### Example:

```yaml
repos:
  - repo: local
    hooks:
      - id: checkstyle
        name: Run Checkstyle
        entry: ./gradlew checkstyleMain
        language: system
        stages: [commit, push]
```

Then install it via:

```bash
pip install pre-commit
pre-commit install --hook-type pre-commit --hook-type pre-push
```

---

### 4. **Gradle Task + Git Alias or Script**

- Add a Gradle task that chains Checkstyle + tests.
    
- Call it manually or via a Git alias like:
    

```bash
git config alias.safe-push '!./gradlew checkstyleMain test && git push'
```

---

### 5. **CI-Only Enforcement (Fallback)**

- Ensure your pipeline (GitHub Actions, GitLab CI, etc.) fails on Checkstyle violations.
    
- Less developer-friendly (feedback only after push).
    

---

## ✅ Recommendation Summary

|Method|Best For|Shared in Repo?|Language-Agnostic?|Easy to Maintain|
|---|---|---|---|---|
|Manual `.git/hooks/`|Simple local setups|❌|✅|⚠️ Manual copy|
|Husky|JavaScript/Node projects|✅|⚠️ (Java via Gradle)|✅|
|pre-commit|Multi-language codebases|✅|✅|✅|
|Git Alias|Solo developer workflows|❌|✅|✅|
|CI-Only|Central enforcement|✅|✅|✅|

---

## ✅ Suggested for Java Projects

- ✅ Use `pre-commit` or manual `pre-commit` Git hook.
    
- ✅ Run `./gradlew checkstyleMain` (and optionally `test` or `lint`).
    
- ✅ Fail on violations to block the commit/push.
    

Let me know if you'd like a ready-to-use `pre-commit-config.yaml` or a shell script template for your `.git/hooks/`!