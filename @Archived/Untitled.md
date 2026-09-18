文档类型
1. 学习文档
2. Solution Design (Feat)
3. Solution Design (Refactor)
4. Design Decision（代码设计 & 考量）
5. Coding Style & Guideline

为什么要有文档框架？
代码错误图录
Reusable Templates

6. **Decision & Pattern Cards (ADRs)**
    
    - **Why**: Highest leverage; documents _why_ over time; great interview ammo.
        
    - **Keep**: context, options, decision, consequences, links to PR/tests.
        
7. **Bug/Incident Playbook**
    
    - **Why**: Diagnosis speed compounds; turns pain into reusable procedures.
        
    - **Keep**: symptom → investigation → root cause → fix → prevention.
        
8. **Reusable Templates & Project Skeletons**
    
    - **Why**: Multiplies delivery speed/quality across projects.
        
    - **Keep**: test scaffolds, Docker compose, CI snippets, ArchUnit rules, JMeter/k6, Pitest config.
        
9. **Test Assets (Contract tests + Mutation testing + Test data factories)**
    
    - **Why**: Directly correlates with defect reduction and safe refactors.
        
    - **Keep**: Pact/SCC stubs, Pitest thresholds, Testcontainers modules, seed scripts.
        
10. **Perf & Observability Baselines**
    
    - **Why**: You can _show_ improvements; prevents bike-shedding.
        
    - **Keep**: p50/p95/p99, error%, JFR/flamegraphs, dashboards, alert rules.
        
11. **Domain Glossary & Context Maps (DDD)**
    
    - **Why**: Aligns code with business; guides boundaries & team communication.
        
    - **Keep**: bounded contexts, relationships, core terms, sample flows.
        
12. **Runbooks & Checklists**
    
    - **Why**: Crush MTTR; reduces cognitive load under stress; scales to teams.
        
    - **Keep**: start/stop, health checks, common failures, rollback, PR/release lists.
        
13. **Migration & Compatibility Playbooks (Schema + Contract Catalog)**
    
    - **Why**: Most “senior” work = safe evolution; avoids breaking clients.
        
    - **Keep**: versioned schemas, deprecation policy, dual-write/read plans, canary steps.
        
14. **Anti-patterns & Smells Catalog**
    
    - **Why**: Prevents repeating mistakes; sharpens code review instincts.
        
    - **Keep**: smell → why it hurts → better pattern → code diff.
        
15. **LLM Prompt & Review Patterns**
    

- **Why**: AI era edge; you orchestrate AI safely and reproducibly.
    
- **Keep**: prompts, guardrails, verification checklists, known failure modes.
    

> These 10 give you **compounding ROI**: they speed you up, raise quality, and signal “systems thinking” to any team.



# What to accumulate (beyond just “code”)

1. **Decision & Pattern Cards (ADR-style)**
    
    - What to store: problem, constraints, options, decision, consequences, links to PR/tests.
        
    - What you gain: faster future decisions, interview stories, a clear “why” behind your code.
        
2. **Bug/Incident Playbook**
    
    - Store: symptom → investigation steps → root cause → fix → prevention.
        
    - Gain: shorter diagnosis time; reusable checklists for similar failures.
        
3. **Reusable Templates**
    
    - Store: unit/integration/contract test scaffolds, Docker compose for Redis/Kafka, CI snippets, ArchUnit rules, JMeter/k6 scripts.
        
    - Gain: dramatic speed-up on new repos; consistency.
        
4. **Anti-patterns & Smells Catalog**
    
    - Store: what it looks like, why it’s bad, better pattern (with code diff).
        
    - Gain: you avoid repeating mistakes; teaches junior teammates.
        
5. **Domain Glossary & Context Maps**
    
    - Store: bounded contexts, relationships, core terms, state diagrams.
        
    - Gain: clearer models, fewer cross-service leaks; you sound like you understand the business (because you do).
        
6. **Perf & Observability Artifacts**
    
    - Store: baseline load tests (p50/p95/p99), JFR/Flamegraphs, dashboards, alert rules.
        
    - Gain: you don’t argue about performance—you show it.
        
7. **Runbooks & Checklists**
    
    - Store: “how to” start locally, deploy, backfill data, rotate keys, roll back; PR checklist, release checklist.
        
    - Gain: fewer outages and “how do I…?” pings; you look senior.
        
