# IFP-RFC-01-redis/ZY/Flow-01

# Flow-01

## ADR

1. `shared` contains contracts/ policies only. All Redis/Caffeine/Resilience4J/etc. implementation should stay under `infrastructure`
2. To avoid unnecessary complexity, Spring Cache uses one Redis `CacheManager`. Maual/L1 caching uses typed `CacheStore<V>`.
3. Modules contribute `CacheDefinition` and `RateLimitDefinition` beans. Central infrastructure only aggregates them.

## Guideline

- Developer
  - To add a new cache/rate limit for your own module, 
- Maintainer
  - Add a new rate-limit algorithm

## Questions

- What is Redisson/ Redis/ Lettuce?
- Still not understand how to configure/choose/understand sentinel/cluster

## Folder Structure

TOOD-AI: I feel weird for this part. In my idea, the shared should provide an interface for the upper layer to use, and its implementation may use any infrastructure provided. So I think RedisCacheStore will be better to be put under shared/cache/RedisCacheStore (as an adpater), with Redis-specific configuration placed under infrastructure/redis. In this case, it would be impl -> use interface, impl -> use infra, and interface/infra doesn't know each other. From developer perpective, they will know what implementation are provided as well?

- module/
  - card/
    - config

I think need a README/ ADR to explain why writing in this way.
- shared/
  - cache/
    - CacheStore.java
    - CacheKey.java
    - CacheDefinition.java
    - CacheKeyFactory.java
    - AppCacheProperties.java
  - ratelimit/
    - RateLimeter.java
    - RateLimitDefinition.java // Still thinking of the naming
    - RateLimitResult.java // Still thinking of the naming
    - RateLimitKey.java //not sure if needed
    - RateLimitPolicy.java
    - RateLimitDecision.java
  - lock/
    - LockRequest.java // Still thinking of the naming
    - DistributedLock.java
    - DistributedLockManager.java
  - counter/
    - DistributedCounter.java
  - idempotency/
    - IdempotencyStore.java

- infrastructure/
  - cache/
    - SpringCacheConfig.java
    - RedisCacheStore.java
    - CaffineCacheStore.java
  - ratelimit/
    - SpringRateLimitConfig.java
    - RedisRateLimiter.java
    - Resilience4JRateLimiter.java
  - lock/
    - SpringDistributedLockManagerConfig.java
    - RedisDistributedLockManager.java
  - counter/
    - SpringDistributedCounterConfig.java
    - redis/
      - RedisDistributedCounter.java
  - redis/
    - config/
      - RedisConfiguration.java
    - serialization/
      - RedisSerializerConfiguration.java
      - RedisObjectMapper.java
    - RedisKey.java
    - RedisKeyFactory.java

## Design

### shared/cache

```java
public record CacheKey(String value) {

    public CacheKey {
        Objects.requireNonNull(value, "value cannot be null");

        if (value.isBlank()) {
            throw new IllegalArgumentException(
                "value cannot be blank"
            );
        }
    }
}

// ADR: Use CacheKey as key as general contract.
public interface CacheStore<V> {

    Optional<V> get(CacheKey cacheKey);

    void put(CacheKey cacheKey, V value);

    void evict(CacheKey cacheKey);
}

@Getter
public class CacheConfiguration {

    private String name;
    private String version;
    private Duration ttl;
    private Duration jitter;

    public CacheConfiguration(String name, String version, Duration ttl, Duration jitter) {
        this.name = Objects.requireNonNull(name, "Cache name must not be null");
        this.version = Objects.requireNonNull(version, "Cache name must not be null");
        this.ttl = Objects.requireNonNull(ttl, "Cache TTL must not be null");
        this.jitter = Objects.requireNonNull(jitter, "Cache jitter must not be null");

        Asserts.notBlank(name, "Cache name must not be blank");
        Asserts.isPositive(ttl, "Cache TTL must be positive");
        Asserts.isPositive(jitter, "Cache TTL jitter must be positive");
    }
}

@Component
public class CacheKeyFactory {

    // TODO-AI: Is this needed? Seems like coupled to Spring design?
    private final AppCacheProperties appCacheProperties;
    private final Map<String, CacheConfiguration> configurationByName;

    public CacheKeyFactory(AppCacheProperties appCacheProperties, List<CacheConfiguration> configurations) {
        this.appCacheProperties = appCacheProperties;
        this.configurationByName = configurations.stream()
            .collect(Collectors.toUnmodifiableMap(
                CacheConfiguration::getName,
                Function.identity(),
                (first, second) -> new IllegalStateException();
            ));
    }

    public CacheKey create(String cacheConfigName, String... segments) {
        CacheConfiguration config = Optional.ofNullable(configurationByName.get(cacheConfigName)).orElseThrow();

        String suffix = String.join(":", segments);

        return new CacheKey("%s:cache:%s:%s".formatted(
            appCacheProperties.getApplication(),
            config.getName(),
            config.getVersion();
            suffix
        ));
    }
}

@CoonfigurationProperties("app.cache")
public class AppCacheProperties {

    private String application = "unknown";
}
```

