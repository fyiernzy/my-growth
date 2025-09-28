
> **目标**
> 1. 系统化地整理学习资源，并结合当下趋势与实战经验不断更新。举个例子，目前市面上较少讲解如何在 SpringBoot 里使用 Virtual Threads 和 Structured Concurrency 的资料，于是我想将这部分整理出来。
> 2. 作为成长的证明。

- Developer Tools: Wireshark, Git, Postman, CLI
	- 
	- 
- Advanced Java
- Basic
    - Reflection
        - MethodHandle, VarHandle, Method, Field
    - Bytecode
    - Collections
    - Stream & Optional (Functional Programming)
- Performance
    - JVM
    - GC
    - JMM
    - Benchmarking using JMH. JFR, Profiler, JMeter, JDK Mission Control
- Concurrency (Apply, Implement, Theory)
    - Object
    - Synchronized
    - JUC Lock StampedLock, ReentrantLock, Semaphore, ReadWriteLock
    - JUC Collection
    - ExecutorService, Thread
    - Future, Runnable, Callable, FutureTask, CompletableFuture
    - VirtualThread
    - Structured Concurrency
    - Design & Application
- 
- Java Networking (IO, NIO, Netty, Tomcat, Basic, debugging)
- SpringBoot basic
- IntelliJ in Depth
	- Mouse-free development: Vim, Google Chrome Shortcut, IntelliJ shortcut
	- Plugins: 
- Testing, Profiling, Benchmarking
	- Unit Tests: JUnit5, Mockito, AssertJ
	- Integration Tests: MockMvc, RestAssured, Testcontaners
	- Load Tests: k6, JMeter
	- JMH
	- 
- Devops: 
	- git
		- git basic commands
		- git aliases (+ online config)
		- git hooks
	- CICD pipeline (YML)
	- Gradle (plugins, dependencies, writing scripts, settings), MVN
	- SonarCube/ Checkstyle/ PMD/ SpotBugs/ etc.
- Common Tools: Lombok, MapStruct, Guava, Apache
- 
- System Design
	- Microservices
		- Communication: Open Feign, gRPC
	- Architecture: EDA, MVC, DDD/Onion/Hexagonal
- Security
	- OAuth2.0 with PAR/JAR/JARM/
	- JWT/JWKS/JWE/PKCE
- Database
	- PostgreSQL, Redis in depth for development and debugging


- 实战经验
	- 

SpringBoot: From Zero to Hero
- IntelliJ Configuration & Plugins
- Git configuration (and recommit-hooks, bash)
    - The basics
    - How to write a valid, correct commit message
    - How to setup commit hooks (Compare between different methods) and why do we need it; Use bash script to enhance simplify the process.
    - How to use git stash/ subtree
    - How to use rebase/ commit —amend
    - How to checkout a log (git log)
    - How to remove cache & staging
    - How to remove a commit (revert, reset)
    - How to recover a disaster (reblog)
    - How to point out an issue
- Controller, Repository (annotations, query dsl, etc.), AOP, Service, Design Pattern, Configuration, build.gradle.kts
- The Lombok
- Kotlin script for build.gradle; How to register a tasks
- Observability: Logging, Metrics, Tracing in App/ K8
- How to use JMH/ JFR/ Gravaal VM to
-  Unit Tests, Integration Tests, Load Tests
- GitLab Ci/CD Pipeline (YML), Code Quality, Code Coverage, Deployment, SonarCube, etc., Docker, K8s, Cloud Server, Nguni, Configuration pattern
- Deployment & Integrating Third Party API
The underlying mechanism

Gradle
- [ ] Add check styleFiles and check locally
- [ ] Push to to remote and perform testing
- [ ] If everything ok, then proceed to continuous refactoring
- [ ] Then MR