8. **Risk/Assumption Log**
    
    - Store: what must be true, failure impact, mitigation plan, owner.
        
    - Gain: you predict issues before they bite (huge differentiator).
        
9. **LLM Prompt & Review Patterns**
    
    - Store: prompts that produce testable code, your validation steps, common hallucinations.
        
    - Gain: you get compounding value from AI instead of random outputs.
        

---

# Documents worth writing (and why they pay off)

1. **One-Pager Design Doc**
    
    - Scope, constraints, 2–3 options table, chosen design, out-of-scope, test plan.
        
    - Payoff: alignment in hours, not weeks; a future YOU can maintain it.
        
2. **ADRs (Architecture Decision Records)**
    
    - Single decision per file; 10–15 lines is fine.
        
    - Payoff: creates institutional memory; enables reversibility.
        
3. **Context Map (DDD) + Glossary**
    
    - Simple diagram + definitions.
        
    - Payoff: trumps bikeshedding; guides service boundaries.
        
4. **Test Strategy (per service)**
    
    - What is unit/slice/integration/contract here? data seeding? mutation targets?
        
    - Payoff: fewer flaky tests; faster CI.
        
5. **Performance Plan + Report**
    
    - Target RPS/latency, scripts, environment, results, bottlenecks, next steps.
        
    - Payoff: you can defend performance decisions with numbers.
        
6. **Runbooks (operate + debug)**
    
    - Startup flags, health checks, common failures, dashboards, on-call commands.
        
    - Payoff: lower MTTR; you become “the person who makes things safe.”
        
7. **Postmortems (blameless)**
    
    - Timeline, root cause, what helped, what hurt, fixes (owner/date).
        
    - Payoff: converts pain into process; super valuable portfolio items.
        
8. **Migration Plan**
    
    - Data/contract change steps, dual-write/dual-read windows, rollback.
        
    - Payoff: safer changes; shows system thinking.
        
9. **Security/Threat Notes (lightweight)**
    
    - Data classes, secrets, authn/z choices, known risks.
        
    - Payoff: you won’t get caught by obvious compliance gaps.
        

---

# Techniques great engineers use to learn advanced topics

- **Feynman Technique**: explain to a junior in 3–5 minutes; rewrite until it’s clear.
    
- **Deliberate Practice**: choose one micro-skill (e.g., cache-aside invalidation), design a measurable exercise, get feedback, repeat.
    
- **Read Code > Read Books**: pick a mature lib (Spring, Caffeine), trace one feature, draw a sequence diagram.
    
- **Paper-to-Prototype**: after a chapter/paper, implement a tiny version (outbox, circuit breaker) and benchmark it.
    
- **Spaced Repetition**: Anki cards for JVM/HTTP/DDD terms you keep forgetting.
    
- **Benchmark-Driven Choices**: A/B two designs with JMH/JMeter; let data decide.
    
- **Red-Team Your Design**: play attacker/client; break assumptions, inject faults.
    
- **Engineering Notebook**: daily 5–10 min—what I tried, what worked, next question.
    
- **Constraint Drills**: build the same feature with a memory/latency budget; forces sharp trade-offs.
    
- **Teach Early**: internal lightning talks; teaching reveals gaps fast.
    

---

# Often-overlooked, by level (and why they matter)

**Juniors**

- Error handling & retries (backoff, idempotency) → prevents cascading failures.
    
- Test data management (factories, slices, Testcontainers) → stable tests.
    
- Observability basics (structured logs, trace IDs) → you can debug.
    
- Backward compatibility (schema evolution, tolerant readers) → safer releases.
    

**Mid-level**

- Boundary enforcement (ArchUnit/modules) → stops entropy.
    
- Data modeling trade-offs (read vs write models, indexing cost) → performance with clarity.
    
- Migration strategies (dual write/read, outbox, feature flags) → ship without drama.
    
- Cost awareness (infra/load test costs) → real-world constraints.
    
- Incident readiness (runbooks, on-call drills) → reliability culture.
    

**Senior**

- Succession & bus factor (docs, mentoring, pairing) → resilient teams.
    
- Decision latency & reversibility (prefer reversible bets) → faster orgs.
    
- “Past solution bias” (don’t force Kafka/DDD everywhere) → fit-for-purpose.
    
- Measurable outcomes over anecdotes (SLOs, error budgets) → avoid thrash.
    
- Governance & deprecation policy → systems that evolve cleanly.