我对 FP 的理解始终如下：




FP vs Traditional Loop (the trade off and how to decide which to use)
- FP is clear, pure, side-effect free and easy to handle. From my own experience, using FP to refactor the code into side-effect free ways, then refactor it again into methods for testing (though need use private method bypassing? How to resolve these issues?) Elegant way to handle this? But usually it involves multiple traversal which can be costly when dealing with the large collections. Any way to optimise it?
- Scoped Traditional Loop with method handling (one of the example I see is hanlde(param1, param2, param3) return Value, then after the method, param1, param2, param3. Are changed, and the value are returned, meaning it has side effects on all the parameters instead of returning the values only, which can be tricky to read and debug when doing it in large repository.
- Using local record/ Collector (For testing - adding more layers & Semantic Meaning)


WTF record can be used locally!!!!!!!!!!!! Never expect it before wow!!!

FP 的技巧 local record, currying, closures (?) Lazy evaluations + small methods

Set.copyOf, List.copyOf
JMH, -Xprof

For loop - maximum control over iteration, early exit (break, continue), huge data and need further consideration
Stream - declarative, readable code, business-logic heavy rather than data-crunching

Performance + Critical = for
Business logic + readability = stream

CPU/GC usage spikes

FP Techniques 
- Local Record (even within the method!)
- Currying/ Partial Applications (Multi-arg function into a chain of single-args functions);
- Function composition (.andThen, compose)
- Grouping & Partitioning
- Collector/ Accumulator/ Reducer

AbstractMap.SimpleEntry (*or using pair/ local record)

Weili code - two passes + currying, forEach with local variables (X changing external state, or external state used carefully) +  + Collectors method, 

How to choose between mutating external state and immutable results *handle & return value really cannot accept’


Don't worry about performance issues until you determine the performance is an issue. Readability and maintainability are far more important.
* A pragmatist I see.


Records just seem like a natural evolution of what was already started by making Enums, anyway.
Java Enums are one of the best, most underused parts of the language. By being actually objects that can implement interfaces and give immutability guarantees "for free", they really help the expressivity of a design. The whole "single element Enum as SINGLETON" train of thought naturally leads to usage as enumerated STRATEGY/STATE, and feels really nice.

Though I love Josh Bloch's reflections in Coders At Work about how unwieldy the Enum signature is and the deep issues in the language that it exposed for him. Really great read! 

The article tries to explain why Java has decided to add records (which are, indeed, more similar to @Value) to the language rather than a feature that is more similar to data classes/@Data. Put crudely, the article attempts to explain why Java has chosen @Value over @Data. It is interesting to point out that even Lombok saw the benefit in treating them as two different features, which might help people understand why records aren't the same as Kotlin's data classes; again, crudely data classes ≅ @Data while records ≅ @Value (of course, plus special treatment by the runtime

Closure

https://www.reddit.com/r/javahelp/comments/1gy576p/records_and_lists/

https://nipafx.dev/java-record-semantics/
https://openjdk.org/jeps/468



https://editorconfig.org/#download