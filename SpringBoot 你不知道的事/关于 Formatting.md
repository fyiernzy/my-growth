IntelliJ 自带 Rules + Git commit hooks + Spotless + SonarCube

- Formatting: google java-format plugins/ .editorconfig + xml file
- Bug Pattern/ Code Smell: SonarCube for IDE (Plugin) > SonarCube + Docker (advanced configuration)
- CI/CD Level Checking - Git Commit Hooks + Spotless + SonarCube

- Checkstyle：按照 predefined rules 检查代码风格，会抛出警示，但不会进行格式化（Formatting）；简单来说，就是会让程序员不舒服，但是不会强制要求程序员更改代码。
- Spotless：按照 predefined rules 格式化代码；强制更改代码。

没有理解错的话，SonarCube 一开始只是 Integration 而已，Integerate Checkstyle, PMD 和 SpotBugs 的 Report，后来才ziyan

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


PMD > Replaced by Sonarcube
### Does this mean PMD and SpotBugs are obsolete with SonarQube?

Not entirely, but their role has shifted:

- **Overlap:** Yes, there is significant overlap. For many common bugs and code smells, SonarQube's native analyzers can detect the same or similar issues as PMD and SpotBugs. In many cases, SonarQube's rules are even more comprehensive or accurate.
    
- **Complementary Use:**
    
    - **Specific Niche Rules:** PMD and SpotBugs might still have very specific rules or bug patterns that SonarQube's native analyzers don't cover, or cover in a different way. If a team has a long-standing custom PMD rule that is critical to their unique codebase, they might continue to use PMD for that.
        
    - **Local/Pre-commit Feedback:** As discussed, PMD and SpotBugs can be run very quickly as part of local build steps or pre-commit hooks, providing immediate, rapid feedback without needing a full SonarQube server analysis. SonarLint (SonarQube's IDE plugin) also provides this real-time feedback from SonarQube's rules.
        
    - **Historical Reasons/Tooling Lock-in:** Some teams might have deep-rooted processes or existing tool integrations around PMD or SpotBugs and choose to continue using them.
        
    - **"Belt and Suspenders" Approach:** Some highly critical projects might run all tools (SonarQube, PMD, SpotBugs, Checkstyle) as an extra layer of defense, even with some redundancy, to maximize issue detection. In such cases, SonarQube can often still import the reports from these external tools to consolidate all findings on its dashboard.
        

**In modern SonarQube setups, for many standard Java projects, you might find that SonarQube's own analyzers (via the SonarJava plugin) are sufficient for detecting most code smells and potential errors, reducing the absolute necessity to run PMD and SpotBugs as separate, mandatory steps unless you have very specific, unique requirements.**


  

We trialed both about 5 years ago. We generally found Sonarqube's analysis to be more comprehensive. We also found Sonar's hosted offering to be lower maintenance and more engaging. For example, the Github PR integration became part of our core PR workflow.

In general, Sonarqube offers a superset of what PMD offers. Also at some point I think SonarQube itself used PMD (and findbugs/spotbugs) behind the scenes. They have since changed to their own scanner.

PMD is great for quick and dirty checks (if you also include findbugs/spotbugs) and checkstyle. But for an "enterprise" solution SonarQube is the way to go.

https://www.reddit.com/r/java/comments/o71pw8/pmd_vs_sonarqube/

Thanks for sharing your experiences and recommending it.

I've chosen SonarQube for the following reasons:

- SonarQube 9 supports **Java 16** _now_ (missing Java 16 support was the reason I looked for another solution initially)
    
- SonarLint **works like a charme with almost no setup** (compared to the annoying Maven setup (incl. lib update for Java 16) required for PMD)
    
- SonarQube has a more comprehensive **rule set with about 600 rules** for Java vs. about 300 in the case of PMD.
    
- The **documentation** of the rules is extensive and helpful.
    
- According to this [master thesis](https://diglib.tugraz.at/download.php?id=5d7ac4b61b279&location=browse) **SonarQube is clearly better than PMD**.
    
- PMD didn't found the bug I placed intentionally into the code. The "bugs" were from their documentation and the resprective rules were activated. Maybe I made some configuration mistake, but even that would be a downside of the tool.
    

Not (yet?) relevant for my project, but very nice is that **SonarLint is also available for VS Code**.


On the assumption that you're not just troll-baiting... :-)

  

At the dawn of time, or at least the dawn of Sonar(Qube) there was no Java analyzer (which is now known as SonarJava). Instead, the founders incorporated the output of the three external tools you mentioned: FindBugs, PMD, and Checkstyle.

  

Then bug reports and requests for changes started to roll in - for those tools. And we couldn't respond adequately to those requests because they weren't for code we controlled. So the work on our analyzers started. 

  

Fast forward to today, and we've replaced all the valuable Checkstyle and PMD rules and a large majority of the FindBugs rules, in addition to creating many, many rules not seen in any of those tools.

  

In short, we feel that SonarJava is [the only rule engine you need](https://blog.sonarsource.com/sonarqube-java-analyzer-the-only-rule-engine-you-need/).
https://groups.google.com/g/sonarqube/c/9M0iZ4OILVM?pli=1