### shared/ratelimit

```java
// Resilience4J/ Redis should be an implementation of RateLimiter
public interface RateLimiter {

    // TODO-AI: Also, how to integrate with external framework like Resilience4J?
    RateLimitDecision tryAcquire(RateLimitPolicyId policyId, String subject);
}

@Component
public class RateLimitDefinitionRegistry {

}

public record RateLimitPolicyId(String value) {

}

// builder/ customizer based?
public record RateLimitDefinition(
    RateLimitPolicyId id,
    RateLimitAlgorithm algorithm,
    long capacity,
    Duration window,
    FailureMode failureMode
) {

}

public record RateLimitDecision(
    boolean allowed,
    long remaining,
    Duration retryAfter
) {

}
```

### shared/lock

```java
public record LockRequest(
    String resource,
    Duration waitTime,
    Duration leaseTime
) {

}

public interface DistributedLock extends AutoCloseable {

    String resource();

    // A fencing token gives every lock acquisition an incresing sequence. It behaves like Spring Data JPA's @Version for optimistic locking. It also resemble the stamp locking in Java.
    OptionalLong fencingToken();

    @Override
    void close();
}

public interface DistributedLockManager {
    
    Optional<DistributedLock> tryAcquire(String resource, Duration leaseTime);

    void release(DistributedLock lock);
}
```

### infrastructure/cache/SpringCacheConfig.java

```java
@EnableCaching
@Configuration(proxyBeanMethods = false)
class SpringCacheConfig {


}
```

### infrastructure/redis/config

```yaml
connect timeout
command timeout
pool
cluster refresh
read-from
Sentinel
Cluster
SSL
credentials
spring:
  cache:
    type: redis

  data:
    redis:
      host: ${REDIS_HOST:localhost}
      port: ${REDIS_PORT:6379}
    username: ${REDIS_USERNAME:}
      password: ${REDIS_PASSWORD:}

      connect-timeout: 1s
      timeout: 1s

      client-name: ${spring.application.name}

      ssl:
        enabled: ${REDIS_SSL:false}
```

