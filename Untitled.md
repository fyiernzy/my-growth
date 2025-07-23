目前做 Checking 的方法有很多，设计思路也有很多，比如：

最古老的方法就是 if then throw，如下：
```java
if(obj == null) {
	throw IllegalArgumentException();
}
```

这个方法是肯定没问题的，只是 if-then-else 通常需要写三行 code，如果要检查很多的话会显得很乱，这些 trivial 的 defensive programming 反而会成为 noise。所以明智的写法是将其缩短成一行：

```java
Assertions.notNull(obj, new IllegalArgumentException());
```

那这个写法最大的问题就是他会先 initialize Exception，不管最后有没有 throw，这会造成资源浪费（因为 initialize 也会需要 memory）==（ #问题 他会浪费多少 memory？如果我有七个checking，他会先 initialize 7 个 exception？）==，所以比较好的办法就是用 supplier，他会等到真的需要的时候才 initialize

```java
Assertions.notNull(obj, () -> new IllegalArgumentException());
```

当然在这个情况下我们也可以用 `Optional`，如下：

```java
Optional.ofNullable(obj)
.orElseThrow(() -> new IllegalArgumentException());
```

但我觉得这个写法不好有两个原因：
1. Initialize Optional 是需要额外开销的，远不如调用 Assertions 的 static method 来得直接；
2. Optional 的语义本来就不是拿来做检测；他是为了进行一系列的map/filter 操作的。对我来说，他的真正作用是 return as something and do some other stuff where there's a null value -> default value, throw error.
3. Optional 能够检测的场景太少，只能 check null

所以综上，w