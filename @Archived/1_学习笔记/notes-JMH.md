## Setup
---
```groovy
plugins {
    id 'java'
    id 'me.champeau.jmh' version '0.7.2'
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

dependencies {
    jmh 'org.openjdk.jmh:jmh-core:1.38'
    jmh 'org.openjdk.jmh:jmh-generator-annprocess:1.38'
}
```

## Coding
---
- **`Blackhole`**: 
	- Consumes values so the JIT compiler can’t optimize benchmark code away.
	- Ensures each method actually executes and its result is used in a non-optimizable way.
	- Without a `Blackhole`, the JVM may eliminate work, producing misleading results.
- **`@BenchmarkMode({Mode.Throughput})`**
    - Specifies which performance metrics to measure.
	- Common modes include `Mode.Throughput` and `Mode.AverageTime`.
- **`@OutputTimeUnit(TimeUnit.NANOSECONDS)`**
	- Sets the time unit for reported results.
	- Common choices: `TimeUnit.NANOSECONDS`, `TimeUnit.MILLISECONDS`
- **`@WarmUp(...)`**
	- Allows the JIT to compile and optimize code before measurements begin.
	- Typical parameters:
	    - `iterations = <count>`
	    - `time = <duration>`
	    - `timeUnit = <TimeUnit>`
	- Example: `@Warmup(iterations = 10, time = 500, timeUnit = TimeUnit.NANOSECONDS)`
- **`@Measurement(...)`**
	- Defines how JMH collects **actual** benchmark data after warmup.
	- Parameters mirror `@Warmup` (e.g., `iterations`, `time`, `timeUnit`).
- `@Fork(...)`
	- Controls how many separate JVMs to spawn for the benchmark run.
- `@State`
	- Defines the lifecycle/scope of benchmark state. 
	- Options:
		- `Scope.Thread`: Each benchmarking thread gets its own instance.
		- `Scope.Group`: One instance per thread group (useful for concurrency tests).
		- `Scope.Benchmark`: One instance shared across all threads in a fork.
- `@Setup`
	- Runs setup code before a chosen scope:
		- `Level.Trial`: Once before all iterations in a fork.
		- `Level.Iteration`: Before each iteration.
		- `Level.Invocation`: Before each individual benchmark method call.
- **`@Benchmark`**
	- Marks the method that JMH should benchmark.
- `@Param`: Provides **multiple values** to run as a Cartesian product across all `@Param` fields.

## Benchmark Interpretation
---
- **Latency** (ns/op): Average time to execute one operation, in nanoseconds per operation.
- **Throughput** (ops/s): Number of operations completed per unit time (commonly reported as `ops/s`; JMH will choose the unit based on the run).

## Coding Example
---
```java
// jmh/java/com/example/MyBenchmark.java

@State(Scope.Benchmark)
@BenchmarkMode({Mode.Throughput, Mode.AverageTime})
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 10, time = 500, timeUnit = TimeUnit.MICROSECONDS)
@Measurement(iterations = 10, time = 500, timeUnit = TimeUnit.MICROSECONDS)
@Fork(2)
public class MyBenchmark {

    private int[] data;

    @Setup(Level.Trial)
    public void setup() { data = java.util.stream.IntStream.range(0, 1_000).toArray(); }

    @Benchmark
    public int sum(Blackhole bh) {
        int s = 0;
        for (int v : data) s += v;
        bh.consume(s);
        return s;
    }
}
```