```java
// TODO-AI: Where usually those config are placed? Inside infrastructure?

// A rule: never call RedisTemplate directly; use the interface like counter, ratelimiter instead!
// how about sentinel, TTL

// TODO-AI: How about RedisInfrastructureConfiguration.java/ RedisPolicityProperties etc.?

    // TODO-AI: How to configure the Spring Cachable?

    // TODO-AI: how to utilize redis properties?

    // TODO-AI: WHat is RedisCacheManager? How to use it

    // TODO-AI: How about connection pooling, Sentinel, Cluster, Lua scripts. distributed locking, Streams, etc.?

    // TODO-AI: How about Redis failure semantics, app/module-specific TTL config? What is cache stampede protection? retry polciy, cluster/sentinel topolocy, atomic operations with Lua, observability
    // TODO-AI: Any industrial pattern of cache config/ Any 踩坑经验 to avoid/ Any industrial pattern of utilizing redis/ cache?

    // TODO-AI: ChatGPT mentioned that it is recommended to remove the genericRedisTemplate, in this case, how to configure the Spring cache?

        // TODO-AI: Any other properties for LettuceClientOptionsBuilderCustomizer

   // TODO-AI: What are the production-level of avoidng Cache Stampede?

// Remark: Spring Cache and Spring Redis Cache already provides all the configuration needed.
// Redis already provides a StringRedisTemplate when there's a single RedisConnectionFactory

// ADR: Having 1 RedisConfig only for now to avoid unnecessary complexity.
@Configuration(proxyBeanMethods = false)
class RedisConfig {

    @Bean
    RedisCacheConfiguration redisCacheDefaults() {
        RedisSerializer<Object> valueSerializer = new GenericJackson2JsonRedisSerializer();

        return RedisCacheConfiguration
            .defaultCacheConfig()
            .disableCachingNullValues()
            .serializeValuesWith(
                RedisSerializationContext.SerializatinPair
                    .fromSerializer(valueSerializer)
            )
            .computePrefixWith(Function.identity());
    }

    // TODO-AI: Seems like the redis is related to cache only? Lik eeverything here ended with "Cache"
    @Bean
    RedisCacheManager redisCacheManager(
        RedisConnectionFactory connectionFactory,
        RedisCacheConfiguration redisCacheDefaults,
        List<CacheConfiguration> configurations
    ) {
        assertUniqueCacheNames(configurations);

        // TODO: BatchStrategies value should be configurable through application.yml
        RedisCacheWriter redisCacheWriter = RedisCacheWriter
            .nonLockingRedisCacheWriter(
                connectionFactory,
                BatchStrategies.scan(1_000)
            );

        // TODO-AI: What is transaction-aware, initialCacheConfiguratin, cacheConfiguration, withResetCachesStrategy? Reference: https://docs.spring.io/spring-data/redis/reference/api/java/org/springframework/data/redis/cache/RedisCacheManager.RedisCacheManagerBuilder.html
        RedisCacheManegr.RedisCacheManagerBuilder builder = 
            RedisCacheManager
                .builder(redisCacheWriter)
                .cacheDefaults(redisCacheDefaults);
        
        // TODO-AI: How about conversion service & prefix? 
        // Refernece: https://docs.spring.io/spring-data/redis/reference/api/java/org/springframework/data/redis/cache/RedisCacheConfiguration.html
        configurations.forEach(configuration -> 
            builder.withCacheConfiguration(
                configuration.getName(),
                redisCacheDefaults.entryTtl(jitteredTtl(
                    config.getTtl(),
                    config.getJitter()
                ));
            )
        )
    }

    private void assertUniqueCacheNames(List<CacheConfiguration> configurations) {
         Set<String> names = new HashSet<>();

        for (CacheDefinition config : configurations) {
            if (!names.add(config.getName())) {
                throw new IllegalStateException(
                    "Duplicate cache definition: " + config.getName()
                );
            }
        }
    }

    /*
        Remark: Cache Stampede 缓存雪崩

        Description:

        Cache Stampede occurs when a cached key expires and hundreds or thousands of simultenous requests rush to recompute the same data, overloading the backend database.

        Solution:

        Add jitter.

    */
    private RedisCacheWriter.TtlFunction jitteredTtl(Duration ttl, Duration jitter) {
        return (key, value) -> {
            long jitterMillis =
                ThreadLocalRandom.current()
                    .nextLong(jitter.toMillis() + 1);

            return ttl.plusMillis(jitterMillis);
        };
    }

    @Bean
    LettuceClientOptionsBuilderCustomizer redisClientOptions() {
        // Remark: Pooling is not required initially unless it is needed for BLPOP/ blocking streams, Redis transactions, native connectoin sharing disabled, specialized high-concurrency workloads
        // Remark: It is needed for bounded disconnected commad queue, custom reconnect behavior, cluster topolocy refresh, socket options, client resources, custom read-from-replica policy
        return builder -> builder
            .disconnectedBehavior(ClientOptions.DisconnectedBehavior.REJECT_COMMANDS)
            .requestQueueSize(1_000);
    }

    @Bean
    RedisTemplate<String, Object> genericRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();

        // TODO-AI: how to configure connectionFactory?
        // TODO-AI: What are the other properties that can be configued on the template?
        template.setConnectionFactory(connectionFactory);

        // TODO-AI: What are the other serializer available?
        StringRedisSerializer keySerializer = new StringRedisSerializer();


        // TODO-AI: For long-lived/ shared data, ChatGPT recommends to use specific DTO + specific serializer + versioned key, yet how?
        GenericJackson2JsonRedisSerializer valueSerializer = new GenericJackson2JsonredisSerializer();

        /*
          Remarks:
          HSET client:123 name ALICE
          HSET client:123 status ACTIVE

          keySerializer -> "client:123"
          hashKeySerializer -> "name" "status"
          hashValueSerializer -> "ALICE" "ACTVE"
         */
        template.setKeySerializer(keySerializer);
        template.setHashKeySerializer(keySerializer);

        template.setValueSerializer(valueSerializer);
        template.setHashValueSerializer(valueSerializer);

        return template;
    }
}
```


## Notes
Redis failure semantcis:

When Redis available:
read -> cache miss and load DB
write -> skip cache write & record metric
distributed lock -> cannot prove ownership, fail closed
counter for quota
idempotency
session/auth secrutiy data -> usually fail closed


## Reference

- [Redis Architecture Design](https://chatgpt.com/c/6aa9de73-cab4-83ec-80c2-ad9a4296cca0)
- [CacheStore vs CacheManager](https://chatgpt.com/c/6aa9ec81-405c-83ec-95e9-be091fac4eb1)
- [Redis cache configuration patterns](https://chatgpt.com/c/6aaa18ac-11a8-83ec-acc0-6b94f5618d5c)
- [Adapt Resilience4j For Redis](https://chatgpt.com/c/6aaa3f6a-2654-83ec-ad82-6f23e0eb0738)
- [Configure Redis Spring Boot](https://chatgpt.com/c/6aa9e7fc-20fc-83ec-8ab3-e4b55301ac74)